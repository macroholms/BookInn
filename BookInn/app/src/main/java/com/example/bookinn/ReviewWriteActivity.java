package com.example.bookinn;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ReviewWriteActivity extends AppCompatActivity {

    private int chek = 0;
    private ToggleButton active = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);
        Intent i = getIntent();
        BDCommunicate communicate = new BDCommunicate();

        SharedPreferences sp = getSharedPreferences("ACCOUNT", Context.MODE_PRIVATE);

        ImageButton back = findViewById(R.id.button_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReviewWriteActivity.this,
                        HotelActivity.class);
                intent.putExtra("HOTEL_NAME", i.getStringExtra("HOTEL_NAME"));
                intent.putExtra("HOTEL_ID", (i.getIntExtra("HOTEL_ID", 0))).toString();
                intent.putExtra("HOTEL_PHOTO", i.getByteArrayExtra("HOTEL_PHOTO"));
                startActivity(intent);
                finish();
            }
        });

        EditText text = findViewById(R.id.comment_text);

        Button sendBtn = findViewById(R.id.save_comment_button);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chek != 0){
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
                                                data.put("hotel_id", i.getIntExtra("HOTEL_ID", 0));
                                                data.put("rating", chek);
                                                data.put("review_text", text.getText().toString());
                                                data.put("review_date", LocalDateTime.now());
                                                data.put("hotel_response", "");
                                                communicate.postReview(data.toString());
                                                Intent intent = new Intent(ReviewWriteActivity.this,
                                                        HotelActivity.class);
                                                intent.putExtra("HOTEL_NAME", i.getStringExtra("HOTEL_NAME"));
                                                intent.putExtra("HOTEL_ID", (i.getIntExtra("HOTEL_ID", 0))).toString();
                                                intent.putExtra("HOTEL_PHOTO", i.getByteArrayExtra("HOTEL_PHOTO"));
                                                startActivity(intent);
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
                }else{
                    Toast.makeText(getApplicationContext(),
                            "Не выбрана оценка", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void chekSwitch(View view) {
        if (active != null){
            active.setChecked(false);
        }
        active = (ToggleButton) view;
        chek = Integer.parseInt((String) ((ToggleButton) view).getTextOn());
    }
}
