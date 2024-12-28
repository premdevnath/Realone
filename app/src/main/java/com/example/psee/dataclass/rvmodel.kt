package com.example.psee.dataclass
import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

//chngep
@Parcelize
data class rvmodel(
    val Name: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val location: String = "",
    val price: String = "",
    val ownerId: String =""
) : Parcelable {
    // No-argument constructor is implicitly created with default values
}