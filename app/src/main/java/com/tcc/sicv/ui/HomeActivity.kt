package com.tcc.sicv.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.tcc.sicv.R
import com.tcc.sicv.base.BaseActivity
import com.tcc.sicv.utils.startActivity
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setupToolbar(R.id.main_toolbar, R.string.app_name, false)
        creatingObservers()
    }

    private fun creatingObservers() {
        purchaseButton.setOnClickListener { startActivity<BuyVehiclesActivity>() }
        vehiclesButton.setOnClickListener { startActivity<MyVehiclesActivity>() }
        maintenanceButton.setOnClickListener { startActivity<MaintenanceActivity>() }
        nfButton.setOnClickListener { startActivity<MyTicketsActivity>() }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_item_profile -> startActivity<ProfileActivity>()
        }
        return super.onOptionsItemSelected(item)
    }
}
