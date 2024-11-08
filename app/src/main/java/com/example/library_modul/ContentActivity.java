package com.example.library_modul;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ContentActivity extends AppCompatActivity {

    EditText etBookTitle, etBookAuthor, etBookGenre, etBookYear, etBookAnnotation;
    ImageView imageViewCover;
    ImageButton btnSave, btnDelete, btnLoadImage;
    DbHelper dbHelper;
    Book book;
    Uri imagePath;
    private final int GALLERY_REQ_CODE = 1;
    private static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_content);

        etBookTitle = findViewById(R.id.tBookTitle);
        etBookAuthor = findViewById(R.id.tBookAuthor);
        etBookGenre = findViewById(R.id.tBookGenre);
        etBookYear = findViewById(R.id.tBookYear);
        etBookAnnotation = findViewById(R.id.tBookAnnotation);
        imageViewCover = findViewById(R.id.imageViewCover);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);
        btnLoadImage = findViewById(R.id.btnLoadImage);

        dbHelper = new DbHelper(this);

        book = (Book) getIntent().getSerializableExtra("key");
        if (book != null) {
            etBookTitle.setText(book.getTitle());
            etBookAuthor.setText(book.getAuthor());
            etBookGenre.setText(book.getGenre());
            etBookYear.setText(String.valueOf(book.getYear()));
            etBookAnnotation.setText(book.getAnnotation());

            try {
                imagePath = Uri.parse(book.getCoverImage());
                loadImage(imagePath);
            } catch (Exception e) {
                requestPermission();
            }
        } else {
            Toast.makeText(this, "Книга не найдена", Toast.LENGTH_SHORT).show();
            finish();
        }

        requestPermission();

        btnSave.setOnClickListener(v -> {
            String title = etBookTitle.getText().toString().trim();
            String author = etBookAuthor.getText().toString().trim();
            String genre = etBookGenre.getText().toString().trim();
            String yearStr = etBookYear.getText().toString().trim();
            String annotation = etBookAnnotation.getText().toString().trim();

            if (title.isEmpty() || author.isEmpty() || genre.isEmpty() || yearStr.isEmpty()) {
                Toast.makeText(ContentActivity.this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show();
                return;
            }

            int year;
            try {
                year = Integer.parseInt(yearStr);
            } catch (NumberFormatException e) {
                Toast.makeText(ContentActivity.this, "Введите корректный год", Toast.LENGTH_SHORT).show();
                return;
            }

            book.setTitle(title);
            book.setAuthor(author);
            book.setGenre(genre);
            book.setYear(year);
            book.setAnnotation(annotation);
            book.setCoverImage(imagePath != null ? imagePath.toString() : null);

            boolean result = dbHelper.updateData(book);
            if (result) {
                Toast.makeText(getApplicationContext(), "Данные обновлены", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Не удалось обновить данные", Toast.LENGTH_SHORT).show();
            }
        });

        btnDelete.setOnClickListener(v -> {
            boolean result = dbHelper.deleteData(book);
            if (result) {
                Toast.makeText(getApplicationContext(), "Книга удалена", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Не удалось удалить книгу", Toast.LENGTH_SHORT).show();
            }
        });

        btnLoadImage.setOnClickListener(v -> openGallery());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == GALLERY_REQ_CODE && data != null) {
            imagePath = data.getData();
            loadImage(imagePath);
        }
    }

    private void loadImage(Uri uri) {
        if (uri != null) {
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageViewCover.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Не удалось загрузить изображение", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openGallery() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ||
                (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                        ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED)) {
            Intent iGallery = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            iGallery.addCategory(Intent.CATEGORY_OPENABLE);
            iGallery.setType("image/*");
            startActivityForResult(iGallery, GALLERY_REQ_CODE);
        } else {
            requestPermission();
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_MEDIA_IMAGES
            }, PERMISSION_REQUEST_CODE);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_CODE);
        }
    }
}
