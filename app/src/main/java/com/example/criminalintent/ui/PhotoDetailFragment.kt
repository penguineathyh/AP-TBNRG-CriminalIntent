package com.example.criminalintent.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.criminalintent.R
import com.example.criminalintent.utils.PictureUtil
import kotlinx.android.synthetic.main.fragment_photo_detail.view.image_view_photo

class PhotoDetailFragment : DialogFragment() {

    private lateinit var photoPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        photoPath = requireArguments().getString(ARG_PHOTO_PATH, "") as String
        if (photoPath == "") {
            Toast.makeText(context, R.string.text_warning_photo_path_miss, Toast.LENGTH_SHORT)
                .show()
            dismiss()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_photo_detail, container, false)
    }

    override fun onStart() {
        super.onStart()
        requireView().image_view_photo.setImageBitmap(
            PictureUtil.getScaledBitmap(photoPath, requireActivity())
        )
    }

    companion object {

        private const val TAG = "photo.detail.dialog"
        private const val ARG_PHOTO_PATH = "photo.path"

        fun showPhotoDetail(
            fragmentManager: FragmentManager,
            path: String
        ): PhotoDetailFragment {
            val fragment = PhotoDetailFragment()
            val bundle = Bundle().apply {
                putString(ARG_PHOTO_PATH, path)
            }.also {
                fragment.arguments = it
            }
            fragment.show(fragmentManager, TAG)
            return fragment
        }
    }
}