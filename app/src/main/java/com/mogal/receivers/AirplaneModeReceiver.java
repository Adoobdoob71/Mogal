package com.mogal.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AirplaneModeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean airplaneModeState = intent.getBooleanExtra("state", false);
        if (airplaneModeState)
            Toast.makeText(context, "In order to use the app, turn Airplane mode off", Toast.LENGTH_SHORT).show();
    }
}
