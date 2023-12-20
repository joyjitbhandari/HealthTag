package com.flynaut.healthtag.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.flynaut.healthtag.R
import com.flynaut.healthtag.adapter.DeviceActivityLogDayAdapter
import com.flynaut.healthtag.databinding.FragmentLogsBinding
import com.flynaut.healthtag.network.RetrofitClient
import com.flynaut.healthtag.util.BottomListSpacingItemDecoration
import com.flynaut.healthtag.util.showToast
import com.flynaut.healthtag.viewmodel.AllLogsListViewModel
import com.flynaut.healthtag.viewmodel.ViewModelFactory

class LogsFragment : BaseFragment<FragmentLogsBinding>(){

    private lateinit var viewModel : AllLogsListViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, ViewModelFactory(RetrofitClient.apiService))[AllLogsListViewModel::class.java]
        initObserver()

//        val logList = arrayListOf<DeviceActivityLog>(
//        DeviceActivityLog(R.drawable.img_device1,"Device Connected","Smart One OXI","21/06/2023","6.00PM"),
//        DeviceActivityLog(null,"Test Started","Test Peak Flow","21/06/2023","6.01PM"),
//        DeviceActivityLog(null,"Test Ended","Test Peak Flow","21/06/2023","6.01PM"),
//        DeviceActivityLog(R.drawable.img_device2,"Device Connected","Smart One OXI","21/06/2023","6.00PM"),
//        DeviceActivityLog(R.drawable.img_device2,"Device Connected","Smart One OXI","21/06/2023","6.00PM"),
//        DeviceActivityLog(R.drawable.img_device2,"Device Connected","Smart One OXI","20/06/2023","6.00PM"),
//        DeviceActivityLog(R.drawable.img_device1,"Device Connected","Smart One OXI","20/06/2023","6.00PM"),
//        DeviceActivityLog(R.drawable.img_device1,"Device Connected","Smart One OXI","20/06/2023","6.00PM"),
//        )
    }



    override fun getLayoutResId(): Int {
        return R.layout.fragment_logs

    }

    override fun initViews() {

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LogsFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onResume() {
        super.onResume()

        getAllLogs()
    }

    private fun initObserver() {
        viewModel.toastMsgError.observe(viewLifecycleOwner) {
            hideProgressDialog()
            context?.showToast(it)
        }
        //error
        viewModel.toastMsg.observe(viewLifecycleOwner) {
            hideProgressDialog()
            context?.showToast(it)
        }

        viewModel.finalLogsResponse.observe(viewLifecycleOwner) {
            hideProgressDialog()
            Log.e("It",it.data.toString());
            val adapter = DeviceActivityLogDayAdapter(it.data)
            binding.logRecycler.adapter = adapter
            val spacing = resources.getDimensionPixelSize(R.dimen.dp_80)
            binding.logRecycler.addItemDecoration(BottomListSpacingItemDecoration(spacing))

            //Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun getAllLogs()
    {
        showProgressDialog()
        viewModel.getLogs()
    }
}