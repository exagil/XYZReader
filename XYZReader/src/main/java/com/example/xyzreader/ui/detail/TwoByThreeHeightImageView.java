package com.example.xyzreader.ui.detail;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

public class TwoByThreeHeightImageView extends ImageView {
    public TwoByThreeHeightImageView(Context context) {
        super(context);
    }

    public TwoByThreeHeightImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TwoByThreeHeightImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TwoByThreeHeightImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = (int) (measuredWidth * (2f / 3f));
        setMeasuredDimension(measuredWidth, measuredHeight);
    }
}
