package com.aifacerating.app.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.net.Uri;
import android.view.View;
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;
import java.io.File;
import java.io.FileOutputStream;
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

    /**
     * Saves bitmap permanently to App's Internal Storage directory so system cache cleanups don't delete images.
     */
    public static String saveToInternalStorage(Context context, Bitmap bitmap, String filename) {
        if (context == null || bitmap == null) return null;
        try {
            File dir = new File(context.getFilesDir(), "app_images");
            if (!dir.exists()) dir.mkdirs();

            File imageFile = new File(dir, filename + ".jpg");
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.flush();
            fos.close();

            return Uri.fromFile(imageFile).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Renders any View (Result Card) into a high-res Bitmap and triggers Intent.ACTION_SEND to share via Instagram/Telegram Story.
     */
    public static void shareViewToSocial(Context context, View view, String shareTitle) {
        if (context == null || view == null) return;
        try {
            Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            view.draw(canvas);

            File cachePath = new File(context.getCacheDir(), "images");
            if (!cachePath.exists()) cachePath.mkdirs();

            File streamFile = new File(cachePath, "share_result_" + System.currentTimeMillis() + ".png");
            FileOutputStream stream = new FileOutputStream(streamFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.flush();
            stream.close();

            Uri contentUri = FileProvider.getUriForFile(context, "com.aifacerating.app.provider", streamFile);

            if (contentUri != null) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                shareIntent.setDataAndType(contentUri, context.getContentResolver().getType(contentUri));
                shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareTitle);
                context.startActivity(Intent.createChooser(shareIntent, "Natijani Ulashish (Instagram / Telegram)"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
