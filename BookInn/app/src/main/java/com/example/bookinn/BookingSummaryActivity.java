package com.example.bookinn;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class BookingSummaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_booking_summary);

        BDCommunicate communicate = new BDCommunicate();

        Intent i = getIntent();
        final SharedPreferences sp = getSharedPreferences("ACCOUNT", Context.MODE_PRIVATE);

        String fiotext = sp.getString("NAME", "Jhon Doe");
        String mailtext = sp.getString("EMAIL", "JD@mail.ru");
        String phonetext = sp.getString("PHONE", "7 800 555 35 35");
        TextView total = findViewById(R.id.total);

        TextView fio = findViewById(R.id.fio);
        TextView mail = findViewById(R.id.mail);
        TextView phone = findViewById(R.id.phone);
        TextView checkInText = findViewById(R.id.checkInText);
        TextView checkOutText = findViewById(R.id.checkOutText);
        TextView romType = findViewById(R.id.romType);
        TextView countGuests = findViewById(R.id.countGuests);
        TextView rooms = findViewById(R.id.rooms);
        ImageView image_hotel = findViewById(R.id.image_hotel);

        TextView name_hotel = findViewById(R.id.name_hotel);
        Button btnContinue = findViewById(R.id.btnContinue);

        Rooms rooms1 = Rooms.fromString(i.getStringExtra("ROOM"));

        String checkInDate = i.getStringExtra("CHECKIN");
        String checkOutDate = i.getStringExtra("CHECKOUT");
        String totalText = String.valueOf(i.getDoubleExtra("TOTAL",0));
        String hotelName = i.getStringExtra("HOTEL_NAME");
        byte[] hotelPhoto = i.getByteArrayExtra("HOTEL_PHOTO");
        String roomType = rooms1.getRoom_type_name();
        int numberOfRooms = i.getIntExtra("NUM_ROOMS", 1);
        int numGuests = i.getIntExtra("NUM_GUESTS", 1);

        fio.setText(fiotext);
        mail.setText(mailtext);
        phone.setText(phonetext);
        checkInText.setText(checkInDate);
        checkOutText.setText(checkOutDate);
        romType.setText(roomType);
        countGuests.setText(String.valueOf(numGuests));
        rooms.setText(String.valueOf(numberOfRooms));
        name_hotel.setText(hotelName);
        Bitmap bitmap = BitmapFactory.decodeByteArray(hotelPhoto, 0, hotelPhoto.length);
        image_hotel.setImageBitmap(bitmap);
        total.setText(totalText + " $");

        ImageButton back = findViewById(R.id.BACK);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookingSummaryActivity.this,
                        HotelActivity.class);
                intent.putExtra("HOTEL_NAME", i.getStringExtra("HOTEL_NAME"));
                intent.putExtra("HOTEL_ID", Integer.parseInt(i.getStringExtra("HOTEL_ID")));
                intent.putExtra("HOTEL_PHOTO", i.getByteArrayExtra("HOTEL_PHOTO"));
                startActivity(intent);
                finish();
            }
        });

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(() -> {
                    try {
                        communicate.getUserWithEmail(sp.getString("EMAIL", "email"),
                                new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        String rBody = response.body().string();
                                        try {
                                            JSONObject object = new JSONObject(rBody);
                                            JSONObject data = new JSONObject();
                                            data.put("user_id", object.getInt("UserId"));
                                            data.put("room_id", rooms1.getRoom_id());
                                            data.put("check_in_date", LocalDate.parse(i.getStringExtra("CHECKIN"), formatter));
                                            data.put("check_out_date", LocalDate.parse(i.getStringExtra("CHECKOUT"), formatter));
                                            data.put("guests_number", numGuests);
                                            data.put("total_price",Integer.valueOf(Math.round(Float.valueOf(totalText))));
                                            data.put("booking_status", 1);
                                            data.put("created_at", LocalDateTime.now());
                                            data.put("special_requests", "ok");
                                            communicate.postBooking(data.toString());
                                            NotificationHelper.showSuccessBookingNotification(
                                                    BookingSummaryActivity.this);
                                            startActivity(new Intent(
                                                    BookingSummaryActivity.this,
                                                    MainActivity.class));
                                            finish();
                                        } catch (JSONException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                });
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }).start();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == NotificationHelper.NOTIFICATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                NotificationHelper.showSuccessBookingNotification(this);
            } else {
                // Пользователь отказал в разрешении
                Toast.makeText(this, "Разрешение на уведомления отклонено", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
