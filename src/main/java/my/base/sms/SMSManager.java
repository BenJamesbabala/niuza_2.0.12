package my.base.sms;

import android.telephony.SmsManager;

public class SMSManager {
    public static void sendTextMessage(String mobileNo, String massage) {
        SmsManager sms = SmsManager.getDefault();
        for (String text : sms.divideMessage(massage)) {
            sms.sendTextMessage(mobileNo, null, text, null, null);
        }
    }
}
