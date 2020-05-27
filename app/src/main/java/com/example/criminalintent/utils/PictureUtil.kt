package com.example.criminalintent.utils

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import kotlin.math.max
import kotlin.math.roundToInt

object PictureUtil {

    fun getScaledBitmap(path: String, destWidth: Int, destHeight: Int): Bitmap {
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

    fun getScaledBitmap(path: String, activity: Activity): Bitmap {
        val size = Point()
        // The method #getDefaultPlay() will be deprecated from Android 11
        // replace by Context#getDisplay()
        activity.windowManager.defaultDisplay.getSize(size)

        return getScaledBitmap(path, size.x, size.y)
    }
}