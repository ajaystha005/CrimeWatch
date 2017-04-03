package com.mvp.crimewatch.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.widget.Toast;

import com.mvp.crimewatch.CrimeWatchApplication;

/**
 * Created by ajayshrestha on 3/19/17.
 */

public final class ViewUtils {

    /**
     * display the toast message
     *
     * @param context
     * @param message
     */
    public static void showToastMessage(final Context context, final String message) {
        runOnUIThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Run in UI thread
     *
     * @param runnable
     */
    public static void runOnUIThread(Runnable runnable) {
        new Handler().post(runnable);
    }


    /**
     * Get the Marker bitmap with color
     *
     * @param colorCode
     * @param width
     * @param height
     * @return
     */
    public static Bitmap getMarkerBitmap(int colorCode, int width, int height) {
        Bitmap markerBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(markerBitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(CrimeWatchApplication.get().getResources().getColor(colorCode));
        canvas.drawOval(new RectF(0, 0, width * 3 / 4, height * 3 / 4), paint);

        return markerBitmap;
    }
}
