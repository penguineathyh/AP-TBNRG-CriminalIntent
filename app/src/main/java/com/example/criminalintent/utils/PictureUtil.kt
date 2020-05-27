package com.example.criminalintent.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlin.math.max
import kotlin.math.roundToInt

object PictureUtil {

    fun getScaleBitmap(path: String, destWidth: Int, destHeight: Int): Bitmap {
        // Read dimensions of the image on disk
        var options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, options)

        val srcWidth = options.outWidth.toFloat()
        val srcHeight = options.outHeight.toFloat()

        // Figure out how much to scale down by
        var inSampleSize = 1
        if (srcHeight > destHeight || srcWidth > destWidth) {
            val sampleScale = max(srcHeight / destHeight, srcWidth / destWidth)
            inSampleSize = sampleScale.roundToInt()
        }

        options = BitmapFactory.Options()
        options.inSampleSize = inSampleSize

        // create final bitmap
        return BitmapFactory.decodeFile(path, options)
    }
}