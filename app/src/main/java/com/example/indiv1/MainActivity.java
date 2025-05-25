package com.example.indiv1;

import android.app.Activity;
import android.os.Bundle;

  // Your project's R class
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

public class MainActivity extends AppCompatActivity {



    @Override
    public boolean onCreatePanelMenu(int featureId, @NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.new_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
      switch (item.getItemId()){
          case R.id.setting:
              Toast.makeText(MainActivity.this, "setting", Toast.LENGTH_SHORT).show();
              return true;

          case R.id.languge:
              Toast.makeText(MainActivity.this, "language", Toast.LENGTH_SHORT).show();
              return true;
          default:
              return super.onOptionsItemSelected(item);

      }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


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
}