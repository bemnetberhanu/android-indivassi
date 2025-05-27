package com.example.indiv1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import com.example.indiv1.R;
import com.example.indiv1.databinding.ActivityMainBinding;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    private ActivityResultLauncher<Intent> resultLauncher;
    Uri uri;
    ActivityMainBinding binding;

    @Override
    protected void attachBaseContext(Context newBase) {
        SharedPreferences prefs = newBase.getSharedPreferences("Language_Settings", MODE_PRIVATE);
        String language = prefs.getString("Language", "en");
        Log.d("Language", "Current Language: " + language);
        super.attachBaseContext(LocaleHelper.setLocale(newBase, language));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

        binding.btnChangelanguge.setOnClickListener(view -> showChangeLanguageDialog());
        register();
        binding.floatingActionButton.setOnClickListener(ve -> selectImage());
        // Initialize date picker
        DatePicker datePicker = binding.datepicker;
        Calendar today = Calendar.getInstance();
        datePicker.init(
                today.get(Calendar.YEAR),
                today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH),
                (view, year, monthOfYear, dayOfMonth) -> {
                    String msg = "The issued date is " + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
        );



        // Set click listener for submit button
        findViewById(R.id.btnSubmit).setOnClickListener(view -> {
            // Get all input values
            EditText nameInput = findViewById(R.id.input);
            EditText idInput = findViewById(R.id.inputid);
            DatePicker DatePicker = findViewById(R.id.datepicker);

            String name = nameInput.getText().toString();
            String id = idInput.getText().toString();
            String date = DatePicker.getDayOfMonth() + "/" + (DatePicker.getMonth() + 1) + "/" + DatePicker.getYear();

            // Start DisplayActivity with the data
            Intent intent = new Intent(MainActivity.this, DisplayActivity.class);
            intent.putExtra("name", name);
            intent.putExtra("id", id);
            intent.putExtra("date", date);
            intent.putExtra("imageUri", uri);
            startActivity(intent);
        });

        // To use: pickMediaLauncher.launch(new PickVisualMediaRequest.Builder().setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE).build());
    }

    private void selectImage(){
        Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
        resultLauncher.launch(intent);
    }

    private void register(){
        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                try {
                    uri = o.getData().getData();

                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "no image selected", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreatePanelMenu(int featureId, @NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.new_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.setting) {
            Toast.makeText(this, "Setting", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.languge) {
            Toast.makeText(this, "Language", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showChangeLanguageDialog() {
        final String[] languages = {"English", "አማርኛ"};
        final String[] languagecode = {"en", "am"};
        SharedPreferences prefs = getSharedPreferences("Languge_Settings", MODE_PRIVATE);
        int currentSelection = prefs.getInt("position", 0);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getString(R.string.select_language));
        builder.setSingleChoiceItems(languages, currentSelection, (dialog, position) -> {
            setLanguage(languages[position], position);
            recreate(); // recreate activity to apply change
            dialog.dismiss();
        });
        builder.setNegativeButton("Close", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
    private void setLanguage(String languageCode, int position) {

        Log.d("LanguageDebug", "Setting language to: " + languageCode);

        SharedPreferences.Editor editor = getSharedPreferences("Languge_Settings", MODE_PRIVATE).edit();
        editor.putString("Languge", languageCode);
        editor.putInt("position", position);
        editor.apply();

        LocaleHelper.setLocale(this, languageCode);

        Intent refresh = new Intent(this, MainActivity.class);
        refresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(refresh);
        finish();
    }

}