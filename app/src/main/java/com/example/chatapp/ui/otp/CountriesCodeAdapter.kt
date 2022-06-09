package com.example.chatapp.ui.otp

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.R
import com.example.chatapp.databinding.RowItemCountryCodeBinding
import com.example.chatapp.domain.model.country_code.CountryCode

class CountriesCodeAdapter(
    private val context: Context,
    private val countryCodeSelected: CountryCode?,
    private val onItemClick: (CountryCode?) -> Unit
) :
    ListAdapter<CountryCode, CountriesCodeAdapter.CountriesCodeViewHolder>(Companion) {

    companion object : DiffUtil.ItemCallback<CountryCode>() {
        override fun areItemsTheSame(oldItem: CountryCode, newItem: CountryCode): Boolean {
            return oldItem.name === newItem.name
        }

        override fun areContentsTheSame(oldItem: CountryCode, newItem: CountryCode): Boolean {
            return oldItem.name == newItem.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountriesCodeViewHolder {
        val binding =
            RowItemCountryCodeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CountriesCodeViewHolder(binding) {
            onItemClick(currentList[it])
        }
    }

    override fun onBindViewHolder(holder: CountriesCodeViewHolder, position: Int) {
        val item = currentList[position]
        item?.let {
            holder.bind(it)
        }
    }

    inner class CountriesCodeViewHolder(
        private val itemBinding: RowItemCountryCodeBinding,
        onItemClick: (Int) -> Unit
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        init {
            itemBinding.layoutContainer.setOnClickListener {
                onItemClick(adapterPosition)
            }
        }

        fun bind(item: CountryCode) {
            item.apply {
                itemBinding.tvCountryName.text = name
                itemBinding.tvCountryDialCode.text = "+$callingCode"

                val selected = countryCodeSelected?.code == code
                itemBinding.selected = selected

                val textColorId = if (selected)
                    R.color.caribbean_green2
                else
                    R.color.charcoal

                itemBinding.tvCountryName.setTextColor(context.getColor(textColorId))
            }
        }
    }

}