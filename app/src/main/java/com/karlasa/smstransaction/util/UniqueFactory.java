package com.karlasa.smstransaction.util;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Base64;

import com.karlasa.smstransaction.Application;

import java.io.UnsupportedEncodingException;

/**
 * Created by kuvh on 2017-04-17.
 */

public class UniqueFactory {

    public static String build() {
        TelephonyManager tm = (TelephonyManager) Application.getContext().getSystemService(Context.TELEPHONY_SERVICE);
        String imei = tm.getDeviceId();
        String pn = tm.getLine1Number();

        String token = imei + ":" + pn;
        String encoded = null;
        try {
            encoded = Base64.encodeToString(token.getBytes("UTF-8"), Base64.NO_WRAP);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encoded;
    }
}
