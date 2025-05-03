package com.example.bookinn;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class HotelActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_details);

        BDCommunicate communicate = new BDCommunicate();

        Intent i = getIntent();

        ImageView imageView = findViewById(R.id.image_hotel_main);
        TextView hotel_name = findViewById(R.id.text_hotel_name);
        TextView hotel_desc = findViewById(R.id.text_hotel_description);
        RecyclerView rv = findViewById(R.id.recycler_rooms);
        ImageButton backBtn = findViewById(R.id.button_back);
        Button reviewsBtn = findViewById(R.id.button_see_reviews1);

        reviewsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HotelActivity.this, ReviewsActivity.class);
                intent.putExtra("HOTEL_NAME", i.getStringExtra("HOTEL_NAME"));
                intent.putExtra("HOTEL_ID", String.valueOf(i.getIntExtra("HOTEL_ID",
                        0)));
                intent.putExtra("HOTEL_PHOTO", i.getByteArrayExtra("HOTEL_PHOTO"));
                startActivity(intent);
                finish();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HotelActivity.this, MainActivity.class));
                finish();
            }
        });
        List<Rooms> roomsList = new ArrayList<>();
        final RoomsAdapter[] adapter = new RoomsAdapter[1];

        imageView.setImageBitmap(BitmapFactory.decodeByteArray(
                i.getByteArrayExtra("HOTEL_PHOTO"),
                0, i.getByteArrayExtra("HOTEL_PHOTO").length));

        new Thread(() -> {
            try {
                communicate.getAHotels(String.valueOf(i.getIntExtra("HOTEL_ID",
                        0)), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(() -> {
                            Toast.makeText(getApplicationContext(), "Произошла ошибка",
                                    Toast.LENGTH_LONG).show();
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String rBody = response.body().string();
                        try {
                            JSONObject object = new JSONObject(rBody);
                            runOnUiThread(() -> {
                                try {
                                    hotel_name.setText(object.getString("Name"));
                                    hotel_desc.setText(object.getString("Description"));
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();

        new Thread(() -> {
            try {
                communicate.getRoomWithHotel(String.valueOf(i.getIntExtra("HOTEL_ID",
                        0)), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try{
                            String rBody = response.body().string();
                            JSONArray array = new JSONArray(rBody);
                            for (int i = 0; i < array.length(); i++){
                                JSONObject obj = array.getJSONObject(i);
                                roomsList.add(new Rooms(
                                        obj.getInt("room_id"),
                                        obj.getInt("hotel_id"),
                                        obj.getString("room_type_name"),
                                        obj.getString("description"),
                                        obj.getInt("capacity"),
                                        obj.getInt("quantity_available"),
                                        obj.getDouble("price_per_night"),
                                        obj.getInt("size"),
                                        Base64.decode(obj.getString("room_photo"),
                                                Base64.DEFAULT)
                                ));
                            }
                            runOnUiThread(() -> {
                                adapter[0] = new RoomsAdapter(getApplicationContext(), roomsList,
                                        new RoomsAdapter.OnRoomClickListener() {
                                    @Override
                                    public void onRooomClick(Rooms room, int position) {
                                        Intent intent = new Intent(HotelActivity.this,
                                                CalenderBookingActivity.class);
                                        intent.putExtra("HOTEL_NAME", i.getStringExtra("HOTEL_NAME"));
                                        intent.putExtra("HOTEL_ID", String.valueOf(i.getIntExtra("HOTEL_ID",
                                                0)));
                                        intent.putExtra("HOTEL_PHOTO", i.getByteArrayExtra("HOTEL_PHOTO"));
                                        intent.putExtra("ROOM", room.toString());
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                                rv.setAdapter(adapter[0]);
                            });
                        }catch (JSONException ex){
                            ex.printStackTrace();
                        }}
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}
