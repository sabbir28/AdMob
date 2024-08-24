To integrate app open ads into your Android app, you'll need to follow the steps outlined in the provided article. Here's a code example demonstrating how you can implement app open ads using Java and integrate it into your existing application:

First, let's create the `MyApplication` class that extends the `Application` class and initializes the Google Mobile Ads SDK:

```java
import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Log.d("MyApplication", "Initialization completed.");
            }
        });
    }
}
```

Make sure to add this `MyApplication` class to your AndroidManifest.xml:

```xml
<application
    android:name=".MyApplication"
    ...>
    ...
</application>
```

Next, let's implement the `AppOpenAdManager` utility class:

```java
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.appopen.AppOpenAd;
import java.util.Date;

public class AppOpenAdManager {

    private static final String LOG_TAG = "AppOpenAdManager";
    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/3419835294"; // Test Ad Unit ID

    private AppOpenAd appOpenAd = null;
    private boolean isLoadingAd = false;
    private boolean isShowingAd = false;
    private long loadTime = 0;

    public void loadAd(Context context) {
        if (isLoadingAd || isAdAvailable()) {
            return;
        }

        isLoadingAd = true;
        AdRequest request = new AdRequest.Builder().build();
        AppOpenAd.load(
                context,
                AD_UNIT_ID,
                request,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
                new AppOpenAd.AppOpenAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull AppOpenAd ad) {
                        appOpenAd = ad;
                        isLoadingAd = false;
                        loadTime = (new Date()).getTime();
                        Log.d(LOG_TAG, "Ad was loaded.");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        isLoadingAd = false;
                        Log.d(LOG_TAG, "Failed to load ad: " + loadAdError.getMessage());
                    }
                });
    }

    public boolean isAdAvailable() {
        return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4);
    }

    public void showAdIfAvailable(Activity activity) {
        if (isShowingAd || !isAdAvailable()) {
            return;
        }

        appOpenAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdDismissedFullScreenContent() {
                appOpenAd = null;
                isShowingAd = false;
                loadAd(activity);
            }

            @Override
            public void onAdFailedToShowFullScreenContent(AdError adError) {
                Log.d(LOG_TAG, "Failed to show ad: " + adError.getMessage());
                loadAd(activity);
            }

            @Override
            public void onAdShowedFullScreenContent() {
                isShowingAd = true;
            }
        });

        appOpenAd.show(activity);
    }

    private boolean wasLoadTimeLessThanNHoursAgo(long numHours) {
        long dateDifference = (new Date()).getTime() - this.loadTime;
        long numMilliSecondsPerHour = 3600000;
        return (dateDifference < (numMilliSecondsPerHour * numHours));
    }
}
```

Now, let's integrate the `AppOpenAdManager` into your `MainActivity` or any other activity where you want to show the app open ad:

```java
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {

    private AppOpenAdManager appOpenAdManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the Ad Manager
        appOpenAdManager = new AppOpenAdManager();
        appOpenAdManager.loadAd(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        appOpenAdManager.showAdIfAvailable(this);
    }
}
```

This code initializes the `AppOpenAdManager` in the `MainActivity` and loads the ad when the activity is created. It also attempts to show the ad when the activity resumes. Make sure to replace the test Ad Unit ID with your actual Ad Unit ID before publishing your app.

You can include this code in your industry label code and adjust it as necessary to fit into your existing application structure. Let me know if you need further assistance!
