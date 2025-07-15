package com.universitatcarlemany.activity4.model.entity

import Instruments
import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
class Category(
    private val id: Int,
    private val name: String,
    private val description: String,
    private val image: String,
    private var instruments: List<Instrument> = emptyList()
) : Parcelable {

    fun getId(): Int = id

    fun getName(): String = name

    fun getDescription(): String = description

    fun getImage(): String = image

    fun setInstruments(list: List<Instrument>) { this.instruments = list }

    fun getInstruments(): List<Instrument> = instruments
}