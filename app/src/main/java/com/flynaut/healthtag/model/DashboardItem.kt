package com.flynaut.healthtag.model

import com.flynaut.healthtag.R

data class DashboardItem (val imageResource: Int, val bgColor: Int,val textColor: Int, val count: String, val measurement: String, val title: String)

val dashboardItems = listOf(
    DashboardItem(R.drawable.ic_bp, R.color._F5F3FF, R.color._5E5497,"00", "mmHg", "BloodPressure"),
    DashboardItem(R.drawable.ic_sugar, R.color._FFF3DC, R.color._FAB32E, "00", "mmol/L", "Sugar/Glucose"),
    DashboardItem(R.drawable.ic_weight_scale, R.color._EAF9E6,R.color._4ABE2B,"00", "Kg", "Weight Scale"),
    DashboardItem(R.drawable.ic_pulse, R.color._E2F4FF, R.color._5BBBFD, "00", "%SpO2", "Pulse Oximeter")
)