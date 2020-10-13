package com.montdeska.android_hacker_news.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.montdeska.android_hacker_news.R

class MainActivity : AppCompatActivity() {
    lateinit var mainNotification: MainNotification
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onBackPressed() {
        mainNotification.onBackPressed()
    }

    fun superOnBackPressed() {
        super.onBackPressed()
    }

    interface MainNotification {
        fun onBackPressed()
    }

}