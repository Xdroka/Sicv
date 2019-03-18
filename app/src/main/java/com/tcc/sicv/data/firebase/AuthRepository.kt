package com.tcc.sicv.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tcc.sicv.base.Result
import com.tcc.sicv.data.model.User
import com.tcc.sicv.utils.Constants.USER_COLLECTION_PATH

class AuthRepository {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun logout(result: Result<Unit>) {
        mAuth.signOut()
        result.onSuccess(Unit)
    }

    fun signIn(email: String, password: String, result: Result<String>) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                result.onSuccess(email)
            } else {
                result.onFailure(task.exception)
            }
        }
    }

    fun getUserProfile(email: String, result: Result<User>) {
        db.collection(USER_COLLECTION_PATH).document(email).get()
                .addOnSuccessListener { documentSnapshot ->
                    val cpf = documentSnapshot.get("cpf") as String?
                    val date = documentSnapshot.get("date") as String?
                    val name = documentSnapshot.get("name") as String?
                    val tel = documentSnapshot.get("tel") as String?
                    result.onSuccess(User(email, "", name, cpf, tel, date))
                }
                .addOnFailureListener { e -> result.onFailure(e) }
    }

    private fun createUserDocument(user: User, result: Result<String>) {
        db.collection(USER_COLLECTION_PATH).document(user.email?:"")
                .set(user)
                .addOnSuccessListener { result.onSuccess(user.email?:"") }
                .addOnFailureListener { e -> result.onFailure(e) }
    }

    fun checkAccountExistAndCreateUser(
            user: User,
            result: Result<String>
    ) {
        mAuth.createUserWithEmailAndPassword(user.email?:"", user.password?:"")
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        createUserDocument(user, result)
                    } else {
                        result.onFailure(task.exception)
                    }
                }
    }
}
