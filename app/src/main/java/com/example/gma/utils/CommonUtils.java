package com.example.gma.utils;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class CommonUtils {
    // For error check but not used now
    static void setError(Object view, String errorMsg) {
        LinearLayout linearLayout = null;
        if (view instanceof TextView) {
            TextView feild = (TextView) view;
            if (feild.getParent() instanceof LinearLayout) {
                linearLayout = (LinearLayout) feild.getParent();
            }
            if (linearLayout != null) {
                if (linearLayout.getVisibility() != View.VISIBLE) {
                    linearLayout.setVisibility(View.VISIBLE);
                }
            }

            feild.setError(errorMsg);
            feild.requestFocus();

        } else if (view instanceof EditText) {
            EditText feild = (EditText) view;
            if (feild.getParent() instanceof LinearLayout) {
                linearLayout = (LinearLayout) feild.getParent();
            }
            if (linearLayout != null) {
                if (linearLayout.getVisibility() != View.VISIBLE) {
                    linearLayout.setVisibility(View.VISIBLE);
                }
            }

            feild.setError(errorMsg);
            feild.requestFocus();

        }
        else if (view instanceof Spinner) {
            Spinner feild = (Spinner) view;
            feild.setFocusable(true);
            if (feild.getParent() instanceof LinearLayout) {
                linearLayout = (LinearLayout) feild.getParent();
            }
            if (linearLayout != null) {
                if (linearLayout.getVisibility() != View.VISIBLE) {
                    linearLayout.setVisibility(View.VISIBLE);
                }
            }
            ((TextView) feild.getSelectedView()).setError(errorMsg);
            //Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_SHORT);
            feild.performClick();
            feild.requestFocus();
        }
    }
    public static long milliseconds(String date)
    {
        //String date_ = date;
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        try
        {
            Date mDate = sdf.parse(date);
            long timeInMilliseconds = mDate.getTime();
//            Log.i("Date in milli", "milliseconds: "+ timeInMilliseconds);
//            System.out.println("Date in milli :: " + timeInMilliseconds);
            return timeInMilliseconds;
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        return 0;
    }
    public static String longintoTime(String time,String pattern){
        Date date = new Date(Long.parseLong(time));
        java.text.DateFormat Format = new SimpleDateFormat(pattern);
        //Format.setTimeZone(TimeZone.getTimeZone("UTC"));
        Format.setTimeZone(TimeZone.getDefault());
        String timeString = Format.format(date);
        return timeString;
    }
}
