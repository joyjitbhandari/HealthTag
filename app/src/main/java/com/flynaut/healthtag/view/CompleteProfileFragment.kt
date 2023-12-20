package com.flynaut.healthtag.view

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.text.*
import android.text.InputFilter.LengthFilter
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.flynaut.healthtag.R
import com.flynaut.healthtag.databinding.FragmentCompleteProfileBinding
import com.flynaut.healthtag.model.UserProfileDataSaved
import com.flynaut.healthtag.model.request.CompleteProfileRequest
import com.flynaut.healthtag.network.RetrofitClient
import com.flynaut.healthtag.util.*
import com.flynaut.healthtag.util.Constant.Companion.IMAGE_URL
import com.flynaut.healthtag.util.SavedData.USER_ID
import com.flynaut.healthtag.viewmodel.CompleteProfileViewModel
import com.flynaut.healthtag.viewmodel.ViewModelFactory
import com.google.gson.Gson
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

private const val REQUEST_IMAGE_PICK = 111
private const val REQUEST_IMAGE_CAPTURE = 112
private const val ARG_IS_EDIT = "arg_is_edit"
class CompleteProfileFragment : BaseFragment<FragmentCompleteProfileBinding>() {

    private var isUpdating = false
    private var isEditMode: Boolean = false
    private val requestCodeCameraPermission = 1001

    //    private late init var data : Data
    var completeProfileRequest = null
    private lateinit var viewModel: CompleteProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(RetrofitClient.apiService)
        )[CompleteProfileViewModel::class.java]
        super.onCreate(savedInstanceState)
        arguments?.let {
            isEditMode = it.getBoolean(ARG_IS_EDIT)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivBack.setOnClickListener{
            activity?.onBackPressed()
        }

        if (isEditMode) {
            setTitleText("Edit", "Profile")
            binding.clProfile.setVisible(true)
            binding.btnContinue.text = "Update"
            showProgressDialog()
            viewModel.getProfile()

        } else {
            setTitleText("Complete", "Your profile")
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_complete_profile
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode)
        {
            REQUEST_IMAGE_PICK -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val imageUri: Uri? = data.data
                    if (imageUri != null) {
                        binding.ivProfile.setImageURI(imageUri)
                    }
                }
            }
            REQUEST_IMAGE_CAPTURE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val imageBitmap = data.extras?.get("data") as Bitmap?
                    if (imageBitmap != null) {
                        binding.ivProfile.setImageBitmap(imageBitmap)
                    }
                }
            }
        }
    }
    override fun initViews() {
        SavedData.loadUserId()
        initObserver()
        binding.tvUploadProfile.setOnClickListener {
            if (ContextCompat.checkSelfPermission(binding.root.context, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                askForCameraPermission()
            } else {
               showOption()
            }
        }

        binding.tvDateOfBirth.setOnClickListener {
            showDatePicker(binding.tvDateOfBirth)
        }

        binding.etPhone.filters = arrayOf<InputFilter>(LengthFilter(12))
        binding.etPhone.addTextChangedListener(object : TextWatcher {
            var length_before = 0
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                length_before = s.length
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (length_before < s.length) {
                    if (s.length == 3 || s.length == 7) s.append("-")
                    if (s.length > 3) {
                        if (Character.isDigit(s[3])) s.insert(3, "-")
                    }
                    if (s.length > 7) {
                        if (Character.isDigit(s[7])) s.insert(7, "-")
                    }
                }
                if (s.isNotEmpty() && s.length < 12) {
                    binding.etPhone.error = "Please enter valid phone number"
                } else {
                    binding.etPhone.error = null
                }
            }
        })

        binding.rgHeightMeasure.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_ft -> {
                    setHeightBoxVisibility(true)
                }
                R.id.rb_cm -> {
                    setHeightBoxVisibility(false)
                }
            }
        }

        binding.rgWeightMeasure.setOnCheckedChangeListener{_, checkedId ->
            val weightText = binding.etWeight.text.toString().trim()
            if (weightText.isNotEmpty()) {
                try {
                    val weight = weightText.toDouble()
                    val convertedWeight = if (checkedId == R.id.rb_kg) lbToKg(weight) else kgToLbs(weight)
                    binding.etWeight.setText(formatDecimalWeight(convertedWeight))
                } catch (e: NumberFormatException) {
                    // Handle the case when the input is not a valid number
                    binding.etWeight.setText("")
                }
            } else {
                // Handle the case when the input is empty
                binding.etWeight.setText("")
            }
        }


        binding.etInch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val text = s.toString().trim()
                if (text.isNotEmpty() && text.toInt() > 11) {
                    binding.etInch.error = "Input value should not be greater than 11"
                } else {
                    binding.etInch.error = null
                }
                if (!isUpdating) {
                    isUpdating = true
                    convertFeetToCm()
                    isUpdating = false
                }
            }
        })

        binding.etFeet.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (!isUpdating) {
                    isUpdating = true
                    convertFeetToCm()
                    isUpdating = false
                }
            }
        })

        binding.etCm.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (!isUpdating) {
                    isUpdating = true
                    convertCmToFeet()
                    isUpdating = false
                }
            }
        })


        val genderList = listOf("Select ", "Male", "Female")
        val adapter = context?.let { ArrayAdapter(it, R.layout.spinner_dropdown_item, genderList) }
        adapter?.setDropDownViewResource(R.layout.spinner_dropdown_item)
        binding.spGender.adapter = adapter
        binding.tvSpGender.setOnClickListener {
            binding.spGender.performClick()
        }

        binding.spGender.setSelection(0, false)

        binding.spGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long,
            ) {
                val gender = parent.getItemAtPosition(position).toString()
                if (gender != "Select") {
                    binding.tvSpGender.text = gender
                } else {
                    binding.spGender.setSelection(0, false)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        val raceList = listOf("Select", "Caucasian", "Asian", "African", "Latino")
        val raceAdapter = context?.let { ArrayAdapter(it, R.layout.spinner_dropdown_item, raceList) }
        adapter?.setDropDownViewResource(R.layout.spinner_dropdown_item)
        binding.spRace.adapter = raceAdapter
        binding.tvRace.setOnClickListener {
            binding.spRace.performClick()
        }

        binding.spRace.setSelection(0, false)

        binding.spRace.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long,
            ) {
                val gender = parent.getItemAtPosition(position).toString()
                if (gender != "Select") {
                    binding.tvRace.text = gender
                } else {
                    binding.spRace.setSelection(0, false)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

//        var isFirstTimeLanguage = true
        val languageList = listOf("English", "Hindi")
        val adapter1 =
            context?.let { ArrayAdapter(it, R.layout.spinner_dropdown_item, languageList) }
        adapter1?.setDropDownViewResource(R.layout.spinner_dropdown_item)
        binding.spLanguage.adapter = adapter1

        binding.tvSpLanguage.setOnClickListener {
            binding.spLanguage.performClick()
        }
        var language = ""
        binding.spLanguage.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long,
            ) {
//                if(isFirstTimeLanguage){
//                    isFirstTimeLanguage = false
//                    return
//                }
                language = parent.getItemAtPosition(position).toString()
                binding.tvSpLanguage.text = language
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                binding.tvSpLanguage.text = binding.spLanguage.selectedItem.toString()

            }
        }

        binding.ivAddress.setOnClickListener {
            (activity as MainActivity).replaceFragment(
                AddressBookFragment(),
                "AddressFragment",
                false
            )
        }

        binding.btnContinue.setOnClickListener {
            if (isEditMode) {
                val isFormValid = areAllFieldsFilled(
                    binding.etFirstName,
                    binding.etLastName,
                    binding.etFeet,
                    binding.etWeight,
                    binding.etPhone
//                    binding.etState,
//                    binding.etZipcode
                )
                if(isFormValid) {
                    showProgressDialog()
                    val phoneNumber = binding.etPhone.text.toString().replace("-", "").trim()
                    viewModel.updateProfile(
                        CompleteProfileRequest(
                            binding.etFirstName.text.toString(),
                            binding.etLastName.text.toString(),
                            binding.tvSpGender.text.toString(),
                            binding.etCm.text.toString().toInt(),
                            binding.etWeight.text.toString().toInt(),
                            binding.tvSpLanguage.text.toString(),
                            "",
                            phoneNumber.toLong(),
                            binding.tvRace.text.let { it.toString()?: "" },
                            binding.tvDateOfBirth.text.toString()
                        )
                    )
                }
            } else {
                val isFormValid = areAllFieldsFilled(
                    binding.etFirstName,
                    binding.etLastName,
                    binding.etFeet,
                    binding.etWeight,
                    binding.etPhone,
//                    binding.etState,
//                    binding.etZipcode
                )
                if(isFormValid){
                    showProgressDialog()
                    val phoneNumber = binding.etPhone.text.toString().replace("-", "").trim()
                    viewModel.completeProfile(
                        USER_ID, CompleteProfileRequest(
                            binding.etFirstName.text.toString(),
                            binding.etLastName.text.toString(),
                            binding.tvSpGender.text.toString(),
                            binding.etCm.text.toString().toInt(),
                            binding.etWeight.text.toString().toInt(),
                            binding.tvSpLanguage.text.toString(),
                            "",
                            phoneNumber.toLong(),
                            binding.tvRace.text.let { it.toString()?: "" },
                            binding.tvDateOfBirth.text.toString()
                        )
                    )
                }
            }
        }

    }

    fun showDatePicker(textView: TextView) {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance().apply {
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, month)
                    set(Calendar.DAY_OF_MONTH, dayOfMonth)
                }
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val selectedDateFormatted = dateFormat.format(selectedDate.time)
                if(selectedDate > calendar){
                    textView.text = "Invalid Date"
                    textView.error = "Invalid Date"
                }
                else{
                    textView.error = null
                    textView.text = selectedDateFormatted
                }

            },
            currentYear,
            currentMonth,
            currentDay
        )
        datePickerDialog.show()
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == requestCodeCameraPermission && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showOption()
            } else {
                Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showOption() {
        val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Choose an option")
        builder.setItems(options) { dialog, item ->
            when {
                options[item] == "Take Photo" -> {
                    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
//                        if (takePictureIntent.resolveActivity(packageManager) != null) {
//                            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
//                        }
                }
                options[item] == "Choose from Gallery" -> {
                    val pickPhotoIntent = Intent(Intent.ACTION_GET_CONTENT)
                    pickPhotoIntent.type = "image/*"
                    startActivityForResult(pickPhotoIntent, REQUEST_IMAGE_PICK)
                }
                options[item] == "Cancel" -> dialog.dismiss()
            }
        }
        builder.show()
    }

    private fun askForCameraPermission() {
        activity?.let { ActivityCompat.requestPermissions(it,
            arrayOf(android.Manifest.permission.CAMERA),
            requestCodeCameraPermission
        )
        }
    }

    private fun convertFeetToCm() {
        val feetText = binding.etFeet.text.toString().trim()
        val inchesText = binding.etInch.text.toString().trim()

        if (feetText.isNotEmpty() || inchesText.isNotEmpty()) {
            val feet = feetText.toDoubleOrNull() ?: 0.0
            val inches = inchesText.toDoubleOrNull() ?: 0.0
            val totalInches = feet * 12 + inches
            val cm = totalInches * 2.54
            binding.etCm.setText(formatDecimal(cm))
        } else {
            binding.etCm.setText("")
        }
    }

    private fun areAllFieldsFilled(vararg fields: EditText): Boolean {
        var isValid = true
        for (field in fields) {
            if (field.text.toString().isEmpty()) {
                field.error = "This Field is required"
                isValid = false
            }
            else{
                if(field.error != null){
                    isValid = false
                }
            }
        }
        return isValid
    }


    private fun formatDecimal(value: Double): String {
        val decimalFormat = DecimalFormat("0.00")
        return decimalFormat.format(value)
    }

    private fun formatDecimalWeight(value: Double): String {
//        return String.format("%.2f", value)
        return String.format("%.0f", value)
    }

    private fun convertCmToFeet() {
        val cmText = binding.etCm.text.toString().trim()
        if (cmText.isNotEmpty()) {
            val cm = cmText.toDouble()
            val totalInches = cm / 2.54
            val feet = totalInches.toInt() / 12
            val inches = totalInches.toInt() % 12
            binding.etFeet.setText(feet.toString())
            binding.etInch.setText(inches.toString())
        } else {
            binding.etFeet.setText("")
            binding.etInch.setText("")
        }
    }
    private fun setTitleText(text1: String, text2: String) {

        val builder = SpannableStringBuilder()

        val firstText = SpannableString("$text1\n")
        val firstFont =
            CustomTypefaceSpan(context?.let { ResourcesCompat.getFont(it, R.font.outfit_regular) })
        firstText.setSpan(firstFont, 0, firstText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        firstText.setSpan(
            ForegroundColorSpan(Color.BLACK),
            0,
            firstText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        val secondText = SpannableString(text2)
        val secondFont =
            CustomTypefaceSpan(context?.let { ResourcesCompat.getFont(it, R.font.outfit_medium) })
        secondText.setSpan(secondFont, 0, secondText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        context?.let {
            secondText.setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(
                        it,
                        R.color.black
                    )
                ), 0, secondText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        builder.append(firstText)
        builder.append(" ")
        builder.append(secondText)

        binding.tvCompletProfile.text = builder
    }

    private fun setHeightBoxVisibility(isFeet: Boolean) {
        binding.etFeet.setInVisible(!isFeet)
        binding.etInch.setInVisible(!isFeet)
        binding.etCm.setInVisible(isFeet)

    }

    private fun kgToLbs(weightInKg: Double): Double {
        return weightInKg * 2.20462
    }

    private fun lbToKg(weightInLb: Double): Double {
        return weightInLb * 0.453592
    }
    private fun initObserver() {
        viewModel.apiResponse.observe(viewLifecycleOwner, Observer {
            hideProgressDialog()
            (activity as MainActivity).replaceFragment(AddressFragment(), "AddressFragment")
//            val completeProfileRequest = CompleteProfileRequest(null, binding.etFirstName.text.toString(), binding.tvSpGender.text.toString(), 0,
//                binding.tvSpLanguage.text.toString(), binding.etLastName.text.toString(), binding.etPhone.text.toString(), 0 )
//            (activity as MainActivity).replaceFragment(AddressFragment.newInstance(completeProfileRequest), "AddressFragment")
            val userProfile: UserProfileDataSaved = Gson().fromJson(
                PrefsManager.get().getString(PrefsManager.PREF_PROFILE, ""),
                UserProfileDataSaved::class.java)
            PrefsManager.get().save(
                PrefsManager.PREF_PROFILE,
                Gson().toJson(UserProfileDataSaved(it.data._id,it.data.firstName, it.data.lastName, it.data.email, it.data.profilePicture,userProfile._paymentId))
            )
        })

        viewModel.getProfileResponse.observe(viewLifecycleOwner, Observer {
            hideProgressDialog()
            val profilePicture = it.data.profilePicture
            val imageUrl = if (profilePicture.isNullOrEmpty()) {
                R.drawable.profile_placeholder
            } else {
                IMAGE_URL + profilePicture
            }
            Glide.with(requireContext())
                .load(imageUrl)
                .into(binding.ivProfile)
            binding.etFirstName.setText(it.data.firstName)
            binding.etLastName.setText(it.data.lastName)
            binding.etPhone.setText(it.data.phoneNumber.toString())
            binding.etWeight.setText(it.data.weight)
            binding.tvDateOfBirth.text = it.data.dateOfBirth
            binding.tvSpGender.text = it.data.gender
            binding.tvRace.text = it.data.race
            binding.tvSpLanguage.text = it.data.languagePreference

            val totalCm = it.data.height.toInt() / 2.54
            val feet = totalCm.toInt() / 12
            val inches = (totalCm.toInt() % 12)+1
            binding.etFeet.setText(feet.toString())
            binding.etInch.setText(inches.toString())

        })


        viewModel.profileUpdateResponse.observe(viewLifecycleOwner, Observer {
            hideProgressDialog()
            val userProfile: UserProfileDataSaved = Gson().fromJson(
                PrefsManager.get().getString(PrefsManager.PREF_PROFILE, ""),
                UserProfileDataSaved::class.java)
            PrefsManager.get().save(
                PrefsManager.PREF_PROFILE,
                Gson().toJson(UserProfileDataSaved(it.data._id,it.data.firstName, it.data.lastName, it.data.email, it.data.profilePicture,userProfile._paymentId))
            )
            context?.showToast(it.message, Toast.LENGTH_SHORT)
            Handler().postDelayed({
                activity?.onBackPressed()
            }, 500L)
        })

        viewModel.toastMsg.observe(viewLifecycleOwner, EventObserver {
            hideProgressDialog()
            context?.showToast(it, Toast.LENGTH_SHORT)
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(isEditMode: Boolean) =
            CompleteProfileFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_IS_EDIT, isEditMode)
                }
            }
    }

}