package com.tcc.sicv.base

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.MenuItem
import android.view.View
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestoreException
import com.tcc.sicv.R
import com.tcc.sicv.ui.LoadingDialogFragment
import com.tcc.sicv.utils.OnAlertButtonClick

open class BaseActivity : AppCompatActivity() {
    private lateinit var loadingDialog: LoadingDialogFragment
    private lateinit var alertDialog: AlertDialog
    private lateinit var confirmAndExitDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setupViews()
    }

    private fun setupViews() { loadingDialog = LoadingDialogFragment() }

    protected fun showLoadingDialog() = loadingDialog.show(supportFragmentManager, "loading")

    protected fun hideLoadingDialog() = loadingDialog.let{
        if(it.isVisible) it.dismiss()
        it
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun setupToolbar(@IdRes toolbarId: Int, @StringRes titleId: Int,
                     navigationHomeEnabled: Boolean? = true) {
        setSupportActionBar(findViewById<View>(toolbarId) as Toolbar)
        supportActionBar?.let {
            it.title = getString(titleId)
            if(navigationHomeEnabled == true) it.setDisplayHomeAsUpEnabled(true)
        }
    }

    protected fun handleErrors(throwable: Throwable?) {
        when(throwable) {
            is FirebaseAuthUserCollisionException ->
                createErrorDialog(getString(R.string.invalidUserEmail))
            is FirebaseAuthInvalidCredentialsException ->
                createErrorDialog(getString(R.string.invalid_password_login))
            is FirebaseAuthInvalidUserException ->
                createErrorDialog(getString(R.string.invalid_email_login))
            is FirebaseNetworkException ->
                createErrorDialog(getString(R.string.internetConnectionError))
            is FirebaseFirestoreException ->
                if (throwable.message?.matches(getString(
                                R.string.regexFirebaseNetworkException).toRegex()) == true) {
                    createErrorDialog(getString(R.string.internetConnectionError))
                }
            else ->
                createErrorDialog(getString(R.string.problemsInServer))
        }
    }

    private fun createErrorDialog(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setIcon(R.drawable.ic_error_red_24dp)
        builder.setTitle(getString(R.string.error))
        builder.setMessage(message)
        builder.setNegativeButton(getString(R.string.OK)) { _, _ -> }
        alertDialog = builder.create()
        alertDialog.show()
    }

    protected fun setPartTextColor(
            text: String, textPositionStart: Int, textPositionEnd: Int, color: Int
    ): Spannable {
        val wordToSpan = SpannableString(text)
        wordToSpan.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(this, color)),
                textPositionStart, textPositionEnd,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return wordToSpan
    }

    fun createDialog(
            message: String,
            positiveListener: OnAlertButtonClick?,
            negativeListener: OnAlertButtonClick?
    ) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(message)
        positiveListener?.let{ builder.setPositiveButton(it.text){ _, _ -> it.onClickButton()} }
                ?: builder.setPositiveButton(R.string.OK, null)

        negativeListener?.let{ builder.setNegativeButton(it.text){ _, _ -> it.onClickButton() } }
        val logoutDialog = builder.create()
        logoutDialog.show()
    }

    fun createConfirmAndExitDialog(
            message: String,
            dismissListener: DialogInterface.OnDismissListener
    ) {
        val builder = AlertDialog.Builder(this)
        builder.apply{
            setIcon(R.drawable.ic_done_blue_24dp)
            setTitle(getString(R.string.success))
            setMessage(message)
            setNegativeButton(getString(R.string.OK)) { _, _ -> confirmAndExitDialog.dismiss() }
            confirmAndExitDialog = create()
            confirmAndExitDialog.show()
            confirmAndExitDialog.setOnDismissListener(dismissListener)
        }
    }
}
