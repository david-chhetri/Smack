package com.foo.smack.Controller

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.foo.smack.R
import com.foo.smack.Services.AuthService
import com.foo.smack.Services.UserDataService
import com.foo.smack.Utilities.BROADCAST_USER_DATA_CHANGE
import kotlinx.android.synthetic.main.activity_create_user.*
import java.util.*

class CreateUserActivity : AppCompatActivity() {

    var userAvatar = "profileDefault"
    var avatarColor = "[0.5,0.5,0.5,1]"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)
        createSpinner.visibility = View.INVISIBLE
    }


    fun generateUserAvatar(view: View){
        val random = Random()
        val color = random.nextInt(2)  //upperbound not included
        val avatar = random.nextInt(28)

        if(color == 0){
            userAvatar = "light$avatar"
        }else{
            userAvatar = "dark$avatar"
        }

        val resourceId = resources.getIdentifier(userAvatar,"drawable",packageName)
        createAvatarImageView.setImageResource(resourceId)
    }

    fun generateColorClicked(view: View){
        val random = Random()
        val r = random.nextInt(255)
        val g = random.nextInt(255)
        val b = random.nextInt(255)

        createAvatarImageView.setBackgroundColor(Color.rgb(r,g,b))

        val savedR = r.toDouble() / 255
        val savedG = g.toDouble() / 255
        val savedB = b.toDouble() / 255
        avatarColor = "[$savedR, $savedG, $savedB, 1]"
    }


    fun createUserClicked(view: View){
        enableSpinner(true)
        val userName = createUserNameTxt.text.toString()
        val email = createEmailTxt.text.toString()
        val password =createPasswordTxt.text.toString()

        if(userName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()){
            AuthService.registerUser(this,email,password){registerSuccess ->
                if(registerSuccess){
                    AuthService.loginUser(this,email,password){loginSuccess ->
                        if(loginSuccess){
                            AuthService.createUser(this,userName,email,userAvatar,avatarColor ){createSuccess ->
                                if(createSuccess){
                                    //let the main activity know by local broadcast manager
                                    val userDataChange = Intent(BROADCAST_USER_DATA_CHANGE)
                                    LocalBroadcastManager.getInstance(this).sendBroadcast(userDataChange)
                                    enableSpinner(false)
                                    finish()
                                }else{ errorToast()}

                            }
                        }else{
                            errorToast()
                        }
                    }
                }else{
                    errorToast()
                }
            }
        }else{
            //one or more fields are empty
            Toast.makeText(this,"Make sure that username, email and password are filled in",
                Toast.LENGTH_LONG).show()
            enableSpinner(false)

        }


    }

    fun errorToast(){
        Toast.makeText(this,"Something went wrong , please try again",
            Toast.LENGTH_LONG).show()
        enableSpinner(false)
    }

    fun enableSpinner(enable: Boolean){
        if(enable){
            createSpinner.visibility = View.VISIBLE
        }else{
            createSpinner.visibility = View.INVISIBLE
        }
        createUserBtn.isEnabled = !enable
        createAvatarImageView.isEnabled = !enable
        backgroundColorBtn.isEnabled = !enable

    }

}
