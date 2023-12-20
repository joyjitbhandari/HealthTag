package com.flynaut.healthtag.view

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.flynaut.healthtag.R
import com.flynaut.healthtag.adapter.ReportDeviceAdapter
import com.flynaut.healthtag.adapter.Device1ResultListAdapter
import com.flynaut.healthtag.adapter.Device2ResultListAdapter
import com.flynaut.healthtag.databinding.FragmentReportBinding
import com.flynaut.healthtag.model.Device1ResultListItem
import com.flynaut.healthtag.model.Device2ResultListItem
import com.flynaut.healthtag.util.BottomListSpacingItemDecoration
import com.flynaut.healthtag.view.deviceOne.BottomWriteNoteFragment
import com.flynaut.healthtag.view.deviceOne.Device1GraphResultFragment
import com.flynaut.healthtag.view.deviceTwo.Device2GraphResultFragment

class ReportFragment : BaseFragment<FragmentReportBinding>(), ReportDeviceAdapter.DeviceCLickListener,
    Device1ResultListAdapter.ReportCLickListener, Device2ResultListAdapter.ReportCLickListener{

    lateinit var reportDeviceAdapter: ReportDeviceAdapter
    lateinit var device1ResultListAdapter: Device1ResultListAdapter
    private var reportList : ArrayList<Device1ResultListItem> = ArrayList()
    private var deviceList : ArrayList<Drawable> = ArrayList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        deviceList.add(resources.getDrawable(R.drawable.img_device1))
        deviceList.add(resources.getDrawable(R.drawable.img_device2))
        reportDeviceAdapter = ReportDeviceAdapter(requireContext(), deviceList,this)
        binding.recyDevice!!.adapter = reportDeviceAdapter

        val reportList = arrayListOf<Device1ResultListItem>(
            Device1ResultListItem("#100",R.drawable.green_btn_bg,"440","28 Oct 2022 at 6:00 PM","FEV1   5.10 L. (50%)","Writer Notes"),
            Device1ResultListItem("#100",R.drawable.red_btn_bg,"440","28 Oct 2022 at 6:00 PM","FEV1   5.10 L. (89%)","Writer Notes"),
            Device1ResultListItem("#100",R.drawable.red_btn_bg,"440","28 Oct 2022 at 6:00 PM","FEV1   5.10 L. (50%)","Writer Notes"),
            Device1ResultListItem("#100",R.drawable.orange_btn_bg,"500","28 Oct 2022 at 6:00 PM","FEV1   5.10 L. (75%)","Writer Notes"),
            Device1ResultListItem("#100",R.drawable.red_btn_bg,"440","28 Oct 2022 at 6:00 PM","FEV1   5.10 L. (50%)","Writer Notes"),
            Device1ResultListItem("#100",R.drawable.green_btn_bg,"650","28 Oct 2022 at 6:00 PM","FEV1   5.10 L. (89%)","Writer Notes"),
        )
        val myLinearLayoutManager = object : LinearLayoutManager(context) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        device1ResultListAdapter = Device1ResultListAdapter(reportList,this)
        binding.recyReportList.layoutManager = myLinearLayoutManager
        binding.recyReportList.adapter = device1ResultListAdapter
        val spacing = resources.getDimensionPixelSize(R.dimen.dp_80)
        binding.recyReportList.addItemDecoration(BottomListSpacingItemDecoration(spacing))
    }


    override fun getLayoutResId(): Int {
        return R.layout.fragment_report
    }

    override fun initViews() {

    }

    override fun onDeviceClicked(position: Int) {
        when(position){
            0->{
                val reportList = arrayListOf<Device1ResultListItem>(
                    Device1ResultListItem("#100",R.drawable.green_btn_bg,"440","28 Oct 2022 at 6:00 PM","FEV1   5.10 L. (50%)","Writer Notes"),
                    Device1ResultListItem("#100",R.drawable.red_btn_bg,"440","28 Oct 2022 at 6:00 PM","FEV1   5.10 L. (89%)","Writer Notes"),
                    Device1ResultListItem("#100",R.drawable.red_btn_bg,"440","28 Oct 2022 at 6:00 PM","FEV1   5.10 L. (50%)","Writer Notes"),
                    Device1ResultListItem("#100",R.drawable.orange_btn_bg,"500","28 Oct 2022 at 6:00 PM","FEV1   5.10 L. (75%)","Writer Notes"),
                    Device1ResultListItem("#100",R.drawable.red_btn_bg,"440","28 Oct 2022 at 6:00 PM","FEV1   5.10 L. (50%)","Writer Notes"),
                    Device1ResultListItem("#100",R.drawable.green_btn_bg,"650","28 Oct 2022 at 6:00 PM","FEV1   5.10 L. (89%)","Writer Notes"),
                )
                val myLinearLayoutManager = object : LinearLayoutManager(context) {
                    override fun canScrollVertically(): Boolean {
                        return false
                    }
                }
                device1ResultListAdapter = Device1ResultListAdapter(reportList,this)
                binding.recyReportList.layoutManager = myLinearLayoutManager
                binding.recyReportList.adapter = device1ResultListAdapter
            }
            1->{
                val reportList = arrayListOf<Device2ResultListItem>(
                    Device2ResultListItem("#200", "55", "81", "28 Oct 2022 at 6:00 PM", "Writer Notes"),
                    Device2ResultListItem("#200", "55", "81", "28 Oct 2022 at 6:00 PM", "Writer Notes"),
                    Device2ResultListItem("#200", "55", "81", "28 Oct 2022 at 6:00 PM", "Writer Notes"),
                    Device2ResultListItem("#200", "55", "81", "28 Oct 2022 at 6:00 PM", "Writer Notes"),
                    Device2ResultListItem("#200", "55", "81", "28 Oct 2022 at 6:00 PM", "Writer Notes"),
                    Device2ResultListItem("#200", "55", "81", "28 Oct 2022 at 6:00 PM", "Writer Notes")
                )
                val myLinearLayoutManager = object : LinearLayoutManager(context) {
                    override fun canScrollVertically(): Boolean {
                        return false
                    }
                }
                val adapter = Device2ResultListAdapter( reportList, this)
                binding.recyReportList.layoutManager = myLinearLayoutManager
                binding.recyReportList.adapter = adapter
            }
        }

    }

    override fun onDevice1ReportClicked(position: Int) {
        (context as MainActivity).replaceFragment(Device1GraphResultFragment(),"Device1GraphResultFragment")
    }

    override fun onDevice1WriterNoteClicked(position: Int) {
        //i have to pass device id
        val bottomSheetDialog = BottomWriteNoteFragment()
        childFragmentManager.let { bottomSheetDialog.show(it, "BottomWriteNoteFragment") }
    }

    override fun onDevice2ReportClicked(position: Int) {
        (context as MainActivity).replaceFragment(Device2GraphResultFragment(),"Device2GraphResultFragment")
    }

    override fun onDevice2WriterNoteClicked(position: Int) {
        //i have to pass device id
        val bottomSheetDialog = BottomWriteNoteFragment()
        childFragmentManager.let { bottomSheetDialog.show(it, "BottomWriteNoteFragment") }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ReportFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}