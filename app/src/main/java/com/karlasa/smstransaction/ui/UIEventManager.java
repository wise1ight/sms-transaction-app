package com.karlasa.smstransaction.ui;

/**
 * Created by kuvh on 2017-03-15.
 */

public class UIEventManager {

    private static UIEventManager instance;
    private UIEventListener listener;

    public static UIEventManager getInstance() {
        if (instance == null) {
            instance = new UIEventManager();
        }
        return instance;
    }

    public void setListener(UIEventListener listener) { this.listener = listener; }

    public void updateUI(int code, Object obj) {
        if (listener != null)
            listener.onUIUpdate(code, obj);
    }

    public void refreshUI() {
        if (listener != null)
            listener.onRefresh();
    }
}
