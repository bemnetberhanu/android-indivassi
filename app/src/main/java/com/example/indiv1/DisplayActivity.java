package com.example.indiv1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.indiv1.databinding.ActicityDisplayBinding;

public class DisplayActivity extends AppCompatActivity {

    ActicityDisplayBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActicityDisplayBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

        // Get data from intent
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String id = intent.getStringExtra("id");
        String date = intent.getStringExtra("date");
        Uri imageUri = intent.getParcelableExtra("imageUri");

        // Initialize views
        TextView nameView = binding.displayName;
        TextView idView = binding.displayId;
        TextView dateView = binding.displayDate;
        ImageView imageView = binding.displayImage;

        // Set data to views
        nameView.setText(getString(R.string.display_name, name));
        idView.setText(getString(R.string.display_id, id));
        dateView.setText(getString(R.string.display_date, date));
        imageView.setImageURI(imageUri);
    }
}