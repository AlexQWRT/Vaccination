package com.dzco.vaccination;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;

import androidx.annotation.NonNull;

import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.droidsonroids.gif.GifImageView;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    private final CircleImageView accountImage;
    private final GifImageView loadingView;
    private final Activity activity;

    public DownloadImageTask(CircleImageView accountImage, GifImageView loadingView, Activity activity) {
        this.accountImage = accountImage;
        this.loadingView = loadingView;
        this.activity = activity;
    }

    protected void onPreExecute() {
        loadingView.setVisibility(View.VISIBLE);
    }

    protected Bitmap doInBackground(@NonNull String... urls) {
        String url = urls[0];
        Bitmap image = null;
        try {
            InputStream in = new java.net.URL(url).openStream();
            image = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    protected void onPostExecute(Bitmap result) {
        loadingView.setVisibility(View.GONE);
        accountImage.setImageBitmap(result);
    }
}
