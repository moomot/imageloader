package com.appsolutions.imageloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Администратор on 13.09.2015.
 */
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    private ImageView bmImage;
    public boolean isLoaded = false;
    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        String url = urls[0];
        Bitmap bitmap = null;
        try {
            InputStream inputStream = new java.net.URL(url).openStream();
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte [] data = new byte[16384];
            while ( (nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
                isLoaded = false;
            }
            buffer.flush();
            byte[] bytes = buffer.toByteArray();

            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            isLoaded = true;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        bmImage.setImageBitmap(bitmap);
    }
}
