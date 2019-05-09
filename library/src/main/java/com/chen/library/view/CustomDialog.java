package com.chen.library.view;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chen.library.R;
import com.chen.library.util.AppUtil;
import com.chen.library.util.DialogClickCallBack;

/**
 * Created by RJS on 2016/8/19.
 */
public class CustomDialog extends Dialog {

    private static TextView dialogTitle, dialogMessage, dialogPositive, dialogNegetive, dialogLine;

    public CustomDialog(Context context) {
        super(context);
        initView(context);
    }

    public CustomDialog(Context context, int themeResId) {
        super(context, themeResId);
        initView(context);
    }

    private void initView(Context context) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.custom_dialog_view, null);
        dialogTitle =  dialogView.findViewById(R.id.custom_dialog_title);
        dialogMessage =  dialogView.findViewById(R.id.custom_dialog_message);
        dialogPositive =  dialogView.findViewById(R.id.custom_dialog_positive);
        dialogNegetive =  dialogView.findViewById(R.id.custom_dialog_negetive);
        dialogLine =  dialogView.findViewById(R.id.custom_dialog_line);
        setContentView(dialogView);

        ViewGroup.LayoutParams params = dialogView.getLayoutParams();
        params.width = (int) (AppUtil.getInstance().getScreenWidth() * 0.8);
        dialogView.setLayoutParams(params);
    }

    public void showDialog(String title, String positive, int dialogID, final DialogClickCallBack callback) {
        showDialog(title, "", positive, "", dialogID, callback);
    }

    public void showDialog(String title, String positive, String negetive, final int dialogID, final DialogClickCallBack callback) {
        showDialog(title, "", positive, negetive, dialogID, callback);
    }

    public void showDialog(String title, String message, String positive, String negetive, final int dialogID, final DialogClickCallBack callback) {
        dialogPositive.setText(positive);
        if (TextUtils.isEmpty(title)) {
            dialogTitle.setVisibility(View.GONE);
        } else {
            dialogTitle.setText(title);
        }

        if (TextUtils.isEmpty(message)) {
            dialogMessage.setVisibility(View.GONE);
        } else {
            dialogMessage.setText(message);
        }

        if (TextUtils.isEmpty(negetive)) {
            dialogLine.setVisibility(View.GONE);
            dialogNegetive.setVisibility(View.GONE);
            dialogPositive.setBackgroundResource(R.drawable.text_selector_bg);
        } else {
            dialogNegetive.setText(negetive);
            dialogNegetive.setOnClickListener(v -> {
                callback.negativeClick(dialogID);
                dismiss();
            });
        }

        dialogPositive.setOnClickListener(v -> {
            callback.positiveClick(dialogID);
            dismiss();
        });
        show();
    }

    @Override
    public void show() {
        if (!isShowing()) {
            super.show();
        }
    }
}
