package com.example.administrator.viewflippercomponent;

import android.database.CursorIndexOutOfBoundsException;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class MainActivity extends AppCompatActivity {
    private ViewFlipper viewFlipper;
    private ViewFlipperLunboComponent viewFlipperLunboComponent;
    private int[] imgs = new int[]{
            R.drawable.img001,
            R.drawable.img002,
            R.drawable.img003,
            R.drawable.img004
    };

    class CustomerClick implements ViewFlipperLunboComponent.CustomerClickListener{
        @Override
        public void onclick(int current) {
            Toast.makeText(MainActivity.this, String.valueOf(current), Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewFlipper = (ViewFlipper) this.findViewById(R.id.viewFlipper);
        viewFlipperLunboComponent =(ViewFlipperLunboComponent)this.findViewById(R.id.customerViewFlipperComponent);
        viewFlipperLunboComponent.setImgs(imgs);
        viewFlipperLunboComponent.setCustomerClickListener(new CustomerClick());
        for (int i = 0; i < imgs.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(imgs[i]);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView, i);
        }
        Message message = handler.obtainMessage(0x123);
        handler.sendMessageDelayed(message,3000);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x123) {
                viewFlipper.showNext();
                Message message = handler.obtainMessage(0x123);
                handler.sendMessageDelayed(message, 3000);
            }
        }
    };
}
