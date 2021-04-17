package com.test.mylifegoale.activities;

import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.test.mylifegoale.R;
import com.test.mylifegoale.adapters.VisionAdapter;
import com.test.mylifegoale.base.BaseActivity;
import com.test.mylifegoale.base.roomDb.AppDatabase;
import com.test.mylifegoale.data.APIService;
import com.test.mylifegoale.data.model.BucketComponents;
import com.test.mylifegoale.data.model.LoggedInUser;
import com.test.mylifegoale.databinding.ActivityVisionBinding;
import com.test.mylifegoale.itemClick.OnAsyncBackground;
import com.test.mylifegoale.model.AffirmationRowModel;
import com.test.mylifegoale.model.VisionModel;
import com.test.mylifegoale.utilities.AdConstants;
import com.test.mylifegoale.utilities.BackgroundAsync;
import com.test.mylifegoale.utilities.Constants;
import com.test.mylifegoale.utilities.SwipeAndDragHelper;
import com.test.mylifegoale.view.AffirmPlayerActivity;

import org.apache.commons.io.IOUtils;

import java.util.ArrayList;
import java.util.Iterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VisionActivity extends BaseActivity {
    ArrayList<AffirmationRowModel> affirmationRowList = new ArrayList<>();
    AppDatabase appDatabase;
    ActivityVisionBinding binding;
    VisionAdapter visionAdapter;
    ArrayList<VisionModel> visionModelArrayList = new ArrayList<>();
    boolean empty = true;

    // API
    public Retrofit retrofit;
    public APIService.API API;
    private static VisionActivity mInstance;
    public APIService.AllBucketListsResponse bucketLists;

    public void setBinding() {
        this.binding = (ActivityVisionBinding) DataBindingUtil.setContentView(this, R.layout.activity_vision);
    }

    public void init() {
        // API do we need to reconnect to API each call??
        mInstance = this;
        this.retrofit = new Retrofit.Builder()
                .baseUrl("https://letsbuckit.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.API = retrofit.create(APIService.API.class);


        this.appDatabase = AppDatabase.getAppDatabase(this);
        this.binding.visionList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        getData();
        AdConstants.bannerad(this.binding.llads, this);

    }


    public void setDefaultLayout() {
        if (!empty) {
            this.binding.defaultMsglayout.setVisibility(View.GONE);
            this.binding.bottomLayout.setVisibility(View.VISIBLE);
            return;
        }

        this.binding.defaultMsglayout.setVisibility(View.VISIBLE);
        this.binding.bottomLayout.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_save, menu);
        menu.findItem(R.id.addAction).setVisible(true);
        menu.findItem(R.id.imgNote).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    private void getData() {
        Log.d("taggy", "getting data for user: " + LoggedInUser.getUserId());

        try {
            APIService.AllBucketListsRequest bucketLists = new APIService.AllBucketListsRequest(LoggedInUser.getUserId());
            API.allBuckets(bucketLists).enqueue(new Callback<APIService.AllBucketListsResponse>() {

                @Override
                public void onResponse(Call<APIService.AllBucketListsResponse> call, Response<APIService.AllBucketListsResponse> response) {
                    Log.d("taggy", "Status Code = " + response.code());
                    APIService.AllBucketListsResponse userBucketData = response.body();
                    Log.d("taggy", userBucketData.error);

                    // If there are bucket lists in DB will return error = ""
                    if (userBucketData.error.equals("")){
                        empty = false;
                        setDefaultLayout();
                        // Create list of all bucket lists
                        ArrayList<BucketComponents> listy = userBucketData.results;

                        // Get caption of first bucketlist item
                        Log.d("taggy", listy.get(0).caption);

                        // Number of bucket lists user has in DB
                        Log.d("taggy", String.valueOf(listy.size()));
                        for (int i = 0; i < listy.size(); i++){
                            VisionModel vm = new VisionModel();
                            BucketComponents listyItem = listy.get(i);
                            vm.setName(listyItem.getItemTitle());
                            vm.setId(listyItem.getID());
                            visionModelArrayList.add(vm);
                        }
                    }
                }

                @Override
                public void onFailure(Call<APIService.AllBucketListsResponse> call, Throwable t) {
                    Log.d("TAGGYTAG", "api failing!");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        new BackgroundAsync(this, true, "", new OnAsyncBackground() {
            public void onPreExecute() {
                Log.i("BackgroundAsync", "onPreExecute: ");
                visionModelArrayList = new ArrayList<>();
            }

            public void doInBackground() {
                Log.i("BackgroundAsync", "doInBackground: ");
                visionModelArrayList.addAll(appDatabase.visionDao().getAll());
            }

            public void onPostExecute() {
                VisionActivity visionActivity = VisionActivity.this;
                visionActivity.visionAdapter = new VisionAdapter(visionActivity, visionActivity.visionModelArrayList);
                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeAndDragHelper(visionAdapter));
                visionAdapter.setTouchHelper(itemTouchHelper);
                binding.visionList.setAdapter(visionAdapter);
                itemTouchHelper.attachToRecyclerView(binding.visionList);
            }
        }).execute(new Object[0]);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.addAction) {
            startActivityForResult(new Intent(this, AddGoalActivity.class), 100);
        }

        return super.onOptionsItemSelected(menuItem);
    }

    public void setToolbar() {
        setToolbarTitle(getString(R.string.vision_board));
        setToolbarBack(true);
    }


    @Override
    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 100 && intent != null) {
            try {
                if (intent.getBooleanExtra(Constants.EDIT_ADD_VISION_TAG, false)) {
                    int indexOf = this.visionModelArrayList.indexOf((VisionModel) intent.getParcelableExtra(Constants.VISION_DATA_TAG));
                    if (intent.getBooleanExtra(Constants.DELETE_VISION_TAG, false)) {
                        this.visionModelArrayList.remove(indexOf);
                        this.visionAdapter.notifyItemRemoved(indexOf);
                        setDefaultLayout();
                        return;
                    }
                    this.visionModelArrayList.set(indexOf, (VisionModel) intent.getParcelableExtra(Constants.VISION_DATA_TAG));
                    this.binding.visionList.getAdapter().notifyItemChanged(indexOf);
                    return;
                }
                this.visionModelArrayList.add((VisionModel) intent.getParcelableExtra(Constants.VISION_DATA_TAG));
                this.visionAdapter.notifyDataSetChanged();
                setDefaultLayout();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onClick(View view) {
        new BackgroundAsync(this, true, "", new OnAsyncBackground() {
            public void onPreExecute() {
                affirmationRowList.clear();
            }

            public void doInBackground() {
                Iterator<VisionModel> it = visionModelArrayList.iterator();
                while (it.hasNext()) {
                    VisionModel next = it.next();
                    String str = next.getName() + IOUtils.LINE_SEPARATOR_UNIX + next.getDescription();
                    Log.i("doInBackground", "doInBackground: " + str);
                    affirmationRowList.add(new AffirmationRowModel(str, next.getVisionProfile(), ""));
                }
            }

            public void onPostExecute() {
                if (affirmationRowList.size() > 0) {
                    Intent intent = new Intent(VisionActivity.this, AffirmPlayerActivity.class);
                    intent.putExtra(AffirmPlayerActivity.FROM_VISION, true);
                    intent.putParcelableArrayListExtra(AffirmPlayerActivity.EXTRA_LIST, affirmationRowList);
                    startActivity(intent);
                }
            }
        }).execute(new Object[0]);
    }
}
