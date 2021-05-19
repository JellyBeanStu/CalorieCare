package com.example.caloriecare.calendar;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.style.LineBackgroundSpan;

import static java.lang.Boolean.*;

public final class AddTextToDate implements LineBackgroundSpan {
    private String dayPrice;
    private String type;

    public void drawBackground(Canvas canvas, Paint paint, int left, int right, int top, int baseline, int bottom, CharSequence text, int start, int end, int lnum) {

        if (this.type == "EXERCISE") {
            paint.setColor(Color.parseColor("#eb2f06"));
            paint.setFakeBoldText(TRUE);
            canvas.drawText(this.dayPrice, (float)((left + right) / 4), (float)(bottom + 33), paint);
        } else if(this.type == "DIET"){
            paint.setColor(Color.parseColor("#1e3799"));
            paint.setFakeBoldText(TRUE);
            canvas.drawText(this.dayPrice, (float)((left + right) / 4), (float)(bottom + 68), paint);
        }
        paint.setColor(Color.BLACK);
        paint.setFakeBoldText(FALSE);
    }

    public AddTextToDate(String text, String type) {
        this.dayPrice = text;
        this.type = type;
    }
}
