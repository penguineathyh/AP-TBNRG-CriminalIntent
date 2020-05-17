package com.example.criminalintent

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

fun FragmentManager.add(
    @IdRes containerId: Int,
    fragmentConstructor: () -> Fragment,
    tag: String? = null
): Fragment {

    var fragment = this.findFragmentById(containerId)
    return if (fragment != null) fragment
    else {
        fragment = fragmentConstructor.invoke()
        this.beginTransaction()
            .add(containerId, fragment, tag)
            .commit()
        fragment
    }
}

fun FragmentManager.replace(
    @IdRes containerId: Int,
    newFragment: Fragment,
    tag: String? = null
) {

    this.beginTransaction()
        .replace(containerId, newFragment, tag)
        .addToBackStack(null)
        .commit()

}