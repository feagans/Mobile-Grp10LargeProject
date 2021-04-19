package com.test.mylifegoale.activities;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.test.mylifegoale.R;
import com.test.mylifegoale.adapters.DashboardAdapter;
import com.test.mylifegoale.adapters.VisionAdapter;
import com.test.mylifegoale.base.BaseActivity;
import com.test.mylifegoale.base.roomDb.AppDatabase;
import com.test.mylifegoale.data.APIService;
import com.test.mylifegoale.data.model.BucketComponents;
import com.test.mylifegoale.data.model.LoggedInUser;
import com.test.mylifegoale.data.model.TodoComponents;
import com.test.mylifegoale.databinding.ActivityDashboardBinding;
import com.test.mylifegoale.databinding.DashboardMainHolderBinding;
import com.test.mylifegoale.itemClick.OnAsyncBackground;
import com.test.mylifegoale.model.VisionModel;
import com.test.mylifegoale.utilities.AdConstants;
import com.test.mylifegoale.utilities.BackgroundAsync;
import com.test.mylifegoale.utilities.Constants;
import com.test.mylifegoale.utilities.SwipeAndDragHelper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DashBoardActivity extends BaseActivity implements DashboardAdapter.DashBoardItemClick {
    ActivityDashboardBinding binding;
    ArrayList<VisionModel> completedList = new ArrayList<>();
    ArrayList<Map.Entry<String, ArrayList<VisionModel>>> entries;
    ArrayList<VisionModel> pendingList = new ArrayList<>();
    String selectedType = "";
    VisionModel selectedVisionModel = new VisionModel();
    public Retrofit retrofit;
    public APIService.API API;
    private static ToDolistActivity mInstance;
    public APIService.AllTodoListsResponse todoLists;

    public void setBinding() {
        this.binding = (ActivityDashboardBinding) DataBindingUtil.setContentView(this, R.layout.activity_dashboard);
    }

    public void init() {
        AdConstants.bannerad(this.binding.llads, this);
        this.binding.allList.setSelected(true);
        this.binding.completedList.setSelected(false);
        this.binding.pendingList.setSelected(false);
        this.retrofit = new Retrofit.Builder()
                .baseUrl("https://letsbuckit.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.API = retrofit.create(APIService.API.class);
        getData();
    }

    private void getData() {
        getTodoData();
        getBucketListData();
    }


    private void getTodoData() {
        Log.d("taggy", "getting data from user: " + LoggedInUser.getUserId());
        try {
            APIService.AllTodoListsRequest todoLists = new APIService.AllTodoListsRequest(LoggedInUser.getUserId());
            API.allTodos(todoLists).enqueue(new Callback<APIService.AllTodoListsResponse>() {
                @Override
                public void onResponse(Call<APIService.AllTodoListsResponse> call, Response<APIService.AllTodoListsResponse> response) {
                    LinkedHashMap<String, ArrayList<VisionModel>> hashMap = new LinkedHashMap<>();

                    Log.d("taggy", "All Todo Status Code = " + response.code());
                    APIService.AllTodoListsResponse userTodoData = response.body();
                    Log.d("taggy", userTodoData.error);

                    // If there are bucket lists in DB will return error = ""
                    if (userTodoData.error.equals("")) {
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

                            if (listyItem.completed) {
                                completedList.add(vm);
                            } else {
                                pendingList.add(vm);
                            }
                        }

                        hashMap.put(getString(R.string.pending_full), pendingList);
                        hashMap.put(getString(R.string.completed_full), completedList);
                        entries = new ArrayList<>(hashMap.entrySet());
                        binding.recyclerView.setLayoutManager(new LinearLayoutManager(DashBoardActivity.this));
                        RecyclerView recyclerView = binding.recyclerView;
                        recyclerView.setAdapter(new AdapterD(entries));
                        Button button = binding.allList;
                        boolean z = true;
                        button.setText(getString(R.string.all_goals, new Object[]{Integer.valueOf(pendingList.size() + completedList.size())}));
                        Button button2 = binding.pendingList;
                        button2.setText(getString(R.string.pending, new Object[]{Integer.valueOf(pendingList.size())}));
                        Button button3 = binding.completedList;
                        button3.setText(getString(R.string.completed, new Object[]{Integer.valueOf(completedList.size())}));
                        if (completedList.size() <= 0 && pendingList.size() <= 0) {
                            z = false;
                        }
                        setDefaultLayout(z);
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
    }

    private void getBucketListData() {
        Log.d("taggy", "getting data from user: " + LoggedInUser.getUserId());
        try {
            APIService.AllBucketListsRequest bucketLists = new APIService.AllBucketListsRequest(LoggedInUser.getUserId());
            API.allBuckets(bucketLists).enqueue(new Callback<APIService.AllBucketListsResponse>() {
                @Override
                public void onResponse(Call<APIService.AllBucketListsResponse> call, Response<APIService.AllBucketListsResponse> response) {
                    LinkedHashMap<String, ArrayList<VisionModel>> hashMap = new LinkedHashMap<>();

                    Log.d("taggy", "All Bucket Status Code = " + response.code());
                    APIService.AllBucketListsResponse userBucketData = response.body();
                    Log.d("taggy", userBucketData.error);

                    // If there are bucket lists in DB will return error = ""
                    if (userBucketData.error.equals("")) {
                        // Add todo items like we did in VisionActivity
                        ArrayList<BucketComponents> listy = userBucketData.results;

                        Log.d("taggy", "Size of bucket list:" + listy.size());

                        // Get itemTitle of first todo item
                        Log.d("taggy", listy.get(0).itemTitle);

                        for (int i = 0; i < listy.size(); i++) {
                            VisionModel vm = new VisionModel();
                            BucketComponents listyItem = listy.get(i);
                            vm.setName(listyItem.getItemTitle());
                            vm.setId(listyItem.getID());
                            vm.setDescription(listyItem.getCaption());

                            if (listyItem.completed) {
                                completedList.add(vm);
                            } else {
                                pendingList.add(vm);
                            }
                        }

                        hashMap.put(getString(R.string.pending_full), pendingList);
                        hashMap.put(getString(R.string.completed_full), completedList);
                        entries = new ArrayList<>(hashMap.entrySet());
                        binding.recyclerView.setLayoutManager(new LinearLayoutManager(DashBoardActivity.this));
                        RecyclerView recyclerView = binding.recyclerView;
                        recyclerView.setAdapter(new AdapterD(entries));
                        Button button = binding.allList;
                        boolean z = true;
                        button.setText(getString(R.string.all_goals, new Object[]{Integer.valueOf(pendingList.size() + completedList.size())}));
                        Button button2 = binding.pendingList;
                        button2.setText(getString(R.string.pending, new Object[]{Integer.valueOf(pendingList.size())}));
                        Button button3 = binding.completedList;
                        button3.setText(getString(R.string.completed, new Object[]{Integer.valueOf(completedList.size())}));
                        if (completedList.size() <= 0 && pendingList.size() <= 0) {
                            z = false;
                        }
                        setDefaultLayout(z);
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
    }


    public void setDefaultLayout(boolean z) {
        if (z) {
            this.binding.defaultMsglayout.setVisibility(View.GONE);
        } else {
            this.binding.defaultMsglayout.setVisibility(View.VISIBLE);
        }
    }

    public void setToolbar() {
        setToolbarTitle(getString(R.string.dashBoard));
        setToolbarBack(true);
    }

    public void onClick(View view) {
        int id = view.getId();
        boolean z = true;
        if (id == R.id.allList) {
            this.binding.allList.setSelected(true);
            this.binding.completedList.setSelected(false);
            this.binding.pendingList.setSelected(false);
            this.binding.recyclerView.setAdapter(new AdapterD(this.entries));
            if (this.completedList.size() <= 0 && this.pendingList.size() <= 0) {
                z = false;
            }
            setDefaultLayout(z);
        } else if (id == R.id.completedList) {
            this.binding.allList.setSelected(false);
            this.binding.completedList.setSelected(true);
            this.binding.pendingList.setSelected(false);
            this.binding.recyclerView.setAdapter(new DashboardAdapter(getString(R.string.completed_full), this.completedList, this, this));
            if (this.completedList.size() <= 0) {
                z = false;
            }
            setDefaultLayout(z);
        } else if (id == R.id.pendingList) {
            this.binding.allList.setSelected(false);
            this.binding.completedList.setSelected(false);
            this.binding.pendingList.setSelected(true);
            this.binding.recyclerView.setAdapter(new DashboardAdapter(getString(R.string.pending_full), this.pendingList, this, this));
            if (this.pendingList.size() <= 0) {
                z = false;
            }
            setDefaultLayout(z);
        }
    }

    public class AdapterD extends RecyclerView.Adapter<AdapterD.ViewHolder> {
        ArrayList<Map.Entry<String, ArrayList<VisionModel>>> entrySet;

        public AdapterD(ArrayList<Map.Entry<String, ArrayList<VisionModel>>> arrayList) {
            this.entrySet = arrayList;
        }

        @NonNull
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(DashBoardActivity.this).inflate(R.layout.dashboard_main_holder, viewGroup, false));
        }

        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            String str = (String) this.entrySet.get(i).getKey();
            ArrayList arrayList = (ArrayList) this.entrySet.get(i).getValue();
            if (arrayList.size() > 0) {
                viewHolder.binding.title.setVisibility(View.VISIBLE);
            } else {
                viewHolder.binding.title.setVisibility(View.GONE);
            }
            viewHolder.binding.title.setText(str);
            RecyclerView recyclerView = viewHolder.binding.recyclerViewDashBoard;
            recyclerView.setAdapter(new DashboardAdapter(str, arrayList, DashBoardActivity.this, DashBoardActivity.this));
        }

        public int getItemCount() {
            ArrayList<Map.Entry<String, ArrayList<VisionModel>>> arrayList = this.entrySet;
            if (arrayList != null) {
                return arrayList.size();
            }
            return 0;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            DashboardMainHolderBinding binding;

            public ViewHolder(@NonNull View view) {
                super(view);
                this.binding = (DashboardMainHolderBinding) DataBindingUtil.bind(view);
                this.binding.recyclerViewDashBoard.setLayoutManager(new LinearLayoutManager(DashBoardActivity.this));
            }
        }
    }


    @Override
    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        ArrayList<VisionModel> arrayList;
        super.onActivityResult(i, i2, intent);
        if (i == 100 && intent != null) {
            boolean z = false;
            if (intent.getBooleanExtra(Constants.EDIT_ADD_VISION_TAG, false)) {
                try {
                    if (this.selectedType.equalsIgnoreCase(getString(R.string.completed_full))) {
                        arrayList = this.completedList;
                    } else {
                        arrayList = this.pendingList;
                    }
                    VisionModel visionModel = (VisionModel) intent.getParcelableExtra(Constants.VISION_DATA_TAG);
                    int indexOf = arrayList.indexOf(visionModel);
                    if (intent.getBooleanExtra(Constants.DELETE_VISION_TAG, false)) {
                        arrayList.remove(indexOf);
                        this.binding.recyclerView.getAdapter().notifyDataSetChanged();
                        this.binding.allList.setText(getString(R.string.all_goals, new Object[]{Integer.valueOf(this.pendingList.size() + this.completedList.size())}));
                        this.binding.pendingList.setText(getString(R.string.pending, new Object[]{Integer.valueOf(this.pendingList.size())}));
                        this.binding.completedList.setText(getString(R.string.completed, new Object[]{Integer.valueOf(this.completedList.size())}));
                        if (this.binding.allList.isSelected()) {
                            if (this.pendingList.size() + this.completedList.size() > 0) {
                            }
                            setDefaultLayout(z);
                            return;
                        }
                        z = true;
                        setDefaultLayout(z);
                        return;
                    }
                    if (this.selectedVisionModel.isPending() != visionModel.isPending()) {
                        if (visionModel.isPending()) {
                            this.pendingList.add(visionModel);
                            this.completedList.remove(visionModel);
                        } else {
                            this.completedList.add(visionModel);
                            this.pendingList.remove(visionModel);
                        }
                        this.entries.get(0).setValue(this.pendingList);
                        this.entries.get(1).setValue(this.completedList);
                        this.binding.allList.setText(getString(R.string.all_goals, new Object[]{Integer.valueOf(this.pendingList.size() + this.completedList.size())}));
                        this.binding.pendingList.setText(getString(R.string.pending, new Object[]{Integer.valueOf(this.pendingList.size())}));
                        this.binding.completedList.setText(getString(R.string.completed, new Object[]{Integer.valueOf(this.completedList.size())}));
                    } else {
                        arrayList.set(indexOf, visionModel);
                    }
                    this.binding.recyclerView.getAdapter().notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void onItemClick(VisionModel visionModel, String str) {
        this.selectedType = str;
        this.selectedVisionModel = visionModel;
        startActivityForResult(new Intent(this, VisionEditActivity.class).putExtra(Constants.EDIT_ADD_VISION_TAG, true).putExtra(Constants.VISION_DATA_TAG, visionModel), 100);
    }
}
