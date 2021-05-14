package com.example.caloriecare.calendar;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.style.LineBackgroundSpan;

public final class AddTextToDate implements LineBackgroundSpan {
    private String dayPrice;
    private boolean type;

    public void drawBackground(Canvas canvas, Paint paint, int left, int right, int top, int baseline, int bottom, CharSequence text, int start, int end, int lnum) {

        if (this.type) {
            paint.setColor(Color.RED);
            canvas.drawText(this.dayPrice, (float)((left + right) / 4), (float)(bottom + 33), paint);
        } else {
            paint.setColor(Color.BLUE);
            canvas.drawText(this.dayPrice, (float)((left + right) / 4), (float)(bottom + 68), paint);
        }
        paint.setColor(Color.BLACK);
    }

    public AddTextToDate(String text, boolean type) {
        this.dayPrice = text;
        this.type = type;
    }
}
