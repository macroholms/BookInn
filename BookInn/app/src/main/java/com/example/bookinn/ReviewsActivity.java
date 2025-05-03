package com.example.bookinn;

import static java.security.AccessController.getContext;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ReviewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        Intent i = getIntent();

        BDCommunicate communicate = new BDCommunicate();
        List<Reviews> reviewsList = new ArrayList<>();
        RecyclerView rv = findViewById(R.id.recycler_reviews);
        final ReviewsAdapter[] adapter = new ReviewsAdapter[1];

        TextView textView = findViewById(R.id.hotel_name);
        textView.setText(i.getStringExtra("HOTEL_NAME"));

        ImageButton backBtn = findViewById(R.id.button_back_rev);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReviewsActivity.this,
                        HotelActivity.class);
                intent.putExtra("HOTEL_NAME", i.getStringExtra("HOTEL_NAME"));
                intent.putExtra("HOTEL_ID", Integer.parseInt(i.getStringExtra("HOTEL_ID")));
                intent.putExtra("HOTEL_PHOTO", i.getByteArrayExtra("HOTEL_PHOTO"));
                startActivity(intent);
                finish();
            }
        });

        Button btn = findViewById(R.id.button_add_comment);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReviewsActivity.this,
                        ReviewWriteActivity.class);
                intent.putExtra("HOTEL_NAME", i.getStringExtra("HOTEL_NAME"));
                intent.putExtra("HOTEL_ID", Integer.parseInt(i.getStringExtra("HOTEL_ID")));
                intent.putExtra("HOTEL_PHOTO", i.getByteArrayExtra("HOTEL_PHOTO"));
                startActivity(intent);
                finish();
            }
        });

        new Thread(() -> {
            try {
                communicate.getReviews(i.getStringExtra("HOTEL_ID"), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("!", e.toString());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try{
                            JSONArray array = new JSONArray(response.body().string());
                            for (int i = 0; i < array.length(); i++){
                                JSONObject obj = array.getJSONObject(i);
                                reviewsList.add(new Reviews(
                                        obj.getInt("hotel_id"),
                                        obj.getString("hotel_name"),
                                        obj.getInt("user_id"),
                                        obj.getString("user_fio"),
                                        obj.getInt("review_id"),
                                        obj.getString("review_text"),
                                        obj.getInt("rating"),
                                        obj.getString("hotel_response"),
                                        LocalDateTime.parse(obj.getString("review_date"))
                                ));
                            }
                            runOnUiThread(() -> {
                                adapter[0] = new ReviewsAdapter(getApplicationContext(), reviewsList);
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
