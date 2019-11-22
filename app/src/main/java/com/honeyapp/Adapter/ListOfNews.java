package com.honeyapp.Adapter;


import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.honeyapp.R;
import com.honeyapp.room.NewsDetails;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import javax.inject.Inject;

public class ListOfNews extends RecyclerView.Adapter<ListOfNews.ListOfNewsViewHolder> {
    private ArrayList<NewsDetails> articlesArrayList;
    private setOnCardClick onCardClick;
    String date_str;

    @Inject
    public ListOfNews(setOnCardClick onCardClick) {
        this.onCardClick = onCardClick;
        articlesArrayList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ListOfNewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_news, parent, false);
        return new ListOfNewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListOfNewsViewHolder holder, int position) {
        final Context context = holder.itemView.getContext();
        holder.card_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("gvsfsdgrg", String.valueOf(articlesArrayList.get(position)));
                onCardClick.launchIntent(articlesArrayList.get(position));
            }
        });
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/robotoslabbold.ttf");
        Typeface typeface1 = Typeface.createFromAsset(context.getAssets(), "fonts/robotoslabregular.ttf");
        holder.text_headline.setTypeface(typeface1);
        holder.text_date.setTypeface(typeface1);
        holder.text_channel.setTypeface(typeface);
        holder.text_channel.setText(articlesArrayList.get(position).getName());
        holder.text_headline.setText(articlesArrayList.get(position).getTitle());
        date_str = articlesArrayList.get(position).getPublishedAt().substring(0, 10);
        holder.text_date.setText(date_str);
        try {
            ContextWrapper cw = new ContextWrapper(context);
            File directory = cw.getDir("imageDirectory", Context.MODE_PRIVATE);
            File mypath = new File(directory, "article" + position + ".jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(mypath));
            holder.image_news.setImageBitmap(b);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return articlesArrayList.size();
    }

    class ListOfNewsViewHolder extends RecyclerView.ViewHolder {
        CardView card_head;
        TextView text_headline, text_channel, text_date;
        ImageView image_news;

        public ListOfNewsViewHolder(@NonNull View itemView) {
            super(itemView);
            card_head = itemView.findViewById(R.id.card_head);
            text_headline = itemView.findViewById(R.id.text_headline);
            text_channel = itemView.findViewById(R.id.text_channel);
            text_date = itemView.findViewById(R.id.text_date);
            image_news = itemView.findViewById(R.id.image_news);
        }
    }

    public interface setOnCardClick {
        void launchIntent(NewsDetails articles);
    }

    public void setNews(ArrayList<NewsDetails> news) {
        this.articlesArrayList.addAll(news);
        notifyDataSetChanged();
    }
}
