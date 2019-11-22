package com.honeyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {
    @BindView(R.id.image_detailed)
    ImageView image_detailed;
    @BindView(R.id.detail_headline)
    TextView detail_headline;
    @BindView(R.id.text_detail)
    TextView text_detail;
    @BindView(R.id.text_channel_detail)
    TextView text_channel_detail;
    @BindView(R.id.text_date_detail)
    TextView text_date_detail;
    String desc, name, title, date;
    String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if (intent != null) {
            desc = intent.getStringExtra("desc");
            name = intent.getStringExtra("name");
            title = intent.getStringExtra("title");
            date = intent.getStringExtra("date");
            image = intent.getStringExtra("image");
        }
        if (image != null) {
            File file = new File(image);
            Bitmap bmp = BitmapFactory.decodeFile(file.getAbsolutePath());
            image_detailed.setImageBitmap(bmp);
        }
        detail_headline.setText(title);
        text_detail.setText(desc);
        text_channel_detail.setText(name);
        text_date_detail.setText(date.substring(0, 10));
        setFont();
    }
    private void setFont() {
        Typeface typeface0 = Typeface.createFromAsset(getAssets(), "fonts/robotoslabbold.ttf");
        Typeface typeface2 = Typeface.createFromAsset(getAssets(), "fonts/robotoslabregular.ttf");
        detail_headline.setTypeface(typeface0);
        text_channel_detail.setTypeface(typeface2);
        text_date_detail.setTypeface(typeface2);
        text_detail.setTypeface(typeface2);
    }
}