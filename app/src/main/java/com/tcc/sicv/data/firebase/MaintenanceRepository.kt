package com.tcc.sicv.data.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.tcc.sicv.base.Result
import com.tcc.sicv.data.model.Logs
import com.tcc.sicv.data.model.MaintenanceVehicle
import com.tcc.sicv.data.model.Vehicle
import com.tcc.sicv.utils.Constants.CODE_VEHICLE_FIELD
import com.tcc.sicv.utils.Constants.COST_FIELD
import com.tcc.sicv.utils.Constants.DATE_FIELD
import com.tcc.sicv.utils.Constants.DESCRIPTION_FIELD
import com.tcc.sicv.utils.Constants.IMAGE_FIELD
import com.tcc.sicv.utils.Constants.LOGS_COLLECTION_PATH
import com.tcc.sicv.utils.Constants.MAINTENANCE_COLLECTION_PATH
import com.tcc.sicv.utils.Constants.MAINTENANCE_FIELD
import com.tcc.sicv.utils.Constants.MODEL_FIELD
import com.tcc.sicv.utils.Constants.RELEASE_VEHICLE_FIELD
import com.tcc.sicv.utils.Constants.USER_COLLECTION_PATH
import com.tcc.sicv.utils.Constants.VEHICLES_COLLECTION_PATH
import java.util.*

class MaintenanceRepository {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun setVehicleInMaintenance(
            email: String,
            vehicle: Vehicle,
            date: String,
            result: Result<MaintenanceVehicle>
    ) {
        val document = db.collection(USER_COLLECTION_PATH)
                .document(email)
                .collection(MAINTENANCE_COLLECTION_PATH)
                .document()

        val maintenanceVehicle = MaintenanceVehicle(
                document.id, vehicle.modelo, vehicle.imagem,
                vehicle.codigo?:"", false
        )
        document
                .set(maintenanceVehicle)
                .addOnFailureListener { e -> result.onFailure(e) }.addOnSuccessListener { setFirstLogInMaintenance(email, maintenanceVehicle, date, result) }
    }

    private fun setFirstLogInMaintenance(
            email: String, maintenanceVehicle: MaintenanceVehicle,
            date: String,
            result: Result<MaintenanceVehicle>
    ) {
        val firstLogDescription = "Entrega do veículo a concessionária"
        db.collection(USER_COLLECTION_PATH).document(email).collection(MAINTENANCE_COLLECTION_PATH)
                .document(maintenanceVehicle.maintenanceCode)
                .collection(LOGS_COLLECTION_PATH)
                .add(Logs(date, firstLogDescription, "R$ 0,00"))
                .addOnSuccessListener { setMyVehicleInMaintenance(email, maintenanceVehicle, result) }
                .addOnFailureListener { e -> result.onFailure(e) }
    }

    private fun setMyVehicleInMaintenance(
            email: String, maintenance: MaintenanceVehicle,
            result: Result<MaintenanceVehicle>
    ) {
        db.collection(USER_COLLECTION_PATH).document(email).collection(VEHICLES_COLLECTION_PATH)
                .document(maintenance.cod_veiculo)
                .update(MAINTENANCE_FIELD, true)
                .addOnSuccessListener { result.onSuccess(maintenance) }
                .addOnFailureListener { e -> result.onFailure(e) }
    }

    fun getVehiclesInMaintenance(
            email: String,
            result: Result<MutableList<MaintenanceVehicle>>
    ) {
        db.collection(USER_COLLECTION_PATH).document(email).collection(MAINTENANCE_COLLECTION_PATH)
                .orderBy(MODEL_FIELD)
                .get()
                .addOnSuccessListener { queryDocumentSnapshots ->
                    val maintenanceList = ArrayList<MaintenanceVehicle>()
                    for (item in queryDocumentSnapshots.documents) {
                        val maintenance = MaintenanceVehicle(
                                item.id,
                                (item.get(MODEL_FIELD) as String?) ?:"",
                                (item.get(IMAGE_FIELD) as String?) ?:"",
                                (item.get(CODE_VEHICLE_FIELD) as String?) ?:"",
                                item.getBoolean(RELEASE_VEHICLE_FIELD)
                        )
                        maintenanceList.add(maintenance)
                    }
                    result.onSuccess(maintenanceList)
                }
                .addOnFailureListener { e -> result.onFailure(e) }
    }

    fun getLogsInMaintenance(
            email: String,
            maintenanceCode: String,
            result: Result<ArrayList<Logs>>
    ) {
        db.collection(USER_COLLECTION_PATH).document(email)
                .collection(MAINTENANCE_COLLECTION_PATH)
                .document(maintenanceCode)
                .collection(LOGS_COLLECTION_PATH)
                .get()
                .addOnSuccessListener { queryDocumentSnapshots ->
                    val logsList = ArrayList<Logs>()
                    for (item in queryDocumentSnapshots.documents) {
                        logsList.add(Logs(
                                (item.get(DATE_FIELD) as String?) ?:"",
                                (item.get(DESCRIPTION_FIELD) as String?) ?:"",
                                (item.get(COST_FIELD) as String?) ?:""
                        ))
                    }
                    result.onSuccess(logsList)
                }
                .addOnFailureListener { e -> result.onFailure(e) }
    }
}
