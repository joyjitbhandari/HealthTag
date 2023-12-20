package com.flynaut.healthtag.view

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.flynaut.healthtag.R
import com.flynaut.healthtag.databinding.FragmentFeedbackBinding
import com.flynaut.healthtag.model.response.FeedbackData
import com.flynaut.healthtag.model.response.FeedbackResponse
import com.flynaut.healthtag.network.ApiService
import com.flynaut.healthtag.network.RetrofitClient
import com.flynaut.healthtag.util.SavedData.USER_ID
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FeedbackFragment : BaseFragment<FragmentFeedbackBinding>() {

    private var selectedEmoji: ImageView? = null
    private var selectedText: TextView? = null
    private var lastSubmittedFeedback: FeedbackData? = null

    override fun getLayoutResId(): Int {
        return R.layout.fragment_feedback
    }

    override fun initViews() {
        binding.ivBack.setOnClickListener {
            activity?.onBackPressed()
        }

        selectedEmoji = binding.star3
        selectedText = binding.feedback3

        binding.star1.setOnClickListener { onRatingChanged(1, binding.star1, binding.feedback1) }
        binding.star2.setOnClickListener { onRatingChanged(2, binding.star2, binding.feedback2) }
        binding.star3.setOnClickListener { onRatingChanged(3, binding.star3, binding.feedback3) }
        binding.star4.setOnClickListener { onRatingChanged(4, binding.star4, binding.feedback4) }
        binding.star5.setOnClickListener { onRatingChanged(5, binding.star5, binding.feedback5) }

        binding.btnSubmit.setOnClickListener {
            showBottomSheetDialog()
        }

        fetchLastSubmittedFeedback()
    }

    private fun fetchLastSubmittedFeedback() {
        val apiService = RetrofitClient.createService(ApiService::class.java)
        val call = apiService.getLastSubmittedFeedback(USER_ID)

        call.enqueue(object : Callback<FeedbackResponse> {
            override fun onResponse(call: Call<FeedbackResponse>, response: Response<FeedbackResponse>) {
                if (response.isSuccessful) {
                    val feedbackResponse = response.body()
                    if (feedbackResponse != null) {
                        val lastFeedback = feedbackResponse.data
                        lastSubmittedFeedback = lastFeedback

                        if (lastFeedback != null) {
                            binding.etFeedback.setText(lastFeedback.feedback)

                            val selectedEmoji = getEmojiImageView(lastFeedback.emoji)
                            val selectedText = getEmojiTextView(lastFeedback.emoji)
                            onRatingChanged(lastFeedback.emoji, selectedEmoji, selectedText)
                        } else {
                            Toast.makeText(requireContext(), "Error: Feedback is null!", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(requireContext(), "Error: Feedback response is null!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Error: Failed to fetch feedback!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<FeedbackResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Error!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun onRatingChanged(rating: Int, selectedEmoji: ImageView, selectedText: TextView) {
        this.selectedEmoji?.let { it.background = null }
        this.selectedText?.let { it.setTextColor(ContextCompat.getColor(binding.root.context, R.color.white_50)) }

        // Set the background of the selected emoji to the circle drawable
        selectedEmoji.setBackgroundResource(R.drawable.circle_white)
        selectedText.setTextColor(ContextCompat.getColor(binding.root.context, R.color.white))
        this.selectedEmoji = selectedEmoji
        this.selectedText = selectedText
    }

    private fun showBottomSheetDialog() {
        val feedback = binding.etFeedback.text.toString().trim()
        val emoji = getSelectedEmoji()

        if (feedback.isNotEmpty() && emoji != null) {
            if (lastSubmittedFeedback != null) {
                updateFeedback(feedback, emoji)
            } else {
                submitFeedback(feedback, emoji)
            }
        } else {
            Toast.makeText(requireContext(), "Error!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun submitFeedback(feedback: String, emoji: Int) {
        val apiService = RetrofitClient.createService(ApiService::class.java)
        val call = apiService.submitFeedback(USER_ID, feedback, emoji)

        call.enqueue(object : Callback<FeedbackResponse> {
            override fun onResponse(call: Call<FeedbackResponse>, response: Response<FeedbackResponse>) {
                if (response.isSuccessful) {
                    val feedbackResponse = response.body()
                    if (feedbackResponse != null) {
                        lastSubmittedFeedback = feedbackResponse.data
                    } else {
                        Toast.makeText(requireContext(), "Error!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Error!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<FeedbackResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Error!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateFeedback(feedback: String, emoji: Int) {
        val apiService = RetrofitClient.createService(ApiService::class.java)
        val fields = mapOf(
            "feedback" to feedback,
            "emoji" to emoji.toString()
        )
        val call = apiService.updateUserFeedback(USER_ID, fields)
        call.enqueue(object : Callback<FeedbackResponse> {
            override fun onResponse(call: Call<FeedbackResponse>, response: Response<FeedbackResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "submitted successfully", Toast.LENGTH_SHORT).show()
                    requireActivity().onBackPressed()
                } else {
                    Toast.makeText(requireContext(), "Error!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<FeedbackResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Error!", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun getSelectedEmoji(): Int? {
        return when (selectedEmoji) {
            binding.star1 -> 1
            binding.star2 -> 2
            binding.star3 -> 3
            binding.star4 -> 4
            binding.star5 -> 5
            else -> null
        }
    }

    private fun getEmojiImageView(emoji: Int): ImageView {
        return when (emoji) {
            1 -> binding.star1
            2 -> binding.star2
            3 -> binding.star3
            4 -> binding.star4
            5 -> binding.star5
            else -> binding.star3
        }
    }

    private fun getEmojiTextView(emoji: Int): TextView {
        return when (emoji) {
            1 -> binding.feedback1
            2 -> binding.feedback2
            3 -> binding.feedback3
            4 -> binding.feedback4
            5 -> binding.feedback5
            else -> binding.feedback3
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FeedbackFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}