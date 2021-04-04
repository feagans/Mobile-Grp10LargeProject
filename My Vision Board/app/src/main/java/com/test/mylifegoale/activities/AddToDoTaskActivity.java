package com.test.mylifegoale.activities;

import android.content.Intent;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.test.mylifegoale.R;
import com.test.mylifegoale.adapters.BodyItemAdapter;
import com.test.mylifegoale.base.BaseActivity;
import com.test.mylifegoale.base.roomDb.AppDatabase;
import com.test.mylifegoale.databinding.ActivityAddToDoTaskBinding;
import com.test.mylifegoale.helper.OnStartDragListener;
import com.test.mylifegoale.helper.SimpleItemTouchHelperCallback;
import com.test.mylifegoale.itemClick.DialogClick;
import com.test.mylifegoale.model.DiaryData;
import com.test.mylifegoale.utilities.AdConstants;
import com.test.mylifegoale.utilities.AllDialog;
import com.test.mylifegoale.utilities.AppConstants;
import com.test.mylifegoale.utilities.Constants;
import com.test.mylifegoale.utilities.adBackScreenListener;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.protocol.HTTP;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class AddToDoTaskActivity extends BaseActivity {
    public static final String BOTH_LIST_SEPARATOR = "$separate$";
    public static final String CHECK_ITEM_SEPARATOR = "|check|";
    public static final String UNCHECK_ITEM_SEPARATOR = "|uncheck|";
    boolean alreadyAdded = false;
    AppDatabase appDatabase;
    ActivityAddToDoTaskBinding binding;
    public boolean bodyDataChanged = false;
    BodyItemAdapter bodyItemAdapter;
    ArrayList<String> bodyItemArrayList = new ArrayList<>();
    ArrayList<String> checkedbodyItemArrayList = new ArrayList<>();
    boolean dataChanged;
    MenuItem delete;
    boolean deleteData = false;
    public DiaryData diaryData = new DiaryData();
    int key;
    String rowId = "";

    public void setBinding() {
        this.binding = (ActivityAddToDoTaskBinding) DataBindingUtil.setContentView(this, R.layout.activity_add_to_do_task);
        this.binding.setDiaryData(this.diaryData);
    }

    public void init() {
        AdConstants ads = new AdConstants();
        ads.loadNativeAd(this, binding.nativeadcontainer);

        this.appDatabase = AppDatabase.getAppDatabase(this);
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            this.key = intent.getExtras().getInt(Constants.INTENT_KEY);
        }
        if (this.key == 102) {
            this.diaryData = (DiaryData) getIntent().getParcelableExtra(Constants.BUNDLE_OBJECT);
            this.alreadyAdded = true;
            this.rowId = this.diaryData.getId();
            this.binding.setDiaryData(this.diaryData);
            this.binding.addNewForCheckList.setVisibility(View.VISIBLE);
            this.binding.addNewForCheckList.setText(this.diaryData.lastModifiedDate());
        } else {
            this.binding.bodyET.requestFocus();
            this.binding.addNewForCheckList.setVisibility(View.GONE);
        }
        setCheckListmenuTitle();
        setTitleBodyEditext();
        setCheckRecyclerView();
    }

    public void setToolbar() {
        setToolbarTitle(getString(R.string.journal));
        setToolbarBack(true);
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id != R.id.checklist) {
            if (id == R.id.plaintext && this.diaryData.getDisplay_status() == 1) {
                convertBodyText();
            }
        } else if (this.diaryData.getDisplay_status() == 0) {
            convertBodyText();
        }
    }

    private void setTitleBodyEditext() {
        this.binding.titleET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                //ADD
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                //ADD
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!binding.titleET.hasFocus()) {
                    return;
                }
                if (!diaryData.getBody().isEmpty() || !editable.toString().isEmpty()) {
                    deleteData = false;
                    diaryData.setTitle(editable.toString());
                    saveAndUpdateData();
                    return;
                }
                deleteData = true;
            }
        });
        this.binding.bodyET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                //ADD
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                //ADD
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!binding.bodyET.hasFocus()) {
                    return;
                }
                if (!diaryData.getBody().isEmpty() || !editable.toString().isEmpty()) {
                    deleteData = false;
                    diaryData.setBody(editable.toString());
                    saveAndUpdateData();
                    return;
                }
                deleteData = true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.write_diary_activity_menu, menu);
        this.delete = menu.findItem(R.id.deleteEntry);
        if (this.rowId.length() <= 0) {
            this.delete.setVisible(false);
        } else {
            this.delete.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == R.id.deleteEntry) {
            deleteDialog();
        } else if (itemId == R.id.save) {
            onBackPressed();
        } else if (itemId == R.id.shareNote) {
            shareNote(this.diaryData.getTitle(), shareNoteText());
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void deleteDialog() {
        new AllDialog().callDialog("", "", "", "", this, new DialogClick() {
            @Override
            public void onNegetiveClick() {
                //ADD
            }

            @Override
            public void onPositiveClick() {
                back(diaryData, true);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (this.alreadyAdded) {
            back(this.diaryData, this.deleteData);
        } else {
            super.onBackPressed();
        }
    }

    public void back(final DiaryData diaryData2, final boolean z) {
        HomeActivity.BackPressedAd(new adBackScreenListener() {
            public void BackScreen() {
                if (dataChanged || z) {
                    Intent intent = new Intent();
                    intent.putExtra(Constants.BUNDLE_OBJECT, diaryData2);
                    intent.putExtra(Constants.INTENT_KEY, key);
                    intent.putExtra(Constants.Data_Changed, bodyDataChanged);
                    intent.putExtra(Constants.DELETE_DATA, z);
                    setResult(-1, intent);
                    finish();
                    return;
                }
                setResult(0);
                finish();
            }
        });
    }

    private String shareNoteText() {
        try {
            StringBuilder sb = new StringBuilder();
            if (this.diaryData.getDisplay_status() == 1) {
                String str = "";
                if (this.diaryData.getDisplay_status() == 1) {
                    str = "☐";
                }
                if (this.bodyItemArrayList.size() > 0) {
                    for (int i = 1; i < this.bodyItemArrayList.size() - 1; i++) {
                        sb.append(str);
                        sb.append("\b" + this.bodyItemArrayList.get(i));
                        sb.append(IOUtils.LINE_SEPARATOR_UNIX);
                    }
                }
                if (this.diaryData.getDisplay_status() == 1 && this.checkedbodyItemArrayList.size() > 0) {
                    Iterator<String> it = this.checkedbodyItemArrayList.iterator();
                    while (it.hasNext()) {
                        sb.append("☑");
                        sb.append("\b" + it.next());
                        sb.append(IOUtils.LINE_SEPARATOR_UNIX);
                    }
                }
            } else {
                sb.append(this.diaryData.getBody());
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public void shareNote(String str, String str2) {
        StringBuilder sb = new StringBuilder();
        sb.append(Html.fromHtml("<b>" + getString(R.string.title_note) + " : " + str + "</b>").toString());
        sb.append("\n\n");
        sb.append(str2);
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType(HTTP.PLAIN_TEXT_TYPE);
        intent.putExtra("android.intent.extra.SUBJECT", str);
        intent.putExtra("android.intent.extra.TEXT", sb.toString());
        startActivity(Intent.createChooser(intent, getResources().getString(R.string.share_using)));
    }

    private void setCheckRecyclerView() {
        setBodyView();
        getBodyData();
        this.bodyItemAdapter = new BodyItemAdapter(this.bodyItemArrayList, this.checkedbodyItemArrayList, this, new BodyItemAdapter.CheckItemClick() {
            public void checkItemClick(int i) {
                try {
                    checkedbodyItemArrayList.add(bodyItemArrayList.remove(i));
                    bodyItemAdapter.notifyItemMoved(i, (checkedbodyItemArrayList.size() + bodyItemArrayList.size()) - 1);
                    bodyTextFromList();
                    saveAndUpdateData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void unCheckItemClick(int i) {
                try {
                    bodyItemArrayList.add(bodyItemArrayList.size() - 1, checkedbodyItemArrayList.remove(i));
                    bodyItemAdapter.notifyItemMoved((i + bodyItemArrayList.size()) - 1, bodyItemArrayList.size() - 2);
                    bodyTextFromList();
                    saveAndUpdateData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void addNewItemClick(int i, String str, int i2) {
                bodyTextFromList();
                binding.uncheckRecyclerView.scrollToPosition(i);
                saveAndUpdateData();
            }

            public void titleItemClick(String str) {
                diaryData.setTitle(str);
                saveAndUpdateData();
            }
        });
        final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SimpleItemTouchHelperCallback(this.bodyItemAdapter));
        itemTouchHelper.attachToRecyclerView(this.binding.uncheckRecyclerView);
        this.bodyItemAdapter.setmDragStartListener(new OnStartDragListener() {
            public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
                itemTouchHelper.startDrag(viewHolder);
            }
        });
        this.binding.uncheckRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.binding.uncheckRecyclerView.setAdapter(this.bodyItemAdapter);
        this.binding.uncheckRecyclerView.setNestedScrollingEnabled(false);
        this.binding.uncheckRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
                return false;
            }
        });
    }

    private void getBodyData() {
        if (this.diaryData.getDisplay_status() == 1) {
            this.bodyItemArrayList.add(this.diaryData.getTitle());
            if (this.alreadyAdded) {
                bodyTextToList();
            }
            this.bodyItemArrayList.add("");
        }
    }

    private void convertBodyText() {
        if (this.diaryData.getDisplay_status() != 1 || this.checkedbodyItemArrayList.size() <= 0) {
            if (this.diaryData.getDisplay_status() == 1) {
                this.bodyItemArrayList.remove(0);
                ArrayList<String> arrayList = this.bodyItemArrayList;
                arrayList.remove(arrayList.size() - 1);
            }
            setCheckListStatus();
            return;
        }
        this.bodyItemArrayList.remove(0);
        ArrayList<String> arrayList2 = this.bodyItemArrayList;
        arrayList2.remove(arrayList2.size() - 1);
        new AllDialog().callDialog("Convert", getString(R.string.delete_checked_item_message), getString(R.string.delete_checked_item), getString(R.string.keep_checked_item), this, new DialogClick() {
            @Override
            public void onPositiveClick() {
                deleteChekckedItem();
                setCheckListStatus();
            }

            @Override
            public void onNegetiveClick() {
                try {
                    bodyItemArrayList.addAll(checkedbodyItemArrayList);
                    checkedbodyItemArrayList.clear();
                    setCheckListStatus();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void deleteChekckedItem() {
        this.checkedbodyItemArrayList.clear();
    }

    public void bodyTextFromList() {
        try {
            StringBuilder sb = new StringBuilder();
            ArrayList arrayList = new ArrayList(this.bodyItemArrayList);
            ArrayList arrayList2 = new ArrayList(this.checkedbodyItemArrayList);
            if (arrayList.size() > 0) {
                arrayList.remove(0);
                arrayList.remove(arrayList.size() - 1);
                sb.append(StringUtils.join((Iterable<?>) arrayList, UNCHECK_ITEM_SEPARATOR));
            }
            if (arrayList2.size() > 0) {
                sb.append(BOTH_LIST_SEPARATOR);
                sb.append(StringUtils.join((Iterable<?>) arrayList2, CHECK_ITEM_SEPARATOR));
            }
            this.diaryData.setBody(sb.toString());
            this.diaryData.setBodyItemArrayList(arrayList);
            this.diaryData.setCheckItemCount(this.checkedbodyItemArrayList.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void bodyTextToList() {
        try {
            String[] splitByWholeSeparator = StringUtils.splitByWholeSeparator(this.diaryData.getBody(), BOTH_LIST_SEPARATOR.replace("$", ""));
            if (splitByWholeSeparator.length >= 1 && splitByWholeSeparator[0] != null) {
                this.bodyItemArrayList.addAll(Arrays.asList(StringUtils.splitByWholeSeparator(splitByWholeSeparator[0].replace("$", ""), UNCHECK_ITEM_SEPARATOR.replace("|", ""))));
                for (int i = 0; i < this.bodyItemArrayList.size(); i++) {
                    this.bodyItemArrayList.set(i, this.bodyItemArrayList.get(i).replace("|", ""));
                }
            }
            if (splitByWholeSeparator.length >= 2 && splitByWholeSeparator[1] != null) {
                this.checkedbodyItemArrayList.addAll(Arrays.asList(StringUtils.splitByWholeSeparator(splitByWholeSeparator[1].replace("$", ""), CHECK_ITEM_SEPARATOR.replace("|", ""))));
                for (int i2 = 0; i2 < this.checkedbodyItemArrayList.size(); i2++) {
                    this.checkedbodyItemArrayList.set(i2, this.checkedbodyItemArrayList.get(i2).replace("|", ""));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setBodyView() {
        if (this.diaryData.getDisplay_status() == 1) {
            this.binding.bodyView.setVisibility(View.GONE);
            this.binding.uncheckRecyclerView.setVisibility(View.VISIBLE);
            return;
        }
        this.binding.bodyView.setVisibility(View.VISIBLE);
        this.binding.uncheckRecyclerView.setVisibility(View.GONE);
    }


    public void setCheckListStatus() {
        if (this.diaryData.getDisplay_status() == 1) {
            this.diaryData.setDisplay_status(0);
        } else {
            this.diaryData.setDisplay_status(1);
        }
        setCheckListmenuTitle();
        setBodyView();
        CheckListToPlaintext();
    }

    private void setCheckListmenuTitle() {
        if (this.diaryData.getDisplay_status() == 1) {
            this.binding.checklist.setSelected(true);
            this.binding.plaintext.setSelected(false);
            return;
        }
        this.binding.checklist.setSelected(false);
        this.binding.plaintext.setSelected(true);
    }

    private void CheckListToPlaintext() {
        try {
            if (this.diaryData.getDisplay_status() == 0) {
                this.diaryData.setBody(StringUtils.join((Iterable<?>) this.bodyItemArrayList, IOUtils.LINE_SEPARATOR_UNIX).trim());
                this.diaryData.setCheckItemCount(this.checkedbodyItemArrayList.size());
                this.binding.addNewForCheckList.setText(this.diaryData.lastModifiedDate());
                this.bodyItemArrayList.clear();
            } else {
                this.bodyItemArrayList.add(0, this.diaryData.getTitle());
                if (this.binding.bodyET.getText().length() > 0) {
                    this.bodyItemArrayList.addAll(Arrays.asList(this.binding.bodyET.getText().toString().split(IOUtils.LINE_SEPARATOR_UNIX)));
                }
                this.bodyItemArrayList.add("");
                this.bodyItemAdapter.notifyDataSetChanged();
                bodyTextFromList();
            }
            saveAndUpdateData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveAndUpdateData() {
        this.dataChanged = true;
        try {
            if (!this.alreadyAdded) {
                this.rowId = AppConstants.getUniqueId();
                this.diaryData.setId(this.rowId);
                this.diaryData.setTimestamp(System.currentTimeMillis());
                this.diaryData.setCreatetTimestamp(System.currentTimeMillis());
                if (this.appDatabase.diaryDataDAO().insert(this.diaryData) > 0) {
                    this.diaryData.setId(this.rowId);
                }
                this.alreadyAdded = true;
                return;
            }
            this.diaryData.setTimestamp(System.currentTimeMillis());
            this.appDatabase.diaryDataDAO().update(this.diaryData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
