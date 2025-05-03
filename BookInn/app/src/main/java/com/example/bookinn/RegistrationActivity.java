package com.example.bookinn;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RegistrationActivity extends AppCompatActivity {

    TextView name, login, email, phone;
    TextInputEditText pass, confPass;

    List<ToggleButton> preferences = new ArrayList<>();
    List<String> checked_prefences = new ArrayList<>();

    Crypt crypt = new Crypt();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logup);

        final SharedPreferences sp = getSharedPreferences("ACCOUNT", Context.MODE_PRIVATE);

        preferences.add(findViewById(R.id.preference1));
        preferences.add(findViewById(R.id.preference2));
        preferences.add(findViewById(R.id.preference3));
        preferences.add(findViewById(R.id.preference4));
        preferences.add(findViewById(R.id.preference5));
        preferences.add(findViewById(R.id.type_of_housing1));
        preferences.add(findViewById(R.id.type_of_housing2));
        preferences.add(findViewById(R.id.type_of_housing));
        preferences.add(findViewById(R.id.type_of_housing4));

        name = findViewById(R.id.name);
        login = findViewById(R.id.login);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        pass = findViewById(R.id.password);
        confPass = findViewById(R.id.confPas);

        Button loginBtn = findViewById(R.id.signin_button);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegistrationActivity.this, Login.class);
                startActivity(i);
                finish();
            }
        });

        BDCommunicate communicate = new BDCommunicate();

        Button signUpBtn = findViewById(R.id.signUpBtn);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = pass.getText().toString();
                if (PasswordValid.isValidPassword(password)) {
                    if (password.equals(confPass.getText().toString())){
                        if (isValidEmail(email.getText().toString())){
                            if (!login.getText().toString().isEmpty() ||
                                    phone.getText().toString().isEmpty()){
                                String passCrypt;
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("LOGIN", login.getText().toString());
                                editor.putString("EMAIL", email.getText().toString());
                                editor.putString("NAME", name.getText().toString());
                                editor.putString("PHONE", phone.getText().toString());
                                try{
                                    passCrypt = crypt.encrypt(password);
                                    editor.putString("PASSWORD", passCrypt);
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                                getCheckedPrefs();
                                editor.putString("PREFS", String.join(",",
                                        checked_prefences));
                                editor.commit();
                                new Thread(() -> {
                                    try {
                                        communicate.postUserToDb("{\r\n  \"fio\": \" " + name.getText().toString()  +
                                                "\",\r\n  \"email\": \"" + email.getText().toString() +
                                                "\",\r\n  \"phone\": \"" +  phone.getText().toString() +
                                                "\",\r\n  \"password_hash\": \"" + passCrypt +
                                                "\",\r\n  \"role_id\": 3,\r\n  \"registration_date\": \"" +
                                                java.time.Instant.now().toString() + "\"\r\n}");
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                }).start();
                                SharedPreferences prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor1 = prefs.edit();
                                editor1.putString("firstStart", "Authorized");
                                editor1.commit();

                                Intent i = new Intent(RegistrationActivity.this,
                                        MainActivity.class);
                                startActivity(i);
                                finish();
                            }else{
                                Toast.makeText(getApplicationContext(),
                                        "Не все поля заполнены",
                                        Toast.LENGTH_LONG).show();
                            }
                        }else {
                            Toast.makeText(getApplicationContext(),
                                    "Некоректный адрес электронной почты",
                                    Toast.LENGTH_LONG).show();
                        }
                    }else
                    {
                        Toast.makeText(getApplicationContext(), "Пароли не совпадают",
                                Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Пароль должен содержать:\n" +
                            "- Не менее 8 символов\n" +
                            "- Заглавные и строчные буквы\n" +
                            "- Цифры\n" +
                            "- Специальные символы (!@#$%^&*())", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }

        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void getCheckedPrefs(){
        for (int i = 0; i < preferences.size(); i++){
            if (preferences.get(i).isChecked()){
                checked_prefences.add(preferences.get(i).getText().toString());
            }
        }
    }
}

