package com.flynaut.healthtag.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.flynaut.healthtag.R
import com.flynaut.healthtag.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding // Declare binding variable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main) // Initialize binding
        val isLogin = intent.getBooleanExtra(ARG_IS_LOGIN, false)
        val isDirectLogin = intent.getBooleanExtra(ARG_IS_DIRECT_LOGIN, false)
        val isOnboard = intent.getBooleanExtra(ARG_IS_ONBOARD, false)
        if (isLogin) {
            addFragment(LoginFragment(), "LoginFragment", false)
        }
        else if (isDirectLogin){
            addFragment(DashboardFragment(), "DashboardFragment", false)
        }else{
            addFragment(ChooseCategoryFragment.newInstance(isOnboard), "ChooseCategoryFragment", false)
        }
    }

    private fun addFragment(fragment: Fragment, tag: String, addToBackStack: Boolean) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fragment_container, fragment, tag)
        if (addToBackStack)
            fragmentTransaction.addToBackStack(tag)
        else
            fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }


    fun replaceFragment(fragment: Fragment, tag: String) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment, tag)
        fragmentTransaction.addToBackStack(tag)
        fragmentTransaction.commit()
    }

    fun replaceFragment(fragment: Fragment, tag: String, addToBackStack: Boolean) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment, tag)
        if (addToBackStack)
            fragmentTransaction.addToBackStack(tag)
        else
            fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    override fun onBackPressed() {
        if (this.supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
}