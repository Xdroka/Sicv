package com.tcc.sicv.ui;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tcc.sicv.R;
import com.tcc.sicv.base.BaseActivity;
import com.tcc.sicv.data.model.FlowState;
import com.tcc.sicv.data.model.Vehicle;
import com.tcc.sicv.presentation.VehicleDetailsViewModel;
import com.tcc.sicv.utils.ImageHelper;

import static com.tcc.sicv.utils.Constants.RESULT_TAG;

public class VehicleDetailsActivity extends BaseActivity {
    private VehicleDetailsViewModel mViewModel;
    private ImageView vehicleIv;
    private Button buyVehicleBt;
    private TextView markTv;
    private TextView modelTv;
    private TextView powerTv;
    private TextView priceTv;
    private TextView typeTv;
    private TextView speedTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_details);
        setupViews();
        mViewModel = new VehicleDetailsViewModel();
        creatingObservers();
        getDataFromBundle();
    }

    private void creatingObservers() {
        mViewModel.getFlowState().observe(this, new Observer<FlowState<Vehicle>>() {
            @Override
            public void onChanged(@Nullable FlowState<Vehicle> flowState) {
                if (flowState != null) {
                    handleWithMainFlow(flowState);
                }
            }
        });

        buyVehicleBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Abrir Dialog",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void handleWithMainFlow(FlowState<Vehicle> flowState) {
        switch (flowState.getStatus()) {
            case ERROR:
                handleErrors(flowState.getThrowable());
                break;
            case SUCCESS:
                Vehicle clickedVehicle = flowState.getData();
                if (clickedVehicle != null) {
                    ImageHelper.loadImageUrl(clickedVehicle.getImagem(), vehicleIv);
                    typeTv.setText(
                            setupVehicleText(R.string.type_format,
                                    clickedVehicle.getTipo(),16)
                    );
                    markTv.setText(
                            setupVehicleText(
                                    R.string.mark_format,
                                    clickedVehicle.getMarca(),6
                            )
                    );
                    speedTv.setText(
                            setupVehicleText(
                                    R.string.speed_format,
                                    clickedVehicle.getVelocidade(),18
                            )
                    );
                    powerTv.setText(
                            setupVehicleText(
                                    R.string.power_format,
                                    clickedVehicle.getPotencia(),9
                            )
                    );
                    modelTv.setText(
                            setupVehicleText(
                                    R.string.model_format,
                                    clickedVehicle.getModelo(),7
                            )
                    );
                    priceTv.setText(
                            setupVehicleText(
                                    R.string.price_format,
                                    clickedVehicle.getPreco(),6
                            )
                    );
                }
                break;
        }
    }

    @NonNull
    private Spannable setupVehicleText(int format,String attribute, int textPositionEnd) {
        return setPartTextColor(
                                String.format(getString(format),
                                        attribute
                                ),
                                0,
                                textPositionEnd,
                                R.color.colorPrimary
                        );
    }

    private void getDataFromBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String gson = (String) bundle.get(RESULT_TAG);
            mViewModel.getVehicle(gson);
        }
    }

    private void setupViews() {
        vehicleIv = findViewById(R.id.detailsVehicleIv);
        buyVehicleBt = findViewById(R.id.detailsBuyVehicleBt);
        markTv = findViewById(R.id.detailsMarkTv);
        modelTv = findViewById(R.id.detailsModelTv);
        powerTv = findViewById(R.id.detailsPowerTv);
        priceTv = findViewById(R.id.detailsPriceTv);
        typeTv = findViewById(R.id.detailsTypeTv);
        speedTv = findViewById(R.id.detailsSpeedTv);
        setupToolbar(R.id.main_toolbar, R.string.vehicle_details, true);
    }
}
