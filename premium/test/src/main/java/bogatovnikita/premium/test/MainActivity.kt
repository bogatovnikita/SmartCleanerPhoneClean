package bogatovnikita.premium.test

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import bogatovnikita.premium.presentation.ui.PremiumScreenFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, PremiumScreenFragment()).commit()
    }
}