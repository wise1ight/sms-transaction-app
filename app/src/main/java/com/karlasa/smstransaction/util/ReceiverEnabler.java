package com.karlasa.smstransaction.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;

import com.karlasa.smstransaction.receiver.SMSReceiver;

/**
 * Created by kuvh on 2017-01-01.
 */

public class ReceiverEnabler {

    Context context;
    PackageManager pm;
    ComponentName component;

    public ReceiverEnabler(Context context) {
        this.context = context;
        pm = context.getPackageManager();
        component = new ComponentName(context, SMSReceiver.class);
    }

    public void setEnable(boolean bool) {
        if(bool)
            pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);
        else
            pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP);
    }

    public boolean isEnabled() {
        return pm.getComponentEnabledSetting(component) == PackageManager.COMPONENT_ENABLED_STATE_ENABLED;
    }
}
