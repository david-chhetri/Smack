package com.foo.smack.ui.home

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.foo.smack.R

class HomeSecondFragmentDirections private constructor() {
  companion object {
    fun actionHomeSecondFragmentToHomeFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_HomeSecondFragment_to_HomeFragment)
  }
}
