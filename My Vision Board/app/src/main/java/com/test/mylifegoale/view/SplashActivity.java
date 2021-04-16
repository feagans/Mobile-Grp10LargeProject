package com.test.mylifegoale.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.exifinterface.media.ExifInterface;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.test.mylifegoale.R;
import com.test.mylifegoale.activities.HomeActivity;
import com.test.mylifegoale.base.roomDb.AppDatabase;
import com.test.mylifegoale.dailyAlarm.AlarmUtil;
import com.test.mylifegoale.itemClick.OnAsyncBackground;
import com.test.mylifegoale.model.AffirmationRowModel;
import com.test.mylifegoale.model.CategoryModel;
import com.test.mylifegoale.model.FolderRowModel;
import com.test.mylifegoale.model.LifePurposeModel;
import com.test.mylifegoale.utilities.AdConstants;
import com.test.mylifegoale.utilities.AppConstants;
import com.test.mylifegoale.utilities.AppPref;
import com.test.mylifegoale.utilities.BackgroundAsync;
import com.test.mylifegoale.utilities.Constants;

public class SplashActivity extends AppCompatActivity {
    boolean Ad_Show = true;
    AffirmationRowModel affirmationRowModel;
    AppDatabase appDatabase;

    public Context context;
    private AppDatabase db;
    FolderRowModel folderRowModel;

    int rowCount = 0;
    int rowCountAll = 0;
    long rowIdFolder = 0;

    public SplashActivity() {
        AdConstants.adCount = 0;
    }

    public void onCreate(Bundle bundle) {
        try {
            requestWindowFeature(1);
            getWindow().setFlags(1024, 1024);
            super.onCreate(bundle);
            setContentView((int) R.layout.activity_splash);
            this.context = this;
            this.db = AppDatabase.getAppDatabase(this);
            ((TextView) findViewById(R.id.versionApp)).setText(AppConstants.getVersion(this.context));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (AppPref.isFirstLaunch(this.context)) {
            this.Ad_Show = true;
            insertDefaultDataList();
        }
        if (!AppPref.getIsAdfree(this.context)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (Ad_Show) {
                        openMainActivity();
                    }
                }
            }, 3000);
            MobileAds.initialize(this.context, (OnInitializationCompleteListener) new OnInitializationCompleteListener() {
                public void onInitializationComplete(InitializationStatus initializationStatus) {
                }
            });
            return;
        }
        this.Ad_Show = true;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Ad_Show) {
                    openMainActivity();
                }
            }
        }, 3000);
    }

    private void insertDefaultDataList() {
        new BackgroundAsync(this, false, "", new OnAsyncBackground() {
            public void onPreExecute() {
            }

            public void doInBackground() {
                insertDefaultList();
            }

            public void onPostExecute() {
                AlarmUtil.setAllAlarm(context);
                AppPref.setIsFirstLaunch(context, false);
            }
        }).execute(new Object[0]);
    }


    public void insertDefaultList() {
        this.appDatabase = AppDatabase.getAppDatabase(this);
        this.appDatabase.lifePurposeDao().insert(new LifePurposeModel("1", "My Purpose", Constants.your_purpose));
        this.appDatabase.lifePurposeDao().insert(new LifePurposeModel(ExifInterface.GPS_MEASUREMENT_2D, "My Vision", Constants.my_vision));
        this.appDatabase.lifePurposeDao().insert(new LifePurposeModel(ExifInterface.GPS_MEASUREMENT_3D, "My Goals", Constants.my_goals));
        AppPref.setIsFirstLaunch(this, false);
        this.appDatabase.categoryDAO().insert(new CategoryModel(AppConstants.getUniqueId(), "Health", true));
        this.appDatabase.categoryDAO().insert(new CategoryModel(AppConstants.getUniqueId(), "Job", true));
        this.appDatabase.categoryDAO().insert(new CategoryModel(AppConstants.getUniqueId(), "Career", true));
        this.appDatabase.categoryDAO().insert(new CategoryModel(AppConstants.getUniqueId(), "Happiness", true));
        this.appDatabase.categoryDAO().insert(new CategoryModel(AppConstants.getUniqueId(), Constants.FOLDER_IMAGE_TYPE_WEALTH, true));
        this.rowCount = 0;
        this.folderRowModel = new FolderRowModel(AppConstants.getUniqueId(), Constants.FOLDER_IMAGE_TYPE_Gratitude, Constants.FOLDER_IMAGE_TYPE_Gratitude, Constants.FOLDER_COLOR_TYPE_Gratitude, (int) this.rowIdFolder, true);
        try {
            this.rowIdFolder = this.db.folderDao().insert(this.folderRowModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (this.rowIdFolder > 0) {
            insertAffirmation("I am thankful to the Universe for everything in my life.", true);
            insertAffirmation("Every person on this planet enriches my life in some way or other. I am thankful to all.", false);
            insertAffirmation("I am so grateful for supportive friends and a loving family.", false);
            insertAffirmation("I am eternally grateful for the love I am capable of giving, and for the love I have yet to receive.", false);
            insertAffirmation("My heart is filled with happiness and gratitude.", false);
            insertAffirmation("I experience gratitude for everything I have in my life.", true);
            insertAffirmation("I am grateful for excellent health, prosperity and true love.", false);
            insertAffirmation("I am grateful for the abundance in my life.", false);
            insertAffirmation("Thank you, Universe for the fun, wealth, joy, and fulfillment that is on the way.", false);
            insertAffirmation("I am grateful for the beauty that I see around me and for the beauty within me.", true);
        }
        this.rowCount = 0;
        this.folderRowModel = new FolderRowModel(AppConstants.getUniqueId(), "Success", "Success", Constants.FOLDER_COLOR_TYPE_Success, (int) this.rowIdFolder, true);
        try {
            this.rowIdFolder = this.db.folderDao().insert(this.folderRowModel);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        if (this.rowIdFolder > 0) {
            insertAffirmation("I have the power to create all the success and prosperity I desire.", true);
            insertAffirmation("My mind is free of resistance and open to exciting new possibilities.", false);
            insertAffirmation("I am worthy of all the good life has to offer, and I deserve to be successful.", false);
            insertAffirmation("I believe in myself and my ability to succeed.", false);
            insertAffirmation("The universe is filled with endless opportunities for me and my career.", false);
            insertAffirmation("I am always open minded and eager to explore new avenues to success.", false);
            insertAffirmation("I recognize opportunity when it knocks and seize the moment.", false);
            insertAffirmation("I am committed to achieving success in every area of my life.", true);
            insertAffirmation("By creating success for myself I am creating success and opportunities for others.", false);
            insertAffirmation("I recognize every new challenge as a new opportunity.", false);
            insertAffirmation("Every day I dress for success in body, mind, and spirit.", false);
            insertAffirmation("I have released all limiting beliefs about my ability to succeed.", false);
            insertAffirmation("I have an endless supply of new ideas that help me become more and more successful.", false);
            insertAffirmation("I always expect a positive outcome and I naturally attract good results.", false);
            insertAffirmation("I trust my intuition and am always guided to make wise decisions.", false);
        }
        this.rowCount = 0;
        this.folderRowModel = new FolderRowModel(AppConstants.getUniqueId(), Constants.FOLDER_IMAGE_TYPE_Confidence, Constants.FOLDER_IMAGE_TYPE_Confidence, "#9ebef1", (int) this.rowIdFolder, true);
        try {
            this.rowIdFolder = this.db.folderDao().insert(this.folderRowModel);
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        if (this.rowIdFolder > 0) {
            insertAffirmation("Self-confidence is what I thrive on. Nothing is impossible and life is great.", true);
            insertAffirmation("I am confident", false);
            insertAffirmation("I am strong and powerful", false);
            insertAffirmation("I am outgoing and confident in social situations", false);
            insertAffirmation("I believe in myself", false);
            insertAffirmation("I always express my thoughts and opinions with confidence", true);
            insertAffirmation("I have unbreakable confidence within myself", false);
            insertAffirmation("Confidence empowers me to take action and live life to the fullest", false);
            insertAffirmation("Feeling confident, assured, and strong is a normal part of my everyday life", false);
        }
        this.rowCount = 0;
        this.folderRowModel = new FolderRowModel(AppConstants.getUniqueId(), Constants.FOLDER_IMAGE_TYPE_Self_Esteem, Constants.FOLDER_IMAGE_TYPE_Self_Esteem, Constants.FOLDER_COLOR_TYPE_Self_Esteem, (int) this.rowIdFolder, true);
        try {
            this.rowIdFolder = this.db.folderDao().insert(this.folderRowModel);
        } catch (Exception e4) {
            e4.printStackTrace();
        }
        if (this.rowIdFolder > 0) {
            insertAffirmation("I have high self esteem", false);
            insertAffirmation("I love and respect myself", true);
            insertAffirmation("My thoughts and opinions are valuable", false);
            insertAffirmation("I am confident that I can achieve anything", false);
            insertAffirmation("I have something special to offer the world", true);
            insertAffirmation("I feel great about myself and my life", false);
            insertAffirmation("I am worthy of having high self esteem", false);
            insertAffirmation("Feeling good about myself is normal for me", false);
            insertAffirmation("I am self-reliant, creative and persistent in whatever I do.", false);
            insertAffirmation("I radiate love and inspire people.", false);
        }
        this.rowCount = 0;
        this.folderRowModel = new FolderRowModel(AppConstants.getUniqueId(), Constants.FOLDER_IMAGE_TYPE_Decision_Making, Constants.FOLDER_IMAGE_TYPE_Decision_Making, Constants.FOLDER_COLOR_TYPE_Decision_Making, (int) this.rowIdFolder, true);
        try {
            this.rowIdFolder = this.db.folderDao().insert(this.folderRowModel);
        } catch (Exception e5) {
            e5.printStackTrace();
        }
        if (this.rowIdFolder > 0) {
            insertAffirmation("I am good at making decisions", false);
            insertAffirmation("I think through all of my options carefully", false);
            insertAffirmation("I analyze all possible outcomes", false);
            insertAffirmation("I invest an appropriate amount of time in the thought process", true);
            insertAffirmation("I am aware of many components when making decisions", false);
            insertAffirmation("I am mindful of how my decisions will affect my surroundings", false);
            insertAffirmation("I have confidence in my decisions", false);
            insertAffirmation("I take total responsibility for myself and the choices and decisions I make for myself", false);
            insertAffirmation("I am confident in making right choices", false);
            insertAffirmation("My inner wisdom guides me to make right decisions and to take the steps necessary to achieve my aspirations", false);
        }
        this.rowCount = 0;
        this.folderRowModel = new FolderRowModel(AppConstants.getUniqueId(), Constants.FOLDER_IMAGE_TYPE_Abundance, Constants.FOLDER_IMAGE_TYPE_Abundance, Constants.FOLDER_COLOR_TYPE_Abundance, (int) this.rowIdFolder, true);
        try {
            this.rowIdFolder = this.db.folderDao().insert(this.folderRowModel);
        } catch (Exception e6) {
            e6.printStackTrace();
        }
        if (this.rowIdFolder > 0) {
            insertAffirmation("I deserve prosperity!", true);
            insertAffirmation("I am surrounded by wealth.", false);
            insertAffirmation("I am open and receptive to all the wealth life offers me.", false);
            insertAffirmation("I live an abundant life.", true);
            insertAffirmation("My life is full of abundance", false);
            insertAffirmation("Money comes to me easily and effortlessly", false);
            insertAffirmation("I am increasingly magnetic to health, wealth, abundance, prosperity, and money.", false);
            insertAffirmation("I am open and ready to attract abundance into my life.", false);
            insertAffirmation("I am open to receiving limitless abundance.", false);
            insertAffirmation("I am living my life in a state of complete abundance", false);
        }
        this.rowCount = 0;
        this.folderRowModel = new FolderRowModel(AppConstants.getUniqueId(), Constants.FOLDER_IMAGE_TYPE_Attitude, Constants.FOLDER_IMAGE_TYPE_Attitude, Constants.FOLDER_COLOR_TYPE_Attitude, (int) this.rowIdFolder, true);
        try {
            this.rowIdFolder = this.db.folderDao().insert(this.folderRowModel);
        } catch (Exception e7) {
            e7.printStackTrace();
        }
        if (this.rowIdFolder > 0) {
            insertAffirmation("I can accomplish anything I set my mind on", true);
            insertAffirmation("I have the energy and passion to make my thoughts reality", true);
            insertAffirmation("I am a creative genius", false);
            insertAffirmation("Everything I touch is a success", false);
            insertAffirmation("Every day is a perfect day for me.", false);
            insertAffirmation("Everything is super in my life", false);
            insertAffirmation("I always see the bright side in life.", false);
            insertAffirmation("I am a radiant being and enjoy life to its fullest", false);
            insertAffirmation("I am passionate about life!", false);
            insertAffirmation("I am unstoppable!", false);
        }
        this.rowCount = 0;
        this.folderRowModel = new FolderRowModel(AppConstants.getUniqueId(), Constants.FOLDER_IMAGE_TYPE_Business, Constants.FOLDER_IMAGE_TYPE_Business, Constants.FOLDER_COLOR_TYPE_Business, (int) this.rowIdFolder, true);
        try {
            this.rowIdFolder = this.db.folderDao().insert(this.folderRowModel);
        } catch (Exception e8) {
            e8.printStackTrace();
        }
        if (this.rowIdFolder > 0) {
            insertAffirmation("I create wonderful business opportunities.", true);
            insertAffirmation("I am passionate about my business and that shows in everything I do.", true);
            insertAffirmation("My business allows me to have a life I love.", false);
            insertAffirmation("I am energized by my business.", false);
            insertAffirmation("I love the freedom my business provides for me.", false);
            insertAffirmation("My business dreams are constantly manifesting", false);
            insertAffirmation("I am a perfect match for my ideal business.", false);
            insertAffirmation("I am thankful for each and every person who has contributed to the success of my business.", false);
            insertAffirmation("My business is a huge success", false);
            insertAffirmation("I can achieve any goals I set myself in my business.", false);
        }
        this.rowCount = 0;
        this.folderRowModel = new FolderRowModel(AppConstants.getUniqueId(), Constants.FOLDER_IMAGE_TYPE_Love, Constants.FOLDER_IMAGE_TYPE_Love, Constants.FOLDER_COLOR_TYPE_Love, (int) this.rowIdFolder, true);
        try {
            this.rowIdFolder = this.db.folderDao().insert(this.folderRowModel);
        } catch (Exception e9) {
            e9.printStackTrace();
        }
        if (this.rowIdFolder > 0) {
            insertAffirmation("Love follows me everywhere I go", true);
            insertAffirmation("My heart is always open to love.", false);
            insertAffirmation("I am attracting my soulmate.", true);
            insertAffirmation("I have the power to give love endlessly.", false);
            insertAffirmation("I am constantly surrounded by love.", false);
            insertAffirmation("I welcome love with open arms.", false);
            insertAffirmation("I naturally attract love everywhere I go.", false);
            insertAffirmation("The more I desire someone the more they become attracted to me.", false);
            insertAffirmation("I draw love and romance into my life with ease.", false);
            insertAffirmation("My relationships are always safe and fulfilling.", false);
        }

        this.rowCount = 0;
        this.folderRowModel = new FolderRowModel(AppConstants.getUniqueId(), Constants.FOLDER_IMAGE_TYPE_Family, Constants.FOLDER_IMAGE_TYPE_Family, Constants.FOLDER_COLOR_TYPE_Family, (int) this.rowIdFolder, true);
        try {
            this.rowIdFolder = this.db.folderDao().insert(this.folderRowModel);
        } catch (Exception e10) {
            e10.printStackTrace();
        }
        if (this.rowIdFolder > 0) {
            insertAffirmation("I see my family through the eyes of love.", true);
            insertAffirmation("I am grateful for every member of my family.", false);
            insertAffirmation("I accept my family members just as they are.", false);
            insertAffirmation("I am grateful that my family accepts me for who I am.", true);
            insertAffirmation("In the presence of my family I feel safe, peaceful, and content.", false);
            insertAffirmation("My family and I communicate with each other freely and openly.", false);
            insertAffirmation("Every day I send the energy of love, light, and happiness to each member of my family.", false);
            insertAffirmation("Every person in my family is healthy and happy.", false);
            insertAffirmation("There is peace and harmony in my family.", false);
            insertAffirmation("I am loved and respected by my family.", false);
        }
        this.rowCount = 0;
        this.folderRowModel = new FolderRowModel(AppConstants.getUniqueId(), Constants.FOLDER_IMAGE_TYPE_Relationship, Constants.FOLDER_IMAGE_TYPE_Relationship, Constants.FOLDER_COLOR_TYPE_Relationship, (int) this.rowIdFolder, true);
        try {
            this.rowIdFolder = this.db.folderDao().insert(this.folderRowModel);
        } catch (Exception e11) {
            e11.printStackTrace();
        }
        if (this.rowIdFolder > 0) {
            insertAffirmation("I have found the right partner for me, who loves me very much.", true);
            insertAffirmation("I am so happy for having a wonderful and loving partner.", false);
            insertAffirmation("My spouse loves me and accepts me as I am.", false);
            insertAffirmation("I have a loving, understanding and supportive partner.", false);
            insertAffirmation("It is so pleasant and fun to be with my life partner.", true);
            insertAffirmation("My partner and I love and fully trust each other. Life is so wonderful.", false);
            insertAffirmation("My partner and I are both happy and in love. Our relationship is joyous.", false);
            insertAffirmation("I am in a loving relationship.", false);
            insertAffirmation("My partner effortlessly senses my love", false);
            insertAffirmation("My partner believes in me and supports me fully.", false);
            insertAffirmation("I always express my love feelings openly to my partner.", false);
        }
        this.rowCount = 0;
        this.folderRowModel = new FolderRowModel(AppConstants.getUniqueId(), Constants.FOLDER_IMAGE_TYPE_Forgiveness, Constants.FOLDER_IMAGE_TYPE_Forgiveness, Constants.FOLDER_COLOR_TYPE_Forgiveness, (int) this.rowIdFolder, true);
        try {
            this.rowIdFolder = this.db.folderDao().insert(this.folderRowModel);
        } catch (Exception e12) {
            e12.printStackTrace();
        }
        if (this.rowIdFolder > 0) {
            insertAffirmation("I am not perfect and make mistakes and that’s okay.", true);
            insertAffirmation("I accept myself for who I am.", false);
            insertAffirmation("I release all judgement, guilt, and shame.", true);
            insertAffirmation("I own my mistakes and forgive myself.", false);
            insertAffirmation("I love and accept myself, fully and unconditionally.", false);
            insertAffirmation("Forgiveness will heal my heart and release all pain and sadness.", false);
            insertAffirmation("I forgive those who have wronged me in order to live a life full of peace, love, and joy.", false);
            insertAffirmation("My happiness is more important than being hurt and holding a grudge.", true);
            insertAffirmation("I feel compassion for those who choose to act out of integrity.", false);
            insertAffirmation("I choose to be free of hurt, anger, and hatred.", false);
        }
        this.rowCount = 0;
        this.folderRowModel = new FolderRowModel(AppConstants.getUniqueId(), Constants.FOLDER_IMAGE_TYPE_Law_of_attraction, Constants.FOLDER_IMAGE_TYPE_Law_of_attraction, Constants.FOLDER_COLOR_TYPE_Law_of_attraction, (int) this.rowIdFolder, true);
        try {
            this.rowIdFolder = this.db.folderDao().insert(this.folderRowModel);
        } catch (Exception e13) {
            e13.printStackTrace();
        }
        if (this.rowIdFolder > 0) {
            insertAffirmation("I am increasingly confident in my ability to create the life I desire.", false);
            insertAffirmation("I am acting on inspiration and insights and I trust my inner guidance.", true);
            insertAffirmation("I am constantly striving to raise my vibration through good thoughts, words and actions.", false);
            insertAffirmation("I am willing to believe that I am the creator of my life experience.", false);
            insertAffirmation("I am worthy of love, abundance, success, happiness and fulfillment.", true);
            insertAffirmation("I am willing to believe that by raising my vibration, I will attract more of what I desire.", false);
            insertAffirmation("I am willing to believe that by focusing on feeling good, I make better choices that lead to desired results.", false);
            insertAffirmation("I am creating my life according to my dominant beliefs; and I am improving the quality of those beliefs.", false);
            insertAffirmation("I am giving and receiving all that is good and all that I desire.", false);
            insertAffirmation("I am making a meaningful contribution to the world and I am wonderfully compensated for my contribution.", false);
        }
        this.rowCount = 0;
        this.folderRowModel = new FolderRowModel(AppConstants.getUniqueId(), "Health", "Health", Constants.FOLDER_COLOR_TYPE_Health, (int) this.rowIdFolder, true);
        try {
            this.rowIdFolder = this.db.folderDao().insert(this.folderRowModel);
        } catch (Exception e14) {
            e14.printStackTrace();
        }
        if (this.rowIdFolder > 0) {
            insertAffirmation("I am strong and healthy.", false);
            insertAffirmation("My body gets all the nutrients it needs.", true);
            insertAffirmation("I have a strong urge to eat only health-giving and nutritious foods.", false);
            insertAffirmation("I am attaining and maintaining my ideal weight.", false);
            insertAffirmation("I am dedicated to improving my health", true);
            insertAffirmation("I am resilient against illness", false);
            insertAffirmation("My immune system is strong", false);
            insertAffirmation("I always make healthy choices", false);
            insertAffirmation("I have a healthy mind body connection", false);
            insertAffirmation("I look younger, more attractive and healthier every day.", false);
        }
        this.rowCount = 0;
        this.folderRowModel = new FolderRowModel(AppConstants.getUniqueId(), Constants.FOLDER_IMAGE_TYPE_Exercise, Constants.FOLDER_IMAGE_TYPE_Exercise, Constants.FOLDER_COLOR_TYPE_Exercise, (int) this.rowIdFolder, true);
        try {
            this.rowIdFolder = this.db.folderDao().insert(this.folderRowModel);
        } catch (Exception e15) {
            e15.printStackTrace();
        }
        if (this.rowIdFolder > 0) {
            insertAffirmation("I am motivated to exercise", false);
            insertAffirmation("I always stick to my exercise plan", false);
            insertAffirmation("I exercise every day and I love it", true);
            insertAffirmation("I always finish all my exercises", false);
            insertAffirmation("I am totally focused on getting myself in shape", false);
            insertAffirmation("I stay motivated throughout my entire workout routine", false);
            insertAffirmation("It feels great when I exercise regularly and take care of myself", false);
            insertAffirmation("I am the kind of person who just loves pushing myself during my workout", false);
            insertAffirmation("I find it easy to get pumped up about going to the gym every day", false);
            insertAffirmation("My motivation to exercise is helping me to achieve optimum health", false);
        }
        this.rowCount = 0;
        this.folderRowModel = new FolderRowModel(AppConstants.getUniqueId(), Constants.FOLDER_IMAGE_TYPE_Pregnancy, Constants.FOLDER_IMAGE_TYPE_Pregnancy, Constants.FOLDER_COLOR_TYPE_Pregnancy, (int) this.rowIdFolder, true);
        try {
            this.rowIdFolder = this.db.folderDao().insert(this.folderRowModel);
        } catch (Exception e16) {
            e16.printStackTrace();
        }
        if (this.rowIdFolder > 0) {
            insertAffirmation("I am healthy and flying through my pregnancy.", true);
            insertAffirmation("My baby is safe and loved", false);
            insertAffirmation("My baby is happy and healthy", false);
            insertAffirmation("I am grateful for my pregnancy.", false);
            insertAffirmation("I have a natural ability to give birth easily", false);
            insertAffirmation("I am deeply attached to my baby.", true);
            insertAffirmation("I am a strong and healthy woman.", false);
            insertAffirmation("My baby is healthy, relaxed and calm.", false);
            insertAffirmation("I choose to be relaxed and calm during my pregnancy.", false);
            insertAffirmation("My baby feels my love.", false);
            insertAffirmation("My body knows how and when to give birth my baby.", false);
            insertAffirmation("I have an amazing support system! When I ask for help, I receive help.", false);
            insertAffirmation("My body is beautifully nourishing the child I carry. My child is perfectly healthy.", false);
        }
        this.rowCount = 0;
        this.folderRowModel = new FolderRowModel(AppConstants.getUniqueId(), Constants.FOLDER_IMAGE_TYPE_Beauty, Constants.FOLDER_IMAGE_TYPE_Beauty, Constants.FOLDER_COLOR_TYPE_Beauty, (int) this.rowIdFolder, true);
        try {
            this.rowIdFolder = this.db.folderDao().insert(this.folderRowModel);
        } catch (Exception e17) {
            e17.printStackTrace();
        }
        if (this.rowIdFolder > 0) {
            insertAffirmation("I am beautiful.", false);
            insertAffirmation("My body is gift and I cherish it daily.", true);
            insertAffirmation("My hair is healthy and beautiful.", false);
            insertAffirmation("My skin is healthy and glowing.", true);
            insertAffirmation("The universe is blessing me that enhance my beauty.", false);
            insertAffirmation("My environment radiates beauty.", false);
            insertAffirmation("I am beautiful and have a pleasing personality.", false);
            insertAffirmation("I have a lovely smile and a beautiful face.", false);
            insertAffirmation("Every day I am transforming into a natural beauty", true);
            insertAffirmation("I am a beautiful person, inside and out.", false);
        }
        this.rowCount = 0;
        this.folderRowModel = new FolderRowModel(AppConstants.getUniqueId(), Constants.FOLDER_IMAGE_TYPE_Women, Constants.FOLDER_IMAGE_TYPE_Women, Constants.FOLDER_COLOR_TYPE_Women, (int) this.rowIdFolder, true);
        try {
            this.rowIdFolder = this.db.folderDao().insert(this.folderRowModel);
        } catch (Exception e18) {
            e18.printStackTrace();
        }
        if (this.rowIdFolder > 0) {
            insertAffirmation("I love myself, respect myself and accept myself exactly as I am.", true);
            insertAffirmation("I deserve to be happy and loved.", false);
            insertAffirmation("I am doing my best and it is enough.", true);
            insertAffirmation("I love myself and treat myself with kindness.", false);
            insertAffirmation("I am responsible for what happens to my body, so I treat it with love, respect, and care.", false);
            insertAffirmation("I am a strong, confident woman and will only continue to become stronger.", false);
            insertAffirmation("I am beautiful and I am worthy of every beautiful thing in this world.", false);
            insertAffirmation("I ask for what I want because I deserve it. I honor my desires today and always.", false);
            insertAffirmation("I trust my own wisdom and intuition. I am the only person who knows what is best for me.", false);
            insertAffirmation("I am constantly amazed by my body and its abilities.", true);
        }
    }

    private void insertAffirmation(String str, boolean z) {
        this.affirmationRowModel = new AffirmationRowModel(AppConstants.getUniqueId(), str, "", "", z, this.folderRowModel.getId(), this.rowCount, z ? this.rowCountAll : -1);
        try {
            this.db.affirmationDao().insert(this.affirmationRowModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.rowCount++;
        if (z) {
            this.rowCountAll++;
        }
    }


    public void openMainActivity() {
        try {
            this.Ad_Show = false;
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
