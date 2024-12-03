package com.jtun.router.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.jtun.router.R;

public class ConfirmDialog implements View.OnClickListener {
    private Context mContext;
    private Dialog mDialog;
    private View mDialogContentView;
    private TextView textTitle;//标题
    private TextView textMessage;//消息
    private TextView textConfirm;//确定
    private TextView textCancel;//取消
    private OnConfirmListener onDialogListener;
    private String content;

    public ConfirmDialog(Context context, OnConfirmListener listener) {
        this.mContext = context;
        this.onDialogListener = listener;
        init();
    }

    private void init() {
        mDialog = new Dialog(mContext, R.style.custom_dialog);
        mDialogContentView = LayoutInflater.from(mContext).inflate(R.layout.dialog_confirm_program, null);
        textTitle = mDialogContentView.findViewById(R.id.text_dialog_title);
        textTitle.setText(R.string.tips);
        textMessage = mDialogContentView.findViewById(R.id.text_dialog_message);
        textMessage.setText(R.string.reboot_message);
        textConfirm = mDialogContentView.findViewById(R.id.text_dialog_confirm);
        textCancel = mDialogContentView.findViewById(R.id.text_dialog_cancel);
        mDialogContentView.findViewById(R.id.close).setOnClickListener(this);
        textConfirm.setOnClickListener(this);
        textCancel.setOnClickListener(this);
        mDialog.setContentView(mDialogContentView);
    }

    public void setInfo(String content, String msg, String confirmStr, String cancelStr) {
        this.content = content;
        textMessage.setText(msg);
        textConfirm.setText(confirmStr);
        textCancel.setText(cancelStr);
    }

    public void setBackground(int color) {
        GradientDrawable gradientDrawable = (GradientDrawable) mDialogContentView.getBackground();
        gradientDrawable.setColor(color);
    }

    public void show() {
        mDialog.show();
    }

    public void dismiss() {
        if (mDialog.isShowing())
            mDialog.dismiss();
    }

    public Dialog getDialog() {
        return mDialog;
    }

    public void setCanceledOnTouchOutside(boolean cancel) {
        mDialog.setCanceledOnTouchOutside(cancel);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.text_dialog_confirm){
            confirm();
        }else{
            cancel();
        }
    }

    /**
     * 确定
     */
    void confirm() {
        if (content == null)
            content = "";
        if (onDialogListener != null) {
            onDialogListener.onConfirm(content);
        }
        dismiss();
    }

    void cancel() {
        if (onDialogListener != null) {
            onDialogListener.onCancel();
        }
        dismiss();
    }

    public void setOnDialogListener(OnConfirmListener onDialogListener) {
        if (onDialogListener == null) {
            return;
        }
        this.onDialogListener = onDialogListener;
    }
}
