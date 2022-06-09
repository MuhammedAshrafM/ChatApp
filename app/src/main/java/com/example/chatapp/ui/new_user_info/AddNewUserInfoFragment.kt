package com.example.chatapp.ui.new_user_info

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.chatapp.R
import com.example.chatapp.core.BaseFragment
import com.example.chatapp.databinding.FragmentAddNewUserInfoBinding
import com.example.chatapp.domain.base.DataState
import com.example.chatapp.domain.model.User
import com.example.chatapp.utils.Constants.REQUEST_CODE_READ_STORAGE_PERMISSION
import com.example.chatapp.utils.Constants.REQUEST_PICK_IMAGE
import com.example.chatapp.utils.autoCleared
import com.example.chatapp.utils.extension.hideKeyboard
import com.example.chatapp.utils.viewRoundedProfilePictureFromStorage
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

@AndroidEntryPoint
class AddNewUserInfoFragment : BaseFragment(), EasyPermissions.PermissionCallbacks,
    View.OnClickListener {

    val args: AddNewUserInfoFragmentArgs by navArgs()

    private var binding: FragmentAddNewUserInfoBinding by autoCleared()

    private val viewModel: AddNewUserInfoViewModel by viewModels()

    private var navController: NavController? = null

    private val uid by lazy {
        args.uid
    }
    private val phoneNumber by lazy {
        args.phoneNumber
    }

    private var downloadUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddNewUserInfoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()


        observeLiveData()
        setListener()

    }

    override fun setListener() {
        binding.ivProfile.setOnClickListener(this)
        binding.ivCamera.setOnClickListener(this)
        binding.tvSave.setOnClickListener(this)

    }

    override fun observeLiveData() {
        viewModel.errorValidationResId.observe(viewLifecycleOwner){ resId ->
            resId?.let{ toastMy(getString(resId)) }
        }

        viewModel.downloadUri.observe(viewLifecycleOwner) { dataState ->
            when (dataState.status) {
                DataState.Status.SUCCESS -> {
                    displayLoading(false)
                    downloadUri = dataState.data
                    binding.ivProfile.viewRoundedProfilePictureFromStorage(requireContext(), downloadUri)
                    toastMy("Profile Photo Uploaded Successfully", true)
                }
                DataState.Status.ERROR -> {
                    displayLoading(false)
                    dataState.error?.throwableMessage?.let { toastMy(it) }
                        ?: dataState.error?.serverMessage?.let { toastMy(it) }
                }
                DataState.Status.LOADING -> displayLoading(true)
            }
        }

        viewModel.resultAdding.observe(viewLifecycleOwner) { dataState ->
            when (dataState.status) {
                DataState.Status.SUCCESS -> {
                    displayLoading(false)

                    viewModel.saveUID(uid)
                    viewModel.setUserAsLogged()
                }
                DataState.Status.ERROR -> {
                    displayLoading(false)
                    dataState.error?.throwableMessage?.let { toastMy(it) }
                        ?: dataState.error?.serverMessage?.let { toastMy(it) }
                }
                DataState.Status.LOADING -> displayLoading(true)
            }
        }

        viewModel.isLoggedSF.observe(viewLifecycleOwner){isLogged ->
            if(isLogged)
                navController?.navigate(R.id.action_addNewUserInfoFragment_to_chatsFragment)
        }

    }


    private fun saveUserInfo() {
        val name = binding.etName.text.toString().trim()
        viewModel.addNewUserInfo(User(uid = uid, name = name, imageUrl = downloadUri.toString(), phoneNumber = phoneNumber))
    }

    private fun requestPermissions() {
        if (hasStoragePermissions())
            return
        requestStoragePermissions()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //if the user actually chose an image
        if (requestCode == REQUEST_PICK_IMAGE && resultCode == RESULT_OK && data != null) {

            //chosenPhotoUri is the Uri of the image the user has picked
            val chosenPhotoUri: Uri? = data.data

            viewModel.uploadProfileImage(chosenPhotoUri, uid)

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        if (requestCode == REQUEST_CODE_READ_STORAGE_PERMISSION)
            openPhotoPicker()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms))
            AppSettingsDialog.Builder(this).build().show()
        else
            requestPermissions()
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.iv_profile, R.id.iv_camera -> {
                hideKeyboard()
                openPhotoPicker()
            }
            R.id.tv_save ->
                saveUserInfo()
        }
    }

}