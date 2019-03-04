package com.tcc.sicv.base;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import com.tcc.sicv.R;
import com.tcc.sicv.presentation.model.FlowState;
import com.tcc.sicv.ui.Exceptions;
import com.tcc.sicv.ui.LoadingDialogFragment;

import java.util.Objects;

public class BaseActivity extends AppCompatActivity {
    LoadingDialogFragment loadingDialog;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        loadingDialog = new LoadingDialogFragment();
    }

    public void setupToolbar(@IdRes int toolbarId, @StringRes int titleId,
                             Boolean navigationHomeEnabled) {
        setSupportActionBar((Toolbar) findViewById(toolbarId));
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            if (navigationHomeEnabled) supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setTitle(titleId);
        }
    }

    protected void showLoadingDialog(){
        loadingDialog.show(getSupportFragmentManager(),"");
    }

    protected void hideLoadingDialog(){
        loadingDialog.dismiss();
    }

    protected void handleErrors(Throwable throwable) {
        if (throwable != null){
            if (throwable instanceof Exceptions.NoInternetException){
                createErrorDialog(getString(R.string.internetConnectionError));
            }else if (throwable instanceof Exceptions.InvalidUserEmailData){
                createErrorDialog(getString(R.string.invalidUserEmail));
            }
            else if(throwable instanceof Exceptions.InvalidLogin){
                createErrorDialog(getString(R.string.invalid_login_error));
            }
            else{
                createErrorDialog(getString(R.string.problemsInServer));
            }
        }
    }

    private void createErrorDialog(String message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_error_red_24dp);
        builder.setTitle(getString(R.string.error));
        builder.setMessage(message);
        builder.setNegativeButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) { }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }
}
