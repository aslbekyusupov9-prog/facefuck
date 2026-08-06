package com.aifacerating.app.utils;

import android.graphics.Bitmap;
import android.net.Uri;

public class ImageHolder {
    private static ImageHolder instance;
    private Bitmap currentBitmap;
    private Uri currentUri;

    private ImageHolder() {}

    public static ImageHolder getInstance() {
        if (instance == null) {
            instance = new ImageHolder();
        }
        return instance;
    }

    public void setImage(Bitmap bitmap, Uri uri) {
        this.currentBitmap = bitmap;
        this.currentUri = uri;
    }

    public Bitmap getBitmap() { return currentBitmap; }
    public Uri getUri() { return currentUri; }
    
    // Hash function for deterministic seed based on image
    public long getImageHash() {
        if (currentUri != null) {
            return currentUri.toString().hashCode();
        } else if (currentBitmap != null) {
            return currentBitmap.hashCode();
        }
        return System.currentTimeMillis(); // fallback
    }
}
