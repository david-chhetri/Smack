package com.foo.smack.Controller

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.foo.smack.R
import com.foo.smack.Services.AuthService
import com.foo.smack.Services.UserDataService
import com.foo.smack.Utilities.BROADCAST_USER_DATA_CHANGE
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.nav_header_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        //hiding soft keyboard from message field on start
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)

        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_home,
            R.id.nav_gallery,
            R.id.nav_slideshow
        ), drawerLayout)

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        LocalBroadcastManager.getInstance(this).registerReceiver(userDataChangeReceiver,
            IntentFilter(BROADCAST_USER_DATA_CHANGE)
        )


    }

    private val userDataChangeReceiver = object: BroadcastReceiver(){

        override fun onReceive(context: Context?, intent: Intent?) {
            //this is where we update
            if(AuthService.isLoggedIn){
                userNameNavHeader.text = UserDataService.name
                userEmailNavHeader.text = UserDataService.email
                val resourceId = resources.getIdentifier(UserDataService.avatarName,"drawable",packageName)
                userimageNavHeader.setImageResource(resourceId)
                userimageNavHeader.setBackgroundColor(UserDataService.returnAvatarColor(UserDataService.avatarColor))
                loginBtnNavHeader.text = "Logout"
            }
        }

    }

    fun loginBtnNavClicked(view: View){
        if(AuthService.isLoggedIn){
            //already logged in so now log out as button says logout
            UserDataService.logout()
            userNameNavHeader.text = ""
            userEmailNavHeader.text = ""
            loginBtnNavHeader.text = "LOGIN"
            userimageNavHeader.setImageResource(R.drawable.profiledefault)
            userimageNavHeader.setBackgroundColor(Color.TRANSPARENT)

        }else {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }

    }

    fun addChannelClicked(view: View){


    }
    fun sendMsgBtnClicked(view: View){

    }



    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
