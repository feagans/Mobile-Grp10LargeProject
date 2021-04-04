package com.test.mylifegoale.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.test.mylifegoale.R;
import com.test.mylifegoale.utilities.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class DownloadActivity extends AppCompatActivity implements View.OnClickListener {


    private RecyclerView recyclerView;
    public ArrayList<ImageDetails> arListGallery;
    private RecyclerView.Adapter mAdapter;

    private GridLayoutManager layoutManager;
    public static ProgressDialog dia;
    LinearLayout LL_NoDataFound;


    int DisplayWidth;

    public void showProgress() {

        dia = new ProgressDialog(this);
        dia.setMessage("Loading ...");
        dia.setIndeterminate(false);
        dia.setCancelable(false);
        dia.setCanceledOnTouchOutside(false);
        dia.show();
    }

    private void fillData() {

        try {
            String path = Environment.getExternalStorageDirectory() + File.separator + Constants.PATH_IMAGE;
            File f = new File(path);
            File file[] = f.listFiles();
            if (file.length > 0) {
                for (int i = 0; i < file.length; i++) {
                    try {
                        ImageDetails object = new ImageDetails();
                        object.ImageName = file[i].getName();
                        object.uri = Uri.fromFile(file[i]);
                        arListGallery.add(object);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (NullPointerException e) {
            LL_NoDataFound.setVisibility(View.VISIBLE);
        } catch (Exception e) {
        }

    }

    public void dismissProgress() {
        if (dia.isShowing())
            dia.dismiss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);


        Display display = getWindowManager().getDefaultDisplay();
        DisplayWidth = display.getWidth();

        ImageView imgButtonImage = (ImageView) findViewById(R.id.imgButtonImage);
        imgButtonImage.setOnClickListener(this);


        LL_NoDataFound = (LinearLayout) findViewById(R.id.LL_NoDataFound);
        LL_NoDataFound.setOnClickListener(this);
        showProgress();

        arListGallery = new ArrayList<ImageDetails>();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        fillData();

        Collections.reverse(arListGallery);

        if (arListGallery.size() == 0) {
            LL_NoDataFound.setVisibility(View.VISIBLE);
            dismissProgress();
        } else {
            LL_NoDataFound.setVisibility(View.GONE);
            mAdapter = new GalleryViewAdapter();
            recyclerView.setAdapter(mAdapter);
            dismissProgress();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.LL_NoDataFound:
                onBackPressed();
                break;
            case R.id.imgButtonImage:
                onBackPressed();
                break;
        }
    }


    class ImageDetails {
        String ImageName;
        Uri uri;
    }

    class GalleryViewAdapter extends RecyclerView.Adapter<GalleryViewAdapter.MyViewHolder> {

        public class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView imageViewIcon;

            public MyViewHolder(View itemView) {
                super(itemView);
                this.imageViewIcon = (ImageView) itemView.findViewById(R.id.ThemePreviewImage);


                this.imageViewIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {

                        Intent intent = new Intent(DownloadActivity.this, FullView.class);
                        intent.putExtra("FileName", "" + arListGallery.get(Integer.parseInt(v.getTag().toString())).ImageName);
                        startActivity(intent);

                    }
                });
            }
        }

        public GalleryViewAdapter() {
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_view_row, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

            Log.e("position", "" + listPosition);

            holder.imageViewIcon.setLayoutParams(new FrameLayout.LayoutParams(DisplayWidth / 3, DisplayWidth / 3));

            if (listPosition == 4) {
                holder.imageViewIcon.setImageURI(arListGallery.get(listPosition).uri);

            } else {
                holder.imageViewIcon.setImageURI(arListGallery.get(listPosition).uri);
            }

            holder.imageViewIcon.setTag("" + listPosition);

        }

        @Override
        public int getItemCount() {
            return arListGallery.size();
        }
    }
}
