package com.example.bookinn;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

public class OnboardingActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private Button btnNext;
    private TextView tvSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        String Start = prefs.getString("firstStart", "firstStart");

        viewPager = findViewById(R.id.viewPager);
        btnNext = findViewById(R.id.next);
        tvSkip = findViewById(R.id.skip);

        // Инициализация ViewPager
        OnboardingAdapter adapter = new OnboardingAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(adapter);

        // Индикатор точек
        DotsIndicator dotsIndicator = findViewById(R.id.dotsIndicator);
        dotsIndicator.setViewPager2(viewPager);

        // Обработчики кликов
        btnNext.setOnClickListener(v -> {
            if(viewPager.getCurrentItem() < 2) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            } else {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("firstStart", "Login");
                editor.apply();
                startActivity(new Intent(this, Login.class));
                finish();
            }
        });

        tvSkip.setOnClickListener(v -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("firstStart", "Login");
            editor.apply();
            startActivity(new Intent(this, Login.class));
            finish();
        });

        // Обновление текста кнопки
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                btnNext.setText(position == 2 ? "Get Started" : "Next");
            }
        });
    }
}
