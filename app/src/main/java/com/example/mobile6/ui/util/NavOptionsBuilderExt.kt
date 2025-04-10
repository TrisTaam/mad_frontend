package com.example.mobile6.ui.util

import androidx.navigation.NavOptions
import androidx.navigation.ui.R

fun NavOptions.Builder.defaultAnim() = apply {
    setEnterAnim(R.anim.nav_default_enter_anim)
    setExitAnim(R.anim.nav_default_exit_anim)
    setPopEnterAnim(R.anim.nav_default_pop_enter_anim)
    setPopExitAnim(R.anim.nav_default_pop_exit_anim)
}