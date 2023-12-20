package com.flynaut.healthtag.adapter

import android.bluetooth.BluetoothAdapter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.flynaut.healthtag.databinding.AdapterDeviceListItemBinding
import com.flynaut.healthtag.model.response.AllDevice
import com.flynaut.healthtag.util.Constant
import com.flynaut.healthtag.util.Utils
import com.flynaut.healthtag.view.MainActivity
import com.flynaut.healthtag.view.addDevice.DeviceListFragment
import com.flynaut.healthtag.view.addDevice.DeviceNameSetFragment
import com.flynaut.healthtag.view.deviceOne.PrepareDeviceFragment
import com.flynaut.healthtag.R


class DeviceListAdapter(
    val activity: FragmentActivity?, private val dataSet: List<AllDevice>, private val context: DeviceListFragment) : RecyclerView.Adapter<DeviceListAdapter.ViewHolder>() {


    class ViewHolder(private val binding: AdapterDeviceListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var blinkingRunnable: Runnable? = null
        private var isBlinking = false
        private var blinkCounter = 0

        private val images = arrayOf(
            binding.cardStart,
            binding.cardDelete,
            binding.cardEdit
        )

        private val initialVisibility = images.map { it.visibility }

        init {
            resetImageVisibility()
        }


        fun bind(activity: FragmentActivity?, item: AllDevice,context: DeviceListFragment) {
            var click = false
            //binding.imgDevice.setImageResource(item.image)
            binding.txtDeviceName.text = item.name
            binding.txtDeviceType.text = item.protocolType

            if (item.advertisementData.equals(Utils.SPIROMETER))
            {
             binding.imgDevice.setImageResource(R.drawable.img_device1)
            }else{
                binding.imgDevice.setImageResource(R.drawable.img_device2)

            }

            binding.cardView.setOnClickListener {
                click = !click

                if (click) {
                    binding.layDeviceDetails.visibility = View.GONE
                    binding.consDeviceButtons.visibility = View.VISIBLE
                    var isSpiro = false
                    binding.cardStart.setOnClickListener {
                        val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
                        if (!mBluetoothAdapter.isEnabled) {
                            Toast.makeText(activity,"Please ON the bluetooth", Toast.LENGTH_SHORT).show()
                        }else{
                            if (item.advertisementData.equals(Utils.SPIROMETER))
                                isSpiro = true
                            else
                                isSpiro = false

                            val fragment = PrepareDeviceFragment.newInstance(item,isSpiro)

                            (activity as MainActivity).replaceFragment(
                                fragment, "PrepareDeviceFragment",false)

                        }
                                           }
                    binding.cardDelete.setOnClickListener {
                        context.deleteDevice(item._id,position,item.name)
                        //Utils.deviceDataList.removeAt(position)
                    }
                    binding.cardEdit.setOnClickListener {

                        val fragment = DeviceNameSetFragment()
                        val bundle = Bundle()
                        bundle.putSerializable(Utils.DEVICE_ID, item._id)
                        bundle.putSerializable(Utils.DEVICE_NAME, item.name)
                        bundle.putSerializable(Utils.DEVICE_EDIT, true)
                        fragment.arguments = bundle
                        (activity as MainActivity).replaceFragment(fragment,"DeviceNameSetFragment")
                    }
                    startBlinking()

                } else {
                    binding.layDeviceDetails.visibility = View.VISIBLE
                    binding.consDeviceButtons.visibility = View.GONE
                    stopBlinking()
                    resetImageVisibility()
                }
            }
        }
        private fun resetImageVisibility() {
            images.forEachIndexed { index, image ->
                image.visibility = initialVisibility[index]
            }
        }

        private fun startBlinking() {
            if (!isBlinking) {
                isBlinking = true
                blinkCounter = 0

                blinkingRunnable = object : Runnable {
                    override fun run() {
                        blinkCounter++
                        images.forEachIndexed { index, image ->
                            image.visibility = if (blinkCounter % images.size == index) View.VISIBLE else View.INVISIBLE
                        }

                        if (isBlinking) {
                            binding.root.postDelayed(this, 500) // Adjust blinking duration as needed
                        }
                    }
                }


                blinkingRunnable?.run()
            }
        }

        private fun stopBlinking() {
            isBlinking = false

            // Remove any pending blinking runnables
            blinkingRunnable?.let {
                binding.root.removeCallbacks(it)
            }
        }
    }


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdapterDeviceListItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size


    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Get element from your dataset at this position
        val item = dataSet[position]
        holder.bind(activity, item,context)
    }


}
