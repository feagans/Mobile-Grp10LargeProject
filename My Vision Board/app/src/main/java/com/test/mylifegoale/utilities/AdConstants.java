package com.test.mylifegoale.utilities;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.test.mylifegoale.R;
import com.test.mylifegoale.itemClick.AdsTwoButtonDialogListener;

public class AdConstants {
    public static final String STR_PRIVACY_URI = "http://appworldinfotech.hol.es/terms/magneticlab/index.html";
    public static final String AD_FULL = "ca-app-pub-3940256099942544/1033173712";
    public static int adCount = 0;
    public static final int AD_LIMIT = 2;
    public static boolean npaflag = false;

    public static void showPersonalizeDialog(boolean z, final Context context, String str, String str2, String str3, String str4, AdsTwoButtonDialogListener adsTwoButtonDialogListener) {
        final AdsTwoButtonDialogListener adsTwoButtonDialogListener2 = adsTwoButtonDialogListener;
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.alert_dialog_addmob);
        boolean z2 = false;
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(17170445);
        RadioButton radioButton = (RadioButton) dialog.findViewById(R.id.radioPersonalized);
        RadioButton radioButton2 = (RadioButton) dialog.findViewById(R.id.radioNonPersonalized);
        final RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.radioGroup);
        Button button = (Button) dialog.findViewById(R.id.btnOk);
        ((TextView) dialog.findViewById(R.id.txtTitle)).setText(str);
        ((TextView) dialog.findViewById(R.id.txtDesc1)).setText(str2);
        ((TextView) dialog.findViewById(R.id.txtDesc2)).setText(str3);
        ((TextView) dialog.findViewById(R.id.txtDesc3)).setText(str4);
        TextView textView = (TextView) dialog.findViewById(R.id.txtprivacy);
        textView.setText("Learn how " + context.getResources().getString(R.string.app_name) + " and our partners collect and use data.");
        textView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AdConstants.privacyuriparse(AdConstants.STR_PRIVACY_URI, context);
            }
        });
        ((Button) dialog.findViewById(R.id.btnCancel)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
                adsTwoButtonDialogListener2.onCancel();
            }
        });
        if (!z) {
            radioButton2.setChecked(ConsentInformation.getInstance(context).getConsentStatus() == ConsentStatus.NON_PERSONALIZED);
            if (ConsentInformation.getInstance(context).getConsentStatus() == ConsentStatus.PERSONALIZED) {
                z2 = true;
            }
            radioButton.setChecked(z2);
        }
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (radioGroup.getCheckedRadioButtonId() == R.id.radioPersonalized) {
                    adsTwoButtonDialogListener2.onOk(true);
                } else {
                    adsTwoButtonDialogListener2.onOk(false);
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void privacyuriparse(String str, Context context) {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(str));
        intent.addFlags(1208483840);
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException unused) {
            context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(AdConstants.STR_PRIVACY_URI)));
        }
    }

    public static void setnpa(Context context) {
        if (ConsentInformation.getInstance(context).getConsentStatus() == ConsentStatus.PERSONALIZED) {
            npaflag = false;
        }
        if (ConsentInformation.getInstance(context).getConsentStatus() == ConsentStatus.NON_PERSONALIZED) {
            npaflag = true;
        }
        Log.d("consentStatus setting", "" + ConsentInformation.getInstance(context).getConsentStatus());
    }

    public static void bannerad(com.google.android.gms.ads.AdView adViews, Context context) {

        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                //add
            }
        });
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        adViews.loadAd(adRequest);

    }

    public void loadNativeAd(final Activity context, final FrameLayout frameLayout) {
        refreshAd(context, frameLayout);
    }

    private void populateUnifiedNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {


        com.google.android.gms.ads.formats.MediaView mediaView = adView.findViewById(R.id.ad_media);
        adView.setMediaView(mediaView);


        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));


        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());


        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        adView.setNativeAd(nativeAd);

    }

    private void refreshAd(final Activity context, final FrameLayout frameLayout) {

        AdLoader.Builder builder = new AdLoader.Builder(context, context.getString(R.string.nativeid));

        builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {

            @Override
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {


                if (nativeAds != null) {
                    nativeAds.destroy();
                }
                nativeAds = unifiedNativeAd;
                UnifiedNativeAdView adView = (UnifiedNativeAdView) context.getLayoutInflater()
                        .inflate(R.layout.adunity, null);
                populateUnifiedNativeAdView(unifiedNativeAd, adView);
                frameLayout.removeAllViews();
                frameLayout.addView(adView);
            }

        });

        VideoOptions videoOptions = new VideoOptions.Builder()
                .setStartMuted(false)
                .build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build();

        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            /**      * @deprecated (when, why, refactoring advice...)      */
            @Deprecated
            public void onAdFailedToLoad(int errorCode) {
            }
        }).build();
        AdRequest adRequest = new AdRequest.Builder()
                .build();

        adLoader.loadAd(adRequest);

    }

    private UnifiedNativeAd nativeAds;

}
