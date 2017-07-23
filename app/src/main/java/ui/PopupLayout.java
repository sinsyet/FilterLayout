package ui;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.sin.myapp.R;

/**
 * 弹出
 */

public class PopupLayout extends RelativeLayout {
    private static final String TAG = "PopupLayout";
    private FrameLayout mContentContainer;
    private FrameLayout mPopupContainer;
    private RelativeLayout.LayoutParams mPopupLayoutParams;
    private View mContentView;
    private View mPopupView;

    private int mShowFlag;
    public static final int DARKEN = 1;
    public static final int SCALE = 1 << 1;

    public PopupLayout(Context context) {
        super(context);
        init();
    }

    public PopupLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PopupLayout(Context context, @Nullable AttributeSet attrs,
                       int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public PopupLayout(Context context, AttributeSet attrs,
                       int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){


        mContentContainer = new FrameLayout(getContext());
        mContentContainer.setLayoutParams(
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
        addView(mContentContainer);

        mPopupContainer = new FrameLayout(getContext());
        mPopupContainer.setVisibility(GONE);
        mPopupContainer.setBackgroundColor(Color.GRAY);

        mPopupLayoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupContainer.setLayoutParams(mPopupLayoutParams);
        addView(mPopupContainer);

        mContentContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closePopupView();
            }
        });
    }

    @UiThread
    public void setContentView(View contentView){
        mContentView = contentView;
        mContentContainer.addView(mContentView);
    }

    @UiThread
    public void setPopupView(View popupView){
        mPopupView = popupView;
        mPopupContainer.addView(mPopupView);
    }

    public void setPopupHeight(int height){
        mPopupLayoutParams.height = height;
    }

    public void setShowFlag(int flag){
        mShowFlag = flag;
    }

    public void showPopupView(){
        Log.e(TAG, "showPopupView: ");
        mPopupContainer.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.pop_in));
        mContentContainer.setAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.content_in));
        mPopupContainer.setVisibility(VISIBLE);
    }

    public void closePopupView(){
        mPopupContainer.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.pop_out));
        mContentContainer.setAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.content_out));
        mPopupContainer.setVisibility(GONE);
    }
}
