package com.example.bookinn;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<Reviews> reviews;

    public ReviewsAdapter(Context context, List<Reviews> reviews) {
        this.inflater = LayoutInflater.from(context);
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_review, parent, false);
        return new ReviewsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsAdapter.ViewHolder holder, int position) {
        Reviews review = reviews.get(position);
        holder.name.setText(review.getUser_fio());
        holder.date.setText(review.getReview_date().
                format(DateTimeFormatter.ofPattern("dd LLLL yyyy")));
        holder.rating.setText(String.valueOf(review.getRating()));
        holder.text.setText(review.getReview_text());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, date, rating, text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.reviewer_name);
            date = itemView.findViewById(R.id.review_date);
            rating = itemView.findViewById(R.id.ratingView);
            text = itemView.findViewById(R.id.review_text);
        }
    }
}
