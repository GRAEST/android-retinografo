package br.com.graest.retinografo.utils

import android.graphics.Bitmap
import android.graphics.Color

object BitmapUtils {
    fun removeGreen(bitmap: Bitmap): Bitmap {
        // Create a new bitmap with the same dimensions
        val resultBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)

        // Copy the original bitmap to the result bitmap, retaining only the red and blue components
        for (x in 0 until bitmap.width) {
            for (y in 0 until bitmap.height) {
                val color = bitmap.getPixel(x, y)
                val red = color shr 16 and 0xFF // Extract red component
                val blue = color and 0xFF // Extract blue component

                // Combine red and blue components into a new color
                val newColor = red shl 16 or blue

                // Set the new color to the result bitmap
                resultBitmap.setPixel(x, y, newColor)
            }
        }

        return resultBitmap
    }
}