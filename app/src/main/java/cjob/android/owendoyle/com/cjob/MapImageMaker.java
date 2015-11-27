/*
* This is a class that makes an image from a url and
* puts it in the provided image view.
* This code is taken from http://stackoverflow.com/questions/2471935/how-to-load-an-imageview-by-url-in-android
* */

package cjob.android.owendoyle.com.cjob;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

class MapImageMaker extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    // gets the ImageView
    public MapImageMaker(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    //changes the url image into a bitmap image
    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    // sets the image when finished
    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}
