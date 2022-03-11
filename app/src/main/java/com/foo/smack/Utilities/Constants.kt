package com.foo.smack.Utilities

/**
 * Created by David Chhetri on 08,March,2022
 */

const val BASE_URL="http://10.0.1.2:3005/v1/"

//10.0.2.2 is default address android looks for from emulator
//const val BASE_URL="http://10.0.2.2:3005/v1/"
const val URL_REGISTER="${BASE_URL}account/register"
const val URL_LOGIN="${BASE_URL}account/login"
const val URL_CREATE_USER="${BASE_URL}user/add"
const val URL_GET_USER= "${BASE_URL}user/byEmail/"

//Broadcast Constants
const val BROADCAST_USER_DATA_CHANGE = "BROADCAST_USER_DATA_CHANGE"