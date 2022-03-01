package com.karlasa.smstransaction.ui;

/**
 * Created by kuvh on 2017-03-15.
 */

public interface UIEventListener {
    void onUIUpdate(int code, Object data);
    void onRefresh();
}
