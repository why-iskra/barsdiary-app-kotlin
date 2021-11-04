package ru.unit.barsdiary.components.bottomnavigationbar;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.DrawableRes;
import androidx.annotation.FontRes;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import java.util.ArrayList;
import java.util.List;

import ru.unit.barsdiary.R;

public class BottomNavBar extends View {

    public BottomNavBar(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public BottomNavBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public BottomNavBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    private final static int OPAQUE = 255;
    private final static int TRANSPARENT = 0;


    @DrawableRes
    private int barBackground = 0;

    private int itemNotificationColor = Color.parseColor("#ffb3b3");
    private int activeItemNotificationColor = Color.parseColor("#ff0000");
    private float itemNotificationRadius = dp2px(getResources(), 5);
    private int barBackgroundColor = Color.parseColor("#afafaf");
    private float barIndicatorRadius = dp2px(getResources(), 20);
    private float barSideMargins = dp2px(getResources(), 10);
    private float itemPadding = dp2px(getResources(), 16);
    private int itemAnimDuration = 200;
    private float itemIconSize = dp2px(getResources(), 24);
    private final float itemIconMargin = dp2px(getResources(), 4);
    private int itemIconTint = Color.parseColor("#7F000000");
    private float itemTextSize = dp2px(getResources(), 14);
    private float itemIndicatorHighlighter = 0.05f;
    private int itemIndicatorAlpha = 25;
    private boolean indicatorVisible = true;

    @FontRes
    private int itemFontFamily = 0;

    private OnItemSelectedListener onItemSelectedListener;
    private OnItemReselectedListener onItemReselectedListener;


    private List<BottomBarItem> bottomBarItems;

    private int activeItemIndex = 0;
    private int lastActiveItemIndex;

    private int indicatorColor;
    private float indicatorLocation = barSideMargins;
    private float indicatorLen = 0;

    private float activeWidth = 0;
    private float inactiveWidth = 0;
    private float finalInactiveWidth = 0;

    private float activeAnimWidth = 0;
    private float lastActiveAnimWidth = 0;

    private int currentIconTint;
    private int lastIconTint;

    private int currentNotificationColor;
    private int lastNotificationColor;

    private final RectF rectIndicator = new RectF();

    private Paint paintIndicator;
    private Paint paintText;
    private Paint paintNotification;

    private void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.BottomNavBar, defStyleAttr, defStyleRes);
        initByAttributes(attributes);
        attributes.recycle();
        initPainters();

        setBackgroundColor(barBackgroundColor);
        setBackgroundResource(barBackground);

        paintText.setColor(bottomBarItems.get(activeItemIndex).color);
        currentIconTint = bottomBarItems.get(activeItemIndex).color;
        lastIconTint = bottomBarItems.get(activeItemIndex).color;
    }

    private void initByAttributes(TypedArray attributes) {
        barBackgroundColor = attributes.getColor(R.styleable.BottomNavBar_backgroundColor, this.barBackgroundColor);
        barBackground = attributes.getResourceId(R.styleable.BottomNavBar_background, this.barBackground);
        barIndicatorRadius = attributes.getDimension(R.styleable.BottomNavBar_indicatorRadius, this.barIndicatorRadius);
        barSideMargins = attributes.getDimension(R.styleable.BottomNavBar_sideMargins, this.barSideMargins);
        itemPadding = attributes.getDimension(R.styleable.BottomNavBar_itemPadding, this.itemPadding);
        itemTextSize = attributes.getDimension(R.styleable.BottomNavBar_textSize, this.itemTextSize);
        itemIconSize = attributes.getDimension(R.styleable.BottomNavBar_iconSize, this.itemIconSize);
        activeItemIndex = attributes.getInt(R.styleable.BottomNavBar_activeItem, this.activeItemIndex);
        itemFontFamily = attributes.getResourceId(R.styleable.BottomNavBar_itemFontFamily, this.itemFontFamily);
        itemAnimDuration = attributes.getInt(R.styleable.BottomNavBar_itemAnimDuration, this.itemAnimDuration);
        itemIconTint = attributes.getColor(R.styleable.BottomNavBar_itemIconTint, this.itemIconTint);
        itemIndicatorHighlighter = attributes.getFloat(R.styleable.BottomNavBar_itemIndicatorHighlighter, this.itemIndicatorHighlighter);
        itemIndicatorAlpha = attributes.getInt(R.styleable.BottomNavBar_itemIndicatorAlpha, this.itemIndicatorAlpha);
        indicatorVisible = attributes.getBoolean(R.styleable.BottomNavBar_indicatorVisible, this.indicatorVisible);

        itemNotificationColor = attributes.getColor(R.styleable.BottomNavBar_itemNotificationColor, this.itemNotificationColor);
        activeItemNotificationColor = attributes.getColor(R.styleable.BottomNavBar_activeItemNotificationColor, this.activeItemNotificationColor);
        itemNotificationRadius = attributes.getDimension(R.styleable.BottomNavBar_itemNotificationRadius, this.itemNotificationRadius);

        try {
            bottomBarItems = new BottomBarParser(getContext(), attributes.getResourceId(R.styleable.BottomNavBar_menu, 0)).parse();
        } catch (Exception e) {
            e.printStackTrace();
            bottomBarItems = new ArrayList<>();
            bottomBarItems.add(new BottomBarItem("error", null, 0));
            bottomBarItems.add(new BottomBarItem("error", null, 0));
            bottomBarItems.add(new BottomBarItem("error", null, 0));
            bottomBarItems.add(new BottomBarItem("error", null, 0));
        }
    }

    private void initPainters() {
        paintIndicator = new Paint();
        paintIndicator.setAntiAlias(true);
        paintIndicator.setStyle(Paint.Style.FILL);

        paintText = new Paint();
        paintText.setAntiAlias(true);
        paintText.setStyle(Paint.Style.FILL);
        paintText.setTextAlign(Paint.Align.CENTER);
        paintText.setFakeBoldText(true);
        paintText.setTextSize(itemTextSize);
        if (itemFontFamily != 0) {
            paintText.setTypeface(ResourcesCompat.getFont(getContext(), itemFontFamily));
        }

        paintNotification = new Paint();
        paintNotification.setAntiAlias(true);
        paintNotification.setStyle(Paint.Style.FILL);
        paintNotification.setColor(itemNotificationColor);
    }

    private void measure() {
        int count = bottomBarItems.size();

        float lastX = barSideMargins;
        float width = (getWidth() - (barSideMargins * 2));

        float itemWidth = (itemIconSize + itemIconMargin + (itemPadding * 2));

        BottomBarItem activeItem = bottomBarItems.get(activeItemIndex);
        activeWidth = paintText.measureText(activeItem.title) + itemIconSize + itemIconMargin + (itemPadding * 2);
        float addFinal = (width - activeWidth - itemWidth * (count - 1)) / (count - 1);

        if (addFinal <= 0) {
            String title = activeItem.title;

            int len = title.length();
            for (int i = 0; i < len; i++) {
                title = title.substring(0, title.length() - 1);

                activeWidth = paintText.measureText(title) + itemIconSize + itemIconMargin + (itemPadding * 2);
                addFinal = (width - activeWidth - itemWidth * (count - 1)) / (count - 1);
                if (addFinal > 0) {
                    break;
                }
            }

            if (title.length() > 0) {
                title = title.substring(0, title.length() - 1);
                activeItem.title = title + getContext().getString(R.string.ellipsis);
            }
        }


        float add = (width - activeAnimWidth - lastActiveAnimWidth - itemWidth * (count - 2)) / (count - 2);

        inactiveWidth = itemWidth + add;
        finalInactiveWidth = itemWidth + addFinal;

        for (int i = 0; i < activeItemIndex; i++) {
            if (lastActiveItemIndex == i) {
                bottomBarItems.get(i).rect.set(lastX, 0f, lastActiveAnimWidth + lastX, (float) getHeight());
                lastX += lastActiveAnimWidth;
            } else {
                bottomBarItems.get(i).rect.set(lastX, 0f, inactiveWidth + lastX, (float) getHeight());
                lastX += inactiveWidth;
            }
        }
        bottomBarItems.get(activeItemIndex).rect.set(lastX, 0f, activeAnimWidth + lastX, (float) getHeight());
        lastX += activeAnimWidth;
        for (int i = activeItemIndex + 1; i < count; i++) {
            if (lastActiveItemIndex == i) {
                bottomBarItems.get(i).rect.set(lastX, 0f, lastActiveAnimWidth + lastX, (float) getHeight());
                lastX += lastActiveAnimWidth;
            } else {
                bottomBarItems.get(i).rect.set(lastX, 0f, inactiveWidth + lastX, (float) getHeight());
                lastX += inactiveWidth;
            }
        }

        rectIndicator.set(indicatorLocation - indicatorLen,
                bottomBarItems.get(activeItemIndex).rect.centerY() - itemIconSize / 2 - itemPadding,
                indicatorLocation + indicatorLen + itemIconMargin,
                bottomBarItems.get(activeItemIndex).rect.centerY() + itemIconSize / 2 + itemPadding);


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setActiveItem(activeItemIndex);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        measure();

        if (indicatorVisible) {
            paintIndicator.setColor(indicatorColor);
            canvas.drawRoundRect(rectIndicator, barIndicatorRadius, barIndicatorRadius, paintIndicator);
        }

        float textHeight = (paintText.descent() + paintText.ascent()) / 2;

        int count = bottomBarItems.size();
        for (int i = 0; i < count; i++) {
            BottomBarItem item = bottomBarItems.get(i);
            float textLength = paintText.measureText(item.title);

            item.icon.mutate();

            int left = (int) (item.rect.centerX() - (itemIconSize / 2) - (textLength / 2) * (1 - (OPAQUE - item.alpha) / (float) OPAQUE));
            int right = (int) (item.rect.centerX() + (itemIconSize / 2) - (textLength / 2) * (1 - (OPAQUE - item.alpha) / (float) OPAQUE));

            int top = getHeight() / 2 - (int) itemIconSize / 2;
            int bottom = getHeight() / 2 + (int) itemIconSize / 2;
            item.icon.setBounds(left, top, right, bottom);

            if (lastActiveItemIndex == activeItemIndex) {
                if (activeItemIndex == i) {
                    paintText.setColor(currentIconTint);
                    DrawableCompat.setTint(item.icon, currentIconTint);
                    paintNotification.setColor(currentNotificationColor);
                } else {
                    paintText.setColor(itemIconTint);
                    DrawableCompat.setTint(item.icon, itemIconTint);
                    paintNotification.setColor(itemNotificationColor);
                }
            } else {
                if (lastActiveItemIndex == i) {
                    paintText.setColor(lastIconTint);
                    paintNotification.setColor(lastNotificationColor);
                    DrawableCompat.setTint(item.icon, lastIconTint);
                } else if (activeItemIndex == i) {
                    paintText.setColor(currentIconTint);
                    paintNotification.setColor(currentNotificationColor);
                    DrawableCompat.setTint(item.icon, currentIconTint);
                } else {
                    paintText.setColor(itemIconTint);
                    paintNotification.setColor(itemNotificationColor);
                    DrawableCompat.setTint(item.icon, itemIconTint);
                }
            }
            item.icon.draw(canvas);

            if (i == activeItemIndex) {
                paintText.setAlpha(item.alpha);
                canvas.drawText(item.title, item.rect.centerX() + itemIconSize / 2 + itemIconMargin, item.rect.centerY() - textHeight, paintText);
            }
            if (item.notification) {
                canvas.drawCircle(right - itemNotificationRadius, top + itemNotificationRadius, itemNotificationRadius, paintNotification);
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int pos = -1;
        if (event.getAction() == MotionEvent.ACTION_UP && Math.abs(event.getDownTime() - event.getEventTime()) < 500) {
            int size = bottomBarItems.size();
            for (int i = 0; i < size; i++) {
                if (bottomBarItems.get(i).rect.contains(event.getX(), event.getY())) {
                    if (i != activeItemIndex) {
                        pos = i;
                    } else {
                        if (onItemReselectedListener != null) {
                            onItemReselectedListener.onItemReselect(i);
                        }
                    }
                }
            }
        }

        if (pos >= 0) {
            setActiveItem(pos);
            if (onItemSelectedListener != null) {
                onItemSelectedListener.onItemSelect(pos);
            }
        }

        return true;
    }

    public void setActiveItem(int pos) {
        lastActiveItemIndex = activeItemIndex;
        activeItemIndex = pos;

        int size = bottomBarItems.size();
        for (int i = 0; i < size; i++) {
            if (i == pos)
                animateAlpha(i, OPAQUE);
            else
                animateAlpha(i, TRANSPARENT);
        }

        measure();
        animateIndicator(pos);
        animateIcons(lastActiveItemIndex);
        animateIconTint(lastActiveItemIndex, pos);
    }

    private void animateIndicator(int pos) {
        float beforeWidth = (barSideMargins + (finalInactiveWidth * pos));
        float activeCenter = (beforeWidth * 2 + activeWidth) * 0.5f;

        ValueAnimator animator = ValueAnimator.ofFloat(indicatorLocation, activeCenter);
        animator.setDuration(itemAnimDuration);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(animation -> {
            indicatorLocation = (float) animation.getAnimatedValue();
            invalidate();
        });

        ValueAnimator animator2 = ValueAnimator.ofFloat(indicatorLen, activeWidth / 2);
        animator2.setDuration(itemAnimDuration);
        animator2.setInterpolator(new DecelerateInterpolator());
        animator2.addUpdateListener(animation -> indicatorLen = (float) animation.getAnimatedValue());

        ValueAnimator animator3 = ValueAnimator.ofObject(new ArgbEvaluator(), indicatorColor, colorHighlight(bottomBarItems.get(pos).color));
        animator3.setDuration(itemAnimDuration);
        animator3.addUpdateListener(animation -> indicatorColor = (int) animation.getAnimatedValue());

        animator.start();
        animator2.start();
        animator3.start();
    }

    private void animateIcons(int last) {
        int count = bottomBarItems.size();

        float width = (getWidth() - (barSideMargins * 2));
        float itemWidth = (itemIconSize + itemIconMargin + (itemPadding * 2));
        float lastActiveWidth = paintText.measureText(bottomBarItems.get(last).title) + itemIconSize + itemIconMargin + (itemPadding * 2);
        float addfinal = (width - lastActiveWidth - itemWidth * (count - 1)) / (count - 1);
        float lastFinalInactiveWidth = itemWidth + addfinal;

        ValueAnimator animator = ValueAnimator.ofFloat(lastFinalInactiveWidth, activeWidth);
        animator.setDuration(itemAnimDuration);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(animation -> {
            activeAnimWidth = (float) animation.getAnimatedValue();
        });


        ValueAnimator animator2 = ValueAnimator.ofFloat(lastActiveWidth, finalInactiveWidth);
        animator2.setDuration(itemAnimDuration);
        animator2.setInterpolator(new DecelerateInterpolator());
        animator2.addUpdateListener(animation -> {
            lastActiveAnimWidth = (float) animation.getAnimatedValue();
        });

        animator.start();
        animator2.start();
    }

    private void animateAlpha(int pos, int to) {
        BottomBarItem item = bottomBarItems.get(pos);
        ValueAnimator animator = ValueAnimator.ofInt(item.alpha, to);
        animator.setDuration(itemAnimDuration);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(animation -> {
            item.alpha = (int) animation.getAnimatedValue();
        });

        animator.start();
    }

    private void animateIconTint(int last, int pos) {
        ValueAnimator animator = ValueAnimator.ofObject(new ArgbEvaluator(), itemIconTint, bottomBarItems.get(pos).color);
        animator.setDuration(itemAnimDuration);
        animator.addUpdateListener(animation -> currentIconTint = (int) animation.getAnimatedValue());

        ValueAnimator animator2 = ValueAnimator.ofObject(new ArgbEvaluator(), bottomBarItems.get(last).color, itemIconTint);
        animator2.setDuration(itemAnimDuration);
        animator2.addUpdateListener(animation -> lastIconTint = (int) animation.getAnimatedValue());

        ValueAnimator animator3 = ValueAnimator.ofObject(new ArgbEvaluator(), itemNotificationColor, activeItemNotificationColor);
        animator3.setDuration(itemAnimDuration);
        animator3.addUpdateListener(animation -> currentNotificationColor = (int) animation.getAnimatedValue());

        ValueAnimator animator4 = ValueAnimator.ofObject(new ArgbEvaluator(), activeItemNotificationColor, itemNotificationColor);
        animator4.setDuration(itemAnimDuration);
        animator4.addUpdateListener(animation -> lastNotificationColor = (int) animation.getAnimatedValue());

        if (last != pos) {
            animator.start();
            animator2.start();
            animator3.start();
            animator4.start();
        }
    }

    private int colorHighlight(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[1] += hsv[1] * itemIndicatorHighlighter;
        hsv[2] += hsv[2] * itemIndicatorHighlighter;
        return Color.HSVToColor(itemIndicatorAlpha, hsv);
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }

    public void setOnItemReselectedListener(OnItemReselectedListener onItemReselectedListener) {
        this.onItemReselectedListener = onItemReselectedListener;
    }

    public int getActiveItemIndex() {
        return activeItemIndex;
    }

    public interface OnItemSelectedListener {
        void onItemSelect(int pos);
    }

    public interface OnItemReselectedListener {
        void onItemReselect(int pos);
    }

    public void setNotification(int index, boolean value) {
        bottomBarItems.get(index).notification = value;
        invalidate();
    }

    public static float dp2px(Resources resources, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
    }

    public static float sp2px(Resources resources, float sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, resources.getDisplayMetrics());
    }

    public boolean getNotification(int index) {
        return bottomBarItems.get(index).notification;
    }
}
