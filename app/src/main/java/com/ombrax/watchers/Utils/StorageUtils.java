package com.ombrax.watchers.Utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Ombrax on 19/08/2015.
 */
public class StorageUtils {

    private static final String STORAGE_FOLDER = "/Watchers/.img";
    private static final File STORAGE = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + STORAGE_FOLDER);

    public static void createStorageFolderIfNotExists(){
        if(!STORAGE.exists()) {
            STORAGE.mkdirs();
        }
    }

    public static String saveImageToExternalStorage(String fileName, Bitmap bitmap) {
        if (fileName != null && !fileName.isEmpty() && bitmap != null) {
            File file = new File(STORAGE, fileName);
            if (file.exists()) {//replace if exists
                file.delete();
            }
            try {
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
                return file.getAbsolutePath();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String getFilePathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static String getFileNameFromPath(String filePath){
        return filePath.substring(filePath.lastIndexOf(File.separator)+1);
    }

    public static boolean removeFromStorage(String filePath){
        if(filePath == null || filePath.isEmpty())
            return false;
        return new File(filePath).delete();
    }
}
