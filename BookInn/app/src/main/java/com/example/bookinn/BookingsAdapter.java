package com.example.bookinn;

import static java.lang.String.format;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class BookingsAdapter extends RecyclerView.Adapter<BookingsAdapter.ViewHolder> {
    private Context context;
    private List<Bookings> bookingList;
    private List<Rooms> roomsList;

    public BookingsAdapter(Context context, List<Bookings> bookingList, List<Rooms> rooms) {
        this.context = context;
        this.bookingList = bookingList;
        this.roomsList = rooms;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_booking, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Bookings booking = bookingList.get(position);
        Rooms room = roomsList.get(position);

        // Установка данных
        holder.name_hotel_booked.setText(room.getRoom_type_name());
        holder.checkInText_booked.setText(booking.getCheck_in_date().
                format(DateTimeFormatter.ofPattern("dd LLLL yyyy")));
        holder.checkOutText_booked.setText(booking.getCheck_out_date().
                format(DateTimeFormatter.ofPattern("dd LLLL yyyy")));
        holder.countGuests_booked.setText(String.valueOf(booking.getGuests_number()));
        holder.rooms_booked.setText(String.valueOf(booking.getBooking_status()));
        holder.totalPrice_booked.setText(String.valueOf(booking.getTotal_price()) + " $");

        holder.image_hotel_booked.setImageBitmap(BitmapFactory.decodeByteArray(room.getRoom_photo(),
                0, room.getRoom_photo().length));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image_hotel_booked;
        TextView name_hotel_booked;
        TextView checkInText_booked;
        TextView checkOutText_booked;
        TextView countGuests_booked;
        TextView rooms_booked;
        TextView totalPrice_booked;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_hotel_booked = itemView.findViewById(R.id.image_hotel_booked);
            name_hotel_booked = itemView.findViewById(R.id.name_hotel_booked);
            checkInText_booked = itemView.findViewById(R.id.checkInText_booked);
            checkOutText_booked = itemView.findViewById(R.id.checkOutText_booked);
            countGuests_booked = itemView.findViewById(R.id.countGuests_booked);
            rooms_booked = itemView.findViewById(R.id.rooms_booked);
            totalPrice_booked = itemView.findViewById(R.id.totalPrice_booked);
        }
    }
}
