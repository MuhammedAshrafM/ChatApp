package com.example.chatapp.ui.onboarding

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.chatapp.R
import com.example.chatapp.databinding.FragmentOnboardingBinding
import com.example.chatapp.utils.Constants.MAX_ONBOARDING_PAGES
import com.example.chatapp.utils.autoCleared
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class OnBoardingFragment : Fragment(), View.OnClickListener{

    private var binding: FragmentOnboardingBinding by autoCleared()

    private val viewModel: OnBoardingViewModel by viewModels()

    @Inject
    lateinit var sliderAdapter: SliderAdapter

    private var navController: NavController? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        setupSliderAdapter()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.sliderItemsSF.collect {
                sliderAdapter.submitList(it.items)
            }
        }

        viewModel.isFirstTimeSF.observe(viewLifecycleOwner){ isFirstTime->
            if (!isFirstTime)
                navController?.navigate(R.id.action_onBoardingFragment_to_loginFragment)
        }

        binding.layoutNext.setOnClickListener(this)
        binding.tvSkip.setOnClickListener(this)
    }

    private fun setupSliderAdapter(){

        binding.viewPager.adapter = sliderAdapter

        binding.dotsIndicator.setViewPager2(binding.viewPager)

        val pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.lastPage = position+1 == MAX_ONBOARDING_PAGES
            }
        }

        binding.viewPager.registerOnPageChangeCallback(pageChangeCallback)

    }

    private fun navigateToLoginScreen() {
        viewModel.disableFirstTime()
//
    }

    private fun nextPageOrLoginScreen() {
        val lastPage = binding.lastPage ?: false
        if (lastPage)
            navigateToLoginScreen()
        else
            binding.viewPager.currentItem = binding.viewPager.currentItem + 1
    }
    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.layout_next -> nextPageOrLoginScreen()
            R.id.tv_skip -> navigateToLoginScreen()
        }
    }

}