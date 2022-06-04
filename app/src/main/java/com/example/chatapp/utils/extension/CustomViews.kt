package com.example.chatapp.utils.extension

import android.util.Log
import androidx.appcompat.widget.SearchView


inline fun SearchView.onQueryTextChanged(crossinline listener: (String) -> Unit) {
    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            clearFocus()
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            Log.d("TAG", "onCreateOptionsMenu: $newText")
            listener(newText.orEmpty()) // return empty string is the text is null
            return true
        }
    })
}