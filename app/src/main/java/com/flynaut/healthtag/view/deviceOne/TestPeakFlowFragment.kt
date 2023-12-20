package com.flynaut.healthtag.view.deviceOne

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.spirobanksmart_sampleapp.extensions.QualityReportExtensions.toReadableString
import com.example.spirobanksmart_sampleapp.services.QualityCodeUtility
import com.flynaut.healthtag.R
import com.flynaut.healthtag.databinding.FragmentTestPeakFlowBinding
import com.flynaut.healthtag.view.BaseFragment
import com.flynaut.healthtag.view.MainActivity
import com.spirometry.spirobanksmartsdk.*
import java.util.*
import kotlin.math.roundToInt

class TestPeakFlowFragment : BaseFragment<FragmentTestPeakFlowBinding>(), PefFev1DeviceCallback {

    private lateinit var patient: Patient
    internal var selectedTurbineType: Device.TurbineType? = null
    private var remainingChances = 3


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Creates a patient, for the predicted targets //
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar[Calendar.YEAR] = calendar[Calendar.YEAR] - 25
        patient = Patient(calendar.time, Date(), 175F, 65F, Gender.MALE, Ethnicity.AFRICAN_AMERICAN)
        // Test callbacks //
        deviceManager.deviceConnected.addDeviceCallback(this)
        startflow()


        binding.yourRedIndicator.setOnClickListener{
                binding.yourValueBg.setCardBackgroundColor(resources.getColor(R.color.peak_card_mid_bg))
                binding.lowPeak.visibility = View.GONE
                binding.midPeak.visibility = View.VISIBLE

        }

        binding.yourOrangeIndicator.setOnClickListener{
            binding.yourValueBg.setCardBackgroundColor(resources.getColor(R.color.peak_card_high_bg))
            binding.midPeak.visibility = View.GONE
            binding.highPeak.visibility = View.VISIBLE

            binding.txtSuggestion.text = "Excellent"

        }

        binding.yourGreenIndicator.setOnClickListener{
            (activity as MainActivity).replaceFragment(TestPeakFlowResultFragment(), "TestPeakFlowResultFragment",false)
        }


    }

    override fun testStarted(device: Device) {
        super.testStarted(device)
        resetIncentiveLayout()
    }

    private fun resetIncentiveLayout() {
        //binding.tvTarget.text = "0%"
        binding.iv.layoutParams.height =
            (0.2 * resources.displayMetrics.density).roundToInt()
        binding.iv.requestLayout()
        binding.ivActual.layoutParams.height =
            (0.2 * resources.displayMetrics.density).roundToInt()
        binding.ivActual.requestLayout()
    }

    fun startflow()
    {
        binding.headerSpinnerTurbine.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedTurbineType = when (position) {
                    0 -> Device.TurbineType.reusable
                    1 -> Device.TurbineType.disposable
                    else -> null
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}

        }
        // Creates the adapter and sets it to the spinner //
        val adapter = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_spinner_item,
            arrayOf("Reusable", "Disposable")
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.headerSpinnerTurbine.adapter = adapter
        binding.headerSpinnerTurbine.setSelection(0)
        deviceManager.deviceConnected.startTest(
            activity,
            Device.TestType.PefFev1,
            Device.TurbineType.reusable,
            15,
            25
        )
    }

    @SuppressLint("SetTextI18n")
    override fun flowUpdated(
        device: Device,
        flow: Float,
        volumeStep: Int,
        isFirstPackage: Boolean
    ) {
        super.flowUpdated(device, flow, volumeStep, isFirstPackage)

        val predictedPercentageOfTarget: Float =
            patient.predictedPercentageOfTargetWithFlow(flow, volumeStep, isFirstPackage)
        val actualPercentageOfTarget: Float =
            patient.actualPercentageOfTargetWithFlow(flow, volumeStep, isFirstPackage)
        //binding.tvTarget.text = predictedPercentageOfTarget.roundToInt().toString() + "%"
        binding.iv.layoutParams.height =
            (200 * (predictedPercentageOfTarget / 100F) * resources.displayMetrics.density).roundToInt()
        binding.iv.requestLayout()
        //binding.textActual.text = actualPercentageOfTarget.roundToInt().toString() + "%"
        binding.ivActual.layoutParams.height =
            (200 * (actualPercentageOfTarget / 100F) * resources.displayMetrics.density).roundToInt()
        binding.ivActual.requestLayout()

        Log.e("Target%",predictedPercentageOfTarget.roundToInt().toString() + "%")
        Log.e("actualPercentageOfTargetTarget%",actualPercentageOfTarget.roundToInt().toString() + "%")

    }


    override fun resultsUpdated(resultsPefFev1: ResultsPefFev1) {
        super.resultsUpdated(resultsPefFev1)
        val resultsText = "Results PEF/FEV1\n" +
                "QUALITY: ${QualityCodeUtility.getReadableBitString(resultsPefFev1.qualityCode)}\n" +
                "PEF: ${resultsPefFev1.pef_cLs} cL/s\n" +
                "FEV1: ${resultsPefFev1.fev1_cL} cL\n" +
                "EVOL: ${resultsPefFev1.geteVol_mL()} mL \n" +
                "PEFTIME: ${resultsPefFev1.pefTime_msec} msec"
        //binding.textResults.text = resultsText
        //LogFragment.addLogEntryToSecondary(LOG_CATEGORY, resultsText)
        val qualityReport = patient.getQualityReport(resultsPefFev1)
        //binding.textQualityReport.text = qualityReport.toReadableString()

        Log.e("Rsult",resultsText)
        Log.e("QualityReport",qualityReport.toReadableString())
        Log.e("Rema",remainingChances.toString())
        remainingChances -= 1

        if (remainingChances <= 0) {
            // Show a dialog indicating that the chances are over
            showNoChancesDialog()
        } else {
            // Show a dialog with remaining chances
            showRemainingChancesDialog(remainingChances)
        }

    }



    private fun showNoChancesDialog() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("No Chances Remaining")
        alertDialogBuilder.setMessage("You have used all your chances.")
        alertDialogBuilder.setPositiveButton("OK") { dialog: DialogInterface, _: Int ->
            dialog.dismiss()
            // Optionally, you can perform any action after all chances are used.
        }
        alertDialogBuilder.create().show()
    }

    private fun showRemainingChancesDialog(remainingChances: Int) {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Remaining Chances: $remainingChances")
        alertDialogBuilder.setMessage("Are you sure you want to try again?")
        alertDialogBuilder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
            // Optionally, you can perform any action when the user selects 'Yes'.
            reset()
        }
        alertDialogBuilder.setNegativeButton("No") { dialog: DialogInterface, _: Int ->
            dialog.dismiss()
        }
        alertDialogBuilder.create().show()
    }


    override fun getLayoutResId(): Int {
        return R.layout.fragment_test_peak_flow
    }

    override fun initViews() {


        binding.ivBack.setOnClickListener {
            activity?.onBackPressed()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        deviceManager.deviceConnected?.removeDeviceCallback(this)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TestPeakFlowFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

   fun showResetDialog()
    {

        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Remaining Chances "+(remainingChances - 1))
        alertDialogBuilder.setMessage("Are you sure you want to try again?")
        alertDialogBuilder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
       remainingChances = remainingChances + 1
        reset()
        }
        alertDialogBuilder.setNegativeButton("No") { dialog: DialogInterface, _: Int ->
            dialog.dismiss()
        }
        alertDialogBuilder.create().show()
    }

    fun reset()
    {
        binding.iv.layoutParams.height =
            (0.2 * resources.displayMetrics.density).roundToInt()
        binding.iv.requestLayout()
        binding.ivActual.layoutParams.height =
            (0.2 * resources.displayMetrics.density).roundToInt()
        binding.ivActual.requestLayout()
    }
}