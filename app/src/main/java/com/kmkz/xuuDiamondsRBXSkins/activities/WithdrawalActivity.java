package com.kmkz.xuuDiamondsRBXSkins.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kmkz.xuuDiamondsRBXSkins.R;
import com.kmkz.xuuDiamondsRBXSkins.utility.Tools;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ibrahimodeh.ibratoastlib.IbraToast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class WithdrawalActivity extends AppCompatActivity {

    ImageView closeImg;
    TextView pointsTxt, convertUSDTxt, minWithdrawalTxt;
    EditText pointsConvertEditText;
    Button withdrawalBtn;
    SharedPreferences prefs;
    int userPoints;
    double usd;
    String paypalEmail;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawal);

        // Change system bar color
        Tools.setSystemBarColor(WithdrawalActivity.this, R.color.colorPrimary);

        prefs = getSharedPreferences("User", Context.MODE_PRIVATE);
        userPoints = prefs.getInt("points", 0);

        initViews();


        pointsTxt.setText(String.valueOf(userPoints));
        minWithdrawalTxt.setText(getString(R.string.minimum_points) + " " + Tools.getAppSettingsSharedPreferences(this, "minimumWithdrawal"));

        pointsConvertEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            
            // if the user delete all text
                if (pointsConvertEditText.getText().toString().isEmpty())
                {
                    return;
                }

                String pointsConverted = pointsConvertEditText.getText().toString();

                usd = Integer.parseInt(pointsConverted) * 0.0001;
                convertUSDTxt.setText("It is = " + usd + " $");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        closeImg.setOnClickListener(v -> finish());


        withdrawalBtn.setOnClickListener( v ->
        {
            if (isConnectingToInternet(this))
            {
                String withdrawalPoints = pointsConvertEditText.getText().toString();

                if(Integer.parseInt(withdrawalPoints) <= userPoints)
                {
                    if(Integer.parseInt(withdrawalPoints) >= Integer.parseInt(Tools.getAppSettingsSharedPreferences(this, "minimumWithdrawal")))
                    {
                        showDialogFullscreen();
                    }else {
                        //showDialogFullscreen(); You don't have the minimum withdrawal!
                        IbraToast.makeText(this, getString(R.string.dont_have_min), Toast.LENGTH_LONG, 2).show();
                    }
                }
                else {
                    IbraToast.makeText(this, getString(R.string.no_points_enough), Toast.LENGTH_LONG, 2).show();
                }
            } else {
                showNoNetDialog();
            }

        });
    }

    private void initViews()
    {
        closeImg = findViewById(R.id.close_img);
        pointsTxt = findViewById(R.id.Points_Txt);
        pointsConvertEditText = findViewById(R.id.pointsConvert_editText);
        convertUSDTxt = findViewById(R.id.convertUSD_txt);
        withdrawalBtn = findViewById(R.id.Withdrawal_Btn);
        minWithdrawalTxt = findViewById(R.id.minWithdrawalTxt);
    }

    private void setDatabaseWithdrawal()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference user = database.getReference("Payments");

        String withdrawalPoints = pointsConvertEditText.getText().toString();

        HashMap<String, Object> result = new HashMap<>();
        result.put("date", currentTime());
        result.put("email", paypalEmail);
        result.put("points", Integer.parseInt(withdrawalPoints));
        result.put("usd", usd);
        result.put("method", Tools.getAppSettingsSharedPreferences(this, "paymentMethod"));

        userPoints = userPoints - Integer.parseInt(withdrawalPoints);
        prefs = getSharedPreferences("User", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("points", userPoints);
        editor.apply();

        user.push().setValue(result);

        IbraToast.makeText(this, getString(R.string.success), Toast.LENGTH_LONG, 1).show();

        pointsTxt.setText(String.valueOf(userPoints));
    }

    public String currentTime()
    {
        Date curDate = new Date();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        return format.format(curDate);
    }

    private void showNoNetDialog()
    {
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setTitle(getString(R.string.splash_no_internet));
        build.setMessage(getString(R.string.no_internet_msg));

        build.setCancelable(false);

        build.setPositiveButton("ok", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = build.create();
        dialog.show();
    }


    @SuppressLint("SetTextI18n")
    private void showDialogFullscreen()
    {
        final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_withdrawal, null);

        ImageButton close = dialogView.findViewById(R.id.bt_close);
        Button SendRequest = dialogView.findViewById(R.id.send_request_btn);
        final EditText emailAddress = dialogView.findViewById(R.id.email_address);
        TextView labelTxt = dialogView.findViewById(R.id.labelTxt);

        labelTxt.setText(getString(R.string.your_email) + " (" + Tools.getAppSettingsSharedPreferences(this, "paymentMethod") + ")");

        close.setOnClickListener(view -> dialogBuilder.dismiss());

        SendRequest.setOnClickListener(view -> {
            dialogBuilder.dismiss();

            if (emailAddress.getText().toString().isEmpty())
            {
                emailAddress.setError(getString(R.string.enter_email));
                return;
            }
            paypalEmail = emailAddress.getText().toString();
            // Send user data
            setDatabaseWithdrawal();
        });


        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
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
}