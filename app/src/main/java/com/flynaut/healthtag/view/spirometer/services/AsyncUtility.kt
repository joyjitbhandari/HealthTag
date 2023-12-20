package com.example.spirobanksmart_sampleapp.services

import android.os.Handler
import android.os.Looper

object AsyncUtility {

    fun doOnMainThread(block: () -> Unit) {
        val mainHandler = Handler(Looper.getMainLooper())
        //val myRunnable = Runnable { block() } // This is your code
        mainHandler.post {
            block()
        }
    }

    fun doOnBackgroundThread(block: () -> Unit) {
        Thread { block.invoke() }.start()
    }

    fun doAfterDelay(delayMs: Int, block: () -> Unit) {
        doOnBackgroundThread {
            try {
                Thread.sleep(delayMs.toLong())
                doOnMainThread(block)
            } catch (e: InterruptedException) {
                //e.printStackTrace();
            }
        }
    }

}