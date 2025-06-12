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
    public boolean execute(String action, JSONArray args, final org.apache.cordova.CallbackContext callbackContext) throws JSONException {
        if ("watch".equals(action)) {
            startWatching(callbackContext);
            return true;
        }
        return false; // invalid action
    }

    private void startWatching(final org.apache.cordova.CallbackContext callbackContext) {
        if (receiver != null) {
            try {
                cordova.getActivity().unregisterReceiver(receiver);
            } catch (IllegalArgumentException e) {
                // Receiver not registered
            }
            receiver = null;
        }

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                    sendEvent("deviceLocked");
                    PluginResult result = new PluginResult(PluginResult.Status.OK, "deviceLocked");
                    result.setKeepCallback(true);
                    callbackContext.sendPluginResult(result);
                } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
                    sendEvent("deviceUnlocked");
                    PluginResult result = new PluginResult(PluginResult.Status.OK, "deviceUnlocked");
                    result.setKeepCallback(true);
                    callbackContext.sendPluginResult(result);
                }
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        cordova.getActivity().registerReceiver(receiver, filter);

        // Сообщаем, что watch запущен и оставляем callback активным
        PluginResult result = new PluginResult(PluginResult.Status.NO_RESULT);
        result.setKeepCallback(true);
        callbackContext.sendPluginResult(result);
        
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