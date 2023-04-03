package yin_kio.garbage_clean.test

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ads.initAds
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        initAds()


    }

}