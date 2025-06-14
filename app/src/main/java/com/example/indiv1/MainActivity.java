package com.example.indiv1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
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



            if(savedInstanceState != null){
                binding.input.setText(savedInstanceState.getString("name"));
                binding.inputid.setText(savedInstanceState.getString("id"));
                uri = savedInstanceState.getParcelable("imageUri");

            }

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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        String name = binding.input.getText().toString();
        String id = binding.inputid.getText().toString();
        String date = binding.datepicker.getDayOfMonth() + "/" + (binding.datepicker.getMonth() + 1) + "/" + binding.datepicker.getYear();
        outState.putString("name", name);
        outState.putString("id", id);
        outState.putString("date", date );
        outState.putParcelable("imageUri", uri);

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
        final String[] languageList = {"English", "Amharic"} ;
        SharedPreferences sharedPreferences = getSharedPreferences("LANG", MODE_PRIVATE);
        int item = sharedPreferences.getInt("item", 0);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.select_language));
        builder.setSingleChoiceItems(languageList, item, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i == 0){
                    setLanguage("en",0);
                    recreate();
                }
                else if (i == 1 ){
                    setLanguage("am",1);
                    recreate();
                }
                dialogInterface.dismiss();
            }
        });

        builder.setNegativeButton("close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void setLanguage(String language, int item) {

        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor  editor = getSharedPreferences("LANG", MODE_PRIVATE).edit();
        editor.putString("language",language);
        editor.putInt("item",item);
        editor.apply();
    }

}