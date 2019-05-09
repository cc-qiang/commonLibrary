package com.chen.library.util;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chen.library.R;

public class CustomLoadingUtil{
    private static volatile CustomLoadingUtil instance;
    private Dialog loadingDialog;
    private TextView tipTextView;

    private CustomLoadingUtil() {
        createDialog(AppUtil.getInstance().getContext());
    }

    public static CustomLoadingUtil getInstance() {
        if (instance == null) {
            synchronized (CustomLoadingUtil.class) {
                if (instance == null) {
                    instance = new CustomLoadingUtil();
                }
            }
        }
        return instance;
    }

    private void createDialog(Context context) {
        if (context != null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View v = inflater.inflate(R.layout.custom_loading_view, null);// 得到加载view
            LinearLayout layout = v.findViewById(R.id.dialog_loading_view);// 加载布局
            tipTextView = v.findViewById(R.id.tipTextView);// 提示文字

            loadingDialog = new Dialog(context, R.style.loadingDialogStyle);
            loadingDialog.setCancelable(true); // 是否可以按“返回键”消失
            loadingDialog.setCanceledOnTouchOutside(false); // 点击加载框以外的区域
            loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局

            loadingDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
            Window window = loadingDialog.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setGravity(Gravity.CENTER);
            window.setAttributes(lp);
            window.setWindowAnimations(R.style.PopWindowAnimStyle);
        }
    }

    public void loadingShow() {
        loadingShow("加载中...");
    }

    public void loadingShow(String message) {
        if (tipTextView == null || loadingDialog == null) {
            createDialog(AppUtil.getInstance().getContext());
        }

        if (loadingDialog != null && !loadingDialog.isShowing()) {
            tipTextView.setText(message);
            loadingDialog.show();
        }
    }

    public void loadingDismiss() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }
}
