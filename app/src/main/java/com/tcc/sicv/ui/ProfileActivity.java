package com.tcc.sicv.ui;

import android.arch.lifecycle.Observer;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tcc.sicv.R;
import com.tcc.sicv.base.BaseActivity;
import com.tcc.sicv.data.model.FlowState;
import com.tcc.sicv.data.model.User;
import com.tcc.sicv.data.preferences.PreferencesHelper;
import com.tcc.sicv.presentation.ProfileViewModel;
import com.tcc.sicv.utils.OnAlertButtonClick;

public class ProfileActivity extends BaseActivity {
    private ProfileViewModel mViewModel;
    private TextView nameTv;
    private TextView cpfTv;
    private TextView emailTv;
    private TextView dateTv;
    private TextView telTv;
    private Button logoutBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setupToolbar(R.id.main_toolbar, R.string.user_profile, true);
        mViewModel = new ProfileViewModel(new PreferencesHelper(getApplication()));
        setupViews();
        creatingObservers();
    }

    private void creatingObservers() {
        mViewModel.getFlowState().observe(this, new Observer<FlowState<User>>() {
            @Override
            public void onChanged(@Nullable FlowState<User> flowState) {
                if (flowState != null) {
                    handleWithMainFlow(flowState);
                }
            }
        });
        mViewModel.getLogoutState().observe(this, new Observer<FlowState<Void>>() {
            @Override
            public void onChanged(@Nullable FlowState<Void> logoutState) {
                handleWithLogoutState(logoutState);
            }
        });
        logoutBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createConfirmLogoutDialog(
                        getString(R.string.logout_message_confirm),
                        positiveListener,
                        negativeListener);
            }
        });
    }

    private void handleWithLogoutState(@Nullable FlowState<Void> logoutState) {
        if (logoutState != null) {
            switch (logoutState.getStatus()) {
                case LOADING:
                    showLoadingDialog();
                    break;
                case SUCCESS:
                    hideLoadingDialog();
                    Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    break;
                case ERROR:
                    hideLoadingDialog();
                    handleErrors(logoutState.getThrowable());
                    break;
            }
        }
    }

    private void handleWithMainFlow(FlowState<User> flowState) {
        switch (flowState.getStatus()) {
            case LOADING:
                showLoadingDialog();
                logoutBt.setEnabled(false);
                mViewModel.getUserProfile();
                break;
            case SUCCESS:
                hideLoadingDialog();
                User userProfile = flowState.getData();
                if (userProfile == null) return;
                logoutBt.setEnabled(true);
                nameTv.setText(userProfile.getName());
                cpfTv.setText(userProfile.getCpf());
                emailTv.setText(userProfile.getEmail());
                dateTv.setText(userProfile.getDate());
                telTv.setText(userProfile.getTel());
                break;
            case ERROR:
                hideLoadingDialog();
                handleErrors(flowState.getThrowable());
                break;
        }
    }

    private void setupViews() {
        logoutBt = findViewById(R.id.logoutButton);
        nameTv = findViewById(R.id.nameTv);
        cpfTv = findViewById(R.id.cpfTv);
        emailTv = findViewById(R.id.emailTv);
        dateTv = findViewById(R.id.dateTv);
        telTv = findViewById(R.id.telephoneTv);
    }

    private OnAlertButtonClick positiveListener = new OnAlertButtonClick() {
        @Override
        public void onClickButton(DialogInterface dialog) { mViewModel.logout(); }
        @Override
        public String getText() { return getString(R.string.positive_button_text); }
    };

    private OnAlertButtonClick negativeListener = new OnAlertButtonClick() {
        public void onClickButton(DialogInterface dialog) { }
        @Override
        public String getText() { return getString(R.string.negative_button_text); }
    };
}
