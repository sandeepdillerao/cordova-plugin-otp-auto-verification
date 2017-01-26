package org.apache.cordova.OTPAutoVerification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * This class echoes a string called from JavaScript.
 */
public class OTPAutoVerification extends CordovaPlugin {

    private BroadcastReceiver mSmsReceiver;
    private IntentFilter filter;
    private static final String TAG = OTPAutoVerification.class.getSimpleName();

    public static String SMS_ORIGIN = null;
    public static String OTP_DELIMITER = null;
    public static int OTP_LENGTH = 0;
    @Override
    public boolean execute(String action, JSONArray options, final CallbackContext callbackContext) throws JSONException {
        if (action.equals("startOTPListener")) {
            Log.i(TAG, options.toString());
            startOTPListener(options, callbackContext);

            return true;
        }else if (action.equals("stopOTPListener")) {
            stopOTPListener();
            return true;
        }
        return false;
    }

    private void startOTPListener(JSONArray options, final CallbackContext callbackContext) {
    /* take init parameter from JS call */
        try {
            SMS_ORIGIN = options.getJSONObject(0).getString("origin");
            OTP_DELIMITER = options.getJSONObject(0).getString("delimiter");
            OTP_LENGTH = options.getJSONObject(0).getInt("length");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");

        if (this.mSmsReceiver == null) {
            this.mSmsReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    final Bundle bundle = intent.getExtras();
                    try {
                        if (bundle != null) {
                            Object[] pdusObj = (Object[]) bundle.get("pdus");
                            for (Object aPdusObj : pdusObj) {
                                SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) aPdusObj);
                                String senderAddress = currentMessage.getDisplayOriginatingAddress();
                                String message = currentMessage.getDisplayMessageBody();

                                Log.e(TAG, "Received SMS: " + message + ", Sender: " + senderAddress);

                                // if the SMS is not from our gateway, ignore the message
                                if (!senderAddress.toLowerCase().contains(SMS_ORIGIN.toLowerCase())) {
                                    return;
                                }

                                // verification code from sms
                                String verificationCode = getVerificationCode(message);

                                Log.e(TAG, "OTP received: " + verificationCode);
                                stopOTPListener();
                                callbackContext.success(verificationCode);
                            }
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Exception: " + e.getMessage());
                    }
                }
            };
            cordova.getActivity().registerReceiver(this.mSmsReceiver, filter);
        }

        PluginResult pluginResult = new PluginResult(PluginResult.Status.NO_RESULT);
        pluginResult.setKeepCallback(true);
        callbackContext.sendPluginResult(pluginResult);
        Log.i("SANDY pluginResult", pluginResult.toString());
    }

    private void stopOTPListener(){
        if(this.mSmsReceiver !=null){
            cordova.getActivity().unregisterReceiver(mSmsReceiver);
            Log.d("SANDY Debugger", "stopOTPListener");
        }
    }
    /**
     * Getting the OTP from sms message body
     * ':' is the separator of OTP from the message
     *
     * @param message
     * @return
     */
    private String getVerificationCode(String message) {
        String code = null;
        int index = message.indexOf(OTP_DELIMITER);

        if (index != -1) {
            int start = index + 8;
            int length = OTP_LENGTH;
            code = message.substring(start, start + length);
            return code;
        }

        return code;
    }


}
