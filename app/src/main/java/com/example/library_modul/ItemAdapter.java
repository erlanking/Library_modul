package com.example.library_modul;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.List;

public class ItemAdapter extends ArrayAdapter<Book> {

    public ItemAdapter(@NonNull Context context, List<Book> books) {
        super(context, R.layout.activity_item, books);
    }

    // Класс ViewHolder для кэширования представлений
    private static class ViewHolder {
        TextView tTitle;
        TextView tAuthor;
        ImageView imageView;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            // Создаем новое представление, если оно не было перепользовано
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.activity_item, parent, false);

            // Инициализируем ViewHolder и кэшируем виджеты
            viewHolder = new ViewHolder();
            viewHolder.tTitle = convertView.findViewById(R.id.tTitle);
            viewHolder.tAuthor = convertView.findViewById(R.id.tAuthor);
            viewHolder.imageView = convertView.findViewById(R.id.imageView);

            // Сохраняем ViewHolder в тег представления
            convertView.setTag(viewHolder);
        } else {
            // Извлекаем ViewHolder из тега, если представление перепользовано
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Получаем текущий объект Book
        Book book = getItem(position);

        if (book != null) {
            // Устанавливаем текст заголовка и автора
            viewHolder.tTitle.setText(book.getTitle());
            viewHolder.tAuthor.setText(book.getAuthor());

            // Загружаем изображение обложки с помощью Glide
            if (book.getCoverImage() != null && !book.getCoverImage().isEmpty()) {
                Glide.with(viewHolder.imageView.getContext())
                        .load(Uri.parse(book.getCoverImage()))
                        .error(R.drawable.unknown_person) // изображение по умолчанию при ошибке
                        .into(viewHolder.imageView);
            } else {
                // Устанавливаем изображение по умолчанию, если обложка отсутствует
                viewHolder.imageView.setImageResource(R.drawable.unknown_person);
            }
        }

        return convertView;
    }
}
