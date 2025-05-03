package com.example.bookinn.ui.dashboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookinn.BDCommunicate;
import com.example.bookinn.Bookings;
import com.example.bookinn.BookingsAdapter;
import com.example.bookinn.HotelActivity;
import com.example.bookinn.ReviewWriteActivity;
import com.example.bookinn.Reviews;
import com.example.bookinn.ReviewsAdapter;
import com.example.bookinn.Rooms;
import com.example.bookinn.databinding.FragmentDashboardBinding;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        BDCommunicate communicate = new BDCommunicate();
        SharedPreferences sp = getContext().getSharedPreferences("ACCOUNT", Context.MODE_PRIVATE);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final RecyclerView rv = binding.dashboard;
        List<Bookings> bookingsList = new ArrayList<>();
        List<Rooms> roomsList = new ArrayList<>();
        BookingsAdapter adapter = new BookingsAdapter(getContext(), bookingsList, roomsList);
        rv.setAdapter(adapter);

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
                                    communicate.getBookingsWithUser(String.valueOf(object.getInt("UserId")), new Callback() {
                                        @Override
                                        public void onFailure(Call call, IOException e) {

                                        }

                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {
                                            String rArray = response.body().string();
                                            try {
                                                JSONArray array = new JSONArray(rArray);
                                                for (int i = 0; i < array.length(); i++){
                                                    JSONObject object1 = array.getJSONObject(i);
                                                    bookingsList.add(new Bookings(
                                                            object1.getInt("BookingId"),
                                                            object1.getInt("UserId"),
                                                            object1.getInt("RoomId"),
                                                            LocalDateTime.parse(object1.getString("CheckInDate")),
                                                            LocalDateTime.parse(object1.getString("CheckOutDate")),
                                                            object1.getInt("GuestsNumber"),
                                                            object1.getInt("TotalPrice"),
                                                            object1.getInt("BookingStatus"),
                                                            LocalDateTime.parse(object1.getString("CreatedAt")),
                                                            ""
                                                    ));
                                                    communicate.getRoomWithRoom(String.valueOf(object1.getInt("RoomId")), new Callback() {
                                                        @Override
                                                        public void onFailure(Call call, IOException e) {

                                                        }

                                                        @Override
                                                        public void onResponse(Call call, Response response) throws IOException {
                                                            String rBody = response.body().string();

                                                            try {
                                                                JSONArray arr = new JSONArray(rBody);
                                                                JSONObject obj = arr.getJSONObject(0);
                                                                roomsList.add(new Rooms(
                                                                        obj.getInt("room_id"),
                                                                        obj.getInt("hotel_id"),
                                                                        obj.getString("room_type_name"),
                                                                        obj.getString("description"),
                                                                        obj.getInt("capacity"),
                                                                        obj.getInt("quantity_available"),
                                                                        obj.getDouble("price_per_night"),
                                                                        obj.getInt("size"),
                                                                        Base64.decode(obj.getString("room_photo"),
                                                                                Base64.DEFAULT)
                                                                ));
                                                                getActivity().runOnUiThread(() -> {
                                                                    adapter.notifyDataSetChanged();
                                                                });
                                                            } catch (JSONException e) {
                                                                throw new RuntimeException(e);
                                                            }
                                                        }
                                                    });
                                                }
                                            } catch (JSONException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }
                                    });
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }).start();
        return root;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}