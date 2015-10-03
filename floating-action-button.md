##Android Custom View---FloatingActionButtonMenu

这次要实现的是一个简易的FAB Menu
首先定义FAB，继承ImageView，只有背景色，图片和高度三个属性，很简单，就不多说了

```
public class FloatingActionButton extends ImageView {
    final float scale = getResources().getDisplayMetrics().density;//Convert dp to pixel
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
```
它是这样的
![](https://raw.githubusercontent.com/jifaxu/image/master/fab1.PNG)
第二步来定义FAB Menu，继承自ViewGroup，利用ValueAnimator来实现动画效果
```
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
```
注意这儿：

```
child.layout(...)
```
layout的定义是这样的 
```
	 * @param l Left position, relative to parent
     * @param t Top position, relative to parent
     * @param r Right position, relative to parent
     * @param b Bottom position, relative to parent
```
也就是说是相对于父类来定位的，不要搞错了，我在写的时候就因为这儿出了问题找了好久
现在你可以在layout里使用它了
```
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    xmlns:custom="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent" android:paddingLeft="@dimen/activity_horizontal_margin"
    android:paddingRight="@dimen/activity_horizontal_margin"
    android:paddingTop="@dimen/activity_vertical_margin"
    android:paddingBottom="@dimen/activity_vertical_margin" tools:context=".MainActivity">

    <TextView android:text="@string/hello_world" android:layout_width="wrap_content"
        android:layout_height="wrap_content" />
    <hust.xujifa.floatingactionbuttonmenu.FloatingActionButtonMenu
        android:layout_alignParentEnd="true"
        android:layout_alignParentBottom="true"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content">
        <hust.xujifa.floatingactionbuttonmenu.FloatingActionButton
            android:visibility="gone"
            custom:fab_icon="@mipmap/ic_launcher"
            custom:backgroundTint="@color/green"
            custom:elevation="4dp"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content" />
        <hust.xujifa.floatingactionbuttonmenu.FloatingActionButton
            android:visibility="gone"

            android:onClick="click1"
            custom:fab_icon="@mipmap/ic_launcher"
            custom:backgroundTint="@color/green"
            custom:elevation="4dp"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content" />
        <hust.xujifa.floatingactionbuttonmenu.FloatingActionButton
            android:visibility="gone"

            custom:fab_icon="@mipmap/ic_launcher"
            custom:backgroundTint="@color/green"
            custom:elevation="4dp"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content" />
    </hust.xujifa.floatingactionbuttonmenu.FloatingActionButtonMenu>


</RelativeLayout>
```
![](https://raw.githubusercontent.com/jifaxu/image/master/fab2.PNG)
至此就全部完成啦，可以去我的github上下载源码，这只是一个简单的框架，你可以加上更多你想要的功能
[](https://github.com/jifaxu/

