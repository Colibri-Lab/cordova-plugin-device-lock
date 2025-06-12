package com.colibri.accessories.devicelock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

public class DeviceLockPlugin extends CordovaPlugin {

    private BroadcastReceiver receiver;

    @Override
    protected void pluginInitialize() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                    sendEvent("deviceLocked");
                } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
                    sendEvent("deviceUnlocked");
                }
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        cordova.getActivity().registerReceiver(receiver, filter);
    }

    private void sendEvent(String eventName) {
        String js = "window.dispatchEvent(new CustomEvent('" + eventName + "'));";
        webView.getEngine().evaluateJavascript(js, null);
    }

    @Override
    public void onDestroy() {
        if (receiver != null) {
            cordova.getActivity().unregisterReceiver(receiver);
        }
        super.onDestroy();
    }
}