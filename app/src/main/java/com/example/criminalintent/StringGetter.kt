package com.example.criminalintent

import android.content.Context
import android.content.res.Resources
import androidx.annotation.StringRes

object StringGetter {

    private lateinit var resources: Resources

    fun initialize(context: Context) {
        resources = context.resources
    }

    fun getString(@StringRes id: Int) = resources.getString(id)

    fun getStringWithArgs(@StringRes id: Int, vararg formatArgs: Any): String {
        return resources.getString(id, *formatArgs)
    }
}