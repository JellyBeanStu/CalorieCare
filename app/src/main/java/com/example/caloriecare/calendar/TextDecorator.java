package com.example.caloriecare.calendar;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Collection;
import java.util.HashSet;

public class TextDecorator implements DayViewDecorator {

    private final HashSet<CalendarDay> dates;
    String text;
    boolean type;

    public TextDecorator(float text, boolean type, Collection<CalendarDay> dates) {

        this.text = Integer.toString(Math.round(text));
        this.type = type;
        this.dates = new HashSet<>(dates);

    }
    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new AddTextToDate(text,type));
    }
}