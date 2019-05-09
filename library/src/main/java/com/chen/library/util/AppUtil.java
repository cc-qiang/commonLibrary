package com.chen.library.util;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.chen.library.callback.PermissionCallBack;
import com.tbruyelle.rxpermissions2.RxPermissions;


/**
 * Created by RJS on 2016/7/26.
 */
public class AppUtil {
    private volatile static AppUtil instance;
    private Context mContext;
    private LayoutInflater _inflater;

    private AppUtil() {
    }

    public void init(Context context) {
        this.mContext = context;
        this._inflater = LayoutInflater.from(context);
    }

    public static AppUtil getInstance() {
        if (instance == null) {
            instance = new AppUtil();
        }
        return instance;
    }

    public static void copyToClipboard(Context context, String text) {
        ClipboardManager clip = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData data = ClipData.newPlainText(ClipDescription.MIMETYPE_TEXT_PLAIN, text);
        clip.setPrimaryClip(data);
    }

    public static void checkPermission(Activity activity, String permission, PermissionCallBack callBack) {
        RxPermissions rx = new RxPermissions(activity);
        if (rx.isGranted(permission)) {
            if (callBack != null) {
                callBack.permissionGrantedCall();
            }
        } else {
            rx.request(permission).subscribe(grant -> {
                if (grant) {
                    if (callBack != null) {
                        callBack.permissionGrantedCall();
                    }
                } else {
                    if (callBack != null) {
                        callBack.permissionRevokedCall();
                    }
                }
            });
        }
    }

    private boolean checkInitState() {
        if (mContext == null) {
            new Throwable("please init before use it!");
            return false;
        }
        return true;
    }

    public Context getContext() {
        return mContext;
    }

    public float getDensity() {
        if (checkInitState()) {
            return mContext.getResources().getDisplayMetrics().density;
        }
        return 0;
    }

    public int getScreenWidth() {
        if (checkInitState()) {
            return mContext.getResources().getDisplayMetrics().widthPixels;
        }
        return 0;
    }

    public int getScreenHeight() {
        if (checkInitState()) {
            return mContext.getResources().getDisplayMetrics().heightPixels;
        }
        return 0;
    }

    public LayoutInflater getLayoutInflater() {
        return _inflater;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(float dpValue) {
        if (checkInitState()) {
            final float scale = getDensity();
            return (int) (dpValue * scale + 0.5f);
        }
        return 0;
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public int px2dip(float pxValue) {
        if (checkInitState()) {
            final float scale = getDensity();
            return (int) (pxValue / scale + 0.5f);
        }
        return 0;
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @param （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static boolean isEmpty(String content) {
        if (TextUtils.isEmpty(content)) {
            return false;
        }
        return true;
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @param （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


    public void showToast(int id) {
        if (checkInitState()) {
            Toast.makeText(mContext, mContext.getResources().getText(id), Toast.LENGTH_SHORT).show();
        }
    }

}
