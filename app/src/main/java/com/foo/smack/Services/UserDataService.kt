package com.foo.smack.Services

import android.graphics.Color
import java.util.*

/**
 * Created by David Chhetri on 09,March,2022
 */
object UserDataService {
    var id = ""
    var avatarColor = ""
    var avatarName = ""
    var email = ""
    var name = ""

    fun logout(){
         id = ""
         avatarColor = ""
         avatarName = ""
         email = ""
         name = ""
        AuthService.isLoggedIn = false
        AuthService.authToken = ""
        AuthService.userEmail = ""
    }

    fun returnAvatarColor(components: String): Int {
        //[0.2901960784313726, 0.09019607843137255, 0.9176470588235294, 1]
        //0.2901960784313726 0.09019607843137255 0.9176470588235294 1

        val strippedColor = components
            .replace("[","")
            .replace("]","")
            .replace(",","")
        var r=0
        var g=0
        var b=0

        val scanner = Scanner(strippedColor)
        if(scanner.hasNext()){
            r = (scanner.nextDouble() * 255).toInt()
            g = (scanner.nextDouble() * 255).toInt()
            b = (scanner.nextDouble() * 255).toInt()
        }

        return  Color.rgb(r,g,b)

    }



}