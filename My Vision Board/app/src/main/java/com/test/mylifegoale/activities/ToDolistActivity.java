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

import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.test.mylifegoale.R;
import com.test.mylifegoale.adapters.DiaryDataAdapter;
import com.test.mylifegoale.base.BaseActivity;
import com.test.mylifegoale.base.roomDb.AppDatabase;
import com.test.mylifegoale.data.APIService;
import com.test.mylifegoale.data.model.BucketComponents;
import com.test.mylifegoale.data.model.LoggedInUser;
import com.test.mylifegoale.data.model.TodoComponents;
import com.test.mylifegoale.databinding.ActivityTodolistBinding;
import com.test.mylifegoale.itemClick.DialogClick;
import com.test.mylifegoale.itemClick.OnAsyncBackground;
import com.test.mylifegoale.itemClick.RecyclerClick;
import com.test.mylifegoale.model.DiaryData;
import com.test.mylifegoale.model.VisionModel;
import com.test.mylifegoale.utilities.AdConstants;
import com.test.mylifegoale.utilities.AllDialog;
import com.test.mylifegoale.utilities.BackgroundAsync;
import com.test.mylifegoale.utilities.Constants;

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
    ActivityTodolistBinding binding;
    public ArrayList<DiaryData> diaryDataList = new ArrayList<>();
    String filterByText = "";
    boolean sortByNew = false;
    public ArrayList<DiaryData> tempDiaryDataList = new ArrayList<>();

    // API
    public Retrofit retrofit;
    public APIService.API API;
    private static ToDolistActivity mInstance;
    public APIService.AllTodoListsResponse todoLists;

    public void onClick(View view) {
    }

    public void setBinding() {
        this.binding = (ActivityTodolistBinding) DataBindingUtil.setContentView(this, R.layout.activity_todolist);
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

        this.appDatabase = AppDatabase.getAppDatabase(this);
        getData();
        setRecyclerView();
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
                        // Add todo items like we did in VisionActivity
                        Log.d("taggy", "User has todos in DB.");

                        // Create array to store
                        ArrayList<TodoComponents> todoListy = userTodoData.results;
                        Log.d("taggy", "Size of todo list:" + todoListy.size());
                        // Get itemTitle of first todo item
                        Log.d("taggy", todoListy.get(0).itemTitle);
                    }
                }

                @Override
                public void onFailure(Call<APIService.AllTodoListsResponse> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        new BackgroundAsync(this, true, "", new OnAsyncBackground() {
            public void onPreExecute() {
            }

            public void doInBackground() {
                for (DiaryData next : appDatabase.diaryDataDAO().getAll()) {
                    String body = next.getBody();
                    if (body == null) {
                        body = "";
                    }
                    next.setBody(body);
                    if (next.getDisplay_status() == 1) {
                        String[] splitByWholeSeparator = StringUtils.splitByWholeSeparator(next.getBody(), AddToDoTaskActivity.BOTH_LIST_SEPARATOR.replace("$", ""));
                        if (splitByWholeSeparator.length >= 1 && splitByWholeSeparator[0] != null) {
                            ArrayList arrayList = new ArrayList(Arrays.asList(StringUtils.splitByWholeSeparator(splitByWholeSeparator[0].replace("$", ""), AddToDoTaskActivity.UNCHECK_ITEM_SEPARATOR.replace("|", ""))));
                            for (int i = 0; i < arrayList.size(); i++) {
                                arrayList.set(i, ((String) arrayList.get(i)).replace("|", ""));
                            }
                            next.setBodyItemArrayList(arrayList);
                        }
                        if (splitByWholeSeparator.length >= 2 && splitByWholeSeparator[1] != null) {
                            next.setCheckItemCount(Arrays.asList(StringUtils.splitByWholeSeparator(splitByWholeSeparator[1].replace("$", ""), AddToDoTaskActivity.CHECK_ITEM_SEPARATOR.replace("|", ""))).size());
                        }
                    }
                    if (next.getImageCount() > 0) {
                        next.setImageCurrentPos(1);
                    }
                    next.setStatus(0);
                    diaryDataList.add(next);
                }
            }

            public void onPostExecute() {
                defaultMsglayout();
            }
        }).execute(new Object[0]);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.to_do_list_menu, menu);
        menu.findItem(R.id.sortByCreated).setChecked(true);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setIconified(false);
        searchView.setSearchableInfo(((SearchManager) getSystemService(SEARCH_SERVICE)).getSearchableInfo(getComponentName()));
        search(searchView);
        return super.onCreateOptionsMenu(menu);
    }

    private void search(SearchView searchView) {
        SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete) searchView.findViewById(R.id.search_src_text);
        searchAutoComplete.setHintTextColor(getResources().getColor(R.color.colorWhite));
        searchAutoComplete.setTextColor(getResources().getColor(R.color.colorWhite));
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            public boolean onClose() {
                return false;
            }
        });
        if (searchView != null) {
            try {
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    public boolean onQueryTextSubmit(String str) {
                        return false;
                    }

                    public boolean onQueryTextChange(String str) {
                        filterByTag(str);
                        return true;
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId != R.id.addTask) {
            switch (itemId) {
                case R.id.sortByCreated:
                    menuItem.setChecked(true);
                    this.sortByNew = false;
                    Collections.sort(this.adapter.getTempDiaryData(), new DiaryData().diaryDataComparatorCreatedFirst);
                    this.adapter.notifyDataSetChanged();
                    break;
                case R.id.sortByModified:
                    menuItem.setChecked(true);
                    this.sortByNew = true;
                    Collections.sort(this.adapter.getTempDiaryData(), new DiaryData().diaryDataComparatorModifiedFirst);
                    this.adapter.notifyDataSetChanged();
                    break;
            }
        } else {
            Intent intent = new Intent(this, AddToDoTaskActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(Constants.INTENT_KEY, 101);
            startActivityForResult(intent, 103);
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void setRecyclerView() {
        this.adapter = new DiaryDataAdapter(this, this.diaryDataList, new RecyclerClick() {
            public void onClick(int i) {
                Intent intent = new Intent(ToDolistActivity.this, AddToDoTaskActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(Constants.INTENT_KEY, 102);
                if (ToDolistActivity.filterFlag) {
                    intent.putExtra(Constants.BUNDLE_OBJECT, tempDiaryDataList.get(i));
                } else {
                    intent.putExtra(Constants.BUNDLE_OBJECT, diaryDataList.get(i));
                }
                startActivityForResult(intent, 103);
            }

            public void onItemDeleteClick(ArrayList<Integer> arrayList) {
                callDialog(arrayList);
            }
        });
        this.binding.recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, 1));
        this.binding.recyclerView.setAdapter(this.adapter);
        this.binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            public void onScrollStateChanged(RecyclerView recyclerView, int i) {
                super.onScrollStateChanged(recyclerView, i);
            }

            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                super.onScrolled(recyclerView, i, i2);
            }
        });
    }

    public void setToolbar() {
        setToolbarTitle(getString(R.string.journal));
        setToolbarBack(true);
    }

    public void callDialog(final ArrayList<Integer> arrayList) {
        new AllDialog().callDialog("", "", "", "", this, new DialogClick() {
            @Override
            public void onNegetiveClick() {
                //ADD
            }

            @Override
            public void onPositiveClick() {
                new DeleteData(ToDolistActivity.this, arrayList).execute(new Void[0]);
            }
        });
    }

    public class DeleteData extends AsyncTask<Void, Integer, Boolean> {
        ProgressDialog pDialog;
        ArrayList<Integer> selectedItems;
        WeakReference<Activity> weakReference;

        public DeleteData(Activity activity, ArrayList<Integer> arrayList) {
            this.weakReference = new WeakReference<>(activity);
            this.selectedItems = arrayList;
        }


        public void onPreExecute() {
            super.onPreExecute();
            if (this.weakReference.get() != null) {
                this.pDialog = new ProgressDialog((Context) this.weakReference.get());
                this.pDialog.setMessage("Deleting file. Please wait...");
                this.pDialog.setIndeterminate(false);
                this.pDialog.setMax(this.selectedItems.size());
                this.pDialog.setProgressStyle(1);
                this.pDialog.setCancelable(false);
                this.pDialog.setCanceledOnTouchOutside(false);
                this.pDialog.show();
            }
            Collections.sort(this.selectedItems);
            Collections.reverse(this.selectedItems);
        }


        public Boolean doInBackground(Void... voidArr) {
            Iterator<Integer> it = this.selectedItems.iterator();
            int i = 1;
            while (it.hasNext()) {
                Integer next = it.next();
                publishProgress(new Integer[]{Integer.valueOf(i)});
                if (appDatabase.diaryDataDAO().delete(diaryDataList.get(next.intValue())) > 0) {
                    diaryDataList.remove(next.intValue());
                }
                i++;
            }
            return null;
        }


        public void onPostExecute(Boolean bool) {
            super.onPostExecute(bool);
            ToDolistActivity.filterFlag = false;
            this.pDialog.dismiss();
            afterDelete();
        }


        public void onProgressUpdate(Integer... numArr) {
            super.onProgressUpdate(numArr);
            this.pDialog.setProgress(numArr[0].intValue());
        }
    }


    public void afterDelete() {
        filterByTag(this.filterByText);
        defaultMsglayout();
    }

    public void defaultMsglayout() {
        if (this.diaryDataList.size() > 0) {
            this.binding.defaultMsglayout.setVisibility(View.GONE);
        } else {
            this.binding.defaultMsglayout.setVisibility(View.VISIBLE);
        }
    }

    public void filterByTag(String str3) {
        this.filterByText = str3;
        this.tempDiaryDataList = new ArrayList<>();
        if (!this.filterByText.isEmpty()) {
            filterFlag = true;
            for (int i = 0; i < this.diaryDataList.size(); i++) {
                DiaryData diaryData = this.diaryDataList.get(i);
                if (filterByAll(diaryData)) {
                    diaryData.setActualPos(i);
                    this.tempDiaryDataList.add(diaryData);
                }
            }
            notifyAdapter(this.tempDiaryDataList);
            return;
        }
        filterFlag = false;
        notifyAdapter(this.diaryDataList);
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 103 && i2 == -1) {
            int intExtra = intent.getIntExtra(Constants.INTENT_KEY, -1);
            DiaryData diaryData = (DiaryData) intent.getParcelableExtra(Constants.BUNDLE_OBJECT);
            intent.getBooleanExtra(Constants.Data_Changed, false);
            if (intent.getBooleanExtra(Constants.DELETE_DATA, false)) {
                if (this.appDatabase.diaryDataDAO().delete(diaryData) > 0) {
                    this.diaryDataList.remove(diaryData);
                }
                afterDelete();
            } else if (intExtra == 102) {
                filterUpdateByTagForSingleItem(0, diaryData);
            } else {
                filterUpdateByTagForSingleItem(-1, diaryData);
            }
        }
    }

    public void filterUpdateByTagForSingleItem(int i, DiaryData diaryData) {
        if (i == -1) {
            try {
                if (filterFlag) {
                    if (!this.diaryDataList.contains(diaryData)) {
                        this.diaryDataList.add(diaryData);
                    }
                    sortList(this.diaryDataList);
                    if (filterByAll(diaryData)) {
                        diaryData.setActualPos(this.diaryDataList.indexOf(diaryData));
                        if (!this.tempDiaryDataList.contains(diaryData)) {
                            this.tempDiaryDataList.add(diaryData);
                        }
                    }
                    sortList(this.tempDiaryDataList);
                    notifyAdapter(this.tempDiaryDataList);
                } else {
                    if (!this.diaryDataList.contains(diaryData)) {
                        this.diaryDataList.add(diaryData);
                    }
                    sortList(this.diaryDataList);
                    notifyAdapter(this.diaryDataList);
                }
                defaultMsglayout();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            this.diaryDataList.set(this.diaryDataList.indexOf(diaryData), diaryData);
            sortList(this.diaryDataList);
            this.adapter.notifyItemChanged(this.diaryDataList.indexOf(diaryData));
            if (filterFlag) {
                if (!this.filterByText.isEmpty()) {
                    if (filterByAll(diaryData)) {
                        if (!this.tempDiaryDataList.contains(diaryData)) {
                            this.tempDiaryDataList.add(diaryData);
                        }
                        diaryData.setActualPos(this.diaryDataList.indexOf(diaryData));
                        this.tempDiaryDataList.set(this.tempDiaryDataList.indexOf(diaryData), diaryData);
                        this.adapter.notifyItemChanged(this.tempDiaryDataList.indexOf(diaryData));
                    } else {
                        this.tempDiaryDataList.remove(diaryData);
                        this.adapter.notifyItemRemoved(this.tempDiaryDataList.indexOf(diaryData));
                    }
                }
                sortList(this.tempDiaryDataList);
            }
        }
    }

    private void sortList(ArrayList<DiaryData> arrayList) {
        if (this.sortByNew) {
            Collections.sort(arrayList, new DiaryData().diaryDataComparatorModifiedFirst);
        } else {
            Collections.sort(arrayList, new DiaryData().diaryDataComparatorCreatedFirst);
        }
        this.adapter.notifyDataSetChanged();
    }

    private boolean filterByAll(DiaryData diaryData) {
        return diaryData.getBody().toLowerCase().contains(this.filterByText.toLowerCase()) || diaryData.getTitle().toLowerCase().contains(this.filterByText.toLowerCase());
    }

    public void notifyAdapter(ArrayList<DiaryData> arrayList) {
        this.adapter.setDiaryData(arrayList);
        this.adapter.notifyDataSetChanged();
    }
}
