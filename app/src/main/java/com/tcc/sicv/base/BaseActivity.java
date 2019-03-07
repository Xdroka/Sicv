package com.tcc.sicv.base;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;

import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.gson.Gson;
import com.tcc.sicv.R;
import com.tcc.sicv.ui.LoadingDialogFragment;
import com.tcc.sicv.utils.OnAlertButtonClick;

public class BaseActivity extends AppCompatActivity {
    LoadingDialogFragment loadingDialog;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setupViews();
    }

    private void setupViews() {
        loadingDialog = new LoadingDialogFragment();
    }

    protected void showLoadingDialog() {
        loadingDialog.show(getSupportFragmentManager(), "");
    }

    protected void hideLoadingDialog() {
        loadingDialog.dismiss();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item != null && item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
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


    protected void handleErrors(Throwable throwable) {
        if (throwable != null) {
            if (throwable instanceof FirebaseAuthUserCollisionException) {
                createErrorDialog(getString(R.string.invalidUserEmail));
            } else if (throwable instanceof FirebaseAuthInvalidCredentialsException) {
                createErrorDialog(getString(R.string.invalid_password_login));
            } else if (throwable instanceof FirebaseAuthInvalidUserException) {
                createErrorDialog(getString(R.string.invalid_email_login));
            } else if (
                    throwable instanceof FirebaseNetworkException ||
                            (
                                    throwable instanceof FirebaseFirestoreException &&
                                            throwable.getMessage().matches(getString(R.string.regexFirebaseNetworkException))
                            )
            ) {
                createErrorDialog(getString(R.string.internetConnectionError));
            } else {
                createErrorDialog(getString(R.string.problemsInServer));
            }
        } else {
            createErrorDialog(getString(R.string.problemsInServer));
        }

    }

    private void createErrorDialog(String message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_error_red_24dp);
        builder.setTitle(getString(R.string.error));
        builder.setMessage(message);
        builder.setNegativeButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }

    @NonNull
    protected Spannable setPartTextColor(String text, int textPositionStart, int textPositionEnd, int color) {
        Spannable wordtoSpan = new SpannableString(text);
        wordtoSpan.setSpan(new ForegroundColorSpan(
                        getResources().getColor(color)), textPositionStart, textPositionEnd,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        return wordtoSpan;
    }

    protected String toJson(Object item){
        return new Gson().toJson(item);
    }

    public void createConfirmLogoutDialog(
            String message,
            final OnAlertButtonClick positiveListener,
            final OnAlertButtonClick negativeListener
    ) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(message);
        if (positiveListener != null) {
            builder.setPositiveButton(positiveListener.getText(),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            positiveListener.onClickButton(dialog);
                        }
                    });
        }
        else{
            builder.setPositiveButton(getString(R.string.ok_button_text), null);
        }
        if (negativeListener != null) {
            builder.setNegativeButton(negativeListener.getText(),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int arg1) {
                            negativeListener.onClickButton(dialog);
                        }
                    });
        }
        AlertDialog logoutDialog = builder.create();
        logoutDialog.show();
    }
}
