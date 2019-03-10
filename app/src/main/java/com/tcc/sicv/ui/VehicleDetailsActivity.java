package com.tcc.sicv.ui;

import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tcc.sicv.R;
import com.tcc.sicv.base.BaseActivity;
import com.tcc.sicv.data.model.FlowState;
import com.tcc.sicv.data.model.MaintenanceVehicle;
import com.tcc.sicv.data.model.State;
import com.tcc.sicv.data.model.Ticket;
import com.tcc.sicv.data.model.Vehicle;
import com.tcc.sicv.data.preferences.PreferencesHelper;
import com.tcc.sicv.presentation.VehicleDetailsViewModel;
import com.tcc.sicv.utils.ImageHelper;

import java.util.Objects;

import static com.tcc.sicv.utils.Constants.BUY_VEHICLE;
import static com.tcc.sicv.utils.Constants.FROM_ACTIVITY;
import static com.tcc.sicv.utils.Constants.MAINTENANCE_KEY;
import static com.tcc.sicv.utils.Constants.MY_VEHICLES;
import static com.tcc.sicv.utils.Constants.TICKET_KEY;

public class VehicleDetailsActivity extends BaseActivity {
    AlertDialog confirmDialog;
    private VehicleDetailsViewModel mViewModel;
    private String fromActivity;
    private ImageView vehicleIv;
    private Button operationVehicleBt;
    private TextView dialogTitleTv;
    private TextView markTv;
    private TextView modelTv;
    private TextView powerTv;
    private TextView atributeTv;
    private TextView typeTv;
    private TextView speedTv;
    private TextView dialogVehicleTv;
    private TextView dialogAtributeTv;
    private EditText dialogDateEt;
    private Button dialogCancelBt;
    private Button dialogOperationBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_details);
        createConfirmOperationDialog();
        setupViews();
        mViewModel = new VehicleDetailsViewModel(new PreferencesHelper(getApplication()));
        creatingObservers();
        getDataFromBundle();
    }

    private void creatingObservers() {
        mViewModel.getMaintenanceFlow().observe(this, new Observer<FlowState<MaintenanceVehicle>>() {
            @Override
            public void onChanged(@Nullable FlowState<MaintenanceVehicle> flowState) {
                if (flowState != null) handleWithMaintenanceFlow(flowState);
            }
        });
        mViewModel.getFlowState().observe(this, new Observer<FlowState<Vehicle>>() {
            @Override
            public void onChanged(@Nullable FlowState<Vehicle> flowState) {
                if (flowState != null) {
                    handleWithMainFlow(flowState);
                }
            }
        });
        mViewModel.getBuyFlow().observe(this, new Observer<FlowState<Vehicle>>() {
            @Override
            public void onChanged(@Nullable FlowState<Vehicle> buyFlow) {
                if (buyFlow != null) {
                    handleWithBuyFlow(buyFlow);
                }
            }
        });
        operationVehicleBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog.show();
            }
        });
        dialogCancelBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog.dismiss();
            }
        });
        dialogOperationBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.processOperation(dialogDateEt.getText().toString(), fromActivity);
            }
        });
        mViewModel.getDateState().observe(this, new Observer<State>() {
            @Override
            public void onChanged(@Nullable State state) {
                if (state != null) {
                    handleWithDateState(state);
                }
            }
        });
    }

    private void handleWithMaintenanceFlow(final FlowState<MaintenanceVehicle> maintenanceFlow) {
        switch (maintenanceFlow.getStatus()) {
            case LOADING:
                confirmDialog.dismiss();
                showLoadingDialog();
                break;
            case SUCCESS:
                hideLoadingDialog();
                DialogInterface.OnDismissListener dismissListener =
                        new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                if (maintenanceFlow.hasData()) {
                                    Intent intent = new Intent(
                                            VehicleDetailsActivity.this,
                                            DetailsMaintenanceActivity.class
                                    );
                                    intent.putExtra(
                                            MAINTENANCE_KEY,
                                            new Gson().toJson(maintenanceFlow.getData())
                                    );
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        };
                createConfirmAndExitDialog(getString(R.string.successful_maintenance), dismissListener);
                break;
            case ERROR:
                hideLoadingDialog();
                if(maintenanceFlow.getThrowable() != null){
                    handleErrors(maintenanceFlow.getThrowable());
                }
                break;
        }
    }

    private void handleWithDateState(State state) {
        switch (state) {
            case EMPTY:
                dialogDateEt.setError(getString(R.string.empty_date));
                break;
            case VALID:
                dialogDateEt.setError(null);
                break;
            case INVALID:
                dialogDateEt.setError(getString(R.string.invalid_date));
                break;
        }
    }

    private void handleWithBuyFlow(final FlowState<Vehicle> buyFlow) {
        switch (buyFlow.getStatus()) {
            case LOADING:
                confirmDialog.dismiss();
                showLoadingDialog();
            case ERROR:
                if (buyFlow.getThrowable() != null) {
                    hideLoadingDialog();
                    handleErrors(buyFlow.getThrowable());
                }
                break;
            case SUCCESS:
                hideLoadingDialog();
                DialogInterface.OnDismissListener dismissListener =
                        new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                if (buyFlow.hasData()) generateTicket(buyFlow.getData());
                            }
                        };
                createConfirmAndExitDialog(getString(R.string.successful_buy), dismissListener);
                break;
        }
    }

    private void handleWithMainFlow(FlowState<Vehicle> flowState) {
        switch (flowState.getStatus()) {
            case ERROR:
                handleErrors(flowState.getThrowable());
                break;
            case SUCCESS:
                Vehicle clickedVehicle = flowState.getData();
                if (clickedVehicle != null) {
                    handleWithSuccessMainFlow(clickedVehicle);
                }
                break;
        }
    }

    private void handleWithSuccessMainFlow(Vehicle selectedVehicle) {
        ImageHelper.loadImageUrl(selectedVehicle.getImagem(), vehicleIv);
        typeTv.setText(
                setupVehicleText(R.string.type_format,
                        selectedVehicle.getTipo(), 16)
        );
        markTv.setText(
                setupVehicleText(
                        R.string.mark_format,
                        selectedVehicle.getMarca(), 6
                )
        );
        speedTv.setText(
                setupVehicleText(
                        R.string.speed_format,
                        selectedVehicle.getVelocidade(), 18
                )
        );
        powerTv.setText(
                setupVehicleText(
                        R.string.power_format,
                        selectedVehicle.getPotencia(), 9
                )
        );
        Spannable modelText = setupVehicleText(
                R.string.model_format,
                selectedVehicle.getModelo(), 7
        );
        modelTv.setText(modelText);
        dialogVehicleTv.setText(modelText);

        if (fromActivity.equals(BUY_VEHICLE)) {
            Spannable priceText = setupVehicleText(
                    R.string.price_format,
                    selectedVehicle.getPreco(), 6
            );
            atributeTv.setText(priceText);
            dialogAtributeTv.setText(priceText);
        } else {
            dialogTitleTv.setText(getString(R.string.title_dialog_do_maintenance));
            if (selectedVehicle.getManutencao()) {
                operationVehicleBt.setVisibility(View.GONE);
            } else {
                operationVehicleBt.setText(getString(R.string.doMaintenance));
            }
            dialogOperationBt.setText(getString(R.string.confirm));
            Spannable codeText = setupVehicleText(
                    R.string.code_format,
                    selectedVehicle.getCodigo(), 7
            );
            atributeTv.setText(codeText);
            dialogAtributeTv.setText(codeText);
        }
    }

    @NonNull
    private Spannable setupVehicleText(int format, String attribute, int textPositionEnd) {
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
        String gson;
        if (bundle != null) {
            fromActivity = (String) bundle.get(FROM_ACTIVITY);
            if (Objects.equals(fromActivity, BUY_VEHICLE)) {
                gson = (String) bundle.get(BUY_VEHICLE);
            } else {
                gson = (String) bundle.get(MY_VEHICLES);
            }
            mViewModel.getVehicle(gson);
        }
    }

    private void createConfirmOperationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_confirm_operation,
                new LinearLayout(this), false);
        setupDialogViews(view);
        builder.setView(view);
        confirmDialog = builder.create();
    }

    private void generateTicket(Vehicle vehicle) {
        if (vehicle == null) return;
        Ticket ticket = new Ticket(vehicle.getPreco(), "compra", vehicle.getCodigo(), ""
                , vehicle.getCodigo().hashCode() + ""
        );
        Intent intent = new Intent(VehicleDetailsActivity.this, TicketDetailsActivity.class);
        intent.putExtra(TICKET_KEY, new Gson().toJson(ticket));
        startActivity(intent);
        finish();
    }

    public void setupDialogViews(View view) {
        dialogTitleTv = view.findViewById(R.id.dialog_tv);
        dialogVehicleTv = view.findViewById(R.id.confirmVehicleTv);
        dialogAtributeTv = view.findViewById(R.id.confirmAtributeTv);
        dialogDateEt = view.findViewById(R.id.confirmDateEt);
        dialogCancelBt = view.findViewById(R.id.confirmExitBt);
        dialogOperationBt = view.findViewById(R.id.confirmOperationBt);
    }

    private void setupViews() {
        vehicleIv = findViewById(R.id.detailsVehicleIv);
        operationVehicleBt = findViewById(R.id.detailsOperationVehicleBt);
        markTv = findViewById(R.id.detailsMarkTv);
        modelTv = findViewById(R.id.detailsModelTv);
        powerTv = findViewById(R.id.detailsPowerTv);
        atributeTv = findViewById(R.id.detailsAtributeTv);
        typeTv = findViewById(R.id.detailsTypeTv);
        speedTv = findViewById(R.id.detailsSpeedTv);
        setupToolbar(R.id.main_toolbar, R.string.vehicle_details, true);
    }
}
