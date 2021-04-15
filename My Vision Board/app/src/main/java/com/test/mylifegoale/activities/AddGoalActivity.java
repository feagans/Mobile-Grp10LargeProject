package com.test.mylifegoale.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.test.mylifegoale.R;
import com.test.mylifegoale.adapters.SpinnerAdapter;
import com.test.mylifegoale.base.BaseActivity;
import com.test.mylifegoale.base.roomDb.AppDatabase;
import com.test.mylifegoale.databinding.ActivityAddGoalBinding;
import com.test.mylifegoale.databinding.AddCategoryDialogBinding;
import com.test.mylifegoale.model.CategoryModel;
import com.test.mylifegoale.model.VisionModel;
import com.test.mylifegoale.utilities.AdConstants;
import com.test.mylifegoale.utilities.AppConstants;
import com.test.mylifegoale.utilities.Constants;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AddGoalActivity extends BaseActivity {
    public int reqCodeDescSpeechInput = 856;
    public int reqCodeTitleSpeechInput = 855;
    AppDatabase appDatabase;
    ActivityAddGoalBinding binding;
    ArrayList<CategoryModel> categoryModelArrayList = new ArrayList<>();
    Context context;
    boolean dataAdded = false;
    DatePickerDialog datePickerDialog;
    boolean isForEdit = false;
    int selectedPos = 1;
    SpinnerAdapter spinnerAdapter;
    VisionModel visionModel = new VisionModel();

    public void setBinding() {
        this.binding = (ActivityAddGoalBinding) DataBindingUtil.setContentView(this, R.layout.activity_add_goal);
        this.binding.setVisionModel(this.visionModel);
    }

    public void init() {
        this.context = this;
        AdConstants.bannerad(this.binding.llads, this);

        this.isForEdit = getIntent().getBooleanExtra(Constants.EDIT_ADD_VISION_TAG, false);
        this.appDatabase = AppDatabase.getAppDatabase(this.context);
        this.categoryModelArrayList.addAll(this.appDatabase.categoryDAO().getAll());
        for (CategoryModel categoryModel : categoryModelArrayList) {
            Log.e("aaaaaaaaa", categoryModel.getTitle());
        }
        this.categoryModelArrayList.add(0, new CategoryModel("", "Add new category", true));
        this.spinnerAdapter = new SpinnerAdapter(this, 0, this.categoryModelArrayList);
        this.binding.addProfile.setImageResource(R.drawable.add_image);
        if (!this.isForEdit) {
            setVisionStatus(true);
        } else if (getIntent() != null) {
            this.visionModel = (VisionModel) getIntent().getParcelableExtra(Constants.VISION_DATA_TAG);
            Log.i("init", "init: " + this.visionModel.getVisionProfile());
            this.binding.setVisionModel(this.visionModel);
            setProfile();
            getPos();
            setVisionStatus(this.visionModel.isPending());
        }
        this.binding.categorySpinner.setAdapter(this.spinnerAdapter);
        this.binding.categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                if (i == 0) {
                    callDialog();
                    return;
                }
                visionModel.setCategory(categoryModelArrayList.get(i).getId());
                visionModel.setCatTitle(categoryModelArrayList.get(i).getTitle());
            }
        });
        this.binding.categorySpinner.setSelection(this.selectedPos);
        this.binding.goalDescription.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (view.getId() == R.id.goalDescription) {
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    if ((motionEvent.getAction() & 255) == 1) {
                        view.getParent().requestDisallowInterceptTouchEvent(false);
                    }
                }
                return false;
            }
        });

        binding.goal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //add
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                visionModel.setName(binding.goal.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //add
            }
        });

        binding.goalDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //add
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                visionModel.setDescription(binding.goalDescription.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //add
            }
        });
    }

    private void setProfile() {
        ((RequestBuilder) ((RequestBuilder) Glide.with((FragmentActivity) this).load(this.visionModel.getVisionProfile()).diskCacheStrategy(DiskCacheStrategy.NONE)).skipMemoryCache(true)).into(this.binding.addProfile);
    }

    private void getPos() {
        for (int i = 0; i < this.categoryModelArrayList.size(); i++) {
            if (this.visionModel.getCategory().equalsIgnoreCase(this.categoryModelArrayList.get(i).getId())) {
                this.selectedPos = i;
                return;
            }
        }
    }

    @SuppressLint("ResourceType")
    public void callDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        final AddCategoryDialogBinding addCategoryDialogBinding = (AddCategoryDialogBinding) DataBindingUtil.inflate(getLayoutInflater(), R.layout.add_category_dialog, (ViewGroup) null, false);
        builder.setView(addCategoryDialogBinding.getRoot());
        final AlertDialog create = builder.create();
        create.getWindow().setBackgroundDrawableResource(17170445);
        addCategoryDialogBinding.okAction.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                CategoryModel categoryModel = new CategoryModel(AppConstants.getUniqueId(), addCategoryDialogBinding.message.getText().toString(), false);
                if (appDatabase.categoryDAO().insert(categoryModel) > 0) {
                    categoryModelArrayList.add(categoryModel);
                    spinnerAdapter.notifyDataSetChanged();
                    binding.categorySpinner.setSelection(categoryModelArrayList.size() - 1);
                }
                create.dismiss();
            }
        });
        addCategoryDialogBinding.cancelAction.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                create.dismiss();
            }
        });
        create.show();
    }

    private void promptSpeechInput(int i) {
        Intent intent = new Intent("android.speech.action.RECOGNIZE_SPEECH");
        intent.putExtra("android.speech.extra.LANGUAGE_MODEL", "free_form");
        intent.putExtra("android.speech.extra.LANGUAGE", Locale.getDefault());
        intent.putExtra("android.speech.extra.PROMPT", getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, i);
        } catch (ActivityNotFoundException unused) {
            Toast.makeText(getApplicationContext(), getString(R.string.speech_not_supported), Toast.LENGTH_SHORT).show();
        }
    }

    public void setToolbar() {
        setToolbarTitle(getString(R.string.add_goal));
        setToolbarBack(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_save, menu);
        menu.findItem(R.id.saveAction).setVisible(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.saveAction) {
            this.visionModel.setName(this.binding.goal.getText().toString());
            this.visionModel.setDescription(this.binding.goalDescription.getText().toString());
            if (!this.isForEdit) {
                if (!validateEndDate()) {
                    return false;
                }
                this.visionModel.setId(AppConstants.getUniqueId());
                this.visionModel.setCreatedTime(System.currentTimeMillis());
                this.visionModel.setOrd(this.appDatabase.visionDao().getMaxOrd());
                if (this.appDatabase.visionDao().insert(this.visionModel) > 0) {
                    this.dataAdded = true;
                    onBackPressed();
                }
            } else if (!validateEndDate()) {
                return false;
            } else {
                if (this.appDatabase.visionDao().update(this.visionModel) > 0) {
                    this.dataAdded = true;
                    onBackPressed();
                }
            }
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private boolean validateEndDate() {
        if (this.visionModel.getName() != null && this.visionModel.getName().isEmpty()) {
            Toast.makeText(this.context, getString(R.string.title_require), Toast.LENGTH_SHORT).show();
            return false;
         //Omitted as android wont let the app upload pictures from this activity
        }/* else if (this.visionModel.getVisionProfile() == null || this.visionModel.getVisionProfile().isEmpty()) {
            Toast.makeText(this.context, getString(R.string.error_profile), Toast.LENGTH_SHORT).show();
            return false;
        } */else {
            if (this.visionModel.getEndTime() != 0) {
                if (this.visionModel.isPending() && AppConstants.getFormattedDateNew(this.visionModel.getEndTime()).before(AppConstants.getFormattedDateNew(System.currentTimeMillis()))) {
                    Toast.makeText(this.context, "End date must be set to today or greater than today.", Toast.LENGTH_LONG).show();
                    return false;
                } else if (!this.visionModel.isPending() && AppConstants.getFormattedDateNew(this.visionModel.getEndTime()).after(AppConstants.getFormattedDateNew(System.currentTimeMillis()))) {
                    Toast.makeText(this.context, "End date must be set to today or smaller than today.", Toast.LENGTH_LONG).show();
                    return false;
                }
            }
            return true;
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addProfile:
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(this);
                return;
            case R.id.clearEndDat:
                this.visionModel.setEndTime(0);
                return;
            case R.id.completed:
                setVisionStatus(false);
                return;
            case R.id.datePicker:
                callDatePickerDialog(this.visionModel.getEndTime());
                return;
            case R.id.descriptionSpeaker:
                promptSpeechInput(this.reqCodeDescSpeechInput);
                return;
            case R.id.goalSpeaker:
                promptSpeechInput(this.reqCodeTitleSpeechInput);
                return;
            case R.id.pending:
                setVisionStatus(true);
                return;
            default:
                return;
        }
    }

    private void setVisionStatus(boolean z) {
        this.visionModel.setPending(z);
        if (z) {
            this.binding.pending.setSelected(true);
            this.binding.completed.setSelected(false);
            return;
        }
        this.binding.pending.setSelected(false);
        this.binding.completed.setSelected(true);
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 203) {
            CropImage.ActivityResult activityResult = CropImage.getActivityResult(intent);
            if (i2 == -1) {
                try {
                    copyFile(new File(activityResult.getUri().getPath()), AppConstants.profilePicStoreParent(this));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (i2 == 204) {
                activityResult.getError();
            }
        }
        if (i == this.reqCodeDescSpeechInput && i2 == -1 && intent != null) {
            Log.i("onActivityResult", "onActivityResult: " + i);
            ArrayList<String> stringArrayListExtra = intent.getStringArrayListExtra("android.speech.extra.RESULTS");
            StringBuilder sb = new StringBuilder();
            sb.append(this.binding.goalDescription.getText().toString());
            sb.append(stringArrayListExtra.size() > 0 ? stringArrayListExtra.get(0) : 0);
            sb.append(" ");
            this.binding.goalDescription.setText(sb.toString());
            AppConstants.cursorPos(this.binding.goalDescription);
        }
        if (i == this.reqCodeTitleSpeechInput && i2 == -1 && intent != null) {
            Log.i("onActivityResult", "onActivityResult:desc " + i);
            ArrayList<String> stringArrayListExtra2 = intent.getStringArrayListExtra("android.speech.extra.RESULTS");
            StringBuilder sb2 = new StringBuilder();
            sb2.append(this.binding.goal.getText().toString());
            sb2.append(stringArrayListExtra2.size() > 0 ? stringArrayListExtra2.get(0) : 0);
            sb2.append(" ");
            this.binding.goal.setText(sb2.toString());
            this.binding.goal.setFocusable(true);
            AppConstants.cursorPos(this.binding.goal);
        }
    }

    public void copyFile(File file, File file2) throws FileNotFoundException, IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        FileOutputStream fileOutputStream = new FileOutputStream(file2);
        byte[] bArr = new byte[1024];
        while (true) {
            int read = fileInputStream.read(bArr);
            if (read <= 0) {
                break;
            }
            fileOutputStream.write(bArr, 0, read);
        }
        fileInputStream.close();
        fileOutputStream.close();
        File file3 = new File(this.visionModel.getVisionProfile());
        if (file3.exists()) {
            file3.delete();
        }
        if (file.exists()) {
            file.delete();
        }
        this.visionModel.setVisionProfile(file2.getAbsolutePath());
        setProfile();
    }

    private void callDatePickerDialog(long j) {
        final Calendar instance = Calendar.getInstance();
        if (j == 0) {
            j = System.currentTimeMillis();
        }
        instance.setTimeInMillis(j);
        int i = instance.get(1);
        int i2 = instance.get(2);
        int i3 = instance.get(5);
        DatePickerDialog.OnDateSetListener r3 = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                try {
                    Calendar instance = Calendar.getInstance();
                    instance.set(1, i);
                    instance.set(2, i2);
                    instance.set(5, i3);
                    instance.set(11, instance.get(11));
                    instance.set(12, instance.get(12));
                    visionModel.setEndTime(instance.getTimeInMillis());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        DatePickerDialog datePickerDialog2 = this.datePickerDialog;
        if (datePickerDialog2 != null && datePickerDialog2.isShowing()) {
            this.datePickerDialog.dismiss();
        }
        this.datePickerDialog = new DatePickerDialog(this, r3, i, i2, i3);
        this.datePickerDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (this.dataAdded) {
            Intent intent = getIntent();
            intent.putExtra(Constants.EDIT_ADD_VISION_TAG, this.isForEdit);
            intent.putExtra(Constants.VISION_DATA_TAG, this.visionModel);
            setResult(0, intent);
            super.onBackPressed();
            return;
        }
        super.onBackPressed();
    }
}
