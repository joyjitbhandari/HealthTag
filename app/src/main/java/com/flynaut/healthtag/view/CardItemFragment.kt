package com.flynaut.healthtag.view

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.flynaut.healthtag.R
import com.flynaut.healthtag.databinding.AdapterCardItemBinding
import com.flynaut.healthtag.model.response.CardDetails
import com.flynaut.healthtag.network.RetrofitClient
import com.flynaut.healthtag.util.EventObserver
import com.flynaut.healthtag.util.Utils.CARD_ID
import com.flynaut.healthtag.util.Utils.POSITION
import com.flynaut.healthtag.viewmodel.CardViewModel
import com.flynaut.healthtag.viewmodel.ViewModelFactory
import kotlin.math.roundToInt

class CardItemFragment(private val listener: CardCLickListener) : BaseFragment<AdapterCardItemBinding>() {
    interface CardCLickListener{
        fun onCardDelete(cardId : String, position: Int)
        fun onItemSelected(position: Int)
    }

    private var cardDetails: CardDetails? = null
    private lateinit var viewModel: CardViewModel
    private lateinit var cardId: String
    private  var position: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            cardId = it.getString(CARD_ID).toString()
            position = it.getInt(POSITION)
           cardDetails  = it.getParcelable<CardDetails>("KeyModel")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this,ViewModelFactory(RetrofitClient.apiService))[CardViewModel::class.java]
        return super.onCreateView(inflater, container, savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun getLayoutResId(): Int {
        return R.layout.adapter_card_item
    }

    override fun initViews() {
       // showProgressDialog()
      //  initObserver()
        //viewModel.getCardDetails(cardId)
     //   val data :CardDetails = it.data
            binding.tvName.text = cardDetails?.name
            binding.tvCard.text = "XXXX XXXX XXXX ${cardDetails?.last4}"
            binding.tvMonth.text = cardDetails?.expMonth.toString()+"/"+cardDetails?.expYear.toString()

        binding.tvDelete.setOnClickListener {
          //  showProgressDialog()
//            deleteCard()
//            viewModel.deleteCard(1,cardId)
            listener.onCardDelete(cardId, position)
        }

        binding.layoutCard.setOnClickListener {
            listener.onItemSelected( position)
        }

    }

    private fun initObserver() {
//        viewModel.getCardApiResponse.observe(viewLifecycleOwner){
//            hideProgressDialog()
//            val data :CardDetails = it.data
//            binding.tvName.text = data.name
//            binding.tvCard.text = "XXXX XXXX XXXX ${data.last4Digits}"
//            binding.tvMonth.text = data.expiryDate
//        }

        viewModel.toastMsg.observe(viewLifecycleOwner, EventObserver{
            hideProgressDialog()
            Toast.makeText(requireContext(),it, Toast.LENGTH_SHORT).show()
        })
    }

//    private fun deleteCard(){
//        viewModel.deleteCardApiResponse.observe(viewLifecycleOwner){
//            hideProgressDialog()
//            PaymentFragment
//            Toast.makeText(requireContext(),it.message, Toast.LENGTH_SHORT).show()
//
//        }
//
//        viewModel.toastMsg.observe(viewLifecycleOwner, EventObserver{
//            hideProgressDialog()
//            Toast.makeText(requireContext(),it, Toast.LENGTH_SHORT).show()
//        })
//    }


    private fun dpToPx(dp: Int): Int {
        val displayMetrics = binding.root.context.resources.displayMetrics
        return (dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
    }


//    companion object {
//        @JvmStatic
//        fun newInstance(cardId: String, position: Int) =
//            CardItemFragment().apply {
//                arguments = Bundle().apply {
//                    putString(CARD_ID, cardId)
//                }
//            }
//    }


}