package com.socialmedianetworking.iremember.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.view.View;

import com.socialmedianetworking.iremember.R;

public class CheckInternetConnection {
    Context ctx;

    public CheckInternetConnection(Context context) {
        ctx = context;
    }

    public void checkConnection() {
        if (!isInternetConnected()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
            builder.setTitle("No Internet")
                    .setMessage(ctx.getString(R.string.Noitrenetconetion) + "\n" + ctx.getString(R.string.noconnection))
                    .setIcon(R.drawable.internetconnection)
                    .setPositiveButton("Connect Now", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (isInternetConnected()) {
                                dialog.dismiss();
                            } else {
                                Intent dialogIntent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                ctx.startActivity(dialogIntent);
                            }
                        }
                    })
                    .setCancelable(false)
                    .show();
        }
    }

    private boolean isInternetConnected() {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}