package com.test.mylifegoale.baseClass;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.test.mylifegoale.itemClick.OnFragmentInteractionListener;

public abstract class BaseFragmentRecyclerBinding extends Fragment implements View.OnClickListener {
    public Context context;


    public abstract void callApi();


    public abstract void fillData();


    public abstract View getViewBinding();


    public abstract void initMethods();


    public abstract void setBinding(LayoutInflater layoutInflater, ViewGroup viewGroup);


    public abstract void setOnClicks();


    public abstract void setRecycler();


    public abstract void setToolbar();

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.context = getActivity();
        setBinding(layoutInflater, viewGroup);
        setToolbar();
        setOnClicks();
        callApi();
        fillData();
        setRecycler();
        initMethods();
        return getViewBinding();
    }

    public void onAttach(Context context2) {
        super.onAttach(context2);
        if (context2 instanceof OnFragmentInteractionListener) {
            return;
        }
        throw new RuntimeException(context2.toString() + " must implement OnFragmentInteractionListener");
    }

    public void onDetach() {
        super.onDetach();
    }
}
