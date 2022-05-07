package com.dzco.vaccination;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;

import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.droidsonroids.gif.GifImageView;
import  com.dzco.vaccination.R;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    private final CircleImageView accountImage;
    private final GifImageView loadingView;
    private final Activity activity;

    public DownloadImageTask(CircleImageView accountImage, GifImageView loadingView, Activity activity) {
        this.accountImage = accountImage;
        this.loadingView = loadingView;
        this.activity = activity;
    }

    protected Bitmap doInBackground(String... urls) {
        loadingView.setVisibility(View.VISIBLE);
        String url = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(url).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        loadingView.setVisibility(View.GONE);
        accountImage.setImageBitmap(result);
    }
}