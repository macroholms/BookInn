package com.example.bookinn;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RoomsAdapter extends RecyclerView.Adapter<RoomsAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<Rooms> rooms;

    public interface OnRoomClickListener{
        void onRooomClick(Rooms room, int position);
    }

    private final RoomsAdapter.OnRoomClickListener onClickListener;

    public RoomsAdapter(Context context, List<Rooms> rooms, OnRoomClickListener onClickListener) {
        this.onClickListener = onClickListener;
        this.inflater = LayoutInflater.from(context);
        this.rooms = rooms;
    }

    @NonNull
    @Override
    public RoomsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_room_option, parent, false);
        return new RoomsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomsAdapter.ViewHolder holder, int position) {
        Rooms room = rooms.get(position);
        holder.room_type.setText(room.getRoom_type_name());
        holder.room_info.setText(room.getInfo());
        holder.room_image.setImageBitmap(BitmapFactory.decodeByteArray(room.getRoom_photo(),
                0, room.getRoom_photo().length));
        holder.bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { onClickListener.onRooomClick(room, position);}
        });
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView room_image;
        TextView room_type, room_info;
        Button bookBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            room_image = itemView.findViewById(R.id.room_image);
            room_type = itemView.findViewById(R.id.place_type);
            room_info = itemView.findViewById(R.id.place_info);
            bookBtn = itemView.findViewById(R.id.book_button);
        }
    }
}
