package com.example.spirobanksmart_sampleapp.extensions

import com.spirometry.spirobanksmartsdk.AtsStandard
import com.spirometry.spirobanksmartsdk.QualityReport

object QualityReportExtensions {

    /**
     * Returns a human-readable string from a quality report
     */
    fun QualityReport.toReadableString(): String {
        var qualityReportText = "ATS Standard: ${standardUsedByCurrentDevice}\n"
        if (standardUsedByCurrentDevice == AtsStandard.ATS_2015)
            qualityReportText += "Trial Acceptability: ${trialAcceptability}"
        else if (standardUsedByCurrentDevice == AtsStandard.ATS_2019)
            qualityReportText += "FVC Acceptability: ${fvcAcceptability}\n" +
                    "FEV1 Acceptability: ${fev1Acceptability}"
        qualityReportText += "\n"
        for (indication in indications) {
            qualityReportText += "\nIndication:\n" +
                    "Quality message: ${indication.qualityMessage}\n" +
                    "Instruction:${indication.instruction}\n"
        }
        return qualityReportText
    }

}