package com.test.mylifegoale.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.test.mylifegoale.R;
import com.test.mylifegoale.adapters.DiaryDataAdapter;
import com.test.mylifegoale.adapters.VisionAdapter;
import com.test.mylifegoale.base.BaseActivity;
import com.test.mylifegoale.base.roomDb.AppDatabase;
import com.test.mylifegoale.data.APIService;
import com.test.mylifegoale.data.model.BucketComponents;
import com.test.mylifegoale.data.model.LoggedInUser;
import com.test.mylifegoale.data.model.TodoComponents;
import com.test.mylifegoale.databinding.ActivityTodolistBinding;
import com.test.mylifegoale.databinding.ActivityVisionBinding;
import com.test.mylifegoale.itemClick.DialogClick;
import com.test.mylifegoale.itemClick.OnAsyncBackground;
import com.test.mylifegoale.itemClick.RecyclerClick;
import com.test.mylifegoale.model.AffirmationRowModel;
import com.test.mylifegoale.model.DiaryData;
import com.test.mylifegoale.model.VisionModel;
import com.test.mylifegoale.utilities.AdConstants;
import com.test.mylifegoale.utilities.AllDialog;
import com.test.mylifegoale.utilities.BackgroundAsync;
import com.test.mylifegoale.utilities.Constants;
import com.test.mylifegoale.utilities.SwipeAndDragHelper;
import com.test.mylifegoale.view.AffirmPlayerActivity;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ToDolistActivity extends BaseActivity {
    public static boolean filterFlag = false;
    DiaryDataAdapter adapter;
    AppDatabase appDatabase;
    ActivityVisionBinding binding;
    public ArrayList<DiaryData> diaryDataList = new ArrayList<>();
    boolean empty = true;
    public ArrayList<DiaryData> tempDiaryDataList = new ArrayList<>();

    VisionAdapter visionAdapter;
    ArrayList<VisionModel> visionModelArrayList = new ArrayList<>();

    // API
    public Retrofit retrofit;
    public APIService.API API;
    private static ToDolistActivity mInstance;
    public APIService.AllTodoListsResponse todoLists;

    public void onClick(View view) {
    }

    public void setBinding() {
        this.binding = (ActivityVisionBinding) DataBindingUtil.setContentView(this, R.layout.activity_vision);
    }

    public void setDefaultLayout() {
        if (!empty) {
            this.binding.defaultMsglayout.setVisibility(View.GONE);
            //this.binding.bottomLayout.setVisibility(View.VISIBLE);
            return;
        }

        this.binding.defaultMsglayout.setVisibility(View.VISIBLE);
        //this.binding.bottomLayout.setVisibility(View.GONE);
    }
    public void init() {
        AdConstants.bannerad(this.binding.llads, this);

        // API do we need to reconnect to API each call??
        mInstance = this;
        this.retrofit = new Retrofit.Builder()
                .baseUrl("https://letsbuckit.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.API = retrofit.create(APIService.API.class);

        this.binding.visionList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        this.appDatabase = AppDatabase.getAppDatabase(this);
        getData();
    }



    private void getData() {
        Log.d("taggy", "getting data from user: " + LoggedInUser.getUserId());

        try {
            APIService.AllTodoListsRequest todoLists = new APIService.AllTodoListsRequest(LoggedInUser.getUserId());
            API.allTodos(todoLists).enqueue(new Callback<APIService.AllTodoListsResponse>() {

                @Override
                public void onResponse(Call<APIService.AllTodoListsResponse> call, Response<APIService.AllTodoListsResponse> response) {
                    Log.d("taggy", "All Todo Status Code = " + response.code());
                    APIService.AllTodoListsResponse userTodoData = response.body();
                    Log.d("taggy", userTodoData.error);

                    // If there are bucket lists in DB will return error = ""
                    if (userTodoData.error.equals("")){
                        empty = false;
                        setDefaultLayout();
                        // Add todo items like we did in VisionActivity
                        ArrayList<TodoComponents> listy = userTodoData.results;

                        Log.d("taggy", "Size of todo list:" + listy.size());

                        // Get itemTitle of first todo item
                        Log.d("taggy", listy.get(0).itemTitle);

                        for (int i = 0; i < listy.size(); i++) {
                            VisionModel vm = new VisionModel();
                            TodoComponents listyItem = listy.get(i);
                            vm.setName(listyItem.getItemTitle());
                            vm.setId(listyItem.getID());
                            // Pending will be true if item is NOT completed
                            vm.setPending(!listyItem.getCompleted());
                            visionModelArrayList.add(vm);
                        }
                    }
                }

                @Override
                public void onFailure(Call<APIService.AllTodoListsResponse> call, Throwable t) {
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
                ToDolistActivity todoActivity = ToDolistActivity.this;
                todoActivity.visionAdapter = new VisionAdapter(todoActivity, todoActivity.visionModelArrayList);
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
        setToolbarTitle(getString(R.string.todo_board));
        setToolbarBack(true);
    }


    @Override
    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        setToolbarTitle(getString(R.string.todo_board));
        super.onActivityResult(i, i2, intent);
        if (i == 100 && intent != null) {
            try {
                if (intent.getBooleanExtra(Constants.EDIT_ADD_VISION_TAG, false)) {
                    int indexOf = this.visionModelArrayList.indexOf((VisionModel) intent.getParcelableExtra(Constants.VISION_DATA_TAG));
                    if (intent.getBooleanExtra(Constants.DELETE_VISION_TAG, false)) {
                        this.visionModelArrayList.remove(indexOf);
                        this.visionAdapter.notifyItemRemoved(indexOf);
                        setDefaultLayout();
                        setToolbarTitle(getString(R.string.todo_board));
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
}