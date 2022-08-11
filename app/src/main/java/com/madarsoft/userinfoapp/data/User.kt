package com.madarsoft.userinfoapp.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "user_table")
@Parcelize
data class User(
    val name: String,
    val age: String,
    val jobTitle: String,
    val gender: String,

    @PrimaryKey(autoGenerate = true) val id: Int = 0
) : Parcelable{
}

