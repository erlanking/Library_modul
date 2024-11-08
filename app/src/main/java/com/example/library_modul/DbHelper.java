package com.example.library_modul;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(@Nullable Context context) {
        super(context, "books.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE books (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT NOT NULL," +
                "author TEXT NOT NULL," +
                "year INTEGER," +
                "genre TEXT," +
                "cover_image TEXT," +
                "annotation TEXT)"); // Добавлено поле для аннотации
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS books");
        onCreate(db);
    }

    public List<Book> getData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM books", null);
        List<Book> books = new ArrayList<>();

        while (cursor.moveToNext()) {
            Book book = new Book(
                    cursor.getString(1),  // title
                    cursor.getString(2),  // author
                    cursor.getInt(3),     // year
                    cursor.getString(4),  // genre
                    cursor.getString(5),  // cover_image
                    cursor.getString(6)   // annotation
            );
            book.setId(cursor.getInt(0)); // id
            books.add(book);
        }
        cursor.close(); // Закрываем курсор
        return books;
    }

    public Book getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM books WHERE _id=?", new String[]{String.valueOf(id)});

        if (cursor != null && cursor.moveToFirst()) {
            Book book = new Book(
                    cursor.getString(1),  // title
                    cursor.getString(2),  // author
                    cursor.getInt(3),     // year
                    cursor.getString(4),  // genre
                    cursor.getString(5),  // cover_image
                    cursor.getString(6)   // annotation
            );
            book.setId(cursor.getInt(0)); // id
            cursor.close(); // Закрываем курсор
            return book;
        }
        cursor.close(); // Закрываем курсор
        return null;
    }

    public boolean insertData(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put("title", book.getTitle());
        content.put("author", book.getAuthor());
        content.put("year", book.getYear());
        content.put("genre", book.getGenre());
        content.put("cover_image", book.getCoverImage());
        content.put("annotation", book.getAnnotation()); // Добавлено поле для аннотации

        long result = db.insert("books", null, content);
        return result != -1; // Возвращает true, если вставка успешна
    }

    public boolean updateData(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put("title", book.getTitle());
        content.put("author", book.getAuthor());
        content.put("year", book.getYear());
        content.put("genre", book.getGenre());
        content.put("cover_image", book.getCoverImage());
        content.put("annotation", book.getAnnotation());

        long result = db.update("books", content, "_id=?", new String[]{String.valueOf(book.getId())});
        return result != -1;
    }

    public boolean deleteData(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete("books", "_id=?", new String[]{String.valueOf(book.getId())});
        return result != -1;
    }
}