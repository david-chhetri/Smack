package com.foo.smack.Controller

import android.content.*
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.foo.smack.Model.Channel
import com.foo.smack.R
import com.foo.smack.Services.AuthService
import com.foo.smack.Services.MessageService
import com.foo.smack.Services.UserDataService
import com.foo.smack.Utilities.BROADCAST_USER_DATA_CHANGE
import com.foo.smack.Utilities.SOCKET_URL
import com.google.android.material.navigation.NavigationView
import io.socket.client.IO
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.nav_header_main.*


class MainActivity : AppCompatActivity() {

    val socket = IO.socket(SOCKET_URL)

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        socket.connect()
        socket.on("channelCreated",onNewChannel)

        //hideKeyboard()
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


    }


    override fun onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(userDataChangeReceiver,
            IntentFilter(BROADCAST_USER_DATA_CHANGE))

        super.onResume()

    }


    override fun onDestroy() {
        socket.disconnect()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(userDataChangeReceiver)
        super.onDestroy()
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
        if(AuthService.isLoggedIn) {
            //create channel and description
            val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.add_chhanel_dialog, null)

            builder.setView(dialogView).setPositiveButton("Add") { dialog, which ->
                //grab channel name & description here
                val nameTextView = dialogView.findViewById<EditText>(R.id.addChannelNameTxt)
                val descTextView = dialogView.findViewById<EditText>(R.id.addChannelDescTxt)

                val channelName = nameTextView.text.toString()
                val channelDesc = descTextView.text.toString()
                //sending information from client to API
                socket.emit("newChannel", channelName, channelDesc)
                println("Dave add channel clicked")

            }.setNegativeButton("Cancel") { dialog, which ->

            }.show()
        }
    }


    private val onNewChannel = Emitter.Listener { args ->
        //println( args[0] as String)
        //io.emit("channelCreated", channel.name, channel.description, channel.id);
        println("Dave emitter is listening")
        runOnUiThread {
            println("Dave emitter is sending something")
            val channelName = args[0] as String
            val channelDescription = args[1] as String
            val channelId = args[2] as String

            val newChannel = Channel(channelName,channelDescription,channelId)
            MessageService.channels.add(newChannel)
            println("Dave " + newChannel.name)
            println("Dave " + newChannel.description)
            println("Dave "+ newChannel.id)
        }
    }


    fun sendMsgBtnClicked(view: View){
        hideKeyboard()
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    fun hideKeyboard(){
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if(inputManager.isAcceptingText){
            inputManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        }
    }
}
