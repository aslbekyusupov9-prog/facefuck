package com.aifacerating.app.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.net.Uri;
import androidx.exifinterface.media.ExifInterface;
import java.io.InputStream;

public class ImageUtils {

    /**
     * Reads EXIF orientation data from Uri and rotates/flips Bitmap to correct upright position.
     */
    public static Bitmap getCorrectlyOrientedBitmap(Context context, Uri uri) {
        try {
            InputStream is = context.getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            if (is != null) is.close();

            InputStream exifStream = context.getContentResolver().openInputStream(uri);
            if (exifStream == null) return bitmap;

            ExifInterface exif = new ExifInterface(exifStream);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            exifStream.close();

            int rotationDegrees = 0;
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotationDegrees = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotationDegrees = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotationDegrees = 270;
                    break;
                default:
                    rotationDegrees = 0;
                    break;
            }

            if (rotationDegrees != 0) {
                Matrix matrix = new Matrix();
                matrix.postRotate(rotationDegrees);
                return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            }

            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Auto-crops Bitmap to focus strictly on the detected face bounding box with padding.
     */
    public static Bitmap cropToFace(Bitmap original, Rect bounds) {
        if (original == null || bounds == null) return original;

        int width = original.getWidth();
        int height = original.getHeight();

        // Add 25% padding around the face bounding box for aesthetic zoom
        int padX = (int) (bounds.width() * 0.25f);
        int padY = (int) (bounds.height() * 0.35f);

        int left = Math.max(0, bounds.left - padX);
        int top = Math.max(0, bounds.top - padY);
        int right = Math.min(width, bounds.right + padX);
        int bottom = Math.min(height, bounds.bottom + padY);

        int cropW = right - left;
        int cropH = bottom - top;

        if (cropW <= 0 || cropH <= 0) return original;

        return Bitmap.createBitmap(original, left, top, cropW, cropH);
    }
}
