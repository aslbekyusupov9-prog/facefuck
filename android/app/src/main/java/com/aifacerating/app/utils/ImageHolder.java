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
    
    // Hash function for deterministic seed based on image pixels or URI
    public long getImageHash(android.content.Context context) {
        if (currentBitmap != null) {
            return getPixelHash(currentBitmap);
        } else if (currentUri != null) {
            try {
                Bitmap bitmap = android.provider.MediaStore.Images.Media.getBitmap(context.getContentResolver(), currentUri);
                return getPixelHash(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
                return currentUri.toString().hashCode();
            }
        }
        return System.currentTimeMillis(); // fallback
    }

    private long getPixelHash(Bitmap bmp) {
        long hash = 0;
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        
        // Sample about 10 pixels from the center cross to get a unique but stable hash
        int cx = width / 2;
        int cy = height / 2;
        int stepX = Math.max(1, width / 10);
        int stepY = Math.max(1, height / 10);
        
        for (int i = 0; i < 5; i++) {
            if (cx - (i*stepX) > 0) hash += bmp.getPixel(cx - (i*stepX), cy);
            if (cx + (i*stepX) < width) hash += bmp.getPixel(cx + (i*stepX), cy);
            if (cy - (i*stepY) > 0) hash += bmp.getPixel(cx, cy - (i*stepY));
            if (cy + (i*stepY) < height) hash += bmp.getPixel(cx, cy + (i*stepY));
        }
        return hash;
    }
}
