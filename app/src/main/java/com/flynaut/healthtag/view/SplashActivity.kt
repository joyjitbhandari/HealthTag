package com.flynaut.healthtag.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.ImageView
import com.flynaut.healthtag.R
import com.flynaut.healthtag.util.PrefsManager
import com.flynaut.healthtag.util.getAccessToken

const val ARG_IS_DIRECT_LOGIN = "isDirectLogin"
const val ARG_IS_ONBOARD = "isOnboard"
class SplashActivity : AppCompatActivity() {

    private val SPLASH_TIME_OUT = 3000L // 3 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        PrefsManager.initialize(this)
        Handler().postDelayed({
            findViewById<ImageView>(R.id.iv_splash).setImageResource(R.mipmap.splash_logo)
        },1000L)

        // Create a handler to delay the start of the next activity
        Handler().postDelayed({
            val isUserOnboarded = PrefsManager(this).getBoolean(PrefsManager.PREF_IS_ONBOARD, false)
            Log.e("isUserOnboarded",isUserOnboarded.toString())
            // Start the next activity (replace MainActivity with your desired activity)

            // Start the next activity (replace MainActivity with your desired activity)
            if (getAccessToken().isEmpty()){
                if(isUserOnboarded){
                    val intent = Intent(this@SplashActivity, MainActivity::class.java)
                    intent.putExtra(ARG_IS_ONBOARD, true)
                    startActivity(intent)
                }

                else
                    startActivity(Intent(this@SplashActivity, OnboardActivity::class.java))

            }else{
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                intent.putExtra(ARG_IS_DIRECT_LOGIN, true)
                startActivity(intent)
            }

            // Close the current activity
            finish()
        }, SPLASH_TIME_OUT)
    }
}