package com.example.chatapp.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.chatapp.R
import com.example.chatapp.core.BaseFragment
import com.example.chatapp.databinding.FragmentLoginBinding
import com.example.chatapp.domain.base.DataState
import com.example.chatapp.domain.model.country_code.CountryCode
import com.example.chatapp.utils.Constants.COUNTRY_CODE_REQUEST
import com.example.chatapp.utils.Constants.COUNTRY_CODE_SELECTED
import com.example.chatapp.utils.autoCleared
import com.example.chatapp.utils.extension.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment() {

    private var binding: FragmentLoginBinding by autoCleared()

    private val viewModel: LoginViewModel by activityViewModels()

    private var navController: NavController? = null

    private var countryCodeSelected: CountryCode? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        binding.apply {
            viewModel = this@LoginFragment.viewModel
        }


        binding.tvContinue.setOnClickListener {
            val phoneNumber = binding.etPhoneNumber.text.toString().trim()
            viewModel.login(requireActivity(), phoneNumber)
        }

        binding.etCountry.setOnClickListener {
            navController?.navigate(
                R.id.action_loginFragment_to_countriesCodeFragment, bundleOf(
                    COUNTRY_CODE_SELECTED to countryCodeSelected
                )
            )
        }

        setFragmentResultListener(COUNTRY_CODE_REQUEST) { key, bundle ->
            if (key == COUNTRY_CODE_REQUEST) {
                hideKeyboard()
                countryCodeSelected = bundle.getParcelable<CountryCode>(COUNTRY_CODE_SELECTED)

                countryCodeSelected?.run {
                    viewModel.setCountryCode("+$callingCode")
                    binding.etCountry.setText(name)
                }
            }
        }

        observeLiveData()

    }

    override fun observeLiveData() {
        viewModel.errorValidationResId.observe(viewLifecycleOwner){ resId ->
            resId?.let{ toastMy(getString(resId)) }
        }
        viewModel.documentSnapshot.observe(viewLifecycleOwner){ dataState ->
            when(dataState.status){
                DataState.Status.SUCCESS -> {
                    displayLoading(false)
                    dataState.data?.let { documnet ->
                        hideKeyboard()
                        if(documnet.exists()) {
                            toastMy(getString(R.string.come_back), true)
                            navController?.navigate(R.id.action_loginFragment_to_chatsFragment)
                        }
                        else {
                            toastMy(getString(R.string.code_sent), true)
                            navController?.navigate(R.id.action_loginFragment_to_OTPValidationFragment)
                        }
                    }
                }
                DataState.Status.ERROR -> {
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
            navController?.navigate(R.id.action_loginFragment_to_OTPValidationFragment)
        }
    }

}