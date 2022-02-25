package com.foo.smack.ui.home

import android.os.Bundle
import androidx.navigation.NavArgs
import java.lang.IllegalArgumentException
import kotlin.String
import kotlin.jvm.JvmStatic

data class HomeSecondFragmentArgs(
  val myArg: String
) : NavArgs {
  fun toBundle(): Bundle {
    val result = Bundle()
    result.putString("myArg", this.myArg)
    return result
  }

  companion object {
    @JvmStatic
    fun fromBundle(bundle: Bundle): HomeSecondFragmentArgs {
      bundle.setClassLoader(HomeSecondFragmentArgs::class.java.classLoader)
      val __myArg : String?
      if (bundle.containsKey("myArg")) {
        __myArg = bundle.getString("myArg")
        if (__myArg == null) {
          throw IllegalArgumentException("Argument \"myArg\" is marked as non-null but was passed a null value.")
        }
      } else {
        throw IllegalArgumentException("Required argument \"myArg\" is missing and does not have an android:defaultValue")
      }
      return HomeSecondFragmentArgs(__myArg)
    }
  }
}
