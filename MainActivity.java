package com.example.wifiscanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public Element [] nets;
    public WifiManager wifiManager;
    public List<ScanResult> wifiList;
    private ArrayAdapter adapter;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ListView listView;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Разрешения
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[] { Manifest.permission.ACCESS_WIFI_STATE},
                    10);
        }

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CHANGE_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[] { Manifest.permission.CHANGE_WIFI_STATE},
                    10);
        }

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[] { Manifest.permission.ACCESS_FINE_LOCATION},
                    10);
        }

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[] { Manifest.permission.ACCESS_COARSE_LOCATION},
                    10);
        }

        setContentView(R.layout.activity_main); // Инициализация главного экрана приложения

        Button Scan_button = findViewById(R.id.Scanning);
        Scan_button.setOnClickListener(new View.OnClickListener() { //Инициализация метода нажатия кнопки запуска сканирования
            @Override
            public void onClick(View view) {

                try{
                    Scan_WiFi();
                }
                catch (Exception e){
                    Snackbar.make(view, "Error: " + e.toString() +
                                    ".\nTry to restart the application :)", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        listView = findViewById(R.id.List_WiFi);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("VIEW", i + " " + l);
                Intent intent = new Intent(MainActivity.this, Info.class);
                intent.putExtra("Name", nets[i].getNetworkName());
                intent.putExtra("Security", nets[i].getSec());
                intent.putExtra("Level", nets[i].getLvl());
                intent.putExtra("Bssid", nets[i].getBssid());
                intent.putExtra("Frequency", nets[i].getFreq());
                intent.putExtra("Timestamp", nets[i].getTimeStamp());
                startActivity(intent);
                 //finish();
            }
        });
    }

    private void Scan_WiFi(){
        arrayList.clear();
        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        this.wifiManager = (WifiManager)getApplicationContext().getSystemService(
                Context.WIFI_SERVICE);
        this.wifiManager.startScan();
        this.wifiList = this.wifiManager.getScanResults();

        if (!wifiManager.isWifiEnabled()) {
            Toast.makeText(this, "WiFi is disabled. Enabling...",
                    Toast.LENGTH_LONG).show();
            wifiManager.setWifiEnabled(true);
        }
        if(this.wifiManager.getScanResults().isEmpty()){
            return;
        }
        this.nets = new Element[wifiList.size()]; // Создаю ссылку на количество эл-тов массива, равное кол-ву сетей

        Log.d("RESULT", wifiList.toString());

        count = 0;
        String ssid = "";
        String bssid = "";
        String frequency = "";
        String timestamp = "";
        String security = "";
        String level = "";

        for (int i = 0; i< wifiList.size(); i++){
            String entry = wifiList.get(i).toString();
            String[] vector_item = entry.split(",");
            String network_name = vector_item[0];
            String network_bssid = vector_item[1];
            String network_cap = vector_item[2];
            String network_signal_lvl = vector_item[3];
            String network_freq = vector_item[4];
            String network_time_stamp = vector_item[5];

            String[] name_wifi = network_name.split(": ");
            if(name_wifi.length == 2){
                ssid = network_name.split(": ")[1];
                bssid = network_bssid.split(": ")[1];
                frequency = network_freq.split(": ")[1];
                timestamp = network_time_stamp.split(": ")[1];
                security = network_cap.split(": ")[1];
                level = network_signal_lvl.split(": ")[1];
                count++;
                nets[i] = new Element(ssid, security, level,bssid,frequency,timestamp);
            }
        }
        Log.d("RESULT", "End scanning");
    }

    BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            unregisterReceiver(this);
            count = 0;
            for(Element element: nets){
                arrayList.add(element.getNetworkName());
                adapter.notifyDataSetChanged();
            }
        }
    };
}