package com.example.spirobanksmart_sampleapp.services

object QualityCodeUtility {

    /**
     * Returns a human-readable string from a quality code, to check every single bit
     */
    fun getReadableBitString(qualityCode: Int): String {
        val binaryString = Integer.toBinaryString(qualityCode).padStart(16, '0')
        return binaryString.chunked(4).joinToString(separator = " ")
    }

}