package com.tcc.sicv.presentation

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.tcc.sicv.base.Result
import com.tcc.sicv.base.ResultListenerFactory
import com.tcc.sicv.data.firebase.MaintenanceRepository
import com.tcc.sicv.data.model.FlowState
import com.tcc.sicv.data.model.FlowState.Companion.loading
import com.tcc.sicv.data.model.Logs
import com.tcc.sicv.data.model.MaintenanceVehicle
import com.tcc.sicv.data.model.Status.LOADING
import com.tcc.sicv.data.model.Ticket
import com.tcc.sicv.data.preferences.PreferencesHelper
import com.tcc.sicv.utils.Constants.MAINTENANCE_COLLECTION_PATH
import java.text.DecimalFormat
import java.util.*

class DetailsMaintenanceViewModel(
        private val preferencesHelper: PreferencesHelper,
        private val maintenance: MaintenanceVehicle?
) : ViewModel() {
    private val repository: MaintenanceRepository = MaintenanceRepository()
    private val flowState: MutableLiveData<FlowState<ArrayList<Logs>>> = MutableLiveData()
    private val resultListener: Result<ArrayList<Logs>>
    var ticket: Ticket? = null
        private set

    val totalCost: String
        get() {
            var totalCost = 0.toFloat()
            val result: String
            return flowState.value?.data?.let { logsList ->
                for (log in logsList) {
                    try {
                        totalCost += java.lang.Float.parseFloat(log.gasto
                                .replace("R$", "")
                                .replace(",", "."))
                    } catch (ignored: NullPointerException) {
                    } catch (ignored: NumberFormatException) {
                    }

                }
                result = convertToString(totalCost)
                setTicket(result)
                return result
            }?: convertToString(totalCost).let {
                setTicket(it)
                it
            }

        }

    val isVehicleFixed: Boolean?
        get() = if (maintenance == null) false else maintenance.veiculo_liberado

    init {
        resultListener = ResultListenerFactory<ArrayList<Logs>>().create(flowState)
        requestLogsMaintenance()
    }

    fun requestLogsMaintenance() {
        if (flowState.value?.status == LOADING || maintenance == null) return
        flowState.postValue(loading())
        repository.getLogsInMaintenance(
                preferencesHelper.email ?: "", maintenance.maintenanceCode, resultListener
        )
    }

    fun getFlowState(): LiveData<FlowState<ArrayList<Logs>>> = flowState

    private fun setTicket(totalCost: String) {
        flowState.value?.data?.let {
            ticket = Ticket(
                    totalCost,
                    MAINTENANCE_COLLECTION_PATH,
                    maintenance!!.cod_veiculo,
                    "",
                    maintenance.maintenanceCode, ""
            )
        }
    }

    private fun convertToString(number: Float?): String {
        val numberString = try {
            val df = DecimalFormat("0.00")
            df.format(number)
                    .replace(".", ",")
        } catch (ignored: Exception) {
            "0,00"
        }

        return "R$ $numberString"
    }
}
