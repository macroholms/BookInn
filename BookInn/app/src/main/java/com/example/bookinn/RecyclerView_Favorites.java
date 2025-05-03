package com.example.bookinn;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerView_Favorites {
    // 1. Модель данных
    public class Hotel {
        private String name;
        private String location;
        private String price;
        private double rating;
        private int imageResId;
        private boolean isLiked;

        public Hotel(String name, String location, String price, double rating, int imageResId) {
            this.name = name;
            this.location = location;
            this.price = price;
            this.rating = rating;
            this.imageResId = imageResId;
            this.isLiked = false;
        }

        // Геттеры и сеттеры
        public String getName() { return name; }
        public String getLocation() { return location; }
        public String getPrice() { return price; }
        public double getRating() { return rating; }
        public int getImageResId() { return imageResId; }
        public boolean isLiked() { return isLiked; }
        public void setLiked(boolean liked) { isLiked = liked; }
    }

    // 2. Адаптер
    public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.ViewHolder> {

        private List<Hotel> hotels;
        private Context context;

        public HotelAdapter(List<Hotel> hotels, Context context) {
            this.hotels = hotels;
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.favorites_element, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Hotel hotel = hotels.get(position);

            // Установка данных
            holder.nameTextView.setText(hotel.getName());
            holder.locationTextView.setText(hotel.getLocation());
            holder.priceTextView.setText(hotel.getPrice());
            holder.ratingTextView.setText(String.valueOf(hotel.getRating()));
            holder.hotelImageView.setImageResource(hotel.getImageResId());

            // Обработка лайка
            holder.likeButton.setImageResource(
                    hotel.isLiked() ? R.drawable.like_filled : R.drawable.like
            );

            holder.likeButton.setOnClickListener(v -> {
                boolean newLikeState = !hotel.isLiked();
                hotel.setLiked(newLikeState);
                holder.likeButton.setImageResource(
                        newLikeState ? R.drawable.like_filled : R.drawable.like
                );
            });
        }

        @Override
        public int getItemCount() {
            return hotels.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView hotelImageView;
            TextView nameTextView;
            TextView locationTextView;
            TextView priceTextView;
            TextView ratingTextView;
            ImageButton likeButton;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                hotelImageView = itemView.findViewById(R.id.hotel);
                nameTextView = itemView.findViewById(R.id.nameHotel);
                locationTextView = itemView.findViewById(R.id.location);
                priceTextView = itemView.findViewById(R.id.price_for_night);
                ratingTextView = itemView.findViewById(R.id.rate);
                likeButton = itemView.findViewById(R.id.like);
            }
        }
    }

    // 3. Использование в Activity/Fragment
    public class MainActivity extends AppCompatActivity {

        private RecyclerView recyclerView;
        private HotelAdapter adapter;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.fragment_favorites);

            // Инициализация RecyclerView
            recyclerView = findViewById(R.id.favorites_recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            // Создание тестовых данных
            List<Hotel> hotels = new ArrayList<>();
            hotels.add(new Hotel("Grand Plaza", "New York", "$200/night", 4.8, R.drawable.l));
            hotels.add(new Hotel("Beach Resort", "Miami", "$350/night", 4.5, R.drawable.l));
            // Добавьте другие отели

            // Установка адаптера
            adapter = new HotelAdapter(hotels, this);
            recyclerView.setAdapter(adapter);
        }
    }
}
