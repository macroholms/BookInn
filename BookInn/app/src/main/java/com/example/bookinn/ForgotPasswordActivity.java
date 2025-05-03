package com.example.bookinn;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ForgotPasswordActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_sassword);

        BDCommunicate communicate = new BDCommunicate();

        ImageButton back = findViewById(R.id.forgotBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPasswordActivity.this, Login.class));
                finish();
            }
        });

        Button next = findViewById(R.id.NextBTN);
        EditText emailET = findViewById(R.id.foremail);
        next.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new Thread(()->{
                            try {
                                String email = emailET.getText().toString();
                                Log.e("!", email);
                                communicate.getUserWithEmail(email, new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        runOnUiThread(() -> {
                                            Toast.makeText(getApplicationContext(),
                                                    "Пользователь с таким Email не найден",
                                                    Toast.LENGTH_LONG).show();
                                        });
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response)
                                            throws IOException {
                                        String responseBody = response.body().string();
                                        Intent i = new Intent(
                                                ForgotPasswordActivity.this,
                                                PasswordRecoveryActivity.class);
                                        i.putExtra("DATA", responseBody);
                                        startActivity(i);
                                        finish();
                                    }
                                });
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }).start();
                    }
                }
        );
    }
}
