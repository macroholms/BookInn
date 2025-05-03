package com.example.bookinn;

import androidx.annotation.NonNull;

import java.util.Base64;

public class Rooms {
    private int room_id;
    private int hotel_id;
    private String room_type_name;
    private String description;
    private int capacity;
    private int quantity_available;
    private Double price_per_night;
    private int size;
    private byte[] room_photo;

    public Rooms(int room_id, int hotel_id, String room_type_name, String description,
                 int capacity, int quantity_available, Double price_per_night, int size,
                 byte[] room_photo) {
        this.room_id = room_id;
        this.hotel_id = hotel_id;
        this.room_type_name = room_type_name;
        this.description = description;
        this.capacity = capacity;
        this.quantity_available = quantity_available;
        this.price_per_night = price_per_night;
        this.size = size;
        this.room_photo = room_photo;
    }

    public Rooms() {}

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public int getHotel_id() {
        return hotel_id;
    }

    public void setHotel_id(int hotel_id) {
        this.hotel_id = hotel_id;
    }

    public String getRoom_type_name() {
        return room_type_name;
    }

    public void setRoom_type_name(String room_type_name) {
        this.room_type_name = room_type_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getQuantity_available() {
        return quantity_available;
    }

    public void setQuantity_available(int quantity_available) {
        this.quantity_available = quantity_available;
    }

    public Double getPrice_per_night() {
        return price_per_night;
    }

    public void setPrice_per_night(Double price_per_night) {
        this.price_per_night = price_per_night;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public byte[] getRoom_photo() {
        return room_photo;
    }

    public void setRoom_photo(byte[] room_photo) {
        this.room_photo = room_photo;
    }

    public String getInfo(){
        return "Size: " + String.valueOf(size) + ", " + "Price: " + String.valueOf(price_per_night)
                +  " $";
    }

    @NonNull
    @Override
    public String toString() {
        String photoBase64 = room_photo != null ? Base64.getEncoder().encodeToString(room_photo) : "";
        return room_id + "," +
                hotel_id + "," +
                escapeCommas(room_type_name) + "," +
                escapeCommas(description) + "," +
                capacity + "," +
                quantity_available + "," +
                price_per_night + "," +
                size + "," +
                photoBase64;
    }

    public static Rooms fromString(String str) {
        Rooms room = new Rooms();
        String[] parts = str.split(",", 9);

        room.room_id = Integer.parseInt(parts[0]);
        room.hotel_id = Integer.parseInt(parts[1]);
        room.room_type_name = unescapeCommas(parts[2]);
        room.description = unescapeCommas(parts[3]);
        room.capacity = Integer.parseInt(parts[4]);
        room.quantity_available = Integer.parseInt(parts[5]);
        room.price_per_night = Double.parseDouble(parts[6]);
        room.size = Integer.parseInt(parts[7]);

        if (parts.length > 8 && !parts[8].isEmpty()) {
            room.room_photo = Base64.getDecoder().decode(parts[8]);
        } else {
            room.room_photo = new byte[0];
        }

        return room;
    }

    private static String escapeCommas(String s) {
        return s == null ? "" : s.replace("\\", "\\\\").replace(",", "\\,");
    }

    private static String unescapeCommas(String s) {
        return s == null ? "" : s.replace("\\,", ",").replace("\\\\", "\\");
    }
}
