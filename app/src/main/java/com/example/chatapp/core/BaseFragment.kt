package com.example.chatapp.core

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.chatapp.domain.base.DataState
import com.example.chatapp.utils.Constants
import com.example.chatapp.utils.Constants.REQUEST_PICK_IMAGE
import com.example.chatapp.utils.LoadingProgressBarDialog
import es.dmoral.toasty.Toasty
import pub.devrel.easypermissions.EasyPermissions


abstract class BaseFragment : Fragment() {

    private var loadingProgressBarDialog: LoadingProgressBarDialog? = null

//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return fragmentView(inflater, container, savedInstanceState)
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadingProgressBarDialog = LoadingProgressBarDialog.getInstance(requireContext())
    }

//    protected abstract fun fragmentView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View?


    open protected fun observeLiveData(){

    }

    open protected fun observeFieldsDataInput(){

    }
    open protected fun setListener(){

    }

    open protected fun initTextWatcher() {

    }
    protected fun displayLoading(show: Boolean){
        if(show)
            loadingProgressBarDialog?.show()
        else
            loadingProgressBarDialog?.dismiss()
    }

//    protected abstract fun onSuccess(data : Any?, idApi: String)

    protected open fun onError(messageError: String) {
        displayLoading(false)
        Toast.makeText(requireContext(), messageError, Toast.LENGTH_LONG).show()

    }

    protected fun onDataReceived(state: DataState<Any>, idApi: String){
        state.let {
            when (it.status) {
                DataState.Status.SUCCESS -> {
                    displayLoading(false)
//                    onSuccess(it.data, idApi)
                }
                DataState.Status.ERROR -> selectError(it.error)
                DataState.Status.LOADING -> displayLoading(true)
            }
        }
    }

    private fun selectError(error: DataState.HttpError?) {
        val messageError = error?.let {
             return@let error.serverMessage ?: error.throwableMessage
        }
        onError(messageError!!)
    }
    protected fun hideSoftKeyboard(activity: FragmentActivity){
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
        activity.currentFocus?.clearFocus()
    }

    fun toastMy(
        message: String,
        success: Boolean = false,
        hideInRelease: Boolean = false
    ) {
        if (hideInRelease) return
        if (success) {
            Toasty.success(
                requireContext(), message, Toasty.LENGTH_SHORT, true
            ).show()
        } else {
            Toasty.error(
                requireContext(), message, Toasty.LENGTH_SHORT, true
            ).show()
        }
    }

    fun hasStoragePermissions() =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            EasyPermissions.hasPermissions(
                requireContext(),
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_MEDIA_LOCATION,
                Manifest.permission.MANAGE_EXTERNAL_STORAGE
            )
        } else {
            EasyPermissions.hasPermissions(
                requireContext(),
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }

    fun requestStoragePermissions() =
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            EasyPermissions.requestPermissions(
                this,
                "Our App Requires a permission to access your storage.",
                Constants.REQUEST_CODE_READ_STORAGE_PERMISSION,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_MEDIA_LOCATION,
                Manifest.permission.MANAGE_EXTERNAL_STORAGE
            )
        }else{
            EasyPermissions.requestPermissions(
                this,
                "Our App Requires a permission to access your storage.",
                Constants.REQUEST_CODE_READ_STORAGE_PERMISSION,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    fun openPhotoPicker(){
        if(hasStoragePermissions()) {

            //Creating an intent object and setting its type and action, then passing it as an input argument along with
            //the PICK_IMAGE_REQUEST code previously declared to the startActivityForResult


            //Creating an intent object and setting its type and action, then passing it as an input argument along with
            //the PICK_IMAGE_REQUEST code previously declared to the startActivityForResult
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)

            intent.addCategory(Intent.CATEGORY_OPENABLE)

            //setType to image/* so that only images will show up

            //setType to image/* so that only images will show up
            intent.type = "image/*"

            intent.action = Intent.ACTION_GET_CONTENT

            startActivityForResult(intent, REQUEST_PICK_IMAGE)
        }else{
            requestStoragePermissions()
        }
    }

    protected fun openStorage() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_MEDIA_LOCATION
                ),
                Constants.REQUEST_CODE_PERMISSION
            )
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                openStorageWithAccessPermission()
            } else {
                openStorageWithoutPermission()
            }
        }else {
            openStorageWithoutPermission()
        }
    }

    private fun openStorageWithAccessPermission() {
        val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        val uri: Uri = Uri.fromParts("package", requireActivity().packageName, null)
        intent.data = uri
        try {
            startActivityForResult(intent, Constants.REQUEST_FILE_PICK)
        } catch (e: ActivityNotFoundException) {
            openStorageWithoutPermission()
        }
    }
    private fun openStorageWithoutPermission() {
        val intent = Intent()
        intent.type = "image/*|application/pdf"
        val mimeTypes = arrayOf("image/*", "application/pdf")
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        intent.action = Intent.ACTION_GET_CONTENT
        try {
            startActivityForResult(intent, Constants.REQUEST_FILE_PICK)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
    }

    protected fun openGallery() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.MANAGE_EXTERNAL_STORAGE),
                Constants.REQUEST_CODE_PERMISSION
            )
            return
        }
        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
        intent.action = Intent.ACTION_GET_CONTENT
        try {
            startActivityForResult(intent, Constants.REQUEST_IMAGE_PICK)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
    }
}