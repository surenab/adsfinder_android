package ie.adsfinder.adsfinder;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static ie.adsfinder.adsfinder.AdsfinderUtils.capitalizeWord;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenImageActivity extends AppCompatActivity {
    private View mContentView;
    private SliderLayout sliderLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen_image);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();



        sliderLayout = findViewById(R.id.imageFullSlider);
        sliderLayout.setIndicatorAnimation(SliderLayout.Animations.FILL); //set indicator animation by using SliderLayout.Animations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderLayout.setScrollTimeInSec(4); //set scroll delay in seconds :

        ArrayList<String> photos = getIntent().getStringArrayListExtra("photos");
        String header = getIntent().getStringExtra("header");
        for (int i = 0; i <= photos.size()-1; i++) {
            SliderView sliderView = new SliderView(this);
            sliderView.setImageUrl(photos.get(i));
            sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
            sliderView.setDescription(capitalizeWord(header));
            sliderLayout.addSliderView(sliderView);
        }

        ImageView back_button = findViewById(R.id.back_arrow);
        back_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

    }
}
