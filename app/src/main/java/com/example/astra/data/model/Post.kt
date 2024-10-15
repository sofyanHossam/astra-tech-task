package com.example.astra.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Post(
    val id: Int,
    val post_title: String,
    val post_message: String,
    val post_image: String
) : Parcelable