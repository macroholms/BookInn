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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class PasswordRecoveryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_password);

        TextInputEditText pass1 = findViewById(R.id.pasRes);
        TextInputEditText pass2 = findViewById(R.id.confPasRes);
        BDCommunicate communicate = new BDCommunicate();
        Crypt crypt = new Crypt();
        final SharedPreferences sp = getSharedPreferences("ACCOUNT", Context.MODE_PRIVATE);
        final SharedPreferences prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);

        Button btn = findViewById(R.id.ResBTN);
        ImageButton btnBack = findViewById(R.id.ResBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PasswordRecoveryActivity.this, Login.class));
                finish();
            }
        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (PasswordValid.isValidPassword(pass1.getText().toString())){
                        if (pass1.getText().toString().equals(pass2.getText().toString())){
                            String passCrypt = crypt.encrypt(pass1.getText().toString());
                            JSONObject obj = new JSONObject(getIntent().getStringExtra("DATA"));
                            new Thread(() -> {
                                try {
                                    communicate.updateUserData(obj.getString("UserId"),
                                            "{\r\n  \"fio\": \" " + obj.getString("Fio")  +
                                                    "\",\r\n  \"email\": \"" + obj.getString("Email") +
                                                    "\",\r\n  \"phone\": \"" +  obj.getString("Phone") +
                                                    "\",\r\n  \"password_hash\": \"" + passCrypt +
                                                    "\",\r\n  \"registration_date\": \"" +
                                                    obj.getString("RegistrationDate") + "\"\r\n}");
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }).start();
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("EMAIL", obj.getString("Email"));
                            editor.putString("NAME", obj.getString("Fio"));
                            editor.putString("PHONE", obj.getString("Phone"));
                            editor.putString("PASSWORD", passCrypt);
                            editor.commit();

                            SharedPreferences prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor1 = prefs.edit();
                            editor1.putString("firstStart", "Authorized");
                            editor1.commit();

                            Intent i = new Intent(PasswordRecoveryActivity.this,
                                    MainActivity.class);
                            startActivity(i);
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(), "Пароли не совпадают",
                                    Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(getApplicationContext(), "Пароль должен содержать:\n" +
                                "- Не менее 8 символов\n" +
                                "- Заглавные и строчные буквы\n" +
                                "- Цифры\n" +
                                "- Специальные символы (!@#$%^&*())", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }
}
