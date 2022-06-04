package com.example.chatapp.ui.splash

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.chatapp.R
import com.example.chatapp.databinding.FragmentSplashBinding
import com.example.chatapp.utils.autoCleared
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : Fragment() {

    private var binding: FragmentSplashBinding by autoCleared()

    private val viewModel: SplashViewModel by viewModels()

    private var navController: NavController? = null

    private var isFirstTime = true
    private var isLogged = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        setAnimation()
    }

    private fun setAnimation(){

        val animationAppLogo = AnimationUtils.loadAnimation(context, R.anim.splash_logo)
        binding.ivLogo.startAnimation(animationAppLogo)
        val animationAppName = AnimationUtils.loadAnimation(context, R.anim.splash_text)
        binding.tvAppName.startAnimation(animationAppName)

        animationAppLogo.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                navigateToNextScreen()
            }
            override fun onAnimationRepeat(animation: Animation) {}
        })

        try {
            lifecycleScope.launchWhenStarted {
                viewModel.isFirstTimeSF.collect { isFirstTime ->
                    this@SplashFragment.isFirstTime = isFirstTime
                    Log.d("TAG", "setAnimation: $isFirstTime")
                }
                viewModel.isLoggedSF.collect { isLogged ->
                    this@SplashFragment.isLogged = isLogged
                }
            }
        } catch (e: Exception) {
            Log.d("TAG", ": $e")
        }
    }

    private fun navigateToNextScreen(){
        val idAction = when {
            isFirstTime -> R.id.action_splashFragment_to_onBoardingFragment
            isLogged -> R.id.action_splashFragment_to_loginFragment
            else -> R.id.action_splashFragment_to_loginFragment
        }
        navController?.navigate(idAction)
    }

}