package com.example.chatapp.domain.model

import com.example.chatapp.R

data class SliderItem(
    val animationRawId: Int,
    val headerResId: Int
)

class SliderItems{

    val items: List<SliderItem>

    init {
        items = mutableListOf(
            SliderItem(R.raw.chatting, R.string.on_boarding_text1),
            SliderItem(R.raw.dating_app_online_chat, R.string.on_boarding_text2),
            SliderItem(R.raw.chat_concept, R.string.on_boarding_text3),
            SliderItem(R.raw.icon_chat, R.string.on_boarding_text4)
        )
    }
}

//smile SliderItem(R.raw.blinkchat, R.string.on_boarding_text1),
//ignore SliderItem(R.raw.chat, R.string.on_boarding_text2),
//typing SliderItem(R.raw.chat_bubble, R.string.on_boarding_text3),
//typing SliderItem(R.raw.chat_bubbles, R.string.on_boarding_text1),
//typing SliderItem(R.raw.chat_loader, R.string.on_boarding_text3),
//ignore SliderItem(R.raw.chatting_animation, R.string.on_boarding_text3),
//typing SliderItem(R.raw.chit_chatting_rounded_revised, R.string.on_boarding_text3),
//ignore SliderItem(R.raw.dating_app_animation, R.string.on_boarding_text3),
//            SliderItem(R.raw.girl_on_floor, R.string.on_boarding_text3),
//typing SliderItem(R.raw.loading_chat, R.string.on_boarding_text3),
//send arrow SliderItem(R.raw.message_send, R.string.on_boarding_text3),
//ignore SliderItem(R.raw.online_chat, R.string.on_boarding_text3)
//            SliderItem(R.raw.social_media_girl, R.string.on_boarding_text3)
//typing SliderItem(R.raw.typing_in_chat, R.string.on_boarding_text3)