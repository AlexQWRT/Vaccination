package com.dzco.myapplication;

import android.app.ProgressDialog;
import android.content.Context;


public class InProgress {
    private ProgressDialog progress;

    public InProgress(Context context) {
        progress = new ProgressDialog(context);
        progress.setMessage(context.getString(R.string.loading));
        progress.setIndeterminate(true);
        progress.setCancelable(false);
    }

    public void show() {
        progress.show();
    }

    public void hide() {
        progress.hide();
    }

}
