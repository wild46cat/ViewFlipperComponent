package com.example.administrator.viewflippercomponent;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-09-26.
 */
public class ViewFlipperLunboComponent extends RelativeLayout {
    private final String TAG = ViewFlipperLunboComponent.class.getSimpleName();
    private ViewFlipper viewFlipper;
    private LinearLayout linearLayout;
    private final int AUTOMSG = 0x990;
    private final int TIMER_INTERVAL = 5000;
    private int[] imgs;
    private List<View> dots;
    private int oldX;
    private int newX;
    //点击后标志，控制是否继续播放下一页
    private boolean flag;
    private int currentNumber;

    //对外暴露的监听器
    public interface CustomerClickListener {
        void onclick(int current);
    }

    private CustomerClickListener customerClickListener;

    public void setCustomerClickListener(CustomerClickListener customerClickListener) {
        this.customerClickListener = customerClickListener;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == AUTOMSG) {
                if (flag) {
                    goShowNext();
                }
                Message message = handler.obtainMessage(AUTOMSG);
                handler.sendMessageDelayed(message, TIMER_INTERVAL);
            }
        }
    };

    public ViewFlipperLunboComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
        flag = true;
        currentNumber = 0;
        Message message = handler.obtainMessage(AUTOMSG);
        handler.sendMessageDelayed(message, TIMER_INTERVAL);
    }

    //设置图片
    public void setImgs(int[] imgs) {
        this.imgs = imgs;
        viewFlipper = new ViewFlipper(getContext());
        addView(viewFlipper);
        if (imgs != null && imgs.length > 0) {
            for (int i = 0; i < imgs.length; i++) {
                ImageView imageView = new ImageView(getContext());
                imageView.setImageResource(imgs[i]);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                viewFlipper.addView(imageView, i);
            }
        }
        currentNumber = 1000 * imgs.length;
        //设置点点
        setDot(currentNumber);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                flag = false;
                oldX = (int) event.getX();
                break;
            case MotionEvent.ACTION_UP:
                newX = (int) event.getX();
                if (newX == oldX) {
                    //设置点击事件
                    if (customerClickListener != null) {
                        int current = currentNumber % viewFlipper.getChildCount();
                        customerClickListener.onclick(current);
                    }
                } else if (newX > oldX) {
                    //左侧滑动
                    goShowPrevious();
                } else if (newX < oldX) {
                    //右侧滑动
                    goShowNext();
                }
                flag = true;
                break;
        }
        return true;
    }

    private void setDot(int currentNumber) {
        //remove
        removeView(linearLayout);
        //设置点点
        linearLayout = new LinearLayout(getContext());
        RelativeLayout.LayoutParams reLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        reLayoutParams.setMargins(0, 0, 0, 30);
        reLayoutParams.addRule(ALIGN_PARENT_BOTTOM);
        linearLayout.setLayoutParams(reLayoutParams);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.RIGHT);
        addView(linearLayout);
        dots = new ArrayList<View>();
        //设置当前选中
        int current = currentNumber % viewFlipper.getChildCount();
        for (int i = 0; i < imgs.length; i++) {
            View view = new View(getContext());
            if (current == i) {
                view.setBackgroundResource(R.drawable.dot_focused);
            } else {
                view.setBackgroundResource(R.drawable.dot_normal);
            }
            LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(30, 30);
            layoutparams.setMargins(5, 0, 5, 0);
            view.setLayoutParams(layoutparams);
            linearLayout.addView(view);
        }
    }

    //下一页
    public void goShowNext() {
        viewFlipper.setInAnimation(getContext(),R.anim.slide_in_right);
        viewFlipper.setOutAnimation(getContext(),R.anim.slide_out_left);

        viewFlipper.showNext();
        currentNumber++;
        setDot(currentNumber);
    }

    //上一页
    public void goShowPrevious() {
        viewFlipper.setInAnimation(getContext(),R.anim.slide_in_left);
        viewFlipper.setOutAnimation(getContext(),R.anim.slide_out_right);
        viewFlipper.showPrevious();
        currentNumber--;
        setDot(currentNumber);
    }

}
