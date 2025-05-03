package com.example.bookinn;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Login extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        BDCommunicate communicate = new BDCommunicate();

        Button signUpBtn = (Button)findViewById(R.id.signup_button);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this,  RegistrationActivity.class);
                startActivity(i);
                finish();
            }
        });

        Button forgotPass = findViewById(R.id.forgot_pas);

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, ForgotPasswordActivity.class));
                finish();
            }
        });

        Button logIn = findViewById(R.id.logInBtn);
        EditText emailET = findViewById(R.id.email);
        TextInputEditText pass = findViewById(R.id.password);
        final SharedPreferences sp = getSharedPreferences("ACCOUNT", Context.MODE_PRIVATE);
        final SharedPreferences prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        Crypt crypt = new Crypt();

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(() -> {
                    try {
                        communicate.getUserWithEmail(emailET.getText().toString(), new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                runOnUiThread(() -> {
                                    Toast.makeText(getApplicationContext(),
                                            "Пользователь с таким Email не найден",
                                            Toast.LENGTH_LONG).show();
                                });
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException{
                                String responseBody = response.body().string();

                                try {
                                    JSONObject obj = new JSONObject(responseBody);
                                    String cryptPass = crypt.decrypt(
                                            obj.getString("PasswordHash"));
                                    if (cryptPass.equals(
                                            pass.getText().toString()))
                                    {
                                        SharedPreferences.Editor editor = sp.edit();
                                        editor.putString("EMAIL", obj.getString("Email"));
                                        editor.putString("NAME", obj.getString("Fio"));
                                        editor.putString("PHONE", obj.getString("Phone"));
                                        editor.putString("PASSWORD", obj.getString("PasswordHash"));
                                        editor.commit();

                                        SharedPreferences prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor1 = prefs.edit();
                                        editor1.putString("firstStart", "Authorized");
                                        editor1.commit();

                                        Intent i = new Intent(Login.this,
                                                MainActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(),
                                                "Неверный пароль",
                                                Toast.LENGTH_LONG).show();
                                    }

                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                            } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });
                }catch (IOException e){
                        throw new RuntimeException(e);
                    }
                }).start();
            }
        });
    }
}
