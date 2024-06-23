package com.dhruv.expancetracker

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Locale

object Utils{
    fun formatDateToHumanReadableForm(dateInMillie : Long) : String{
        val dateFormatter = SimpleDateFormat("dd/MM/YYYY" , Locale.getDefault())
        return dateFormatter.format(dateInMillie)
    }

    fun formatToDecimalValue(d: Double) : String{
        return String.format("%.2f" , d)
    }
}