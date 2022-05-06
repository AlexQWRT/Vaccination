package com.dzco.myapplication;

import android.app.ProgressDialog;
import android.content.Context;


public class InProgress {
    private ProgressDialog progress;
    private Context context;

    public InProgress(Context newContext) {
        setContext(newContext);
        progress = new ProgressDialog(context);
        progress.setMessage(context.getString(R.string.loading));
        progress.setIndeterminate(true);
        progress.setCancelable(false);
    }

    public void setContext(Context newContext) {
        context = newContext;
    }

    public void show() {
        progress.show();
    }

    public void hide() {
        progress.hide();
    }

}
