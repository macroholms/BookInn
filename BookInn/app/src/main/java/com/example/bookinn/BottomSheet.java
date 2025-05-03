package com.example.bookinn;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookinn.ui.home.HomeFragment;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class BottomSheet extends BottomSheetDialogFragment {

    private Button buttonReset;
    private Button buttonApplyFilter;

    private EditText editTextCity;

    private ToggleButton toggleButton5;
    private ToggleButton toggleButton4;
    private ToggleButton toggleButton3;
    private ToggleButton toggleButton2;
    private ToggleButton toggleButton1;

    private ToggleButton preference1;
    private ToggleButton preference2;
    private ToggleButton preference3;
    private ToggleButton preference4;
    private ToggleButton preference5;

    private OnFilterSelectedListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.filter, container, false);

        buttonReset = v.findViewById(R.id.button_reset);
        buttonApplyFilter = v.findViewById(R.id.button_apply_filter);

        editTextCity = v.findViewById(R.id.editText_city);

        toggleButton5 = v.findViewById(R.id.toggleButton5);
        toggleButton4 = v.findViewById(R.id.toggleButton4);
        toggleButton3 = v.findViewById(R.id.toggleButton3);
        toggleButton2 = v.findViewById(R.id.toggleButton2);
        toggleButton1 = v.findViewById(R.id.toggleButton1);

        preference1 = v.findViewById(R.id.preference1);
        preference2 = v.findViewById(R.id.preference2);
        preference3 = v.findViewById(R.id.preference3);
        preference4 = v.findViewById(R.id.preference4);
        preference5 = v.findViewById(R.id.preference5);

        // Назначение слушателя для кнопки "Применить фильтр"
        buttonApplyFilter.setOnClickListener(v1 -> applyFilters());

        // Назначение слушателя для кнопки "Сбросить"
        buttonReset.setOnClickListener(v1 -> resetFilters());

        return v;
    }

    private void applyFilters() {
        // Собираем выбранные фильтры
        String city = editTextCity.getText().toString().trim();
        HashSet<Integer> starRatings = new HashSet<>();
        if (toggleButton5.isChecked()) starRatings.add(5);
        if (toggleButton4.isChecked()) starRatings.add(4);
        if (toggleButton3.isChecked()) starRatings.add(3);
        if (toggleButton2.isChecked()) starRatings.add(2);
        if (toggleButton1.isChecked()) starRatings.add(1);

        HashSet<String> facilities = new HashSet<>();
        if (preference1.isChecked()) facilities.add("Wifi");
        if (preference2.isChecked()) facilities.add("Gym");
        if (preference3.isChecked()) facilities.add("Smoking Arena");
        if (preference4.isChecked()) facilities.add("Wellness");
        if (preference5.isChecked()) facilities.add("Swimming Pool");

        // Отправляем фильтры обратно в HomeFragment
        if (listener != null) {
            List<Hotels> filteredList = filterHotels(city, starRatings, facilities);
            listener.onFiltersApplied(filteredList);
        }
        dismiss(); // Закрываем диалог
    }

    private void resetFilters() {
        editTextCity.setText("");
        toggleButton5.setChecked(false);
        toggleButton4.setChecked(false);
        toggleButton3.setChecked(false);
        toggleButton2.setChecked(false);
        toggleButton1.setChecked(false);
        preference1.setChecked(false);
        preference2.setChecked(false);
        preference3.setChecked(false);
        preference4.setChecked(false);
        preference5.setChecked(false);
        applyFilters();
    }

    private List<Hotels> filterHotels(String city, HashSet<Integer> starRatings, HashSet<String> facilities) {
        List<Hotels> filteredHotels = new ArrayList<>();

        Fragment target = getTargetFragment();
        if (target instanceof HomeFragment) {
            HotelAdapter adapter = ((HomeFragment) target).getAdapter();
            List<Hotels> fullList = adapter.getHotelsList(); // Должен быть геттер

            for (Hotels hotel : fullList) {
                boolean matchesCity = city.isEmpty() || hotel.getCity().toLowerCase().contains(city.toLowerCase());
                boolean matchesStars = starRatings.isEmpty() || starRatings.contains((int)Math.round(hotel.getStarRating()));

                boolean matchesFacilities = true;
                String description = hotel.getDescription();
                if (description == null) description = "";

                for (String facility : facilities) {
                    if (!description.toLowerCase().contains(facility.toLowerCase())) {
                        matchesFacilities = false;
                        break;
                    }
                }

                if (matchesCity && matchesStars && matchesFacilities) {
                    filteredHotels.add(hotel);
                }
            }
        }

        return filteredHotels;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        Fragment target = getTargetFragment();
        if (target instanceof OnFilterSelectedListener) {
            listener = (OnFilterSelectedListener) target;
        } else {
            throw new RuntimeException(target != null
                    ? target.getClass().getName() + " must implement OnFilterSelectedListener"
                    : "Target fragment is null. Did you call setTargetFragment()?");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnFilterSelectedListener {
        void onFiltersApplied(List<Hotels> filteredHotels);
    }
}