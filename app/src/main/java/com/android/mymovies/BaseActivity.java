package com.android.mymovies;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

/**
 * Base Activity class used across the application to handle common functionality related to Activities
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected BaseActivity context;
    private ProgressDialog mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setCurrentActivity(context);
    }

    /**
     * Navigates to another screen from current screen
     *
     * @param toScreen - screen object to navigate
     */
    protected void navigateToScreen(Class toScreen) {
        Intent intent = new Intent(context, toScreen);
        startActivity(intent);
    }

    /**
     * Shows and adds action to back icon in tool bar
     *
     * @param toolbar - toolbar
     */
    protected void showAndHandleBackIcon(Toolbar toolbar) {
        // display the back icon always in top bar
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    /**
     * Shows progress bar with message
     *
     * @param message - message to be displayed
     */
    public void showProgressBar(String message) {
        if (mProgressBar != null && mProgressBar.isShowing()) {
            return;
        }
        mProgressBar = new ProgressDialog(context, R.style.Theme_ProgressDialogWithText);
        mProgressBar.setCancelable(false);
        mProgressBar.setCanceledOnTouchOutside(false);
        mProgressBar.setMessage(message);
        mProgressBar.show();
    }

    /**
     * Hide progress bar
     */
    public void hideProgressBar() {
        if (mProgressBar != null &&
                mProgressBar.isShowing()) {
            mProgressBar.dismiss();
        }
    }


    public void showToast(int id) {
        Toast.makeText(context, getString(id), Toast.LENGTH_LONG).show();
    }
}