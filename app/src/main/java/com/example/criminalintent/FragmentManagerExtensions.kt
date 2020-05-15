package com.example.criminalintent

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

fun FragmentManager.add(@IdRes containerId: Int, fragment: Fragment, tag: String) {

    if (this.findFragmentById(containerId) != null) return
    else {
        this.beginTransaction()
            .add(containerId, fragment, tag)
            .commit()
    }
}