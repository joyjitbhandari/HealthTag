package com.flynaut.healthtag.view

import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import com.flynaut.healthtag.R
import com.flynaut.healthtag.databinding.FragmentCreateTicketBinding
import com.flynaut.healthtag.model.SpinnerCategoryModel
import com.flynaut.healthtag.model.request.AddTicketRequest
import com.flynaut.healthtag.model.response.DataItemAllTickets
import com.flynaut.healthtag.network.RetrofitClient
import com.flynaut.healthtag.util.CustomTypefaceSpan
import com.flynaut.healthtag.util.showToast
import com.flynaut.healthtag.viewmodel.AllTicketsViewModel
import com.flynaut.healthtag.viewmodel.ViewModelFactory

class CreateTicketFragment(var listner:TicketCLickListener ) : BaseBottomSheetFragment<FragmentCreateTicketBinding>() {

    private var ticketData= ArrayList<SpinnerCategoryModel>()
    private lateinit var viewModel: AllTicketsViewModel
    override val layoutResourceId: Int
        get() = R.layout.fragment_create_ticket

    override fun initViews() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(RetrofitClient.apiService)
        )[AllTicketsViewModel::class.java]
        initObserver()
        initData()
        setClick()
    }

    private fun setClick() {
        binding.btnSubmit.setOnClickListener {
            if (setValidation()) {
                showProgressDialog()
                viewModel.addTicket(
                    AddTicketRequest(
                        ticketTitle = binding.etTicketTitle.text.toString(),
                        description = binding.etIssue.text.toString(),
                        topic = binding.etTopic.text.toString()
                    )
                )
            }
        }
    }

    private fun initData() {
        setCustomText()
        getCategoriesApi()

       // val addressTypes = listOf( "Select Address Type","Home", "Office", "Other")

       // binding.etTopic.text = "payments2"
    }

    private fun getCategoriesApi() {
        showProgressDialog()
        viewModel. getAllTicketsDetail()
    }

    private fun initObserver() {
        viewModel.addTicketResponse.observe(viewLifecycleOwner) {
            hideProgressDialog()
            context?.showToast( it.message, Toast.LENGTH_SHORT)
            listner.btnBackAfterSaveTicket()
            this.dismiss()
        }


        viewModel.finalTicketDetailsResponse.observe(viewLifecycleOwner) {
            hideProgressDialog()
            it.data?.forEach {data->
                ticketData.add(SpinnerCategoryModel(data.name,data.id))
            }
            var isFirstTime = true
            val adapter = context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, ticketData) }
            adapter?.setDropDownViewResource(android.R.layout.simple_spinner_item)
            binding.spTopic.adapter = adapter
            binding.etTopic.setOnClickListener {
                binding.spTopic.performClick()
            }

            binding.spTopic.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    if(isFirstTime){
                        isFirstTime = false
                        return
                    }
                    binding.etTopic.text = parent.getItemAtPosition(position).toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    binding.etTopic.text = binding.spTopic.selectedItem.toString()
                }
            }
        }
        //error
        viewModel.toastMsg.observe(viewLifecycleOwner) {
            hideProgressDialog()
            context?.showToast(it, Toast.LENGTH_LONG)
        }
    }

    private fun setValidation(): Boolean {
        if (binding.etTicketTitle.text.isEmpty()) {
            binding.etTicketTitle.error = "Please add ticket title"
            return false
        } else if (binding.etTopic.text.isEmpty()) {
            binding.etTopic.error = "Please add ticket description"
            return false
        } else if (binding.etIssue.text.isEmpty()) {
            binding.etIssue.error = "Please describe your issue"
            return false
        }
        return true
    }

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme;
    }

    private fun setCustomText() {
        val context = binding.root.context
        val builder = SpannableStringBuilder()

        val firstText = SpannableString("Create a")
        val firstFont =
            CustomTypefaceSpan(context?.let { ResourcesCompat.getFont(it, R.font.outfit_regular) })
        firstText.setSpan(firstFont, 0, firstText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        val secondText = SpannableString(" Ticket")
        val secondFont =
            CustomTypefaceSpan(context?.let { ResourcesCompat.getFont(it, R.font.outfit_semibold) })
        secondText.setSpan(secondFont, 0, secondText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        builder.append(firstText)
        builder.append(secondText)

//        binding.tvResend.movementMethod = LinkMovementMethod.getInstance()
        binding.tvCreateTicket.text = builder
    }
    interface TicketCLickListener{
        fun btnBackAfterSaveTicket()
    }
}