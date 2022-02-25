package com.foo.smack.ui.home

import android.os.Bundle
import androidx.navigation.NavDirections
import com.foo.smack.R
import kotlin.Int
import kotlin.String

class HomeFragmentDirections private constructor() {
  private data class ActionHomeFragmentToHomeSecondFragment(
    val myArg: String
  ) : NavDirections {
    override fun getActionId(): Int = R.id.action_HomeFragment_to_HomeSecondFragment

    override fun getArguments(): Bundle {
      val result = Bundle()
      result.putString("myArg", this.myArg)
      return result
    }
  }

  companion object {
    fun actionHomeFragmentToHomeSecondFragment(myArg: String): NavDirections =
        ActionHomeFragmentToHomeSecondFragment(myArg)
  }
}
