package ru.unit.barsdiary.components.bottomnavigationbar;

import android.graphics.RectF;
import android.graphics.drawable.Drawable;

public class BottomBarItem {
    RectF rect;
    String title;
    Drawable icon;
    int color;
    int alpha;

    boolean notification;

    public BottomBarItem(String title, Drawable icon, int color) {
        this.title = title;
        this.icon = icon;
        this.color = color;
        this.alpha = 0;
        this.notification = false;
        this.rect = new RectF();
    }
}
