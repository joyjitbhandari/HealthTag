package com.flynaut.healthtag.util

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

fun View.setVisible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun View.setInVisible(isVisible: Boolean) {
    visibility = if (isVisible) View.INVISIBLE else View.VISIBLE
}

fun RecyclerView.autoFitColumns(columnWidth: Int) {
    val displayMetrics = this.context.resources.displayMetrics
    val noOfColumns = ((displayMetrics.widthPixels / displayMetrics.density) / columnWidth).toInt()
    this.layoutManager = GridLayoutManager(this.context, noOfColumns)
}

fun Context.showToast(msg: Any, duration: Int = Toast.LENGTH_SHORT) {
    when (msg) {
        is Int -> {
            Toast.makeText(this, getString(msg), Toast.LENGTH_LONG).show()
        }

        is String -> {
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
        }
    }


    fun ViewPager2.findFragmentAtPosition(
        fragmentManager: FragmentManager,
        position: Int
    ): Fragment? {
        return fragmentManager.findFragmentByTag("f$position")
    }
}
fun getAccessToken(): String {
    return if (PrefsManager.get().getString(PrefsManager.PREF_PROFILE, "").isEmpty())
        ""
    else {
        val accessToken = PrefsManager.get().getString(PrefsManager.PREF_API_TOKEN, "")
       // val value = "\"" + accessToken + "\""
        accessToken
    }
}
