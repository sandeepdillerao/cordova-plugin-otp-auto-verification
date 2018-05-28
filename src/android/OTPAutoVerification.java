package org.apache.cordova.OTPAutoVerification;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
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

    private IntentFilter filter;
    public static final String RECEIVE_SMS_PERMISSION = Manifest.permission.RECEIVE_SMS;
    public static final int REQUEST_CODE = 125;
    private static final String TAG = OTPAutoVerification.class.getSimpleName();

    public static String SMS_ORIGIN = null;
    public static String OTP_DELIMITER = null;
    public static int OTP_LENGTH = 0;
    public JSONArray options;
    public CallbackContext callbackContext;
    @Override
    public boolean execute(String action, JSONArray options, final CallbackContext callbackContext) throws JSONException {
        if (action.equals("startOTPListener")) {
            Log.i(TAG, options.toString());
            this.options = options;
            this.callbackContext = callbackContext;
            if(cordova.hasPermission(RECEIVE_SMS_PERMISSION)) {
                Log.i("OTPAutoVerification", "Has Permission");
                startOTPListener(options, callbackContext);
            }
            else
            {
                getPermission(REQUEST_CODE);
            }

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

            SMSListener.bindListener(new Common.OTPListener() {
                @Override
                public void onOTPReceived(String message, String sender) {
                    Log.e(TAG, "Received SMS: " + message + ", Sender: " + sender);

                    // if the SMS is not from our gateway, ignore the message
                    if (!sender.toLowerCase().contains(SMS_ORIGIN.toLowerCase())) {
                        return;
                    }

                    // verification code from sms
                    String verificationCode = getVerificationCode(message);

                    Log.e(TAG, "OTP received: " + verificationCode);
                    stopOTPListener();
                    callbackContext.success(verificationCode);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
        filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        cordova.getActivity().registerReceiver(new SMSListener(), filter);
        PluginResult pluginResult = new PluginResult(PluginResult.Status.NO_RESULT);
        pluginResult.setKeepCallback(true);
        callbackContext.sendPluginResult(pluginResult);
        Log.i("SMS pluginResult", pluginResult.toString());
    }

    private void stopOTPListener(){
        Log.d("OTPAutoVerification", "stopOTPListener");
        SMSListener.unbindListener();
        Log.d("SANDY Debugger", "stopOTPListener");
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
            int start = index + OTP_DELIMITER.length()+1;
            int length = OTP_LENGTH;
            code = message.substring(start, start + length);
            return code;
        }

        return code;
    }

    protected void getPermission(int requestCode)
    {
        cordova.requestPermission(this, requestCode, RECEIVE_SMS_PERMISSION);
    }

    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) throws JSONException
    {
        for(int r:grantResults)
        {
            if(r == PackageManager.PERMISSION_DENIED)
            {
                Log.i("OTPAutoVerification", "SMS Permission Denied");
//                callbackContext.failure("User Denied the permission to read SMS");
                return;
            }
        }
        Log.i("OTPAutoVerification", "SMS Permission Granted");
        startOTPListener(options, callbackContext);
    }

    /*
    * Interface for OTP sms Listener
    * */
    public interface Common {
        interface OTPListener {
            void onOTPReceived(String otp, String sender);
        }
    }

    /*
    * broadcast listener to listen for MESSAGE
    * @return originalMessage and Sender
    * onOTPReceived(smsMessage.getDisplayMessageBody(), senderAddress);
    * */
    public static class SMSListener extends BroadcastReceiver {

        private static OTPAutoVerification.Common.OTPListener mListener; // this listener will do the magic of throwing the extracted OTP to all the bound views.

        @Override
        public void onReceive(Context context, Intent intent) {

            // this function is trigged when each time a new SMS is received on device.

            Bundle data = intent.getExtras();

            Object[] pdus = new Object[0];
            if (data != null) {
                pdus = (Object[]) data.get("pdus"); // the pdus key will contain the newly received SMS
            }

            if (pdus != null) {
                for (Object pdu : pdus) { // loop through and pick up the SMS of interest
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);

                    // your custom logic to filter and extract the OTP from relevant SMS - with regex or any other way.
                    String senderAddress = smsMessage.getDisplayOriginatingAddress();
                    if (mListener!=null)
                        mListener.onOTPReceived(smsMessage.getDisplayMessageBody(), senderAddress);
                    break;
                }
            }
        }

        public static void bindListener(OTPAutoVerification.Common.OTPListener listener) {
            mListener = listener;
        }

        public static void unbindListener() {
            mListener = null;
        }
    }
}
