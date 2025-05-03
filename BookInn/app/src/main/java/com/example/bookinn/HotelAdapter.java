package com.example.bookinn;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.ViewHolder>
        implements Filterable {

    public List<Hotels> getHotelsList() {
        return hotels;
    }

    public interface OnHotelClickListener{
        void onHotelClick(Hotels hotel, int position);
    }

    private final OnHotelClickListener onClickListener;

    private LayoutInflater inflater;
    private final List<Hotels> hotels;
    private final List<Hotels> fullHotels;

    public HotelAdapter(Context context, List<Hotels> hotelsList,
                        OnHotelClickListener onClickListener){
        this.inflater = LayoutInflater.from(context);
        this.hotels = new ArrayList<>(hotelsList);
        this.fullHotels = new ArrayList<>(hotelsList);
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public HotelAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.hotel_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelAdapter.ViewHolder holder, int position) {
        Hotels hotel = hotels.get(position);
        holder.placeholder.setImageBitmap(BitmapFactory.decodeByteArray(hotel.getPhoto(),
                0, hotel.getPhoto().length));
        holder.nameView.setText(hotel.getName());
        holder.location.setText(hotel.location());
        holder.rating.setText(String.valueOf(hotel.getStarRating()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onHotelClick(hotel, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return hotels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView placeholder;
        final TextView nameView, rating, location;
        ViewHolder(View view){
            super(view);
            placeholder = view.findViewById(R.id.hotel_image);
            nameView = view.findViewById(R.id.hotel_name);
            rating = view.findViewById(R.id.rating);
            location = view.findViewById(R.id.location);
        }
    }

    public void updateList(List<Hotels> newList) {
        hotels.clear();
        hotels.addAll(newList);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Hotels> filteredList = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(fullHotels);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    for (Hotels hotel : fullHotels) {
                        if (hotel.getName().toLowerCase().contains(filterPattern) ||
                                hotel.getAddress().toLowerCase().contains(filterPattern) ||
                                hotel.getCity().toLowerCase().contains(filterPattern)) {
                            filteredList.add(hotel);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                hotels.clear();
                hotels.addAll((List) results.values);
                notifyDataSetChanged();
            }
        };
    }
}
