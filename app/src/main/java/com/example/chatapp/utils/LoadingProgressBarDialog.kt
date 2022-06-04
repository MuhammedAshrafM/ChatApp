package com.example.chatapp.utils

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import com.example.chatapp.R
import com.example.chatapp.databinding.ProgressBarBinding


class LoadingProgressBarDialog private constructor(context: Context, themeResId: Int) :
    Dialog(context, themeResId) {

    private var _binding: ProgressBarBinding? = null
    private val binding get() = _binding!!

    companion object {
        private lateinit var loadingProgressBarDialog: LoadingProgressBarDialog

        @SuppressLint("ResourceAsColor")
        public fun getInstance(context: Context): LoadingProgressBarDialog {
            loadingProgressBarDialog =
                LoadingProgressBarDialog(context, R.style.MaterialDialogSheet)
            loadingProgressBarDialog.setCancelable(false)
            loadingProgressBarDialog.window?.setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            loadingProgressBarDialog.window?.setGravity(Gravity.CENTER)
            loadingProgressBarDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
            loadingProgressBarDialog.setCanceledOnTouchOutside(false)
            loadingProgressBarDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            return loadingProgressBarDialog
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = layoutInflater.inflate(R.layout.progress_bar, null)
        _binding = ProgressBarBinding.bind(view)
        setContentView(view)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        _binding = null
    }
}