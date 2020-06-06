package com.example.criminalintent.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.navigation.navGraphViewModels
import com.example.criminalintent.CrimeDetailViewModel
import com.example.criminalintent.R
import com.example.criminalintent.utils.PictureUtil
import kotlinx.android.synthetic.main.fragment_photo_detail.view.image_view_photo

class PhotoDetailFragment : DialogFragment() {

    private val viewModel: CrimeDetailViewModel by navGraphViewModels(R.id.crime_navigation_graph)
    private lateinit var photoPath: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_photo_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.crimeLiveData.observe(
            viewLifecycleOwner,
            Observer { crime->
                crime?.let {
                    photoPath = viewModel.getPhotoFile(it).path
                    if (checkPhotoPathValidity()){
                        updatePhotoView()
                    }
                }
            }
        )
    }

    private fun checkPhotoPathValidity():Boolean{
        return if (photoPath == "") {
            Toast.makeText(context, R.string.text_warning_photo_path_miss, Toast.LENGTH_SHORT)
                .show()
            dismiss()
            false
        }else true
    }

    private fun updatePhotoView(){
        requireView().image_view_photo.setImageBitmap(
            PictureUtil.getScaledBitmap(photoPath, requireActivity())
        )
    }
}