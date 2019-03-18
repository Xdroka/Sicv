package com.tcc.sicv.ui

import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.google.gson.Gson
import com.tcc.sicv.R
import com.tcc.sicv.base.BaseActivity
import com.tcc.sicv.data.model.*
import com.tcc.sicv.data.preferences.PreferencesHelper
import com.tcc.sicv.presentation.VehicleDetailsViewModel
import com.tcc.sicv.utils.Constants.BUY_VEHICLE
import com.tcc.sicv.utils.Constants.FROM_ACTIVITY
import com.tcc.sicv.utils.Constants.MAINTENANCE_KEY
import com.tcc.sicv.utils.Constants.MY_VEHICLES
import com.tcc.sicv.utils.Constants.TICKET_KEY
import com.tcc.sicv.utils.hide
import com.tcc.sicv.utils.loadImageUrl
import com.tcc.sicv.utils.startActivityAndFinish
import com.tcc.sicv.utils.toJson
import com.vicmikhailau.maskededittext.MaskedEditText
import kotlinx.android.synthetic.main.activity_vehicle_details.*
import kotlinx.android.synthetic.main.dialog_confirm_operation.view.*

class VehicleDetailsActivity : BaseActivity() {
    private lateinit var confirmDialog: AlertDialog
    private lateinit var mViewModel: VehicleDetailsViewModel
    private var fromActivity: String? = null
    private lateinit var dialogTitleTv: TextView
    private lateinit var dialogVehicleTv: TextView
    private lateinit var dialogAtributeTv: TextView
    private lateinit var dialogDateEt: MaskedEditText
    private lateinit var dialogCancelBt: Button
    private lateinit var dialogOperationBt: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle_details)
        createConfirmOperationDialog()
        setupViews()
        mViewModel = VehicleDetailsViewModel(PreferencesHelper(application))
        creatingObservers()
        getDataFromBundle()
    }

    private fun creatingObservers() {
        mViewModel.getMaintenanceFlow().observe(this, Observer {
            handleWithMaintenanceFlow(it)
        })
        mViewModel.getFlowState().observe(this, Observer { handleWithMainFlow(it) })
        mViewModel.getBuyFlow().observe(this, Observer { handleWithBuyFlow(it) })
        detailsOperationVehicleBt.setOnClickListener { confirmDialog.show() }
        dialogCancelBt.setOnClickListener { confirmDialog.dismiss() }
        dialogOperationBt.setOnClickListener {
            fromActivity?.let { it1 -> mViewModel.processOperation(dialogDateEt.text.toString(), it1) }
        }
        mViewModel.getDateState().observe(this, Observer { handleWithDateState(it) })
    }

    private fun handleWithMaintenanceFlow(maintenanceFlow: FlowState<MaintenanceVehicle>?) {
        when (maintenanceFlow?.status) {
            Status.LOADING -> {
                confirmDialog.dismiss()
                showLoadingDialog()
            }
            Status.SUCCESS -> {
                hideLoadingDialog()
                val dismissListener = DialogInterface.OnDismissListener {
                    maintenanceFlow.data?.let {
                        startActivityAndFinish<DetailsMaintenanceActivity>(
                                mapOf(MAINTENANCE_KEY to it.toJson())
                        )
                    }
                }
                createConfirmAndExitDialog(getString(R.string.successful_maintenance), dismissListener)
            }
            Status.ERROR -> {
                hideLoadingDialog()
                if (maintenanceFlow.throwable != null) {
                    handleErrors(maintenanceFlow.throwable)
                }
            }
            else -> {}
        }
    }

    private fun handleWithDateState(state: State?) {
        when (state) {
            State.EMPTY -> dialogDateEt.error = getString(R.string.empty_date)
            State.VALID -> dialogDateEt.error = null
            State.INVALID -> dialogDateEt.error = getString(R.string.invalid_date)
            else -> {}
        }
    }

    private fun handleWithBuyFlow(buyFlow: FlowState<Vehicle>?) {
        when (buyFlow?.status) {
            Status.LOADING -> {
                confirmDialog.dismiss()
                showLoadingDialog()
                if (buyFlow.throwable != null) {
                    hideLoadingDialog()
                    handleErrors(buyFlow.throwable)
                }
            }
            Status.ERROR -> if (buyFlow.throwable != null) {
                hideLoadingDialog()
                handleErrors(buyFlow.throwable)
            }
            Status.SUCCESS -> {
                hideLoadingDialog()
                val dismissListener = DialogInterface.OnDismissListener {
                    buyFlow.data?.let{ generateTicket(it)}
                }
                createConfirmAndExitDialog(getString(R.string.successful_buy), dismissListener)
            }
            else -> {}
        }
    }

    private fun handleWithMainFlow(flowState: FlowState<Vehicle>?) {
        when (flowState?.status) {
            Status.ERROR -> handleErrors(flowState.throwable)
            Status.SUCCESS -> {
                val clickedVehicle = flowState.data
                if (clickedVehicle != null) {
                    handleWithSuccessMainFlow(clickedVehicle)
                }
            }
            else -> {}
        }
    }

    private fun handleWithSuccessMainFlow(selectedVehicle: Vehicle) {
        detailsVehicleIv.loadImageUrl(selectedVehicle.imagem)
        detailsTypeTv.text = setupVehicleText(R.string.type_format,
                selectedVehicle.tipo, 16)
        detailsMarkTv.text = setupVehicleText(
                R.string.mark_format,
                selectedVehicle.marca, 6
        )
        detailsSpeedTv.text = setupVehicleText(
                R.string.speed_format,
                selectedVehicle.velocidade, 18
        )
        detailsPowerTv.text = setupVehicleText(
                R.string.power_format,
                selectedVehicle.potencia, 9
        )
        val modelText = setupVehicleText(
                R.string.model_format,
                selectedVehicle.modelo, 7
        )
        detailsModelTv.text = modelText
        dialogVehicleTv.text = modelText

        if (fromActivity == BUY_VEHICLE) {
            val priceText = setupVehicleText(
                    R.string.price_format,
                    selectedVehicle.preco, 6
            )
            detailsAtributeTv.text = priceText
            dialogAtributeTv.text = priceText
        } else {
            dialogTitleTv.text = getString(R.string.title_dialog_do_maintenance)
            if (selectedVehicle.manutencao == true) {
                detailsOperationVehicleBt.hide()
            } else {
                detailsOperationVehicleBt.text = getString(R.string.doMaintenance)
            }
            dialogOperationBt.text = getString(R.string.confirm)
            val codeText = setupVehicleText(
                    R.string.code_format,
                    selectedVehicle.codigo, 7
            )
            detailsAtributeTv.text = codeText
            dialogAtributeTv.text = codeText
        }
    }

    private fun setupVehicleText(format: Int, attribute: String?, textPositionEnd: Int): Spannable {
        return setPartTextColor(
                String.format(getString(format),
                        attribute
                ),
                0,
                textPositionEnd,
                R.color.colorPrimary
        )
    }

    private fun getDataFromBundle() {
        val bundle = intent.extras
        val gson: String
        if (bundle != null) {
            fromActivity = bundle.get(FROM_ACTIVITY) as String
            gson = if (fromActivity == BUY_VEHICLE) {
                bundle.get(BUY_VEHICLE) as String
            } else {
                bundle.get(MY_VEHICLES) as String
            }
            mViewModel.getVehicle(gson)
        }
    }

    private fun createConfirmOperationDialog() {
        val builder = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.dialog_confirm_operation,
                LinearLayout(this), false)
        setupDialogViews(view)
        builder.setView(view)
        confirmDialog = builder.create()
    }

    private fun generateTicket(vehicle: Vehicle) {
        var ticketId = vehicle.codigo.hashCode()
        if (ticketId < 0) {
            ticketId = -ticketId
        }
        val ticket = Ticket(
                vehicle.preco, "Compra", vehicle.codigo?: "",
                "", ticketId.toString() + "", mViewModel.selectedDate?:""
        )
        val intent = Intent(this@VehicleDetailsActivity, TicketDetailsActivity::class.java)
        intent.putExtra(TICKET_KEY, Gson().toJson(ticket))
        startActivity(intent)
        finish()
    }

    fun setupDialogViews(view: View) {
        dialogTitleTv = view.dialog_tv
        dialogVehicleTv = view.confirmVehicleTv
        dialogAtributeTv = view.confirmAtributeTv
        dialogDateEt = view.confirmDateEt
        dialogCancelBt = view.confirmExitBt
        dialogOperationBt = view.confirmOperationBt
    }

    private fun setupViews() =
            setupToolbar(R.id.main_toolbar, R.string.vehicle_details, true)
}
