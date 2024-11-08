package com.example.library_modul;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.library_modul.DbHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    ImageButton btnAdd;
    DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        dbHelper = new DbHelper(this);
        listView = findViewById(R.id.listView);
        btnAdd = findViewById(R.id.btnAdd);

        fillData();
        updateListView(); // Обновляем список книг

        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddActivity.class);
            startActivity(intent);
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ContentActivity.class);
                Book book = (Book) parent.getAdapter().getItem(position);
                intent.putExtra("key", book);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateListView(); // Обновляем список при возвращении в активность
    }

    // Метод для обновления списка книг
    private void updateListView() {
        List<Book> books = dbHelper.getData();
        ItemAdapter adapter = new ItemAdapter(this, books);
        listView.setAdapter(adapter);
    }

    // Метод для добавления тестовых данных в базу данных, если она пустая
    void fillData() {
        if (dbHelper.getData().isEmpty()) {
            List<Book> books = new ArrayList<>();
            books.add(new Book("Book 1", "Author 1", 2021, "Genre 1", "cover1.jpg", "This is a short annotation for Book 1."));
            books.add(new Book("Book 2", "Author 2", 2020, "Genre 2", "cover2.jpg", "This is a short annotation for Book 2."));
            books.add(new Book("Book 3", "Author 3", 2022, "Genre 3", "cover3.jpg", "This is a short annotation for Book 3."));

            for (Book book : books) {
                dbHelper.insertData(book);
            }
        }
    }
}
