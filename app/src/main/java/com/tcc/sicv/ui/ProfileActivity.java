package com.tcc.sicv.ui;

import android.os.Bundle;
import android.widget.TextView;

import com.tcc.sicv.R;
import com.tcc.sicv.base.BaseActivity;

public class ProfileActivity extends BaseActivity {
    TextView nameTv;
    TextView cpfTv;
    TextView emailTv;
    TextView dateTv;
    TextView telTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setupToolbar(R.id.main_toolbar,R.string.user_profile,true);
        setupViews();
    }

    private void setupViews() {
        nameTv = findViewById(R.id.nameTv);
        cpfTv = findViewById(R.id.cpfTv);
        emailTv = findViewById(R.id.emailTv);
        dateTv = findViewById(R.id.dateTv);
        telTv = findViewById(R.id.telephoneTv);
    }
}
