package com.foo.smack.Controller

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.foo.smack.R
import com.foo.smack.Services.AuthService
import com.foo.smack.Services.UserDataService
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.nav_header_main.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun loginLoginBtnClicked(view: View){
        val email = loginEmailTxt.text.toString()
        val password = loginPasswordTxt.text.toString()
        //first login successfully
        AuthService.loginUser(this,email,password){ loginSuccess ->
            if(loginSuccess){
                AuthService.findUserByEmail(this){ findingUserSuccess ->
                    if(findingUserSuccess){
                        finish()

                    }

                }

            }

        }



    }

    fun loginCreateUserBtnClicked(view: View){
        val createUserIntent = Intent(this, CreateUserActivity::class.java)
        startActivity(createUserIntent)
        finish()

    }
}
