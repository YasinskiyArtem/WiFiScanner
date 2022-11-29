package com.example.wifiscanner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

public class Info extends AppCompatActivity {

    String List_elem [] = {
            "Name",
            "Security",
            "Level",
            "Bssid",
            "Frequency",
            "Timestamp"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.infowifi);

        Bundle bundle = getIntent().getExtras();
        Log.d("Second", bundle.getString("Name"));
        TextView name,security, level, bssid, freq, time;
        name = findViewById(R.id.name);
        security = findViewById(R.id.Security);
        level = findViewById(R.id.level);
        bssid = findViewById(R.id.bssid);
        freq = findViewById(R.id.freq);
        time = findViewById(R.id.time);

        TextView elements[] = new TextView[]{name, security, level, bssid, freq, time};
        Log.d("MASS", elements.toString());

        for(int i = 0; i!= 6; i++){
            if(List_elem[i].equals("Level")){
                int lev = Integer.parseInt(bundle.getString(List_elem[i]));
                if (lev>-50){
                    elements[i].setText("Отличный");
                } else if (lev <= -50 & lev>-80){
                    elements[i].setText("Нормальный");
                } else if (lev <= -80){
                    elements[i].setText("Плохой");
                }
            }
            else{
                elements[i].setText(bundle.getString(List_elem[i]));
            }
        }
    }
}