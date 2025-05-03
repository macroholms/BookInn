package com.example.bookinn;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class EdetProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_editprofile);

        BDCommunicate communicate = new BDCommunicate();

        SharedPreferences sp = getSharedPreferences("ACCOUNT", Context.MODE_PRIVATE);

        ImageButton imageButton = findViewById(R.id.BACKBAKBTN);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EdetProfileActivity.this,
                        MainActivity.class));
                finish();
            }
        });

        EditText name, email, phone, login;
        name = findViewById(R.id.nameET);
        email = findViewById(R.id.emailET);
        phone = findViewById(R.id.phoneET);
        login = findViewById(R.id.loginET);

        name.setText(sp.getString("NAME", "John Doe"));
        email.setText(sp.getString("EMAIL", "email"));
        phone.setText(sp.getString("PHONE", "+8 800 555 35 35"));
        login.setText(sp.getString("LOGIN", "JD"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        Button submit = findViewById(R.id.UPDATEBTN);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sp.edit();

                new Thread(() -> {
                    try {
                        communicate.getUserWithEmail(sp.getString("EMAIL", "email"), new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String body = response.body().string();
                                try {
                                    JSONObject object = new JSONObject(body);
                                    JSONObject data = new JSONObject();
                                    data.put("fio", name.getText().toString());
                                    data.put("email", email.getText().toString());
                                    data.put("phone", phone.getText().toString());
                                    data.put("password_hash", object.getString("PasswordHash"));
                                    data.put("registration_date",  LocalDateTime.now());
                                    Log.e("!", String.valueOf(object.getInt("UserId")));
                                    communicate.putUser(String.valueOf(object.getInt("UserId")), data.toString());
                                    editor.putString("LOGIN", login.getText().toString());
                                    editor.putString("EMAIL", email.getText().toString());
                                    editor.putString("NAME", name.getText().toString());
                                    editor.putString("PHONE", phone.getText().toString());
                                    editor.commit();
                                    startActivity(new Intent(EdetProfileActivity.this, MainActivity.class));
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
}
