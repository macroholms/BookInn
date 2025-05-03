package com.example.bookinn;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class OnboardingFragment extends Fragment {
    private static final String ARG_POSITION = "position";

    public static OnboardingFragment newInstance(int position) {
        OnboardingFragment fragment = new OnboardingFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout, container, false);

        int position = getArguments().getInt(ARG_POSITION);

        TextView title = view.findViewById(R.id.title);
        TextView description = view.findViewById(R.id.description);
        ImageView imageView = view.findViewById(R.id.onboarding_image); // Находим ImageView

        switch (position) {
            case 0:
                title.setText("Найди Свой Уголок");
                description.setText("Где бы вы ни мечтали оказаться, мы поможем вам найти идеальное место для отдыха.");
                imageView.setImageResource(R.drawable.l); // Картинка 1
                break;
            case 1:
                title.setText("Бронируйте Легко, Путешествуйте Свободно");
                description.setText("Планируйте поездки без лишних усилий — мы сделаем все за вас.");
                imageView.setImageResource(R.drawable.ll); // Картинка 2
                break;
            case 2:
                title.setText("Позвольте Себе Расслабиться");
                description.setText("Мы позаботимся о вашем комфорте, чтобы вы могли наслаждаться каждым моментом отдыха.");
                imageView.setImageResource(R.drawable.lll); // Картинка 3
                break;
        }

        return view;
    }
}