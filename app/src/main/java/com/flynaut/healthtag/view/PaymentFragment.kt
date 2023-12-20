package com.flynaut.healthtag.view

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.flynaut.healthtag.R
import com.flynaut.healthtag.adapter.CardPagerAdapter
import com.flynaut.healthtag.databinding.FragmentPaymentsBinding
import com.flynaut.healthtag.model.request.DeleteCardRequest
import com.flynaut.healthtag.model.request.GetCardRequest
import com.flynaut.healthtag.network.RetrofitClient
import com.flynaut.healthtag.util.EventObserver
import com.flynaut.healthtag.util.PrefsManager
import com.flynaut.healthtag.util.PrefsManager.Companion.KEY_CARD_SELECTION
import com.flynaut.healthtag.util.SavedData.PREF_CREATE_CUSTOMER_KEY
import com.flynaut.healthtag.util.SavedData.PREF_ID
import com.flynaut.healthtag.util.setPreviewBothSide
import com.flynaut.healthtag.viewmodel.CardViewModel
import com.flynaut.healthtag.viewmodel.ViewModelFactory
import kotlin.math.roundToInt


class PaymentFragment : BaseFragment<FragmentPaymentsBinding>(),
    CardItemFragment.CardCLickListener {

    private lateinit var viewModel: CardViewModel
    var isCheckout = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(RetrofitClient.apiService)
        )[CardViewModel::class.java]
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivBack.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }

    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_payments
    }

    override fun initViews() {
        showProgressDialog()
        initObserver()
        deleteCard()
        viewModel.getAllCardDetails(
            PREF_ID, GetCardRequest(
                PREF_CREATE_CUSTOMER_KEY
            )
        )

        binding.viewPager.setPreviewBothSide(40, 0)

        binding.clMasterCard.setOnClickListener {
            (activity as MainActivity).replaceFragment(AddCardFragment(), "AddCardFragment")
        }
        binding.clVisaCard.setOnClickListener {
            (activity as MainActivity).replaceFragment(AddCardFragment(), "AddCardFragment")
        }
        binding.ivAdd.setOnClickListener {
            (activity as MainActivity).replaceFragment(AddCardFragment(), "AddCardFragment")
        }

    }

    private fun initObserver() {
        viewModel.getAllCardList.observe(viewLifecycleOwner) { cardList ->
            hideProgressDialog()
//             adapter = CardAdapter(cardList,this)
//             binding.cardRecycler.adapter = adapter
//             binding.indicator.setRecyclerView(binding.cardRecycler)

            val cardPagerAdapter = CardPagerAdapter(requireActivity(), cardList, this)
            binding.viewPager.adapter = cardPagerAdapter

            binding.indicator.setViewPager(binding.viewPager)
        }

        viewModel.deleteCardApiResponse.observe(viewLifecycleOwner) {
            hideProgressDialog()
            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
        }

        viewModel.toastMsg.observe(viewLifecycleOwner, EventObserver {
            hideProgressDialog()
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        })
    }

    private fun dpToPx(dp: Int): Int {
        val displayMetrics = binding.root.context.resources.displayMetrics
        return (dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
    }

    companion object {
        @JvmStatic
        fun newInstance(Checkout: Boolean) =
            PaymentFragment().apply {
                arguments = Bundle().apply {
                    isCheckout = Checkout
                }
            }
    }

    override fun onCardDelete(cardId: String, position: Int) {
        showProgressDialog()

        viewModel.deleteCard(DeleteCardRequest(PREF_CREATE_CUSTOMER_KEY,cardId))
        (binding.viewPager.adapter as? CardPagerAdapter)?.removeItem(position)

    }

    override fun onItemSelected(position: Int) {
        if (isCheckout){
            PrefsManager.get().save(KEY_CARD_SELECTION, position.toString())
            (activity as MainActivity).onBackPressed()
        }

    }

    private fun deleteCard() {
//        viewModel.deleteCardApiResponse.observe(viewLifecycleOwner) {
//            hideProgressDialog()
//            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
//        }

//        viewModel.toastMsg.observe(viewLifecycleOwner, EventObserver {
//            hideProgressDialog()
//            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
//        })
    }

}