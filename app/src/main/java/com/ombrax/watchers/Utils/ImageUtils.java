package com.ombrax.watchers.Utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;

import com.ombrax.watchers.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Ombrax on 2/08/2015.
 */
public class ImageUtils {

    //region method

    public static Bitmap getScaledCircularBitmap(Drawable drawable, int newSize) {
        Bitmap sourceBitmap = getBitmapFromDrawable(drawable);
        Bitmap croppedBitmap = getCroppedBitmap(sourceBitmap);
        int squareDimension = croppedBitmap.getWidth();

        Matrix matrix = new Matrix();
        float deltaSize = ((float) newSize / squareDimension);
        matrix.postScale(deltaSize, deltaSize);
        Bitmap resizedBitmap = Bitmap.createBitmap(croppedBitmap, 0, 0, squareDimension, squareDimension, matrix, true);
        int resizedDimension = resizedBitmap.getWidth();

        Paint paint = new Paint();
        paint.setShader(new BitmapShader(resizedBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);

        Bitmap circularBitmap = Bitmap.createBitmap(resizedDimension, resizedDimension, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(circularBitmap);
        canvas.drawCircle(resizedDimension / 2, resizedDimension / 2, resizedDimension / 2, paint);

        return circularBitmap;
    }
    //endregion

    //region helper
    private static Bitmap getBitmapFromDrawable(Drawable drawable) {
        Bitmap bitmap;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    private static Bitmap getCroppedBitmap(Bitmap sourceBitmap) {
        int width = sourceBitmap.getWidth();
        int height = sourceBitmap.getHeight();
        int finalDimension = Math.min(width, height);
        return Bitmap.createBitmap(sourceBitmap, calcOffset(width, height), calcOffset(height, width), finalDimension, finalDimension);
    }

    private static int calcOffset(int a, int b) {
        return a > b ? Math.abs((a - b) / 2) : 0;
    }
    //endregion


    //TEMP - May be remove when images can be loaded from file
    private static List<Integer> resourceList = new ArrayList<>(Arrays.asList(R.drawable.yu_gi_oh, R.drawable.naruto, R.drawable._3, R.drawable._4, R.drawable._5));

    private static int index = 0;

    public static int getSingleResource() {
        return resourceList.get(index++%resourceList.size());
    }
}
