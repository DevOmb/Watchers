package com.ombrax.watchers.Activities;

import android.os.Bundle;

import com.ombrax.watchers.Fragments.CropImageFragment;
import com.ombrax.watchers.R;
import com.ombrax.watchers.Utils.StorageUtils;


public class ImageActivity extends FullScreenActivity {

    //region create
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StorageUtils.createStorageFolderIfNotExists();
    }
    //endregion

    //region helper
    @Override
    protected void displayFullScreenContent() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new CropImageFragment()).commit();
    }
    //endregion
}
