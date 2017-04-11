package com.dragonide.wifidistancecalculator;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    DistanceAdapter mAdapter;
    private List<Distance> wifi_list;
    ImageButton imageButton;
    WifiReceiver receiverWifi;
    private final Handler handler = new Handler();
    WifiManager wifi;
    int size = 0;
    List<ScanResult> results;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh();
            }
        });
        imageButton = (ImageButton) findViewById(R.id.no_wifi_button);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleWiFi(true);
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.rview);
        wifi_list = new ArrayList<>();
        mAdapter = new DistanceAdapter(wifi_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(MainActivity.this, "Khup fukat basla ahat, Assignment Jhale vatta sagle", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        refresh();
    }

    public void toggleWiFi(boolean status) {
        WifiManager wifiManager = (WifiManager) this.getSystemService(WIFI_SERVICE);
        if (status && !wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        } else if (!status && wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
        }
    }

    public void refresh() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            } else {
                executeNow(); // the actual wifi scanning
            }
        } else {
            executeNow();
        }
    }

    public void executeNow() {
        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        if (wifi.isWifiEnabled()) {

            receiverWifi = new WifiReceiver();
            registerReceiver(receiverWifi, new IntentFilter(
                    WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
            if (!wifi.isWifiEnabled()) {
                wifi.setWifiEnabled(true);
            }
            Log.d("testtillhere", "belowwifienabled");


            imageButton.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);

            doInback();
            //   wifi.startScan();
            //  checkWifi();
        } else {
            imageButton.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiverWifi);
    }

    public void doInback() {

        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

                receiverWifi = new WifiReceiver();

                registerReceiver(receiverWifi, new IntentFilter(
                        WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

                mAdapter.notifyDataSetChanged();
                wifi.startScan();
                doInback();
            }
        }, 2000);

    }

    @Override
    protected void onResume() {
        registerReceiver(receiverWifi, new IntentFilter(
                WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onResume();


    }

    public void checkWifi() {

        //final WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        try {
            size = size - 1;
            while (size >= 0) {

                Distance dis1 = new Distance((double) results.get(size).frequency,
                        (double) results.get(size).level, calculateDistance((double) results.get(size).level, (double) results.get(size).frequency), results.get(size).SSID);
                wifi_list.add(dis1);


                size--;
                mAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

     /*   Distance dis1 = new Distance("Mad Max: Fury Road", "Action & Adventure", "2015", "");
        wifi_list.add(dis1);*/

    }

    public double calculateDistance(double signalLevelInDb, double freqInMHz) {
        double exp = (27.55 - (20 * Math.log10(freqInMHz)) + Math.abs(signalLevelInDb)) / 20.0;
        return Math.pow(10.0, exp);
    }

   /* public boolean checkIfWifiIsOn() {

        WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);


        return wifi.isWifiEnabled();
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.setGroupVisible(0, false);

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
            }
            doInback();
        }
    }

    class WifiReceiver extends BroadcastReceiver {

        // This method call when number of wifi connections changed
        public void onReceive(Context c, Intent intent) {
            wifi_list.clear();
            mAdapter.notifyDataSetChanged();
            results = wifi.getScanResults();
            size = results.size();

            try {


                for (int i = 0; i < size; i++) {

                    Distance dis1 = new Distance((double) results.get(i).frequency,
                            (double) results.get(i).level, round(calculateDistance((double)
                            results.get(i).level, (double) results.get(i).frequency), 2), results.get(i).SSID);

                    wifi_list.add(dis1);

                    mAdapter.notifyDataSetChanged();
                }
            } catch (Exception e) {
                Log.d("testtillhere", "inside CATCH");
                e.printStackTrace();
            }
        }

    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private MainActivity.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final MainActivity.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
