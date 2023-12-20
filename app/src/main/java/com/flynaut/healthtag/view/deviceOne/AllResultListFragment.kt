package com.flynaut.healthtag.view.deviceOne

import android.os.Bundle
import android.view.View
import com.flynaut.healthtag.R
import com.flynaut.healthtag.adapter.Device1ResultListAdapter
import com.flynaut.healthtag.adapter.Device2ResultListAdapter
import com.flynaut.healthtag.databinding.FragmentAllResultListBinding
import com.flynaut.healthtag.model.Device1ResultListItem
import com.flynaut.healthtag.model.Device2ResultListItem
import com.flynaut.healthtag.util.LinearListSpacingItemDecoration
import com.flynaut.healthtag.util.Utils.DEVICE1
import com.flynaut.healthtag.util.Utils.DEVICE2
import com.flynaut.healthtag.util.Utils.DEVICE_ID
import com.flynaut.healthtag.view.BaseFragment
import com.flynaut.healthtag.view.MainActivity
import com.flynaut.healthtag.view.deviceTwo.Device2GraphResultFragment

class AllResultListFragment : BaseFragment<FragmentAllResultListBinding>(),
    Device1ResultListAdapter.ReportCLickListener,
    Device2ResultListAdapter.ReportCLickListener {

    private var deviceId: String? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            deviceId = it.getString(DEVICE_ID).toString()
        }

        val deviceResultRecyclerView = binding.deviceResultRecycler
        //adding space
        val space = resources.getDimensionPixelSize(R.dimen.dp_10)
        deviceResultRecyclerView.addItemDecoration(LinearListSpacingItemDecoration(space))

        when (deviceId) {
            DEVICE1 -> {

                val reportList = arrayListOf<Device1ResultListItem>(
                    Device1ResultListItem(
                        "#100",
                       R.drawable.red_btn_bg,
                        "440",
                        "28 Oct 2022 at 6:00 PM",
                        "FEV1   5.10 L. (50%)",
                        "Writer Notes"
                    ),
                    Device1ResultListItem(
                        "#100",
                        R.drawable.green_btn_bg,
                        "440",
                        "28 Oct 2022 at 6:00 PM",
                        "FEV1   5.10 L. (89%)",
                        "Writer Notes"
                    ),
                    Device1ResultListItem(
                        "#100",
                        R.drawable.red_btn_bg,
                        "440",
                        "28 Oct 2022 at 6:00 PM",
                        "FEV1   5.10 L. (50%)",
                        "Writer Notes"
                    ),
                    Device1ResultListItem(
                        "#100",
                        R.drawable.orange_btn_bg,
                        "500",
                        "28 Oct 2022 at 6:00 PM",
                        "FEV1   5.10 L. (75%)",
                        "Writer Notes"
                    ),
                    Device1ResultListItem(
                        "#100",
                        R.drawable.red_btn_bg,
                        "440",
                        "28 Oct 2022 at 6:00 PM",
                        "FEV1   5.10 L. (50%)",
                        "Writer Notes"
                    ),
                    Device1ResultListItem(
                        "#100",
                        R.drawable.green_btn_bg,
                        "650",
                        "28 Oct 2022 at 6:00 PM",
                        "FEV1   5.10 L. (89%)",
                        "Writer Notes"
                    ),
                )

                val adapter = Device1ResultListAdapter(reportList, this)
                deviceResultRecyclerView.adapter = adapter


            }
            DEVICE2 -> {
                val reportList = arrayListOf<Device2ResultListItem>(
                    Device2ResultListItem(
                        "#200",
                        "55",
                        "81",
                        "28 Oct 2022 at 6:00 PM",
                        "Writer Notes"
                    ),
                    Device2ResultListItem(
                        "#200",
                        "55",
                        "81",
                        "28 Oct 2022 at 6:00 PM",
                        "Writer Notes"
                    ),
                    Device2ResultListItem(
                        "#200",
                        "55",
                        "81",
                        "28 Oct 2022 at 6:00 PM",
                        "Writer Notes"
                    ),
                    Device2ResultListItem(
                        "#200",
                        "55",
                        "81",
                        "28 Oct 2022 at 6:00 PM",
                        "Writer Notes"
                    ),
                    Device2ResultListItem(
                        "#200",
                        "55",
                        "81",
                        "28 Oct 2022 at 6:00 PM",
                        "Writer Notes"
                    ),
                    Device2ResultListItem(
                        "#200",
                        "55",
                        "81",
                        "28 Oct 2022 at 6:00 PM",
                        "Writer Notes"
                    )
                )

                val adapter = Device2ResultListAdapter( reportList, this)
                deviceResultRecyclerView.adapter = adapter
            }
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_all_result_list
    }

    override fun initViews() {


        binding.ivShare.setOnClickListener {
            val bottomSheetDialog = BottomShareReportFragment()
            childFragmentManager?.let { bottomSheetDialog.show(it, "BottomShareReportFragment") }
        }

        binding.ivBack.setOnClickListener {
            activity?.onBackPressed()
        }

    }

    companion object {
        @JvmStatic
        fun newInstance(deviceId: String): AllResultListFragment {
            val fragment = AllResultListFragment()
            val bundle = Bundle()
            bundle.putString(DEVICE_ID, deviceId)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onDevice1ReportClicked(position: Int) {
        (context as MainActivity).replaceFragment(Device1GraphResultFragment(),"Device1GraphResultFragment",false)
    }

    override fun onDevice1WriterNoteClicked(position: Int) {
        //i have to pass device id
        val bottomSheetDialog = BottomWriteNoteFragment()
        childFragmentManager.let { bottomSheetDialog.show(it, "BottomWriteNoteFragment") }
    }

    override fun onDevice2ReportClicked(position: Int) {
        (context as MainActivity).replaceFragment(Device2GraphResultFragment(),"Device2GraphResultFragment",false)
    }

    override fun onDevice2WriterNoteClicked(position: Int) {
        //i have to pass device id
        val bottomSheetDialog = BottomWriteNoteFragment()
        childFragmentManager.let { bottomSheetDialog.show(it, "BottomWriteNoteFragment") }
    }

}