package com.foo.smack.Services

import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.RetryPolicy
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.foo.smack.Utilities.URL_CREATE_USER
import com.foo.smack.Utilities.URL_LOGIN
import com.foo.smack.Utilities.URL_REGISTER
import kotlinx.android.synthetic.main.activity_create_user.*
import org.json.JSONException
import org.json.JSONObject


/**
 * Created by David Chhetri on 08,March,2022
 */
object AuthService {
    var isLoggedIn = false
    var userEmail = ""
    var authToken = ""


    fun registerUser(context: Context, email: String, password: String, complete: (Boolean) -> Unit){

        val jsonBody = JSONObject()
        jsonBody.put("email", email)
        jsonBody.put("password", password)

        val requestBody = jsonBody.toString()

        val registerRequest = object : StringRequest(Method.POST, URL_REGISTER, Response.Listener { response ->
            println(response)
            complete(true)
        }, Response.ErrorListener { error ->
            Log.d("ERROR", "Not registered: $error")
            complete(false)
        }){
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }
        Volley.newRequestQueue(context).add(registerRequest)

    }


    //The purpose is to build a service
    fun loginUser(context: Context, email: String,password: String, complete: (Boolean) -> Unit){

        val jsonBody = JSONObject()
        jsonBody.put("email", email)
        jsonBody.put("password", password)

        val requestBody = jsonBody.toString()
        //val loginRequest = object: JsonObjectRequest(Method.POST, URL_LOGIN,null,
        val loginRequest = object : JsonObjectRequest(Method.POST,URL_LOGIN,null,Response.Listener { response ->
            //parse json response
            try {
                userEmail = response.getString("user")
                authToken = response.getString("token")
                println("email : $userEmail")
                println("authToken: $authToken")
                complete(true)
                isLoggedIn = true
            }catch (ex: JSONException){
                Log.d("MSG :",ex.toString())
                complete(false)
            }
        }, Response.ErrorListener {error ->
            Log.d("ERROR", "$error" )
            complete(false)
            isLoggedIn = false

        }){
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }

        }

        Volley.newRequestQueue(context).add(loginRequest)

    }

    fun createUser(context: Context, name: String, email: String, avatarName: String, avatarColor: String,complete: (Boolean) -> Unit){

        val jsonBody = JSONObject()
        jsonBody.put("name", name)
        jsonBody.put("email", email)
        jsonBody.put("avatarName", avatarName)
        jsonBody.put("avatarColor", avatarColor)

        val requestBody = jsonBody.toString()


        val createRequest = object: JsonObjectRequest(Method.POST, URL_CREATE_USER,null,Response.Listener {
                response ->

            try {
                UserDataService.name = response.getString("name")
                UserDataService.email = response.getString("email")
                UserDataService.avatarColor = response.getString("avatarColor")
                UserDataService.avatarName = response.getString("avatarName")
                UserDataService.id = response.getString("_id")
                complete(true)
            }catch(e: JSONException){
                Log.d("JSON", "EXC " + e.localizedMessage)
                complete(false)
            }

        }, Response.ErrorListener {
                error ->
            Log.d("ERROR", "Could not add user: $error")
            complete(false)

        }){
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer $authToken")
                return headers
            }


        }

        Volley.newRequestQueue(context).add(createRequest)

    }


}