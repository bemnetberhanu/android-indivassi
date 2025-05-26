package com.example.indiv1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.wifi.hotspot2.ConfigParser;
import android.os.Bundle;

 import com.example.indiv1.R; // Your project's R class
import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import androidx.annotation.RequiresApi;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {



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
            Toast.makeText(this, "setting", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.languge) {
            Toast.makeText(this, "language", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        locallanguge();
        findViewById(R.id.btnChangelanguge).setOnClickListener(view ->{
            showChangeLanguageDialog();
        });


        //iniialize date picker from layout
        DatePicker datepicker= findViewById(R.id.datepicker);
        //geting today's date
        Calendar todaydate = Calendar.getInstance();
        //initalize date picker with current date
        datepicker.init(
                todaydate.get(Calendar.YEAR),
                todaydate.get(Calendar.MONTH),
                todaydate.get(Calendar.DAY_OF_MONTH),
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        //display seleceted date in toast message
                        String msg = "t he issued date is"+ dayOfMonth + "/" + monthOfYear + "/" + year;
                        Toast.makeText(MainActivity.this, msg,Toast.LENGTH_SHORT).show();
                    }
                }
        );
        //registering in single mode
        ActivityResultLauncher<PickVisualMediaRequest> pickMidea =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri ->
                {//photo picker
                    if( uri != null){
                        Log.d("photoPicker" ,"selected URI" + uri);
                    }else{
                        Log.d("photopicker", "no midiea selected");
                    }
                });
        //launch photo picker and let user choose
        //pickMidea.launch(new PickVisualMediaRequest.Builder().setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE.build()));
       // onCreateOptionsMenu(`6)
    // public boolean onCreateOptionsMenu(Menu menu){
       //MenuInflater inflater = getMenuInflater();
     // inflater.inflate(R.menu.new_menu, menu);
      // return true;


    //}



    }

    private void showChangeLanguageDialog() {
        final String[] languages = {"English", "Amharic"};
        final String[] languageCodes = {"en", "am"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getString(R.string.select_languge));
        builder.setSingleChoiceItems(languages, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int postion) {
              if(postion == 0){
                  setLanguage("en",0);
                  recreate();
              }else if(postion == 1){
                  setLanguage("am",1);
                  recreate();
              }
              dialog.dismiss();
            }
        });
        builder.setNegativeButton("close",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alterDialog = builder.create();
        alterDialog.show();
    }

    private void setLanguage(String en, int i) {
        Locale locale =  new Locale(en);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration,getBaseContext().getResources().getDisplayMetrics());


        SharedPreferences.Editor editor = getSharedPreferences("Languge_Settings",MODE_PRIVATE).edit();
        editor.putString("Languge",en);
        editor.putInt("position",i);
        editor.apply();

    }

    private void locallanguge(){
        SharedPreferences sharedPreferences = getSharedPreferences("Languge_Settings",MODE_PRIVATE);
        String languge = sharedPreferences.getString("Languge","en");
        int position = sharedPreferences.getInt("position",0);
        setLanguage(languge,position);
    }
}