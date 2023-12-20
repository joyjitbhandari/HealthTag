package com.flynaut.healthtag.view.addDevice

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.flynaut.healthtag.R
import com.flynaut.healthtag.adapter.DeviceListAdapter
import com.flynaut.healthtag.databinding.FragmentDeviceListBinding
import com.flynaut.healthtag.model.DeviceListItem
import com.flynaut.healthtag.model.response.AllDevice
import com.flynaut.healthtag.model.response.AllDeviceResponse
import com.flynaut.healthtag.network.RetrofitClient
import com.flynaut.healthtag.util.EventObserver
import com.flynaut.healthtag.util.Utils
import com.flynaut.healthtag.util.Utils.DEVICE1
import com.flynaut.healthtag.util.Utils.DEVICE2
import com.flynaut.healthtag.view.BaseFragment
import com.flynaut.healthtag.view.MyDeviceFragment
import com.flynaut.healthtag.viewmodel.DeviceListViewModel
import com.flynaut.healthtag.viewmodel.ViewModelFactory

class DeviceListFragment : BaseFragment<FragmentDeviceListBinding>() {

    private lateinit var viewModel : DeviceListViewModel
    private var dataSet = ArrayList<AllDevice>()
    private lateinit var deviceListAdapter : DeviceListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this, ViewModelFactory(RetrofitClient.apiService))[DeviceListViewModel::class.java]

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserver()
        showProgressDialog()
        viewModel.allDevice()

    }

    private fun initObserver() {

        binding.btnAddDevice.setOnClickListener {
            val parentFragment = parentFragment as? MyDeviceFragment
            parentFragment?.button2Select()
        }

        viewModel.apiResponse.observe(viewLifecycleOwner) {
            hideProgressDialog()
            if (it.status.equals(200))
                setDeviceList(it)
            else if (it.status.equals(404))
                Toast.makeText(requireContext(), "No Data", Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
        }

        viewModel.deleteResponse.observe(viewLifecycleOwner) {
            hideProgressDialog()
//            if (it.status.equals(200))
//            {
//                //viewModel.allDevice()
//            }

            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
        }

        viewModel.toastMsg.observe(viewLifecycleOwner, EventObserver {
            hideProgressDialog()
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        })
    }

    fun setDeviceList(data : AllDeviceResponse)
    {
        dataSet = data.data as ArrayList<AllDevice>
        //data.data.add(AllDevice("1","salman","12","1","1","323","4343","454","5465","767","$6","23",2323))
        if (data.data.size > 0)
        {
            binding.deviceListEmptyView.visibility = View.GONE
            binding.deviceRecycler.visibility = View.VISIBLE

            // RecyclerView set
             deviceListAdapter = DeviceListAdapter(requireActivity(), data.data,this)
            binding.deviceRecycler.adapter = deviceListAdapter
        }else
        {
            showEmptyView()
        }

    }


    override fun getLayoutResId(): Int {
        return R.layout.fragment_device_list
    }

    override fun initViews() {

        Utils.deviceDataList.add(DeviceListItem(DEVICE1,R.drawable.img_device1,"Device1","Smart One OXI"))
        Utils.deviceDataList.add(DeviceListItem(DEVICE2,R.drawable.img_device2,"Device2","BerryMed Pulse Oximeter"))
        if (Utils.deviceDataList.isEmpty()) {
            binding.deviceListEmptyView.visibility = View.VISIBLE
            binding.deviceRecycler.visibility = View.GONE

            binding.btnAddDevice.setOnClickListener {
                val parentFragment = parentFragment as? MyDeviceFragment
                parentFragment?.button2Select()
            }

        } else {
//            binding.deviceListEmptyView.visibility = View.GONE
//            binding.deviceRecycler.visibility = View.VISIBLE
//
//            // RecyclerView set
//            val deviceListAdapter = DeviceListAdapter(requireActivity(), Utils.deviceDataList)
//            binding.deviceRecycler.adapter = deviceListAdapter
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DeviceListFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onResume() {
        super.onResume()

    }

    fun deleteDevice(_id : String,pos : Int, name : String)
    {
        AlertDialog.Builder(activity)
            .setTitle("Are you sure want to Delete "+name+"?")
            .setMessage("")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, _ ->
                dataSet.removeAt(pos)
                viewModel.deleteDevice(_id)
                deviceListAdapter.notifyDataSetChanged()
                Log.e("Size",dataSet.size.toString())
                if (dataSet.size == 0)
                {
                   showEmptyView()
                }
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    fun showEmptyView()
    {
        binding.deviceListEmptyView.visibility = View.VISIBLE
        binding.deviceRecycler.visibility = View.GONE
    }


}
