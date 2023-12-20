package com.flynaut.healthtag.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.flynaut.healthtag.R
import com.flynaut.healthtag.adapter.FaqCategoryAdapter
import com.flynaut.healthtag.adapter.TicketListAdapter
import com.flynaut.healthtag.databinding.FragmentFaqBinding
import com.flynaut.healthtag.model.response.DataItemAllTickets
import com.flynaut.healthtag.model.response.DataItemFaq
import com.flynaut.healthtag.network.RetrofitClient
import com.flynaut.healthtag.util.EventObserver
import com.flynaut.healthtag.viewmodel.FaqViewModel
import com.flynaut.healthtag.viewmodel.ViewModelFactory


class FaqFragment : BaseFragment<FragmentFaqBinding>() {
    private lateinit var adapterRecentTicketList: TicketListAdapter
    private lateinit var adapterFaq: FaqCategoryAdapter
    private lateinit var viewModel: FaqViewModel
    var ticketArray = ArrayList<DataItemAllTickets>()
    var faqArray = ArrayList<DataItemFaq>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(RetrofitClient.apiService)
        )[FaqViewModel::class.java]
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    override fun getLayoutResId(): Int {
        return R.layout.fragment_faq
    }

    //
    override fun initViews() {
        showProgressDialog()
        setClick()
        viewModel.getFaqCategory()

        viewModel.getRecentTicket()
        initObserver()

        val layoutManager = LinearLayoutManager(context)
        adapterRecentTicketList = TicketListAdapter(ticketArray, true) { msg ->
            (activity as MainActivity).replaceFragment(
                SupportChatFragment.newInstance(msg),
                "SupportChatFragment"
            )
        }
        binding.rvRecentTickets.layoutManager = layoutManager
        binding.rvRecentTickets.adapter = adapterRecentTicketList

        //******faq adapter
        adapterFaq = FaqCategoryAdapter(faqArray) { model ->
            (activity as MainActivity).replaceFragment(
                FaqCategoryDetailsFragment.newInstance(model),
                "FaqCategoryDetailsFragment"
            )
        }
        binding.rvRecyclerQuestion.layoutManager = GridLayoutManager(context, 2)
        binding.rvRecyclerQuestion.adapter = adapterFaq

    }

    private fun setClick() {
        binding.tvViewAll.setOnClickListener {
            (activity as MainActivity).replaceFragment(TicketsFragment(), "TicketsFragment")
        }
        binding.ivChat.setOnClickListener {
            (activity as MainActivity).replaceFragment(TicketsFragment(), "TicketsFragment")
        }

        binding.clContactSupport.setOnClickListener {
            (activity as MainActivity).replaceFragment(TicketsFragment(), "TicketsFragment")
        }
        binding.ivBack.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        adapterRecentTicketList.notifyDataSetChanged()
    }

    private fun initObserver() {
        viewModel.finalFaqResponse.observe(viewLifecycleOwner) {
            //  hideProgressDialog()
            if ((it.data?.size ?: 0) == 0) {
                binding.txtNoData.visibility = View.VISIBLE
                binding.rvRecyclerQuestion.visibility = View.INVISIBLE
            } else {
                faqArray.clear()
                it.data.let { it?.let { it1 -> faqArray.addAll(it1) } }
                binding.txtNoData.visibility = View.GONE
                binding.rvRecyclerQuestion.visibility = View.VISIBLE
            }
            adapterFaq.notifyDataSetChanged()
        }

        viewModel.finalRecentTicketResponse.observe(viewLifecycleOwner) {
            hideProgressDialog()
            if ((it.data?.size ?: 0) == 0) {

                binding.txtNoDataTicket.visibility = View.GONE
                binding.tvRecent.visibility = View.GONE
                binding.rvRecentTickets.visibility = View.INVISIBLE
            } else {
                ticketArray.clear()
                it.data.let { it?.let { it1 -> ticketArray.addAll(it1) } }
                binding.txtNoDataTicket.visibility = View.GONE
                binding.rvRecentTickets.visibility = View.VISIBLE
                binding.tvRecent.visibility = View.VISIBLE
            }
            if (ticketArray.size > 3) {
                binding.tvViewAll.visibility = View.VISIBLE
            } else {
                binding.tvViewAll.visibility = View.INVISIBLE
            }
            adapterRecentTicketList.notifyDataSetChanged()
        }

        viewModel.toastMsg.observe(viewLifecycleOwner, EventObserver {
            hideProgressDialog()
            binding.txtNoData.visibility = View.VISIBLE
            binding.rvRecyclerQuestion.visibility = View.INVISIBLE
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        })

        viewModel.toastMsgErrorRecent.observe(viewLifecycleOwner, EventObserver {
            hideProgressDialog()
            binding.txtNoDataTicket.visibility = View.VISIBLE
            binding.rvRecentTickets.visibility = View.INVISIBLE
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FaqFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
            }
    }


}