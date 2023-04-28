package com.smart.cleaner.phoneclean

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        initSubscription()  //TODO реклама
//        this.supportFragmentManager.beginTransaction()
//            .add(R.id.fragment_container, PremiumScreenFragment()).commit()
    }
}