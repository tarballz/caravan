package edu.cmps121.app.api;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class CaravanUtils {
    public static void shortToast(AppCompatActivity activity, String msg) {
        Toast.makeText(
                activity,
                msg,
                Toast.LENGTH_SHORT
        ).show();
    }

    public static boolean isValidString(String str) {
        return str != null && !str.isEmpty();
    }
}
