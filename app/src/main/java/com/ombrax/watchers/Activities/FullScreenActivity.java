package com.ombrax.watchers.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.ombrax.watchers.R;

/**
 * Created by Ombrax on 31/08/2015.
 */
public abstract class FullScreenActivity extends AppCompatActivity {

    //region create
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fullScreenMode();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);
        displayFullScreenContent();
    }
    //endregion

    //region helper
    private void fullScreenMode() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    protected abstract void displayFullScreenContent();
    //endregion
}
