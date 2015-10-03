package hust.xujifa.floatingactionbuttonmenu;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by xujifa on 2015/9/29.
 */
public class FloatingActionButtonMenu extends ViewGroup implements ValueAnimator.AnimatorUpdateListener {
    int h;
    final float scale = getResources().getDisplayMetrics().density;
    private int count;
    boolean show = false;
    public FloatingActionButtonMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        FloatingActionButton b = new FloatingActionButton(context);
        b.setIcon(R.mipmap.add);
        addView(b);
        b.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });
    }

    public FloatingActionButtonMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        count = getChildCount();
        int height = 0;
        for (int i = count - 1; i >= 0; i--) {
            View child = getChildAt(i);
            if(child.getVisibility()==GONE)continue;
            child.layout(0, (height), (int) (56 * scale), (int) (height += 56 * scale));
            height += 10 * scale;
        }
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        count = getChildCount();
        h = 0;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            h += child.getMeasuredHeight();
            h += 10 * scale;
        }
        setMeasuredDimension((int) (56 * scale), h);
    }
    public void start(){
        ValueAnimator animator = ValueAnimator.ofInt(0, 100);
        animator.setStartDelay(0);
        animator.setDuration(200);
        animator.addUpdateListener(this);
        animator.start();
    }
    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        int d = (int) animation.getAnimatedValue();
        if (d == 0 && !show) {
            for (int i = getChildCount() - 1; i > 0; i--) {
                View cv = getChildAt(i);
                cv.setAlpha(0);
                cv.setVisibility(VISIBLE);
            }
        }
        int i;
        for (i = getChildCount() - 1; i > 0; i--) {
            View v = getChildAt(i);
            if (show) {
                v.setAlpha(1.0f - 45*d/100f);
            } else {
                v.setAlpha(45*d/100f);
            }
        }
        View v = getChildAt(0);
        if (!show)
            v.setRotation(45 * d / 100);
        else v.setRotation(45 - 45 * d / 100);

        if (d == 100 && show) {
                for (i = getChildCount() - 1; i > 0; i--) {
                    View cv = getChildAt(i);
                    cv.setVisibility(GONE);
                }
        }
        if(d==100)
            show=!show;
    }
}
