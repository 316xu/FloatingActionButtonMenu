package hust.xujifa.floatingactionbuttonmenu;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by xujifa on 2015/9/29.
 */
public class FloatingActionButton extends ImageView {
    final float scale = getResources().getDisplayMetrics().density;
    private int backgroundTint;
    private float elevation;
    private int icon;
    public FloatingActionButton(Context context) {
        this(context,null);
    }
    public FloatingActionButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public FloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array=context.obtainStyledAttributes(attrs,R.styleable.FloatingActionButton);
        backgroundTint=array.getColor(R.styleable.FloatingActionButton_backgroundColor, getResources().getColor(R.color.green));
        elevation=array.getDimension(R.styleable.FloatingActionButton_fabelevation, 4f);
        icon=array.getResourceId(R.styleable.FloatingActionButton_fab_icon, R.mipmap.ic_launcher);
        setIcon(icon);
        if(Build.VERSION.SDK_INT>20)
            setElevation(elevation);
        array.recycle();
        setBg(backgroundTint);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setPadding((int) (16 * scale), (int) (16 * scale), (int) (16 * scale), (int) (16 * scale));
        setMeasuredDimension((int) (56 * scale), (int) (56 * scale));
    }
    private void setBg(@ColorInt int color) {
        OvalShape shape=new OvalShape();
        ShapeDrawable drawable=new ShapeDrawable(shape);
        drawable.getPaint().setColor(color);
        setBackground(drawable);
    }
    public void setIcon(@DrawableRes int resId){
        Bitmap b=BitmapFactory.decodeResource(getResources(),resId);
        setImageBitmap(b);
    }
}
