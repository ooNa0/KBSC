package com.example.mysmartgarden;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Decorator implements DayViewDecorator {

//    Context context = this;

    private final Calendar calendar = Calendar.getInstance();

//    private final Drawable highlightDrawable;
    private CalendarDay date;

    public Decorator() {
//        highlightDrawable = new ColorDrawable(context.getResources().getColor(R.color.red,context.getTheme()));
//            date = CalendarDay.today();
        date = CalendarDay.from(calendar);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        day.copyTo(calendar);
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        return date != null && day.equals(date);
    }

    @Override
    public void decorate(DayViewFacade view) {
        //view.setBackgroundDrawable(highlightDrawable);
    }
}

class SaturdayDecorator implements DayViewDecorator {

    private final Calendar calendar = Calendar.getInstance();

    public SaturdayDecorator() {
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        day.copyTo(calendar);
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        return weekDay == Calendar.SATURDAY;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new ForegroundColorSpan(Color.BLUE));
    }
}

class SundayDecorator implements DayViewDecorator {

    private final Calendar calendar = Calendar.getInstance();
    public SundayDecorator() {
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        day.copyTo(calendar);
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        return weekDay == Calendar.SUNDAY;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new ForegroundColorSpan(Color.RED));
    }
}

class EventDecorator implements DayViewDecorator {

    //private final int color;
    private final HashSet<CalendarDay> dates;

//    public EventDecorator(int color, Collection<CalendarDay> dates) {
//        this.color = color;
//        this.dates = new HashSet<>(dates);
//    }
    public EventDecorator(Collection<CalendarDay> dates) {
        this.dates = new HashSet<>(dates);
    }

//    public EventDecorator(HashSet<CalendarDay> dates) {
//        this.dates = new HashSet<>(dates);
//    }
    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new DotSpan(5, Color.parseColor("#1D872A")));
    }
}

class NoeventDecorator implements DayViewDecorator {

    //private final int color;
    private final HashSet<CalendarDay> dates;

    //    public EventDecorator(int color, Collection<CalendarDay> dates) {
//        this.color = color;
//        this.dates = new HashSet<>(dates);
//    }
    public NoeventDecorator(Collection<CalendarDay> dates) {
        this.dates = new HashSet<>(dates);
    }

    //    public EventDecorator(HashSet<CalendarDay> dates) {
//        this.dates = new HashSet<>(dates);
//    }
    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new DotSpan(5, Color.parseColor("#00ff0000")));
    }
}

class OneDayDecorator implements DayViewDecorator {

    private CalendarDay date;

    public OneDayDecorator() {
        date = CalendarDay.today();
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return date != null && day.equals(date);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new StyleSpan(Typeface.BOLD));
        view.addSpan(new RelativeSizeSpan(1.4f));
        view.addSpan(new ForegroundColorSpan(Color.parseColor("#50A387")));
    }

    public void setDate(Date date) {
        this.date = CalendarDay.from(date);
    }
}