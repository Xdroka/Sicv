package com.tcc.sicv.data.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.tcc.sicv.base.Result
import com.tcc.sicv.data.model.Vehicle
import com.tcc.sicv.utils.Constants.IMAGE_FIELD
import com.tcc.sicv.utils.Constants.MAINTENANCE_FIELD
import com.tcc.sicv.utils.Constants.MARK_FIELD
import com.tcc.sicv.utils.Constants.MODEL_FIELD
import com.tcc.sicv.utils.Constants.POWER_FIELD
import com.tcc.sicv.utils.Constants.PRICE_FIELD
import com.tcc.sicv.utils.Constants.SPEED_FIELD
import com.tcc.sicv.utils.Constants.TYPE_FIELD
import com.tcc.sicv.utils.Constants.USER_COLLECTION_PATH
import com.tcc.sicv.utils.Constants.VEHICLES_COLLECTION_PATH
import java.util.*

class VehiclesRepository {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun getMyVehicles(
            email: String,
            flowState: Result<MutableList<Vehicle>>
    ) {
        db.collection(USER_COLLECTION_PATH).document(email).collection(VEHICLES_COLLECTION_PATH)
                .orderBy(MODEL_FIELD)
                .get()
                .addOnSuccessListener { queryDocumentSnapshots ->
                    val list = ArrayList<Vehicle>()
                    for (item in queryDocumentSnapshots.documents) {
                        list.add(Vehicle(
                                (item.get(IMAGE_FIELD) as String?) ?:"",
                                (item.get(MODEL_FIELD) as String?) ?:"",
                                (item.get(POWER_FIELD) as String?) ?:"",
                                (item.get(PRICE_FIELD) as String?) ?:"",
                                (item.get(SPEED_FIELD) as String?) ?:"",
                                (item.get(MARK_FIELD) as String? ) ?:"",
                                (item.get(TYPE_FIELD) as String? ) ?:"",
                                item.id,
                                item.getBoolean(MAINTENANCE_FIELD)
                        )
                        )
                    }
                    flowState.onSuccess(list)
                }
                .addOnFailureListener { e -> flowState.onFailure(e) }
    }

    fun getAllVehicles(flowState: Result<ArrayList<Vehicle>>) {
        db.collection(VEHICLES_COLLECTION_PATH)
                .orderBy(MODEL_FIELD)
                .get()
                .addOnSuccessListener { queryDocumentSnapshots ->
                    val list = ArrayList<Vehicle>()
                    for (item in queryDocumentSnapshots.documents) {
                        list.size
                        list.add(Vehicle(
                                (item.get(IMAGE_FIELD) as String?) ?: "",
                                (item.get(MODEL_FIELD) as String?) ?: "",
                                (item.get(POWER_FIELD) as String?) ?: "",
                                (item.get(PRICE_FIELD) as String?) ?: "",
                                (item.get(SPEED_FIELD) as String?) ?: "",
                                (item.get(MARK_FIELD) as String? ) ?: "",
                                (item.get(TYPE_FIELD) as String? ) ?: "",
                                "",
                                item.getBoolean(MAINTENANCE_FIELD)
                        )
                        )
                    }
                    flowState.onSuccess(list)
                }
                .addOnFailureListener { e -> flowState.onFailure(e) }
    }

    fun buyVehicle(
            email: String, model: String,
            result: Result<Vehicle>
    ) {
        db.collection(VEHICLES_COLLECTION_PATH).document(model).get()
                .addOnSuccessListener { documentSnapshot ->
                    val imagem = documentSnapshot.get(IMAGE_FIELD) as String?
                    val modelo = documentSnapshot.get(MODEL_FIELD) as String?
                    val potencia = documentSnapshot.get(POWER_FIELD) as String?
                    val preco = documentSnapshot.get(PRICE_FIELD) as String?
                    val velocidade = documentSnapshot.get(SPEED_FIELD) as String?
                    val marca = documentSnapshot.get(MARK_FIELD) as String?
                    val tipo = documentSnapshot.get(TYPE_FIELD) as String?

                    val vehicle = Vehicle(
                            imagem?:"", modelo?:"", potencia?:"",
                            preco ?:"", velocidade ?:"", marca ?:"",
                            tipo ?:"", null, false
                    )
                    putVehicleInUserVehicles(vehicle, email, result)
                }
                .addOnFailureListener { e -> result.onFailure(e) }
    }

    private fun putVehicleInUserVehicles(
            vehicle: Vehicle,
            email: String,
            result: Result<Vehicle>
    ) {
        db.collection(USER_COLLECTION_PATH).document(email).collection(VEHICLES_COLLECTION_PATH)
                .add(vehicle)
                .addOnSuccessListener { documentReference ->
                    vehicle.codigo = documentReference.id
                    result.onSuccess(vehicle)
                }
                .addOnFailureListener { e -> result.onFailure(e) }
    }
}

