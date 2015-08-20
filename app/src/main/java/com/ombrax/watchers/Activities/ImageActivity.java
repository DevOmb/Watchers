package com.ombrax.watchers.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.ombrax.watchers.Fragments.CropImageFragment;
import com.ombrax.watchers.R;
import com.ombrax.watchers.Utils.StorageUtils;


public class ImageActivity extends AppCompatActivity {

    //region create
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fullScreenMode();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        StorageUtils.createStorageFolderIfNotExists();
        displayContent();
    }
    //endregion

    //region helper
    private void fullScreenMode() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void displayContent() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new CropImageFragment()).commit();
    }
    //endregion
}
