package com.kmkz.xuuDiamondsRBXSkins.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kmkz.xuuDiamondsRBXSkins.MainActivity;
import com.kmkz.xuuDiamondsRBXSkins.R;
import com.kmkz.xuuDiamondsRBXSkins.utility.Tools;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    private ImageView logo;
    private TextView textApp;
    private ProgressBar progressBar;
    private LinearLayout lytNoConnection;
    int splashTimeOut = 1000; // 1 seconds of splash time
    DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        logo = findViewById(R.id.logo);
        textApp = findViewById(R.id.textApp);
        progressBar = findViewById(R.id.progress_bar);
        lytNoConnection = findViewById(R.id.lyt_no_connection);

        progressBar.setVisibility(View.GONE);
        lytNoConnection.setVisibility(View.GONE);

        Animation myanim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        logo.startAnimation(myanim);
        textApp.startAnimation(myanim);

        new Handler().postDelayed(() -> {
            if (isConnectingToInternet(SplashActivity.this)) {
                new SendRequest().execute();
            } else {
                showNoInterNet();
            }
        }, splashTimeOut);


        lytNoConnection.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            lytNoConnection.setVisibility(View.GONE);

            new Handler().postDelayed(() -> {
                if (isConnectingToInternet(SplashActivity.this)) {
                    goHome();
                } else {
                    progressBar.setVisibility(View.GONE);
                    lytNoConnection.setVisibility(View.VISIBLE);
                }
            }, 1000);
        });
    }

    private void goHome() {
        Intent goHome = new Intent(SplashActivity.this, MainActivity.class);
        goHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(goHome);
    }

    private void showNoInterNet()
    {
        logo.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        textApp.setVisibility(View.GONE);
        lytNoConnection.setVisibility(View.VISIBLE);
    }

    public static boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivity =
                (ConnectivityManager) context.getSystemService(
                        Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }


    public class SendRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){}

        protected String doInBackground(String... arg0) {

            try{
                URL url = new URL("https://ibrahimodeh.com/codecanyon/app-var.php?userApp=" + getPackageName());

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("userApp", getPackageName());
                Log.e("params",postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, StandardCharsets.UTF_8));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK)
                {
                    BufferedReader in=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while((line = in.readLine()) != null)
                    {
                        sb.append(line);
                        break;
                    }
                    in.close();
                    return sb.toString();
                }
                else {
                    return "false : " + responseCode;
                }
            }
            catch(Exception e){
                return "Exception: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {

            if (result.equals("False"))
            {
                finish();
                Toast.makeText(SplashActivity.this, "please contact me for this error: support@ibrahimodeh.com", Toast.LENGTH_LONG).show();
            }
            else {
                getTheAppSettingFirebase();
            }
        }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }

    private void getTheAppSettingFirebase()
    {
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("Admin").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    String AdsNetwork = dataSnapshot.child("AdsNetwork").getValue().toString();
                    String email = dataSnapshot.child("email").getValue().toString();
                    String website = dataSnapshot.child("website").getValue().toString();
                    String privacy_policy = dataSnapshot.child("privacy").getValue().toString();
                    String dailyGiftPoints = dataSnapshot.child("dailyGiftPoints").getValue().toString();
                    String minimumWithdrawal = dataSnapshot.child("minimumWithdrawal").getValue().toString();
                    String paymentMethod = dataSnapshot.child("paymentMethod").getValue().toString();
                    String smallAdsPoints = dataSnapshot.child("smallAdsPoints").getValue().toString();
                    String watchVideoPoints = dataSnapshot.child("watchVideoPoints").getValue().toString();

                    Tools.setAppSettingsSharedPreferences(SplashActivity.this, "email", email);
                    Tools.setAppSettingsSharedPreferences(SplashActivity.this, "website", website);
                    Tools.setAppSettingsSharedPreferences(SplashActivity.this, "privacy_policy", privacy_policy);
                    Tools.setAppSettingsSharedPreferences(SplashActivity.this, "dailyGiftPoints", dailyGiftPoints);
                    Tools.setAppSettingsSharedPreferences(SplashActivity.this, "minimumWithdrawal", minimumWithdrawal);
                    Tools.setAppSettingsSharedPreferences(SplashActivity.this, "paymentMethod", paymentMethod);
                    Tools.setAppSettingsSharedPreferences(SplashActivity.this, "smallAdsPoints", smallAdsPoints);
                    Tools.setAppSettingsSharedPreferences(SplashActivity.this, "watchVideoPoints", watchVideoPoints);
                    Tools.setAppSettingsSharedPreferences(SplashActivity.this, "AdsNetwork", AdsNetwork);

                    goHome();
                } else {
                   // Toast.makeText(SplashActivity.this, "Firebase Error! Try Again", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}