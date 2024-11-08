package com.example.library_modul;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddActivity extends AppCompatActivity {
    EditText etTitle, etAuthor, etYear, etGenre, etAnnotation; // Добавлено поле для аннотации
    ImageButton btnLoadImage;
    ImageView imageView;
    Uri imagePath = null;
    DbHelper dbHelper;

    private static final int PICK_IMAGE_REQUEST = 1; // Константа для выбора изображения

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        // Настройка отступов для работы с системными барами
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new DbHelper(this);

        etTitle = findViewById(R.id.etTitle);
        etAuthor = findViewById(R.id.etAuthor);
        etYear = findViewById(R.id.etYear);
        etGenre = findViewById(R.id.etGenre);
        etAnnotation = findViewById(R.id.etAnnotation); // Инициализация поля для аннотации
        btnLoadImage = findViewById(R.id.btnLoadImage);
        imageView = findViewById(R.id.imageView);
        Button btnInsert = findViewById(R.id.btnInsert);

        // Обработка выбора изображения
        btnLoadImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*"); // Устанавливаем тип, чтобы выбрать только изображения
            startActivityForResult(intent, PICK_IMAGE_REQUEST); // Ожидаем результат выбора изображения
        });

        // Обработка добавления новой книги
        btnInsert.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String author = etAuthor.getText().toString().trim();
            String yearStr = etYear.getText().toString().trim();
            String genre = etGenre.getText().toString().trim();
            String annotation = etAnnotation.getText().toString().trim(); // Получение аннотации
            String coverImage = (imagePath != null) ? imagePath.toString() : null;

            if (title.isEmpty() || author.isEmpty() || yearStr.isEmpty() || genre.isEmpty() || annotation.isEmpty()) {
                Toast.makeText(AddActivity.this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show();
                return;
            }

            int year;
            try {
                year = Integer.parseInt(yearStr);
            } catch (NumberFormatException e) {
                Toast.makeText(AddActivity.this, "Введите корректный год", Toast.LENGTH_SHORT).show();
                return;
            }

            Book book = new Book(title, author, year, genre, coverImage, annotation); // Передаем аннотацию
            if (dbHelper.insertData(book)) {
                Toast.makeText(AddActivity.this, "Книга успешно добавлена", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(AddActivity.this, "Ошибка при добавлении книги", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Обработка результата выбора изображения
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imagePath = data.getData(); // Получаем Uri выбранного изображения
            imageView.setImageURI(imagePath); // Устанавливаем изображение в ImageView
        }
    }
}
