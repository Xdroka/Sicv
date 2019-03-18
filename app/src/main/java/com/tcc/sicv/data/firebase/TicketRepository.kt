package com.tcc.sicv.data.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.tcc.sicv.base.Result
import com.tcc.sicv.data.model.Ticket
import com.tcc.sicv.utils.Constants.CODE_VEHICLE_FIELD
import com.tcc.sicv.utils.Constants.CODE_VEHICLE_TICKET_FIELD
import com.tcc.sicv.utils.Constants.COST_TICKET_FIELD
import com.tcc.sicv.utils.Constants.DATE_BUY_TICKET_FIELD
import com.tcc.sicv.utils.Constants.MAINTENANCE_COLLECTION_PATH
import com.tcc.sicv.utils.Constants.MAINTENANCE_FIELD
import com.tcc.sicv.utils.Constants.TICKET_COLLECTION_PATH
import com.tcc.sicv.utils.Constants.TIME_TICKET_FIELD
import com.tcc.sicv.utils.Constants.TYPE_FIELD
import com.tcc.sicv.utils.Constants.USER_COLLECTION_PATH
import com.tcc.sicv.utils.Constants.VEHICLES_COLLECTION_PATH
import java.text.SimpleDateFormat
import java.util.*

class TicketRepository {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun setTicket(email: String, ticket: Ticket, result: Result<Boolean>) {
        ticket.time = SimpleDateFormat("dd/MM/yyyy HH:mm:ss",
                Locale.getDefault()).format(Date())

        db.collection(USER_COLLECTION_PATH).document(email).collection(TICKET_COLLECTION_PATH)
                .document(ticket.ticketId!!)
                .set(ticket)
                .addOnSuccessListener {
                    if (ticket.tipo == MAINTENANCE_FIELD) {
                        removingVehicleFromMaintenance(
                                email, ticket, result
                        )
                    } else
                        result.onSuccess(true)
                }
                .addOnFailureListener { e -> result.onFailure(e) }
    }

    private fun removingVehicleFromMaintenance(
            email: String, ticket: Ticket, result: Result<Boolean>
    ) {
        db.collection(USER_COLLECTION_PATH).document(email).collection(MAINTENANCE_COLLECTION_PATH)
                .whereEqualTo(CODE_VEHICLE_FIELD, ticket.codigoVeiculo)
                .get()
                .addOnSuccessListener { queryDocumentSnapshots ->
                    for (item in queryDocumentSnapshots.documents) {
                        val maintenanceCode = item.id
                        db.collection(USER_COLLECTION_PATH)
                                .document(email)
                                .collection(MAINTENANCE_COLLECTION_PATH)
                                .document(maintenanceCode)
                                .delete()
                                .addOnSuccessListener { updateFieldInVehicle(email, ticket.codigoVeiculo, result) }
                                .addOnFailureListener { e -> result.onFailure(e) }
                    }
                }
                .addOnFailureListener { e -> result.onFailure(e) }
    }

    private fun updateFieldInVehicle(
            email: String, codeVehicle: String, result: Result<Boolean>
    ) {
        db.collection(USER_COLLECTION_PATH).document(email)
                .collection(VEHICLES_COLLECTION_PATH)
                .document(codeVehicle)
                .update(MAINTENANCE_FIELD, false)
                .addOnFailureListener { e -> result.onFailure(e) }
                .addOnSuccessListener { result.onSuccess(true) }
    }

    fun getTickets(email: String, result: Result<MutableList<Ticket>>) {
        db.collection(USER_COLLECTION_PATH).document(email)
                .collection(TICKET_COLLECTION_PATH)
                .get()
                .addOnSuccessListener { queryDocumentSnapshots ->
                    val ticketList = ArrayList<Ticket>()
                    for (item in queryDocumentSnapshots.documents) {
                        ticketList.add(Ticket(
                                (item.get(COST_TICKET_FIELD) as String?) ?:"",
                                (item.get(TYPE_FIELD) as String?) ?: "",
                                (item.get(CODE_VEHICLE_TICKET_FIELD) as String?) ?:"",
                                item.get(TIME_TICKET_FIELD) as String?,
                                item.id,
                                (item.get(DATE_BUY_TICKET_FIELD) as String?) ?:""
                        ))
                    }
                    result.onSuccess(ticketList)
                }
                .addOnFailureListener { e -> result.onFailure(e) }
    }
}
