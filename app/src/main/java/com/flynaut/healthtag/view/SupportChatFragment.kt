package com.flynaut.healthtag.view

import android.content.DialogInterface
import android.graphics.Bitmap
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.flynaut.healthtag.R
import com.flynaut.healthtag.adapter.ChatAdapter
import com.flynaut.healthtag.databinding.FragmentSupportChatBinding
import com.flynaut.healthtag.model.ModelChat
import com.flynaut.healthtag.model.UserProfileDataSaved
import com.flynaut.healthtag.model.response.DataItemAllTickets
import com.flynaut.healthtag.util.Constant
import com.flynaut.healthtag.util.PrefsManager
import com.flynaut.healthtag.util.showToast
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.google.gson.Gson
import java.util.Date

class SupportChatFragment : BaseFragment<FragmentSupportChatBinding>() {
    private lateinit var builder: AlertDialog.Builder
    private lateinit var ticketData: DataItemAllTickets
    private lateinit var adapter: ChatAdapter
    private lateinit var databaseReference: DatabaseReference
    var nChat = ArrayList<ModelChat>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        FirebaseApp.initializeApp(requireContext())
        binding.tvTitle.text=ticketData.ticketTitle
        setClick()
        showProgressDialog()

        readMessage()
    }
    private fun setClick() {
        binding.btnImage.setOnClickListener {
            selectImage()
        }

        binding.ivBack.setOnClickListener {
            activity?.onBackPressed()
        }
        binding.send.setOnClickListener {
            if (binding.etMsg.text.toString().isEmpty()) {
                context?.showToast("Please enter your message")

            } else {
                val timeStamp = "" + System.currentTimeMillis()
                val databaseReference1 = FirebaseDatabase.getInstance().reference
                val hashMap = HashMap<String, Any>()
                hashMap["sender"] = ticketData.user.toString()
                hashMap["receiver"] = "Admin"
                hashMap["text"] = binding.etMsg.text.toString()
                hashMap["type"] = "text"
                hashMap["timeStamp"] = getDateTime()
                databaseReference1.child("messages")
                    .child(ticketData.id.toString()).push().setValue(hashMap)
                binding.etMsg.setText("")

            }

        }
    }

    private fun getDateTime(): String {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        val date = Date()
        return dateFormat.format(date)
    }

    private fun selectImage() {
        val options = arrayOf<CharSequence>(
            getString(R.string.take_photo), getString(R.string.choose_from_gallery), getString(
                R.string.cancel
            )
        )
        builder = AlertDialog.Builder(requireContext(), R.style.AlertDialogCustom)
        builder.setTitle("Add Photo!")
        builder.setItems(options) { dialog: DialogInterface, item: Int ->
            if (options[item] == "Take Photo") {
                if (Constant().check_permissions(requireActivity())) {
                    cropImage.launch(
                        CropImageContractOptions(
                            uri = null,
                            cropImageOptions = CropImageOptions(
                                guidelines = CropImageView.Guidelines.ON,
                                imageSourceIncludeCamera = true,
                                imageSourceIncludeGallery = false,
                                outputCompressQuality = 20,
                                outputCompressFormat = Bitmap.CompressFormat.WEBP
                            ),
                        ),
                    )
                }
            } else if (options[item] == "Choose from Gallery") {
                if (Constant().check_permissions(requireActivity())) {
                    cropImage.launch(
                        CropImageContractOptions(
                            uri = null,
                            cropImageOptions = CropImageOptions(
                                guidelines = CropImageView.Guidelines.ON,
                                imageSourceIncludeCamera = false,
                                outputCompressQuality = 20,
                                imageSourceIncludeGallery = true,
                                outputCompressFormat = Bitmap.CompressFormat.WEBP
                            ),
                        ),
                    )
                }
            } else if (options[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()
    }

    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            // Use the returned uri.
            val uriContent = result.uriContent
            val uriFilePath = result.getUriFilePath(requireContext()) // optional usage
            //    mAdapter.update(uriContent)//add temp bases
            sendImage(uriContent)
            showProgressDialog()
        } else {
            // An error occurred.
            val exception = result.error
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        viewModel = ViewModelProvider(
//            this,
//            ViewModelFactory(RetrofitClient.apiService)
//        )[CompleteProfileViewModel::class.java]
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun readMessage() {
        databaseReference = FirebaseDatabase.getInstance().getReference("messages").child(ticketData.id.toString())
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                nChat.clear()
                for (snapshot in dataSnapshot.children) {
                    if (snapshot!=null){
                        val chat = snapshot.getValue(ModelChat::class.java)
                        chat?.let { nChat.add(it) }
                    }
                }
                hideProgressDialog()
                if (nChat.size>0){
                    binding.rvQuestions.scrollToPosition(adapter.itemCount - 1)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                hideProgressDialog()
            }
        })
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_support_chat
    }

    override fun initViews() {
        val userProfile: UserProfileDataSaved = Gson().fromJson(
            PrefsManager.get().getString(PrefsManager.PREF_PROFILE, ""),
            UserProfileDataSaved::class.java)
        val profilePicture = userProfile.profilePicture
        val imageUrl = if (profilePicture.isNullOrEmpty()) {
            R.drawable.profile_placeholder // Replace with the ID of your default profile picture drawable
        } else {
            Constant.IMAGE_URL + profilePicture
        }
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = ChatAdapter(nChat,imageUrl) {data->
            (activity as MainActivity).replaceFragment(FullScreenMediaFragment.newInstance(data), "FullScreenMediaFragment")
        }
        binding.rvQuestions.layoutManager = layoutManager
        binding.rvQuestions.setHasFixedSize(true)
        binding.rvQuestions.adapter = adapter
    }

    private fun sendImage(image_uri: Uri?) {
        showProgressDialog()
        val filenameAndPath = "ChatImages/" + "chat_" + System.currentTimeMillis()
        val ref = FirebaseStorage.getInstance().reference.child(filenameAndPath)
        ref.putFile(image_uri!!).addOnSuccessListener { taskSnapshot: UploadTask.TaskSnapshot ->
            val uriTask =
                taskSnapshot.storage.downloadUrl
            while (!uriTask.isSuccessful);
            val downloadUri = uriTask.result.toString()
            if (uriTask.isSuccessful) {
                val databaseReference1 =
                    FirebaseDatabase.getInstance().reference
                val hashMap = HashMap<String, Any>()
                hashMap["sender"] = ticketData.user.toString()
                hashMap["receiver"] = "Admin"
                hashMap["text"] =downloadUri
                hashMap["type"] = "image"
                hashMap["timeStamp"] = getDateTime()
                databaseReference1.child("messages")
                    .child(ticketData.id.toString()).push().setValue(hashMap)
                    .addOnSuccessListener { aVoid: Void? ->
                      hideProgressDialog()
                        context?.showToast("Image Sent")
                    }
            }
        }
    }
    companion object {
        @JvmStatic
        fun newInstance(data: DataItemAllTickets) =
            SupportChatFragment().apply {
                ticketData = data
            }
    }

}