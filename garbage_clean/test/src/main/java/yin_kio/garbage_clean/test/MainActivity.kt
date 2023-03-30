package yin_kio.garbage_clean.test

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.ads.initAds
import com.example.permissions.manageExternalStorageIntent

class MainActivity : AppCompatActivity() {

    private val activityResultCallback = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){

        Log.d("!!!", "${it}")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val launchIntent = activityResultCallback.contract.createIntent(this, manageExternalStorageIntent())
//
//        activityResultCallback.launch(launchIntent)

        startActivityForResult(manageExternalStorageIntent(), 2296)

        initAds()


    }

    override fun onStart() {
        super.onStart()
        Log.d("!!!", "onStart")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("!!!", "onActivityResult")

    }

}