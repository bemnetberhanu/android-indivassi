package com.example.indiv1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        SharedPreferences prefs = newBase.getSharedPreferences("Languge_Settings", MODE_PRIVATE);
        String language = prefs.getString("Languge", "en");
        Context context = LocaleHelper.setLocale(newBase, language);
        super.attachBaseContext(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnChangelanguge).setOnClickListener(view -> showChangeLanguageDialog());

        // Initialize date picker
        DatePicker datePicker = findViewById(R.id.datepicker);
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

        // Photo picker setup
        ActivityResultLauncher<PickVisualMediaRequest> pickMediaLauncher =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                    if (uri != null) {
                        Log.d("photoPicker", "Selected URI: " + uri);
                    } else {
                        Log.d("photoPicker", "No media selected");
                    }
                });

        // To use: pickMediaLauncher.launch(new PickVisualMediaRequest.Builder().setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE).build());
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
        SharedPreferences.Editor editor = getSharedPreferences("Languge_Settings", MODE_PRIVATE).edit();
        editor.putString("Languge", languageCode);
        editor.putInt("position", position);
        editor.apply();
    }
}