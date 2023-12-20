    package com.flynaut.healthtag.view

    import android.app.AlertDialog
    import android.app.ProgressDialog
    import android.os.Bundle
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import androidx.databinding.DataBindingUtil
    import androidx.databinding.ViewDataBinding
    import androidx.fragment.app.Fragment
    import com.flynaut.healthtag.R
    import com.spirometry.spirobanksmartsdk.DeviceManager


    abstract class BaseFragment<Binding : ViewDataBinding> : Fragment() {

        protected lateinit var binding: Binding
        var mProgressDialog: ProgressDialog? = null

        protected val deviceManager: DeviceManager get() = DeviceManager.getInstance(activity)

        open fun showProgressDialog() {
            if (mProgressDialog == null) {
                mProgressDialog = ProgressDialog(this.context)
                mProgressDialog!!.setMessage("Loading ...")
                mProgressDialog!!.isIndeterminate = true
            }
            mProgressDialog!!.show()
        }

        open fun hideProgressDialog() {
            if (mProgressDialog != null && mProgressDialog!!.isShowing) {
                mProgressDialog!!.dismiss()
            }
        }

        override fun onStop() {
            super.onStop()
            hideProgressDialog()
        }
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            binding = DataBindingUtil.inflate(inflater, getLayoutResId(), container, false)
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            binding.lifecycleOwner = viewLifecycleOwner
            initViews()
        }

        protected abstract fun getLayoutResId(): Int

        protected abstract fun initViews()

        fun addFragment(fragment: Fragment, tag: String) {
            val transaction = childFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_dashboard_container, fragment,tag)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        protected fun showDialog(text: String, onOkPressed: (() -> Unit)? = null) {
            AlertDialog.Builder(activity)
                .setTitle(R.string.app_name)
                .setMessage(text)
                .setPositiveButton("Ok") { _, _ ->
                    onOkPressed?.invoke()
                }
                .show()
        }
    }
