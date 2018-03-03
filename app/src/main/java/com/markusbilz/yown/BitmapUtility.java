package com.markusbilz.yown;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;


class BitmapUtility {

    public static byte[] bitmap2Byte(Bitmap bitmap) {
        if (bitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            return stream.toByteArray();
        } else {
            return new byte[0];
        }
    }

    public static Bitmap byte2Bitmap(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

}
