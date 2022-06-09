package com.example.chatapp.ui.otp

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.chatapp.R
import com.example.chatapp.core.BaseFragment
import com.example.chatapp.databinding.FragmentLoginBinding
import com.example.chatapp.databinding.FragmentOtpValidationBinding
import com.example.chatapp.domain.base.DataState
import com.example.chatapp.ui.login.LoginViewModel
import com.example.chatapp.utils.Constants.OTP_CODE_COUNT
import com.example.chatapp.utils.autoCleared
import com.example.chatapp.utils.extension.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty

@AndroidEntryPoint
class OTPValidationFragment : BaseFragment() {

    private var binding: FragmentOtpValidationBinding by autoCleared()

    private val viewModel: LoginViewModel by activityViewModels()

    private var navController: NavController? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOtpValidationBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        binding.apply {
            viewModel = this@OTPValidationFragment.viewModel
        }

        binding.otpView.addTextChangedListener {
            val inputCompleted = binding.otpView.text?.toString()?.length == OTP_CODE_COUNT
            if(inputCompleted)
                viewModel.verifyOTPCode(requireActivity(), binding.otpView.text.toString())
        }

        binding.tvVerify.setOnClickListener {
            val length = binding.otpView.text?.toString()?.length ?: 0
            if(length < OTP_CODE_COUNT){
                toastMy(getString(R.string.enter_full_otp_code))
            }else{
                viewModel.verifyOTPCode(requireActivity(), binding.otpView.text.toString())
            }
        }

        binding.tvResendOtp.setOnClickListener {
            binding.otpView.setText("")
            viewModel.resendOTP(requireActivity())
        }

        observeLiveData()
    }

    override fun observeLiveData() {
        viewModel.documentSnapshot.observe(viewLifecycleOwner){ dataState ->
            when(dataState.status){
                DataState.Status.SUCCESS -> {
                    displayLoading(false)
                    dataState.data?.let { documnet ->
                        hideKeyboard()
                        if(documnet.exists()) {
                            toastMy(getString(R.string.come_back), true)
                            viewModel.saveUID(documnet.id)
                            viewModel.setUserAsLogged()
                        }
                        else {
                            toastMy(getString(R.string.welcome_chat_app), true)
                            val phoneNumber = viewModel.countryCode.value + viewModel.phoneNumber.value
                            val action =
                                OTPValidationFragmentDirections.actionOTPValidationFragmentToAddNewUserInfoFragment(
                                    documnet.id,
                                    phoneNumber
                                )
                            navController?.navigate(action)
                        }
                    }
                }
                DataState.Status.ERROR ->{
                    displayLoading(false)
                    dataState.error?.throwableMessage?.let { toastMy(it) }
                }
                DataState.Status.LOADING -> displayLoading(true)
            }
        }

        viewModel.verificationCodeInfo.observe(viewLifecycleOwner){
            hideKeyboard()
            displayLoading(false)
            toastMy(getString(R.string.code_sent), true)
        }

        viewModel.isLoggedSF.observe(viewLifecycleOwner){isLogged ->
            if(isLogged)
                navController?.navigate(R.id.action_OTPValidationFragment_to_chatsFragment)
        }
    }

}