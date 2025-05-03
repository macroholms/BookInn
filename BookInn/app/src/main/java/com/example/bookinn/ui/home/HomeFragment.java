package com.example.bookinn.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.RecoverySystem;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookinn.BDCommunicate;
import com.example.bookinn.BottomSheet;
import com.example.bookinn.HotelActivity;
import com.example.bookinn.HotelAdapter;
import com.example.bookinn.Hotels;
import com.example.bookinn.MainActivity;
import com.example.bookinn.ResponseCallBack;
import com.example.bookinn.databinding.FragmentHomeBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import android.util.Base64;
import java.util.HashSet;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HomeFragment extends Fragment implements BottomSheet.OnFilterSelectedListener {

    private FragmentHomeBinding binding;

    private HotelAdapter adapter;
    private List<Hotels> hotelsList = new ArrayList<>();
    private List<Hotels> originalHotelsList = new ArrayList<>();
    HotelAdapter.OnHotelClickListener onHotelClickListener;
    RecyclerView rv;
    SearchView searchView;// Сохраняем оригинальный список

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        rv = binding.hotelsRecyclerView;
        searchView = binding.search;

        BDCommunicate communicator = new BDCommunicate();

        onHotelClickListener= new HotelAdapter.OnHotelClickListener() {
            @Override
            public void onHotelClick(Hotels hotel, int position) {
                Intent intent = new Intent(requireContext(), HotelActivity.class);
                intent.putExtra("HOTEL_ID", hotel.getId());
                intent.putExtra("HOTEL_PHOTO", hotel.getPhoto());
                intent.putExtra("HOTEL_NAME", hotel.getName());

                startActivity(intent);
                requireActivity().finish();
            }
        };

        new Thread(() -> {
            try {
                communicator.getAllHotels(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("!", e.toString());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            JSONArray array = new JSONArray(response.body().string());
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject obj = array.getJSONObject(i);
                                hotelsList.add(new Hotels(
                                        obj.getInt("hotel_id"),
                                        obj.getString("name"),
                                        obj.getString("description"),
                                        obj.getString("address"),
                                        obj.getString("city"),
                                        obj.getString("country"),
                                        obj.getDouble("star_rating"),
                                        obj.getString("email"),
                                        obj.getString("phone"),
                                        Base64.decode(obj.getString("hotel_photo"),
                                                Base64.DEFAULT)
                                ));
                            }
                            requireActivity().runOnUiThread(() -> {
                                originalHotelsList.addAll(hotelsList); // Сохраняем оригинальный список
                                adapter = new HotelAdapter(getContext(), hotelsList, onHotelClickListener);
                                rv.setAdapter(adapter);
                                svF(adapter);
                            });
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();

        // Открываем BottomSheet при нажатии на кнопку фильтрации
        binding.filter.setOnClickListener(v -> {
            BottomSheet bottomSheet = new BottomSheet();
            bottomSheet.setTargetFragment(this, 1);
            bottomSheet.show(getParentFragmentManager(), "bottom_sheet_tag");
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onFiltersApplied(List<Hotels> filteredHotels) {

            hotelsList.clear();
            HotelAdapter adapter1 = new HotelAdapter(getContext(), filteredHotels, onHotelClickListener);
            rv.setAdapter(adapter1);
            svF(adapter1);
            Log.d("FilterResult", "New list size: " + hotelsList.size());

    }

    public HotelAdapter getAdapter() {
        return adapter;
    }

    public void svF(HotelAdapter a){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                a.getFilter().filter(newText);
                return false;
            }
        });
    }
}