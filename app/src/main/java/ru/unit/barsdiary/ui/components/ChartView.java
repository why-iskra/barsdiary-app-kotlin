package ru.unit.barsdiary.ui.components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.core.content.res.ResourcesCompat;

import java.util.Arrays;

import ru.unit.barsdiary.R;

// Decompile from my old project :)
public class ChartView extends View {
    private int animationAlpha;
    private float animationDataLine;
    private int animationDelayHideAndShow;
    private int animationDuration;
    private int background;
    private int backgroundColor;
    private int countViewElementsNames;
    private int dataLineColor;
    private Paint dataLinePaint;
    private float dataLineSize;
    private float dataLineSmoothing;
    private int dataShadowLineColor;
    private Element[] elements;
    private int fontFamily;
    private int heightSize;
    private int levelLineColor;
    private Paint levelLinePaint;
    private float levelLineSize;
    private LevelLine[] levelLines;
    private int maxElements;
    private float padding;
    private boolean removeUnusedLevels;
    private float shadowDataLineIndent;
    private Paint shadowDataLinePaint;
    private int textColor;
    private Paint textDataPaint;
    private Paint textLevelPaint;
    private float textMargin;
    private float textSize;
    private int widthSize;
    private boolean zipElements;

    public ChartView(Context context) {
        super(context);
        this.dataLineColor = Color.rgb(0, 0, 0);
        this.dataShadowLineColor = Color.argb(32, 0, 0, 0);
        this.levelLineColor = Color.rgb(164, 164, 164);
        this.textColor = Color.rgb(164, 164, 164);
        this.textMargin = dp2px(getResources(), 8.0f);
        this.textSize = sp2px(getResources(), 14.0f);
        this.padding = dp2px(getResources(), 8.0f);
        this.levelLineSize = dp2px(getResources(), 2.0f);
        this.dataLineSize = dp2px(getResources(), 4.0f);
        this.shadowDataLineIndent = dp2px(getResources(), 16.0f);
        this.animationDuration = 1000;
        this.animationDelayHideAndShow = 500;
        this.removeUnusedLevels = false;
        this.zipElements = true;
        this.maxElements = 40;
        this.countViewElementsNames = 5;
        this.dataLineSmoothing = 1.0f;
        this.background = 0;
        this.backgroundColor = 0;
        this.levelLines = new LevelLine[0];
        this.elements = new Element[0];
        this.animationAlpha = 0;
        this.animationDataLine = 0.0f;
        init(context, null, 0, 0);
    }

    public ChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.dataLineColor = Color.rgb(0, 0, 0);
        this.dataShadowLineColor = Color.argb(32, 0, 0, 0);
        this.levelLineColor = Color.rgb(164, 164, 164);
        this.textColor = Color.rgb(164, 164, 164);
        this.textMargin = dp2px(getResources(), 8.0f);
        this.textSize = sp2px(getResources(), 14.0f);
        this.padding = dp2px(getResources(), 8.0f);
        this.levelLineSize = dp2px(getResources(), 2.0f);
        this.dataLineSize = dp2px(getResources(), 4.0f);
        this.shadowDataLineIndent = dp2px(getResources(), 16.0f);
        this.animationDuration = 1000;
        this.animationDelayHideAndShow = 500;
        this.removeUnusedLevels = false;
        this.zipElements = true;
        this.maxElements = 40;
        this.countViewElementsNames = 5;
        this.dataLineSmoothing = 1.0f;
        this.background = 0;
        this.backgroundColor = 0;
        this.levelLines = new LevelLine[0];
        this.elements = new Element[0];
        this.animationAlpha = 0;
        this.animationDataLine = 0.0f;
        init(context, attrs, 0, 0);
    }

    public ChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.dataLineColor = Color.rgb(0, 0, 0);
        this.dataShadowLineColor = Color.argb(32, 0, 0, 0);
        this.levelLineColor = Color.rgb(164, 164, 164);
        this.textColor = Color.rgb(164, 164, 164);
        this.textMargin = dp2px(getResources(), 8.0f);
        this.textSize = sp2px(getResources(), 14.0f);
        this.padding = dp2px(getResources(), 8.0f);
        this.levelLineSize = dp2px(getResources(), 2.0f);
        this.dataLineSize = dp2px(getResources(), 4.0f);
        this.shadowDataLineIndent = dp2px(getResources(), 16.0f);
        this.animationDuration = 1000;
        this.animationDelayHideAndShow = 500;
        this.removeUnusedLevels = false;
        this.zipElements = true;
        this.maxElements = 40;
        this.countViewElementsNames = 5;
        this.dataLineSmoothing = 1.0f;
        this.background = 0;
        this.backgroundColor = 0;
        this.levelLines = new LevelLine[0];
        this.elements = new Element[0];
        this.animationAlpha = 0;
        this.animationDataLine = 0.0f;
        init(context, attrs, defStyleAttr, 0);
    }

    public ChartView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.dataLineColor = Color.rgb(0, 0, 0);
        this.dataShadowLineColor = Color.argb(32, 0, 0, 0);
        this.levelLineColor = Color.rgb(164, 164, 164);
        this.textColor = Color.rgb(164, 164, 164);
        this.textMargin = dp2px(getResources(), 8.0f);
        this.textSize = sp2px(getResources(), 14.0f);
        this.padding = dp2px(getResources(), 8.0f);
        this.levelLineSize = dp2px(getResources(), 2.0f);
        this.dataLineSize = dp2px(getResources(), 4.0f);
        this.shadowDataLineIndent = dp2px(getResources(), 16.0f);
        this.animationDuration = 1000;
        this.animationDelayHideAndShow = 500;
        this.removeUnusedLevels = false;
        this.zipElements = true;
        this.maxElements = 40;
        this.countViewElementsNames = 5;
        this.dataLineSmoothing = 1.0f;
        this.background = 0;
        this.backgroundColor = 0;
        this.levelLines = new LevelLine[0];
        this.elements = new Element[0];
        this.animationAlpha = 0;
        this.animationDataLine = 0.0f;
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Chart, defStyleAttr, defStyleRes);
        initByAttributes(attributes);
        attributes.recycle();
        setBackgroundColor(this.backgroundColor);
        setBackgroundResource(this.background);
        initPainters();
    }

    private void initByAttributes(TypedArray attributes) {
        int color = attributes.getColor(R.styleable.Chart_dataLineColor, this.dataLineColor);
        this.dataLineColor = color;
        this.dataShadowLineColor = attributes.getColor(R.styleable.Chart_dataShadowLineColor, Color.argb(32, Color.red(color), Color.green(this.dataLineColor), Color.blue(this.dataLineColor)));
        this.levelLineColor = attributes.getColor(R.styleable.Chart_levelLineColor, this.levelLineColor);
        this.textColor = attributes.getColor(R.styleable.Chart_textColor, this.textColor);
        this.textMargin = attributes.getDimension(R.styleable.Chart_textMargin, this.textMargin);
        this.textSize = attributes.getDimension(R.styleable.Chart_android_textSize, this.textSize);
        this.padding = attributes.getDimension(R.styleable.Chart_padding, this.padding);
        this.levelLineSize = attributes.getDimension(R.styleable.Chart_levelLineSize, this.levelLineSize);
        this.dataLineSize = attributes.getDimension(R.styleable.Chart_dataLineSize, this.dataLineSize);
        this.shadowDataLineIndent = attributes.getDimension(R.styleable.Chart_shadowDataLineIndent, this.shadowDataLineIndent);
        this.animationDuration = attributes.getInt(R.styleable.Chart_animationDuration, this.animationDuration);
        this.animationDelayHideAndShow = attributes.getInt(R.styleable.Chart_animationDelayHideAndShow, this.animationDelayHideAndShow);
        this.removeUnusedLevels = attributes.getBoolean(R.styleable.Chart_removeUnusedLevels, this.removeUnusedLevels);
        this.zipElements = attributes.getBoolean(R.styleable.Chart_zipElements, this.zipElements);
        this.maxElements = attributes.getInt(R.styleable.Chart_maxElements, this.maxElements);
        this.countViewElementsNames = attributes.getInt(R.styleable.Chart_countViewElementsNames, this.countViewElementsNames);
        this.dataLineSmoothing = attributes.getFloat(R.styleable.Chart_dataLineSmoothing, this.dataLineSmoothing);
        this.fontFamily = attributes.getResourceId(R.styleable.Chart_android_fontFamily, this.fontFamily);
        this.background = attributes.getResourceId(R.styleable.Chart_background, this.background);
        this.backgroundColor = attributes.getColor(R.styleable.Chart_backgroundColor, this.backgroundColor);
    }

    public void update(LevelLine[] levelLines2, Element[] elements2, boolean animation, UpdateEnd updateEnd) {
        boolean hide = this.animationAlpha > 0;
        if (animation && hide) {
            animateHide(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animator) {
                    updateShow(levelLines2, elements2, true, true, updateEnd);
                    super.onAnimationEnd(animator);
                }
            });
        } else {
            updateShow(levelLines2, elements2, animation, hide, updateEnd);
        }
    }

    private void updateShow(LevelLine[] levelLines2, Element[] elements2, boolean animation, boolean hide, UpdateEnd updateEnd) {
        LevelLine[] levelLineArr = new LevelLine[levelLines2.length];
        this.levelLines = levelLineArr;
        System.arraycopy(levelLines2, 0, levelLineArr, 0, levelLines2.length);
        Arrays.sort(this.levelLines, (o1, o2) ->
                Float.compare(o2.level, o1.level)
        );
        if (this.zipElements) {
            zippingElements(elements2);
        } else {
            this.elements = elements2;
        }
        if (this.removeUnusedLevels) {
            removingUnusedLevels();
        }
        if (animation) {
            animateShow(hide, new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    updateEnd.run();
                    super.onAnimationEnd(animation);
                }
            });
        } else {
            this.animationDataLine = 1.0f;
            this.animationAlpha = 255;
            invalidate();
            updateEnd.run();
        }
        requestLayout();
    }

    private void animateShow(boolean hide, Animator.AnimatorListener animatorListener) {
        ValueAnimator animator1 = ValueAnimator.ofFloat(0.0f, 1.0f);
        animator1.setDuration(this.animationDuration);
        if (hide) {
            animator1.setStartDelay((this.animationDuration * 2L) + this.animationDelayHideAndShow);
        }
        animator1.setInterpolator(new DecelerateInterpolator());
        animator1.addUpdateListener(animation -> {
            this.animationDataLine = (Float) animation.getAnimatedValue();
            invalidate();
        });
        ValueAnimator animator2 = ValueAnimator.ofInt(0, 255);
        animator2.setDuration(this.animationDuration);
        int i = this.animationDuration;
        animator2.setStartDelay(i + (hide ? (i * 2L) + this.animationDelayHideAndShow : 0));
        animator2.setInterpolator(new DecelerateInterpolator());
        animator2.addUpdateListener(animation -> {
            this.animationAlpha = (Integer) animation.getAnimatedValue();
            invalidate();
        });
        animator1.start();
        animator2.start();
        animator2.addListener(animatorListener);
    }

    private void animateHide(Animator.AnimatorListener animatorListener) {
        ValueAnimator animator1 = ValueAnimator.ofFloat(1.0f, 0.0f);
        animator1.setDuration(this.animationDuration);
        animator1.setInterpolator(new DecelerateInterpolator());
        animator1.addUpdateListener(animation -> {
            this.animationDataLine = (Float) animation.getAnimatedValue();
            invalidate();
        });
        ValueAnimator animator2 = ValueAnimator.ofInt(255, 0);
        animator2.setDuration(this.animationDuration);
        animator2.setStartDelay(this.animationDuration);
        animator2.setInterpolator(new DecelerateInterpolator());
        animator2.addUpdateListener(animation -> {
            this.animationAlpha = (Integer) animation.getAnimatedValue();
            invalidate();
        });
        animator1.start();
        animator2.start();
        animator2.addListener(animatorListener);
    }

    private void removingUnusedLevels() {
        if (this.levelLines.length > 0) {
            float max = 0.0f;
            int i = 0;
            while (true) {
                Element[] elementArr = this.elements;
                if (i >= elementArr.length) {
                    break;
                }
                float value = elementArr[i].value;
                if (max < value) {
                    max = value;
                }
                i++;
            }
            float min = max;
            int i2 = 0;
            while (true) {
                Element[] elementArr2 = this.elements;
                if (i2 >= elementArr2.length) {
                    break;
                }
                float value2 = elementArr2[i2].value;
                if (min > value2) {
                    min = value2;
                }
                i2++;
            }
            int maxLine = getLineByMax(max);
            int minLine = getLineByMin(min);
            if (maxLine < 0) {
                maxLine = 0;
            }
            if (minLine < 0) {
                minLine = this.levelLines.length - 1;
            }
            LevelLine[] newLevelLines = new LevelLine[((minLine - maxLine) + 1)];
            System.arraycopy(this.levelLines, maxLine, newLevelLines, 0, (minLine - maxLine) + 1);
            this.levelLines = newLevelLines;
        }
    }

    private int getLineByMin(float min) {
        int i = 0;
        while (true) {
            LevelLine[] levelLineArr = this.levelLines;
            if (i >= levelLineArr.length) {
                return -1;
            }
            if (levelLineArr[i].level - min < 0.0f) {
                return i;
            }
            i++;
        }
    }

    private int getLineByMax(float max) {
        for (int i = this.levelLines.length - 1; i >= 0; i--) {
            if (max - this.levelLines[i].level < 0.0f) {
                return i;
            }
        }
        return -1;
    }

    private void zippingElements(Element[] elements2) {
        int length = elements2.length;
        if (length > maxElements) {
            int len = elements2.length;
            int countZip = (int) Math.ceil(((double) len) / ((double) maxElements));
            this.elements = new Element[((int) Math.ceil(((double) len) / ((double) countZip)))];
            int pos = 0;
            int i = 0;
            while (i < elements2.length) {
                if (len > countZip) {
                    float zipValue = 0.0f;
                    for (int j = i; j < i + countZip; j++) {
                        zipValue += elements2[j].value;
                    }
                    i += countZip;
                    len -= countZip;
                    this.elements[pos] = new Element(elements2[i].name, zipValue / ((float) countZip));
                } else {
                    int remainder = elements2.length - i;
                    float zipValue2 = 0.0f;
                    for (int j2 = i; j2 < i + remainder; j2++) {
                        zipValue2 += elements2[j2].value;
                    }
                    i += remainder;
                    len -= remainder;
                    this.elements[pos] = new Element(elements2[elements2.length - 1].name, zipValue2 / ((float) remainder));
                }
                pos++;
            }
            return;
        }
        this.elements = elements2;
    }

    private void initPainters() {
        Paint paint = new Paint();
        this.dataLinePaint = paint;
        paint.setAntiAlias(true);
        this.dataLinePaint.setStyle(Paint.Style.STROKE);
        this.dataLinePaint.setStrokeCap(Paint.Cap.ROUND);
        this.dataLinePaint.setStrokeWidth(this.dataLineSize);
        this.dataLinePaint.setColor(this.dataLineColor);
        Paint paint2 = new Paint();
        this.shadowDataLinePaint = paint2;
        paint2.setAntiAlias(true);
        this.shadowDataLinePaint.setStyle(Paint.Style.STROKE);
        this.shadowDataLinePaint.setStrokeCap(Paint.Cap.ROUND);
        this.shadowDataLinePaint.setStrokeWidth(this.dataLineSize);
        this.shadowDataLinePaint.setColor(this.dataShadowLineColor);
        Paint paint3 = new Paint();
        this.levelLinePaint = paint3;
        paint3.setAntiAlias(true);
        this.levelLinePaint.setStrokeCap(Paint.Cap.ROUND);
        this.levelLinePaint.setPathEffect(new DashPathEffect(new float[]{5.0f, 15.0f}, 0.0f));
        this.levelLinePaint.setStrokeWidth(this.levelLineSize);
        this.levelLinePaint.setColor(this.levelLineColor);
        Paint paint4 = new Paint();
        this.textLevelPaint = paint4;
        paint4.setAntiAlias(true);
        this.textLevelPaint.setStyle(Paint.Style.FILL);
        this.textLevelPaint.setTextSize(this.textSize);
        this.textLevelPaint.setColor(this.textColor);
        Paint paint5 = new Paint();
        this.textDataPaint = paint5;
        paint5.setAntiAlias(true);
        this.textDataPaint.setStyle(Paint.Style.FILL);
        this.textDataPaint.setTextAlign(Paint.Align.CENTER);
        this.textDataPaint.setTextSize(this.textSize);
        this.textDataPaint.setColor(this.textColor);
        if (this.fontFamily != 0) {
            this.textLevelPaint.setTypeface(ResourcesCompat.getFont(getContext(), this.fontFamily));
            this.textDataPaint.setTypeface(ResourcesCompat.getFont(getContext(), this.fontFamily));
        }
    }

    /* access modifiers changed from: protected */
    @SuppressLint("DrawAllocation")
    public void onDraw(Canvas canvas) {
        LevelLine[] levelLineArr;
        super.onDraw(canvas);
        this.textLevelPaint.setAlpha(this.animationAlpha);
        this.textDataPaint.setAlpha(this.animationAlpha);
        this.levelLinePaint.setAlpha(this.animationAlpha);
        float heightLevelText = this.textLevelPaint.descent() + this.textLevelPaint.ascent();
        float heightDataText = this.textDataPaint.descent() + this.textDataPaint.ascent();
        float f = this.dataLineSize;
        float addDataLineSize = heightLevelText < f ? f / 2.0f : 0.0f;
        float f2 = this.padding;
        float startLevelLineY = (f2 - heightLevelText) + addDataLineSize;
        int i = this.heightSize;
        float nameElemYPos = (((float) i) - f2) + heightDataText;
        float levelStep = ((((((float) i) + heightDataText) - f2) - addDataLineSize) - this.shadowDataLineIndent) / ((float) this.levelLines.length);
        int i2 = 0;
        float levelNameWidth = 0.0f;
        while (true) {
            if (i2 >= levelLines.length) {
                break;
            }
            float tw = this.textLevelPaint.measureText(levelLines[i2].name);
            levelNameWidth = Math.max(levelNameWidth, tw);
            i2++;
        }
        float f3 = this.padding;
        float f4 = this.dataLineSize;
        float startLevelLineX = levelNameWidth + this.textMargin + f3 + f4;
        float stopLevelLineX = (((float) this.widthSize) - f3) - f4;
        int i3 = this.countViewElementsNames;
        float cellWidth = (stopLevelLineX - startLevelLineX) / ((float) i3);
        int i4 = 0;
        if (this.elements.length >= i3) {
            int i5 = 0;
            while (true) {
                int i6 = this.countViewElementsNames;
                if (i5 >= i6) {
                    break;
                }
                String name = elements[(elements.length / i6) * i5].name;
                if (this.textDataPaint.measureText(name) > cellWidth) {
                    int len = name.length();
                    for (int j = 0; j < len; j++) {
                        name = name.substring(i4, name.length() - 1);
                        if (this.textDataPaint.measureText(name) <= cellWidth) {
                            break;
                        }
                    }
                    if (name.length() > 0) {
                        name = name.substring(i4, name.length() - 1) + getContext().getString(R.string.ellipsis);
                    }
                }
                this.textDataPaint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(name, startLevelLineX + ((((float) i5) + 0.5f) * cellWidth), nameElemYPos, this.textDataPaint);
                i5++;
                i4 = 0;
            }
        }
        int i7 = 0;
        float positionY = startLevelLineY;
        while (true) {
            levelLineArr = this.levelLines;
            if (i7 >= levelLineArr.length) {
                break;
            }
            canvas.drawLine(startLevelLineX, positionY, stopLevelLineX, positionY, this.levelLinePaint);
            canvas.drawText(this.levelLines[i7].name, this.padding, positionY - (heightLevelText / 2.0f), this.textLevelPaint);
            positionY += levelStep;
            i7++;
        }
        this.levelLinePaint.setShader(new LinearGradient(startLevelLineX, 0.0f, (float) this.widthSize, 0.0f, new int[]{0, this.levelLineColor, 0}, new float[]{0.0f, 0.5f, 1.0f}, Shader.TileMode.REPEAT));

        if (levelLineArr.length > 0) {
            if (elements.length > 0) {
                float positionDataLine = startLevelLineX;
                float stepDataLineX = ((stopLevelLineX - this.dataLineSize) - startLevelLineX) / ((float) (((elements.length + 1) * 2) - 2));
                float max = levelLineArr[0].level;
                float min = levelLines[levelLines.length - 1].level;
                float maxDataLineY = positionY - levelStep;
                float region = maxDataLineY - startLevelLineY;
                @SuppressLint("DrawAllocation") Path dataLine = new Path();
                @SuppressLint("DrawAllocation") Path shadowDataLine = new Path();
                float first = maxDataLineY - (((this.elements[0].value - min) / (max - min)) * region);
                dataLine.moveTo(positionDataLine, first);
                shadowDataLine.moveTo(positionDataLine, this.shadowDataLineIndent + first);
                dataLine.moveTo(positionDataLine, first);
                shadowDataLine.moveTo(positionDataLine, this.shadowDataLineIndent + first);
                int i8 = 0;
                while (true) {
                    if (i8 < elements.length * 2) {
                        float pos = ((elements[i8 / 2].value - min) / (max - min)) * region;
                        dataLine.lineTo(positionDataLine + stepDataLineX, maxDataLineY - pos);
                        shadowDataLine.lineTo(positionDataLine + stepDataLineX, (maxDataLineY - pos) + this.shadowDataLineIndent);
                        positionDataLine += stepDataLineX;
                        i8++;
                    } else {
                        this.dataLinePaint.setPathEffect(new CornerPathEffect(this.dataLineSmoothing * stepDataLineX));
                        this.shadowDataLinePaint.setPathEffect(new CornerPathEffect(this.dataLineSmoothing * stepDataLineX));
                        canvas.save();
                        float f5 = this.dataLineSize;
                        canvas.clipRect(startLevelLineX - f5, 0.0f, (stopLevelLineX - (((stopLevelLineX - startLevelLineX) + (2.0f * f5)) * (1.0f - this.animationDataLine))) + f5, (float) this.heightSize);
                        canvas.drawPath(shadowDataLine, this.shadowDataLinePaint);
                        canvas.drawPath(dataLine, this.dataLinePaint);
                        canvas.restore();
                        return;
                    }
                }
            }
        }
    }

    public int getMinHeight() {
        float heightDataText = this.textDataPaint.descent() + this.textDataPaint.ascent();
        float heightLevelText = this.textLevelPaint.descent() + this.textLevelPaint.ascent();
        float f = this.dataLineSize;
        return (int) (((this.padding * 2.0f) - heightDataText) + ((((heightLevelText < f ? f / 2.0f : 0.0f) - heightLevelText) + 20.0f) * ((float) this.levelLines.length)) + this.shadowDataLineIndent);
    }

    /* access modifiers changed from: protected */
    @SuppressLint("DrawAllocation")
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width;
        int height;
        int desiredHeight = getMinHeight();
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize2 = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize2 = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == 1073741824) {
            width = widthSize2;
        } else if (widthMode == Integer.MIN_VALUE) {
            width = Math.min(300, widthSize2);
        } else {
            width = 300;
        }
        if (heightMode == 1073741824) {
            height = heightSize2;
        } else if (heightMode == Integer.MIN_VALUE) {
            height = Math.min(desiredHeight, heightSize2);
        } else {
            height = desiredHeight;
        }
        setMeasuredDimension(width, height);
        this.widthSize = width;
        this.heightSize = height;
    }

    public static class LevelLine {
        private final float level;
        private final String name;

        public LevelLine(String name2, float level2) {
            this.name = name2;
            this.level = level2;
        }

        public LevelLine(float level2) {
            this.level = level2;
            this.name = ChartView.prettyFloat(level2);
        }

        public String getName() {
            return this.name;
        }

        public float getLevel() {
            return this.level;
        }
    }

    public static class Element {
        private final String name;
        private final float value;

        public Element(String name2, float value2) {
            this.name = name2;
            this.value = value2;
        }

        public Element(float value2) {
            this.value = value2;
            this.name = ChartView.prettyFloat(value2);
        }

        public String getName() {
            return this.name;
        }

        public float getValue() {
            return this.value;
        }
    }

    /* access modifiers changed from: private */
    public static String prettyFloat(float f) {
        if (f == ((float) ((long) f))) {
            return Long.toString((long) f);
        }
        return Float.toString(f);
    }

    public static float dp2px(Resources resources, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
    }

    public static float sp2px(Resources resources, float sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, resources.getDisplayMetrics());
    }

    public interface UpdateEnd {
        void run();
    }
}
