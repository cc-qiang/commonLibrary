package com.chen.library.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author DELL
 */
public final class ObserverManager {

    private volatile static ObserverManager _instance;
    private HashMap<String, ObserverListener> mMap;

    private ObserverManager() {
        mMap = new HashMap<>();
    }

    public static ObserverManager getInstance() {
        if (_instance == null) {
            synchronized (ObserverManager.class) {
                if (_instance == null) {
                    _instance = new ObserverManager();
                }
            }
        }
        return _instance;
    }

    public void addListener(ObserverListener listener) {
        if (listener != null && mMap != null) {
            mMap.put(listener.getClass().getName(), listener);
        }
    }

    public void removeListener(ObserverListener listener) {
        if (listener != null && mMap != null) {
            String key = listener.getClass().getName();
            if (mMap.containsKey(key)) {
                mMap.remove(key);
            }
        }
    }

    public void clearListener() {
        if (mMap != null) {
            mMap.clear();
        }
    }

    public void doRefreshAll() {
        if (mMap != null) {
            for (Map.Entry<String, ObserverListener> entry : mMap.entrySet()) {
                entry.getValue().onRefresh();
            }
        }
    }

    public void doRefresh(String... keys) {
        if (mMap != null && keys.length > 0) {
            for (String key : keys) {
                mMap.get(key).onRefresh();
            }
        }
    }
}
