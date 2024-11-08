package com.example.library_modul;

import android.content.Intent;

import java.io.Serializable;

public class Book  implements Serializable {
    private int id;
    private String title;
    private String author;
    private int year;
    private String genre;
    private String coverImage;
    private String annotation; // Добавлено поле аннотации

    public Book(String title, String author, int year, String genre, String coverImage, String annotation) {
        this.title = title;
        this.author = author;
        this.year = year;
        this.genre = genre;
        this.coverImage = coverImage;
        this.annotation = annotation; // Инициализация аннотации
    }

    // Геттеры и сеттеры для всех полей, включая annotation

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getAnnotation() {
        return annotation; // Геттер для аннотации
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation; // Сеттер для аннотации
    }


}

