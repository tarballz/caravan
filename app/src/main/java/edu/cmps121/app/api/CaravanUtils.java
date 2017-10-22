package edu.cmps121.app.api;

import android.support.v7.app.AppCompatActivity;
import android.util.StringBuilderPrinter;
import android.widget.Toast;

import edu.cmps121.app.CreatePartyActivity;

/**
 * Created by glarwood on 10/18/17.
 */

public class CaravanUtils {
    public static void shortToast(AppCompatActivity activity, String msg) {
        Toast.makeText(
                activity,
                msg,
                Toast.LENGTH_SHORT
        ).show();
    }

    public static String strAppend(String inStr, String appStr) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(inStr);
        stringBuilder.append(appStr);

        return stringBuilder.toString();
    }
}
