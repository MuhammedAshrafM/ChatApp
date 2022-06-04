package com.example.chatapp.ui.onboarding

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.databinding.RowItemSliderBinding
import com.example.chatapp.domain.model.SliderItem

class SliderAdapter(var context: Context) :
    RecyclerView.Adapter<SliderAdapter.SliderViewHolder>() {

    private var items: List<SliderItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val binding =
            RowItemSliderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SliderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount() = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(items: List<SliderItem>) {
        this.items = items
        notifyDataSetChanged()
    }

    inner class SliderViewHolder(
        private val itemBinding: RowItemSliderBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(item: SliderItem) {
            item.apply {
                itemBinding.tvHeader.text = context.getString(headerResId)
                itemBinding.animationView.setAnimation(animationRawId)
            }
        }
    }

}