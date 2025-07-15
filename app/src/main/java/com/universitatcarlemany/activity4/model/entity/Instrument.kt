package com.universitatcarlemany.activity4.model.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Instrument(
    private val id: Int,
    private val name: String,
    private val description: String,
    private var price: Double,
    private val condition: String,
    private val features: String,
    private val image: String,
    private var units: Int,
) : Parcelable {
    init {
        if (price < 0) throw IllegalArgumentException("Price must be positive")
        if (units < 0) throw IllegalArgumentException("Units must be positive")
        setPrice(price)
    }

    fun getId(): Int = id

    fun getName(): String = name

    fun getDescription(): String = description

    fun getPrice(): Double = price

    fun getCondition(): String = condition

    fun getFeatures(): String = features

    fun getImage(): String = image

    fun getUnits(): Int = units

    private fun setPrice(value: Double) {
        price = Math.round(value * 100.0) / 100.0
    }

    fun addUnits(units: Int) {
        this.units += units
    }

    fun decUnits() {
        this.units--
    }
}
