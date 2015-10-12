package com.ombrax.watchers.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.ombrax.watchers.R;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by Ombrax on 2/08/2015.
 */
public class ImageUtils {

    //region constant
    private static final int HD_WIDTH = 1280;
    //endregion

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

    public static Bitmap getScaledBitmap(String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        int dstWidth = HD_WIDTH;
        int srcWidth = options.outWidth;

        if (dstWidth > srcWidth) {
            dstWidth = srcWidth;
        }

        // Calculate the correct inSampleSize/scale value. This helps reduce memory use. It should be a power of 2
        int inSampleSize = 1;
        if (srcWidth > 0 && srcWidth != dstWidth) {
            while (srcWidth / 2 > dstWidth) {
                srcWidth /= 2;
                inSampleSize *= 2;
            }
        }

        float desiredScale = (float) dstWidth / srcWidth;

        // Decode with inSampleSize
        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inSampleSize = inSampleSize;
        options.inScaled = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap sampledSrcBitmap = BitmapFactory.decodeFile(filePath, options);

        // Resize
        Matrix matrix = new Matrix();
        matrix.postScale(desiredScale, desiredScale);
        return Bitmap.createBitmap(sampledSrcBitmap, 0, 0, sampledSrcBitmap.getWidth(), sampledSrcBitmap.getHeight(), matrix, true);
    }

    public static void loadImageFromFile(final ImageView container, String path) {
        if (path != null && !path.isEmpty()) {
            File imageFile = new File(path);
            if (imageFile.exists()) {
                Picasso.with(container.getContext())
                        .load(imageFile)
                        .placeholder(R.drawable.img_loading)
                        .error(R.drawable.img_not_available)
                        .into(container);
            }
        }
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
}
