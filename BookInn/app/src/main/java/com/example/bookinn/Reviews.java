package com.example.bookinn;

import java.time.LocalDateTime;
import java.util.Date;

public class Reviews {
    public int hotel_id;
    public String hotel_name;
    public int user_id;
    public String user_fio;
    public int review_id;
    public int rating;
    public String review_text;
    public String hotel_response;
    public LocalDateTime review_date;

    public Reviews(int hotel_id, String hotel_name, int user_id, String user_fio,
                   int review_id, String review_text, int rating,
                   String hotel_response, LocalDateTime review_date) {
        this.hotel_id = hotel_id;
        this.hotel_name = hotel_name;
        this.user_id = user_id;
        this.user_fio = user_fio;
        this.review_id = review_id;
        this.review_text = review_text;
        this.rating = rating;
        this.hotel_response = hotel_response;
        this.review_date = review_date;
    }

    public int getHotel_id() {
        return hotel_id;
    }

    public void setHotel_id(int hotel_id) {
        this.hotel_id = hotel_id;
    }

    public String getHotel_name() {
        return hotel_name;
    }

    public void setHotel_name(String hotel_name) {
        this.hotel_name = hotel_name;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_fio() {
        return user_fio;
    }

    public void setUser_fio(String user_fio) {
        this.user_fio = user_fio;
    }

    public int getReview_id() {
        return review_id;
    }

    public void setReview_id(int review_id) {
        this.review_id = review_id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReview_text() {
        return review_text;
    }

    public void setReview_text(String review_text) {
        this.review_text = review_text;
    }

    public LocalDateTime getReview_date() {
        return review_date;
    }

    public void setReview_date(LocalDateTime review_date) {
        this.review_date = review_date;
    }

    public String getHotel_response() {
        return hotel_response;
    }

    public void setHotel_response(String hotel_response) {
        this.hotel_response = hotel_response;
    }
}
