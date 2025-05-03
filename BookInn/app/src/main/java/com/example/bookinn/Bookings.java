package com.example.bookinn;

import java.time.LocalDateTime;

public class Bookings {
    public int booking_id;
    public int user_id;
    public int room_id;
    public LocalDateTime check_in_date;
    public LocalDateTime check_out_date;
    public int guests_number;
    public int total_price;
    public int booking_status;
    public LocalDateTime created_at;
    public String special_requests;

    public Bookings(int booking_id, int user_id, int room_id, LocalDateTime check_in_date,
                    LocalDateTime check_out_date, int guests_number, int total_price,
                    int booking_status, LocalDateTime created_at, String special_requests) {
        this.booking_id = booking_id;
        this.user_id = user_id;
        this.room_id = room_id;
        this.check_in_date = check_in_date;
        this.check_out_date = check_out_date;
        this.guests_number = guests_number;
        this.total_price = total_price;
        this.booking_status = booking_status;
        this.created_at = created_at;
        this.special_requests = special_requests;
    }

    public int getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(int booking_id) {
        this.booking_id = booking_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public LocalDateTime getCheck_in_date() {
        return check_in_date;
    }

    public void setCheck_in_date(LocalDateTime check_in_date) {
        this.check_in_date = check_in_date;
    }

    public LocalDateTime getCheck_out_date() {
        return check_out_date;
    }

    public void setCheck_out_date(LocalDateTime check_out_date) {
        this.check_out_date = check_out_date;
    }

    public int getGuests_number() {
        return guests_number;
    }

    public void setGuests_number(int guests_number) {
        this.guests_number = guests_number;
    }

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }

    public int getBooking_status() {
        return booking_status;
    }

    public void setBooking_status(int booking_status) {
        this.booking_status = booking_status;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public String getSpecial_requests() {
        return special_requests;
    }

    public void setSpecial_requests(String special_requests) {
        this.special_requests = special_requests;
    }
}
