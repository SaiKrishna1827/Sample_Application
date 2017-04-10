package com.android.myapplication;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONArray;


public class SplashScreen extends AppCompatActivity {


    ClientServerInterface clientServerInterface = new ClientServerInterface();
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        pb = (ProgressBar) findViewById(R.id.progressBar);


        final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

        if (ContextCompat.checkSelfPermission(SplashScreen.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(SplashScreen.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {

                ActivityCompat.requestPermissions(SplashScreen.this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);

            }
        }else{
            new RetreiveData().execute();
        }



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        new RetreiveData().execute();
    }

    class RetreiveData extends AsyncTask<String,String,JSONArray> {

        @Override
        protected JSONArray doInBackground(String... arg0) {

            SplashScreen.this.runOnUiThread(new Runnable() {
                public void run() {

                    pb.setVisibility(View.VISIBLE);

                }
            });

            JSONArray jobj = clientServerInterface.makeHttpRequest("https://chsaikrishna105.000webhostapp.com/get_sample_data.php");

            return jobj;
        }
        protected void onPostExecute(JSONArray jobj){

           Intent intent = new Intent(SplashScreen.this,MapsActivity.class);
           intent.putExtra("jobj",jobj.toString());
           startActivity(intent);
           finish();




            SplashScreen.this.runOnUiThread(new Runnable() {
                public void run() {

                    pb.setVisibility(View.GONE);

                }
            });
        }

    }



}
