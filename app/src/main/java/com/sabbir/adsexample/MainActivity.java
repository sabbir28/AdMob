package com.sabbir.adsexample;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.nativead.NativeAd;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TemplateView templateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        templateView = findViewById(R.id.my_template);

        if (templateView == null) {
            Log.e(TAG, "TemplateView is not found in the layout.");
            return;
        }

        initializeCustomAds();
    }

    private void initializeCustomAds() {
        MobileAds.initialize(this, initializationStatus -> {
            AdLoader adLoader = new AdLoader.Builder(this, "ca-app-pub-3940256099942544/2247696110")
                    .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                        @Override
                        public void onNativeAdLoaded(NativeAd nativeAd) {
                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder()
                                    .withMainBackgroundColor(new ColorDrawable(Color.parseColor("#FF0000")))
                                    .build();
                            if (templateView != null) {
                                templateView.setStyles(styles);
                                templateView.setNativeAd(nativeAd);
                            } else {
                                Log.e(TAG, "TemplateView is null when setting native ad.");
                            }
                        }
                    })
                    .withAdListener(new com.google.android.gms.ads.AdListener() {
                        public void onAdFailedToLoad(AdError adError) {
                            Log.e(TAG, "Failed to load ad: " + adError.getMessage());
                        }
                    })
                    .build();

            adLoader.loadAd(new AdRequest.Builder().build());
        });
    }

}
