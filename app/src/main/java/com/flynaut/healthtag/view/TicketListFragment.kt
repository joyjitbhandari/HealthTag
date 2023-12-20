package com.flynaut.healthtag.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.flynaut.healthtag.R
import com.flynaut.healthtag.adapter.TicketListAdapter
import com.flynaut.healthtag.databinding.FragmentTicketListBinding
import com.flynaut.healthtag.model.response.DataItemAllTickets
import com.flynaut.healthtag.network.RetrofitClient
import com.flynaut.healthtag.util.EventObserver
import com.flynaut.healthtag.viewmodel.AllTicketsViewModel
import com.flynaut.healthtag.viewmodel.ViewModelFactory

class TicketListFragment : BaseFragment<FragmentTicketListBinding>() {
    private lateinit var adapter: TicketListAdapter
    private lateinit var viewModel: AllTicketsViewModel
    var currentPosition = 0
    var ticketArray=ArrayList<DataItemAllTickets>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(RetrofitClient.apiService)
        )[AllTicketsViewModel::class.java]
        return super.onCreateView(inflater, container, savedInstanceState)
    }
    override fun getLayoutResId(): Int {
        return R.layout.fragment_ticket_list
    }

    override fun initViews() {
        val dataset = listOf("Ticket 1", "Ticket 2")
        showProgressDialog()

        viewModel.getAllTicketsDetail(setStatus(currentPosition))
        initObserver()
        val layoutManager = LinearLayoutManager(context)
            adapter = TicketListAdapter(ticketArray,false) { msg ->
                (activity as MainActivity).replaceFragment(
                    SupportChatFragment.newInstance(msg),
                    "SupportChatFragment"
                )
            }
            binding.rvTicket.layoutManager = layoutManager
            binding.rvTicket.adapter = adapter

        binding.refreshLayout.setOnRefreshListener {
            viewModel.getAllTicketsDetail(setStatus(currentPosition))
            showProgressDialog()
            binding.refreshLayout.isRefreshing = false
        }

    }

    private fun setStatus(currentPosition: Int): String {
        if (currentPosition==0){
            return ""
        }
        if (currentPosition==1){
            return "0"
        }
        if (currentPosition==2){
            return "1"
        }
        return ""
    }

    private fun initObserver() {
        viewModel.finalTicketDetailsResponse.observe(viewLifecycleOwner) {
                hideProgressDialog()
            ticketArray.clear()
            if (it.data?.size == 0||it.data==null ) {
                binding.txtNoData.visibility=View.VISIBLE
                binding.rvTicket.visibility=View.GONE
            } else {
                it.data.let { it.let { it1 -> ticketArray.addAll(it1) } }
                binding.txtNoData.visibility=View.GONE
                binding.rvTicket.visibility=View.VISIBLE
            }
            adapter.notifyDataSetChanged()
        }
        viewModel.toastMsg.observe(viewLifecycleOwner, EventObserver {
            hideProgressDialog()
              binding.txtNoData.visibility = View.VISIBLE
              binding.rvTicket.visibility = View.GONE
            Toast.makeText(requireContext(), "No Ticket found", Toast.LENGTH_SHORT).show()
        })
    }
    companion object {
        fun newInstance(position: Int) =
            TicketListFragment().apply {
                currentPosition = position

            }
    }

    override fun onResume() {
        super.onResume()
        showProgressDialog()
        viewModel.getAllTicketsDetail(setStatus(currentPosition))

    }

}