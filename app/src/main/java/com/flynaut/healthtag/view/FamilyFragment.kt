package com.flynaut.healthtag.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import com.flynaut.healthtag.R
import com.flynaut.healthtag.adapter.FamilyAdapter
import com.flynaut.healthtag.databinding.FragmentFamilyBinding
import com.flynaut.healthtag.util.SwipeToDeleteCallback
import com.flynaut.healthtag.model.response.FamilyMember
import com.flynaut.healthtag.network.RetrofitClient
import com.flynaut.healthtag.util.EventObserver
import com.flynaut.healthtag.viewmodel.FamilyMemberViewModel
import com.flynaut.healthtag.viewmodel.ViewModelFactory

class FamilyFragment : BaseFragment<FragmentFamilyBinding>()  {
    private lateinit var viewModel: FamilyMemberViewModel
    private lateinit var list : List<FamilyMember>
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.ivBack.setOnClickListener{
            activity?.onBackPressed()
        }
        super.onViewCreated(view, savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(RetrofitClient.apiService)
        )[FamilyMemberViewModel::class.java]
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_family
    }

    override fun initViews() {
        showProgressDialog()
        initObserver()
        viewModel.getAllFamilyMember()

        binding.btnAddFamilyMember.setOnClickListener {
            (activity as MainActivity).replaceFragment(AddFamilyMemberFragment(), "AddFamilyMemberFragment")
        }

        binding.ivAdd.setOnClickListener {
            (activity as MainActivity).replaceFragment(AddFamilyMemberFragment(), "AddFamilyMemberFragment")
        }
    }

    private fun initObserver() {
        viewModel.getAllFamilyMemberList.observe(viewLifecycleOwner) {familyList->
            hideProgressDialog()
            if(familyList.isEmpty()){
                binding.clEmpty.visibility = View.VISIBLE
                binding.rvMembers.visibility = View.GONE

            }else{
                val membersRecycler = binding.rvMembers
                binding.clEmpty.visibility = View.GONE
                membersRecycler.visibility = View.VISIBLE
                list = familyList
                val adapter = FamilyAdapter(requireActivity(),familyList,viewModel)
                membersRecycler.adapter = adapter

                val swipeToDeleteCallback = SwipeToDeleteCallback(adapter, viewModel)
                val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
                itemTouchHelper.attachToRecyclerView(membersRecycler)

            }
        }

        viewModel.toastMsg.observe(viewLifecycleOwner, EventObserver {
            hideProgressDialog()
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        })
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FamilyFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }


}