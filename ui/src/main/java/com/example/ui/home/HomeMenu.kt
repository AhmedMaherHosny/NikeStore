package com.example.ui.home

import com.example.ui.R

enum class HomeMenu(val title: String, val icon: Int) {
    PROFILE("Profile", R.drawable.username_ic),
    LOGOUT("Log out", R.drawable.logout_icon),
}

sealed class HomeMenuAction {
    object Close : HomeMenuAction()
    object Logout : HomeMenuAction()
    object Profile : HomeMenuAction()
    data class MenuSelected(val menu: HomeMenu) : HomeMenuAction()
}