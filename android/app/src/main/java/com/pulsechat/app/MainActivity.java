package com.pulsechat.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.webkit.WebView;
import android.widget.ScrollView;
import android.widget.TextView;
import com.getcapacitor.BridgeActivity;

public class MainActivity extends BridgeActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, final Throwable throwable) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        showCrashDialog(throwable);
                    }
                });
            }
        });

        try {
            WebView.setWebContentsDebuggingEnabled(true);
            super.onCreate(savedInstanceState);
        } catch (Throwable t) {
            showCrashDialog(t);
        }
    }

    private void showCrashDialog(Throwable t) {
        try {
            setTheme(androidx.appcompat.R.style.Theme_AppCompat_Dialog_Alert);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Posti Error Inspector");

            StringBuilder sb = new StringBuilder();
            sb.append("Error Details:\n").append(t.toString()).append("\n\nStack:\n");
            for (StackTraceElement el : t.getStackTrace()) {
                sb.append("at ").append(el.toString()).append("\n");
            }
            Throwable cause = t.getCause();
            if (cause != null) {
                sb.append("\nCaused by:\n").append(cause.toString()).append("\n");
                for (StackTraceElement el : cause.getStackTrace()) {
                    sb.append("at ").append(el.toString()).append("\n");
                }
            }

            ScrollView sv = new ScrollView(this);
            TextView tv = new TextView(this);
            tv.setText(sb.toString());
            tv.setPadding(32, 32, 32, 32);
            tv.setTextIsSelectable(true);
            sv.addView(tv);

            builder.setView(sv);
            builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.setCancelable(false);
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
                     }
