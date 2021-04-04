package com.test.mylifegoale.baseClass;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivityBinding extends AppCompatActivity implements View.OnClickListener {
    public Context context;


    public abstract void initMethods();


    public abstract void setBinding();


    public abstract void setOnClicks();


    public abstract void setToolbar();


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.context = this;
        setBinding();
        setToolbar();
        setOnClicks();
        initMethods();
    }
}
