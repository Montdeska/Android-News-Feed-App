package com.montdeska.android_hacker_news.base

import androidx.fragment.app.Fragment
import com.montdeska.android_hacker_news.ui.main.MainActivity

open class BaseFragment: Fragment(), MainActivity.MainNotification {

    override fun onBackPressed() {
        (requireActivity() as MainActivity).superOnBackPressed()
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).mainNotification = this
    }
}