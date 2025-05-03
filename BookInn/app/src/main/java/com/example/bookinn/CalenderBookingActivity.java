package com.example.bookinn;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalenderBookingActivity extends AppCompatActivity {

    String[] pMethods = { "Paypal", "CreditCard", "Cash" };

    private TextView textViewStartDate, textViewEndDate, textViewTotalPrice;
    private CalendarView calendarView;
    private TextView roomCountText, guestCountText;

    private long startDate = 0;
    private long endDate = 0;
    private double totalPrice = 0;

    private int numberOfRooms = 1;
    private int numberOfGuests = 1;

    private boolean isStartDateSelected = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_bookingform);

        Intent i = getIntent();
        Rooms rooms = Rooms.fromString(i.getStringExtra("ROOM"));

        Spinner spinner = findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item,
                pMethods);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        textViewStartDate = findViewById(R.id.textViewStartDate);
        textViewEndDate = findViewById(R.id.textViewEndDate);
        textViewTotalPrice = findViewById(R.id.totalPriceText);
        calendarView = findViewById(R.id.calendarView);
        roomCountText = findViewById(R.id.roomCountText);
        guestCountText = findViewById(R.id.guestCountText);

        ImageButton btnMinusRoom = findViewById(R.id.btnMinusRoom);
        ImageButton btnPlusRoom = findViewById(R.id.btnPlusRoom);
        ImageButton btnMinusGuest = findViewById(R.id.btnMinusGuest);
        ImageButton btnPlusGuest = findViewById(R.id.btnPlusGuest);
        Button button = findViewById(R.id.continueButton);

        ImageButton backbtn = findViewById(R.id.BackBTN);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalenderBookingActivity.this,
                        HotelActivity.class);
                intent.putExtra("HOTEL_NAME", i.getStringExtra("HOTEL_NAME"));
                intent.putExtra("HOTEL_ID", Integer.parseInt(i.getStringExtra("HOTEL_ID")));
                intent.putExtra("HOTEL_PHOTO", i.getByteArrayExtra("HOTEL_PHOTO"));
                startActivity(intent);
                finish();
            }
        });

        btnMinusRoom.setOnClickListener(v -> {
            if (numberOfRooms > 1) {
                numberOfRooms--;
                updateCountsAndPrice(rooms);
            }
        });

        btnPlusRoom.setOnClickListener(v -> {
            numberOfRooms++;
            updateCountsAndPrice(rooms);
        });

        btnMinusGuest.setOnClickListener(v -> {
            if (numberOfGuests > 1) {
                numberOfGuests--;
                updateGuestText();
            }
        });

        btnPlusGuest.setOnClickListener(v -> {
            if (numberOfGuests < rooms.getCapacity() * numberOfRooms) {
                numberOfGuests++;
                updateGuestText();
            } else {
                Toast.makeText(this, "Превышено максимальное число гостей", Toast.LENGTH_SHORT).show();
            }
        });

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar selectedCal = Calendar.getInstance();
            selectedCal.set(year, month, dayOfMonth);
            long selectedDateInMillis = selectedCal.getTimeInMillis();

            if (!isStartDateSelected) {
                startDate = selectedDateInMillis;
                textViewStartDate.setText(formatDate(startDate));
                isStartDateSelected = true;
                Toast.makeText(getApplicationContext(), "Теперь выберите дату выезда", Toast.LENGTH_SHORT).show();
            } else {
                if (selectedDateInMillis < startDate) {
                    Toast.makeText(getApplicationContext(), "Дата выезда не может быть раньше даты заезда", Toast.LENGTH_SHORT).show();
                    return;
                }

                endDate = selectedDateInMillis;
                textViewEndDate.setText(formatDate(endDate));
                updateCountsAndPrice(rooms);
                isStartDateSelected = false;
            }
        });

        button.setOnClickListener(v -> {
            if (startDate != 0 && endDate != 0) {
                Intent intent = new Intent(CalenderBookingActivity.this,
                        BookingSummaryActivity.class);
                intent.putExtra("CHECKIN", formatDate(startDate));
                intent.putExtra("CHECKOUT", formatDate(endDate));
                intent.putExtra("TOTAL", totalPrice);
                intent.putExtra("HOTEL_NAME", i.getStringExtra("HOTEL_NAME"));
                intent.putExtra("HOTEL_ID", i.getStringExtra("HOTEL_ID"));
                intent.putExtra("HOTEL_PHOTO", i.getByteArrayExtra("HOTEL_PHOTO"));
                intent.putExtra("ROOM", rooms.toString());
                intent.putExtra("NUM_ROOMS", numberOfRooms);
                intent.putExtra("NUM_GUESTS", numberOfGuests);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(CalenderBookingActivity.this, "Выберите обе даты", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateCountsAndPrice(Rooms room) {
        if (startDate == 0 || endDate == 0) return;

        long diffInMillis = endDate - startDate;
        int days = (int) (diffInMillis / (1000 * 60 * 60 * 24));
        totalPrice = days * room.getPrice_per_night() * numberOfRooms;

        textViewTotalPrice.setText("Total: " + String.format("%.2f $", totalPrice));
        roomCountText.setText(String.valueOf(numberOfRooms));

        // Ограничиваем гостей
        if (numberOfGuests > room.getCapacity() * numberOfRooms) {
            numberOfGuests = room.getCapacity() * numberOfRooms;
            updateGuestText();
            Toast.makeText(this, "Количество гостей ограничено", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateGuestText() {
        guestCountText.setText(String.valueOf(numberOfGuests));
    }

    private String formatDate(long dateInMillis) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        return sdf.format(new Date(dateInMillis));
    }
}