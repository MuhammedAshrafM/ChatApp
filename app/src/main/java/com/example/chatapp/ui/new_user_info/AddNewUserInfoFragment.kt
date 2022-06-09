package com.example.chatapp.ui.new_user_info

import android.R.attr
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.example.chatapp.R
import com.example.chatapp.core.BaseFragment
import com.example.chatapp.databinding.FragmentAddNewUserInfoBinding
import com.example.chatapp.databinding.FragmentLoginBinding
import com.example.chatapp.utils.Constants.REQUEST_PICK_IMAGE
import com.example.chatapp.utils.autoCleared
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions


class AddNewUserInfoFragment : BaseFragment(), EasyPermissions.PermissionCallbacks {

    private var binding: FragmentAddNewUserInfoBinding by autoCleared()

    private lateinit var viewModel: AddNewUserInfoViewModel

    private var navController: NavController? = null

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

    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AddNewUserInfoViewModel::class.java)

        openPhotoPicker()
    }

    private fun requestPermissions(){
        if(hasStoragePermissions()) {
            return
        }
        requestStoragePermissions()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //if the user actually chose an image
        //if the user actually chose an image
        if (requestCode == REQUEST_PICK_IMAGE && resultCode == RESULT_OK && data != null) {

            //chosenPhotoUri is the Uri of the image the user has picked
            val chosenPhotoUri: Uri? = data.data



            //display the chosen photo in the image view
//            Glide.with(this).load(chosenPhotoUri).into<Target<Drawable>>(imageView)
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
        if (requestCode == REQUEST_PICK_IMAGE)
            openPhotoPicker()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this, perms))
            AppSettingsDialog.Builder(this).build().show()
        else
            requestPermissions()
    }

}