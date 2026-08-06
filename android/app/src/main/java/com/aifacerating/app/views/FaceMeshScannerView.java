package com.aifacerating.app.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class FaceMeshScannerView extends View {
    private Paint pointPaint;
    private Paint linePaint;
    private Paint scanLinePaint;
    private List<Point> meshPoints;
    private int scanLineY = 0;
    private boolean isScanningDown = true;

    private static class Point {
        float x, y;
        float alpha;
        Point(float x, float y) { this.x = x; this.y = y; this.alpha = 1f; }
    }

    public FaceMeshScannerView(Context context) {
        super(context);
        init();
    }

    public FaceMeshScannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        pointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        pointPaint.setColor(Color.WHITE);
        pointPaint.setStyle(Paint.Style.FILL);

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(Color.parseColor("#80FFFFFF"));
        linePaint.setStrokeWidth(2f);

        scanLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        scanLinePaint.setColor(Color.parseColor("#E63946")); // Accent color
        scanLinePaint.setStrokeWidth(8f);
        
        meshPoints = new ArrayList<>();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        generateFaceMesh(w, h);
    }

    private void generateFaceMesh(int w, int h) {
        meshPoints.clear();
        Random r = new Random();
        int cx = w / 2;
        int cy = h / 2;
        
        // Generate random symmetrical-looking points around center
        for(int i = 0; i < 20; i++) {
            float dx = 20 + r.nextInt(cx - 40);
            float dy = -cy + 40 + r.nextInt(h - 80);
            
            // Add point on right and its mirror on left
            meshPoints.add(new Point(cx + dx, cy + dy));
            meshPoints.add(new Point(cx - dx, cy + dy));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int w = getWidth();
        int h = getHeight();
        if (w == 0 || h == 0) return;

        // Draw connections
        for (int i = 0; i < meshPoints.size(); i++) {
            Point p1 = meshPoints.get(i);
            // Draw points
            pointPaint.setAlpha((int) (255 * (0.5f + 0.5f * Math.sin(System.currentTimeMillis() / 200.0 + p1.x))));
            canvas.drawCircle(p1.x, p1.y, 6f, pointPaint);
            
            // Connect to next 2 points
            for(int j = i + 1; j < Math.min(i + 3, meshPoints.size()); j++) {
                Point p2 = meshPoints.get(j);
                if(Math.abs(p1.x - p2.x) < w/2f && Math.abs(p1.y - p2.y) < h/2f) { // Only connect nearby points
                   canvas.drawLine(p1.x, p1.y, p2.x, p2.y, linePaint);
                }
            }
        }

        // Draw scanner line moving up and down
        if (isScanningDown) {
            scanLineY += 15;
            if (scanLineY >= h) isScanningDown = false;
        } else {
            scanLineY -= 15;
            if (scanLineY <= 0) isScanningDown = true;
        }

        canvas.drawLine(0, scanLineY, w, scanLineY, scanLinePaint);
        
        // Draw glow area around scanline
        Paint glow = new Paint();
        glow.setColor(Color.parseColor("#33E63946"));
        canvas.drawRect(0, scanLineY - 40, w, scanLineY, glow);

        // Keep redrawing for animation
        postInvalidateDelayed(16); // ~60 FPS
    }
}
