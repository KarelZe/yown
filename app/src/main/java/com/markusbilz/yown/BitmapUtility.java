package com.markusbilz.yown;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.ByteArrayOutputStream;


class BitmapUtility {
    /**
     * Function converts image to byte array without compressing the image.
     *
     * @param bitmap Bitmap or null reference
     * @return Byte array with image data or empty Byte Array.
     */
    static byte[] bitmapToByte(@Nullable Bitmap bitmap) {
        if (bitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            return stream.toByteArray();
        } else {
            return new byte[0];
        }
    }

    /**
     * Function converts byte array to Bitmap file.
     *
     * @param image Byte array for conversion.
     * @return Bitmap file
     */
    static Bitmap byteToBitmap(@NonNull byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

}
