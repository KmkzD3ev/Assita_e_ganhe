package com.kmkz.xuuDiamondsRBXSkins.utility;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.ColorRes;

import com.kmkz.xuuDiamondsRBXSkins.MainActivity;

public class Tools {

    public static void setSystemBarColor(Activity act, @ColorRes int color) {
        Window window = act.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(act.getResources().getColor(color));
    }

    /**
     * Start new activity
     */
    public static void startNewActivity(Activity activity, Class<?> cls) {
        Intent intent = new Intent(activity, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
    }

    public static String getVersionName(Activity activity)
    {
        String versionName = "";
        try {
            versionName = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    public static void setSystemBarLight(Activity act) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View view = act.findViewById(android.R.id.content);
            int flags = view.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
        }
    }

    public static String getAppSettingsSharedPreferences(Activity activity, String key)
    {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("app_settings", MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    public static void setAppSettingsSharedPreferences(Activity activity, String key, String value)
    {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("app_settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void addThePoints(Activity activity,int thePoints)
    {
        SharedPreferences prefs = activity.getSharedPreferences("User", Context.MODE_PRIVATE);
        int userPoints = prefs.getInt("points", 0);

        userPoints = userPoints + thePoints;
        prefs = activity.getSharedPreferences("User", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("points", userPoints);
        editor.apply();

        MainActivity.pointsTxt.setText(String.valueOf(userPoints));
    }

    public static void addAchieveProgress(Activity activity, int ac_number)
    {
        SharedPreferences prefs = activity.getSharedPreferences("User", Context.MODE_PRIVATE);
        int ac_one_count = prefs.getInt("ac_one_count", 0);
        int ac_two_count = prefs.getInt("ac_two_count", 0);
        int ac_three_count = prefs.getInt("ac_three_count", 0);

        SharedPreferences.Editor editor = prefs.edit();
        switch (ac_number) {
            case 1:
                ac_one_count = ac_one_count + 1;
                editor.putInt("ac_one_count", ac_one_count);
                break;
            case 2:
                ac_two_count = ac_two_count + 1;
                editor.putInt("ac_two_count", ac_two_count);
                break;
            case 3:
                ac_three_count = ac_three_count + 1;
                editor.putInt("ac_three_count", ac_three_count);
                break;
        }
        editor.apply();
    }

}
