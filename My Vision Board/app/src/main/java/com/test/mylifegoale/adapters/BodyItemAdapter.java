package com.test.mylifegoale.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.test.mylifegoale.R;
import com.test.mylifegoale.activities.AddToDoTaskActivity;
import com.test.mylifegoale.databinding.BodyAddItemRawLayoutBinding;
import com.test.mylifegoale.databinding.BodyItemRawLayoutBinding;
import com.test.mylifegoale.databinding.EditAddTitleHolderBinding;
import com.test.mylifegoale.databinding.ModifiedDateLayoutBinding;
import com.test.mylifegoale.helper.ItemTouchHelperAdapter;
import com.test.mylifegoale.helper.OnStartDragListener;
import com.test.mylifegoale.utilities.AppConstants;

import java.util.ArrayList;
import java.util.Collections;

public class BodyItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemTouchHelperAdapter {
    CheckItemClick checkItemClick;
    ArrayList<String> checkedbodyItemArrayList;
    AddToDoTaskActivity context;
    int curPos = -1;
    long delay = 10;
    Handler handler = new Handler();
    InputMethodManager imm;
    RunnableClass input_finish_checker = new RunnableClass();
    long last_text_edit = 0;

    public OnStartDragListener mDragStartListener;
    ArrayList<String> modelArrayList;

    public interface CheckItemClick {
        void addNewItemClick(int i, String str, int i2);

        void checkItemClick(int i);

        void titleItemClick(String str);

        void unCheckItemClick(int i);
    }

    public BodyItemAdapter(ArrayList<String> arrayList, ArrayList<String> arrayList2, AddToDoTaskActivity addToDoTaskActivity, CheckItemClick checkItemClick2) {
        this.modelArrayList = arrayList;
        this.checkedbodyItemArrayList = arrayList2;
        this.context = addToDoTaskActivity;
        this.checkItemClick = checkItemClick2;
        this.imm = (InputMethodManager) addToDoTaskActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public void setmDragStartListener(OnStartDragListener onStartDragListener) {
        this.mDragStartListener = onStartDragListener;
    }

    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == 5) {
            return new EditedDateHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.modified_date_layout, viewGroup, false));
        }
        switch (i) {
            case 0:
                return new TitleViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.edit_add_title_holder, viewGroup, false));
            case 1:
                return new BodyItemHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.body_item_raw_layout, viewGroup, false), (CTextWatcher) null, 1);
            case 2:
                return new AddItemHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.body_add_item_raw_layout, viewGroup, false));
            case 3:
                return new CheckedItemHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.body_item_raw_layout, viewGroup, false), (CTextWatcher) null, 3);
            default:
                return null;
        }
    }

    public int getPos(int i) {
        return (i >= this.modelArrayList.size() && i - this.modelArrayList.size() < this.checkedbodyItemArrayList.size()) ? i - this.modelArrayList.size() : i;
    }

    public int getItemViewType(int i) {
        if (i == 0) {
            return 0;
        }
        if (i < this.modelArrayList.size() && i == this.modelArrayList.size() - 1) {
            return 2;
        }
        if (i < this.modelArrayList.size()) {
            return 1;
        }
        if (i - this.modelArrayList.size() < this.checkedbodyItemArrayList.size()) {
            return 3;
        }
        if (i == this.modelArrayList.size() + this.checkedbodyItemArrayList.size()) {
            return 5;
        }
        return 1;
    }

    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof BodyItemHolder) {
            BodyItemHolder bodyItemHolder = (BodyItemHolder) viewHolder;
            bodyItemHolder.binding.handle.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getActionMasked() != 0) {
                        return false;
                    }
                    mDragStartListener.onStartDrag(viewHolder);
                    return false;
                }
            });
            bodyItemHolder.setData(this.modelArrayList.get(i), i);
        }
        if (viewHolder instanceof TitleViewHolder) {
            ((TitleViewHolder) viewHolder).setData(this.modelArrayList.get(i));
        }
        if (viewHolder instanceof CheckedItemHolder) {
            ((CheckedItemHolder) viewHolder).setData(this.checkedbodyItemArrayList.get(i - this.modelArrayList.size()), i - this.modelArrayList.size());
        }
        if (viewHolder instanceof EditedDateHolder) {
            ((EditedDateHolder) viewHolder).binding.addNewForCheckList.setText(this.context.diaryData.lastModifiedDate());
        }
    }

    public int getItemCount() {
        return this.modelArrayList.size() + this.checkedbodyItemArrayList.size() + 1;
    }

    public boolean onItemMove(int i, int i2) {
        Collections.swap(this.modelArrayList, i, i2);
        notifyItemMoved(i, i2);
        return true;
    }

    class CheckedItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener, TextWatcher {
        BodyItemRawLayoutBinding binding;


        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        public CheckedItemHolder(@NonNull View view, CTextWatcher cTextWatcher, int i) {
            super(view);
            this.binding = (BodyItemRawLayoutBinding) DataBindingUtil.bind(view);
            this.binding.checkboxButton.setOnClickListener(this);
            this.binding.checkboxButton.setImageResource(R.drawable.ic_check_box);
            this.binding.checkboxButton.setColorFilter(ContextCompat.getColor(context, R.color.black), PorterDuff.Mode.SRC_IN);
            this.binding.handle.setVisibility(View.INVISIBLE);
            this.binding.removeItem.setOnClickListener(this);
            this.binding.itemName.setTextColor(ContextCompat.getColor(context, R.color.black));
            this.binding.itemName.setPaintFlags(this.binding.itemName.getPaintFlags() | 16);
            this.binding.itemName.setRawInputType(1);
            this.binding.itemName.setImeActionLabel("NEXT", 5);
            this.binding.itemName.setImeOptions(5);
        }

        public void setData(String str, int i) {
            this.binding.itemName.setText(str);
            if (curPos == i) {
                this.binding.itemName.post(new Runnable() {
                    public void run() {
                        if (CheckedItemHolder.this.binding.itemName.requestFocus()) {
                            AppConstants.cursorPos(CheckedItemHolder.this.binding.itemName);
                            imm.showSoftInput(CheckedItemHolder.this.binding.itemName, 1);
                        }
                    }
                });
                curPos = -1;
            }
            this.binding.itemName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View view, boolean z) {
                    try {
                        int pos = getPos(CheckedItemHolder.this.getAdapterPosition());
                        Log.i("onTextChanged", "onFocusChange: " + z + pos);
                        if (z) {
                            CheckedItemHolder.this.binding.itemName.addTextChangedListener(CheckedItemHolder.this);
                            AppConstants.cursorPos(CheckedItemHolder.this.binding.itemName);
                            CheckedItemHolder.this.binding.removeItem.setVisibility(View.VISIBLE);
                        } else {
                            CheckedItemHolder.this.binding.removeItem.setVisibility(View.INVISIBLE);
                        }
                        CheckedItemHolder.this.binding.itemName.setTag("Change");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            this.binding.itemName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    try {
                        int pos = getPos(CheckedItemHolder.this.getAdapterPosition());
                        if (keyEvent == null && i == 5 && checkedbodyItemArrayList.size() > 0) {
                            Log.i("onEditorAction", "onTextChanged: " + CheckedItemHolder.this.binding.itemName.getText().toString().length() + "//" + CheckedItemHolder.this.binding.itemName.getSelectionStart());
                            if (CheckedItemHolder.this.binding.itemName.getText().toString().length() > CheckedItemHolder.this.binding.itemName.getSelectionStart()) {
                                CheckedItemHolder.this.binding.itemName.setTag("Stop");
                                checkedbodyItemArrayList.set(pos, CheckedItemHolder.this.binding.itemName.getText().toString().substring(0, CheckedItemHolder.this.binding.itemName.getSelectionStart()));
                                notifyItemChanged(pos);
                            }
                            checkedbodyItemArrayList.set(pos, CheckedItemHolder.this.binding.itemName.getText().toString().substring(0, CheckedItemHolder.this.binding.itemName.getSelectionStart()));
                            notifyItemChanged(CheckedItemHolder.this.getAdapterPosition());
                            String substring = CheckedItemHolder.this.binding.itemName.getText().toString().length() > CheckedItemHolder.this.binding.itemName.getSelectionStart() ? CheckedItemHolder.this.binding.itemName.getText().toString().substring(CheckedItemHolder.this.binding.itemName.getSelectionStart(), CheckedItemHolder.this.binding.itemName.getText().toString().length()) : "";
                            int i2 = pos + 1;
                            curPos = i2;
                            checkedbodyItemArrayList.add(i2, substring);
                            notifyItemInserted(CheckedItemHolder.this.getAdapterPosition() + 1);
                            checkItemClick.addNewItemClick(CheckedItemHolder.this.getAdapterPosition() + 1, substring, 0);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                }
            });
            this.binding.itemName.setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View view, int i, KeyEvent keyEvent) {
                    try {
                        if (keyEvent.getAction() != 0 || i != 67) {
                            return false;
                        }
                        int pos = getPos(CheckedItemHolder.this.getAdapterPosition());
                        if ((checkedbodyItemArrayList.size() <= 1 || pos <= 0) && CheckedItemHolder.this.binding.itemName.getText().toString().length() != 0) {
                            return false;
                        }
                        Log.i("onKey", "onKey: " + pos);
                        if (!removeItem(pos, checkedbodyItemArrayList)) {
                            return false;
                        }
                        notifyItemRemoved(CheckedItemHolder.this.getAdapterPosition());
                        if (pos > 0) {
                            pos--;
                        }
                        curPos = pos;
                        CheckedItemHolder.this.binding.itemName.setTag("Change");
                        if (CheckedItemHolder.this.binding.itemName.getText().toString().length() > 0) {
                            checkedbodyItemArrayList.set(pos, checkedbodyItemArrayList.get(pos).concat(CheckedItemHolder.this.binding.itemName.getText().toString().length() > CheckedItemHolder.this.binding.itemName.getSelectionStart() ? CheckedItemHolder.this.binding.itemName.getText().toString().substring(CheckedItemHolder.this.binding.itemName.getSelectionStart(), CheckedItemHolder.this.binding.itemName.getText().toString().length()) : ""));
                            notifyItemChanged(curPos + modelArrayList.size());
                        } else {
                            notifyItemChanged(curPos + modelArrayList.size());
                        }
                        updateBodyText();
                        return false;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                }
            });
            this.binding.executePendingBindings();
        }

        public void onClick(View view) {
            int id = view.getId();
            if (id == R.id.checkboxButton) {
                try {
                    this.binding.itemName.setTag("Stop");
                    checkItemClick.unCheckItemClick(getPos(getAdapterPosition()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (id == R.id.removeItem) {
                try {
                    int pos = getPos(getAdapterPosition());
                    removeItem(pos, checkedbodyItemArrayList);
                    notifyItemRemoved(getAdapterPosition());
                    updateBodyText();
                    curPos = pos;
                    if (pos == checkedbodyItemArrayList.size() - 1) {
                        curPos = pos - 1;
                    }
                    this.binding.itemName.setTag("Stop");
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (this.binding.itemName.getTag() != null && !this.binding.itemName.getTag().equals("Stop")) {
                Log.i("onTextChanged", "afterTextChanged: " + editable.toString());
                try {
                    checkedbodyItemArrayList.set(getPos(getAdapterPosition()), editable.toString());
                    updateBodyText();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void updateBodyText() {
        try {
            this.context.bodyDataChanged = true;
            this.context.bodyTextFromList();
            this.context.saveAndUpdateData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class BodyItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener, TextWatcher {
        BodyItemRawLayoutBinding binding;

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        public BodyItemHolder(@NonNull View view, CTextWatcher cTextWatcher, int i) {
            super(view);
            this.binding = (BodyItemRawLayoutBinding) DataBindingUtil.bind(view);
            this.binding.checkboxButton.setImageResource(R.drawable.ic_check_box_outline);
            this.binding.removeItem.setOnClickListener(this);
            this.binding.checkboxButton.setOnClickListener(this);
            this.binding.itemName.setRawInputType(1);
            this.binding.itemName.setImeActionLabel("NEXT", 5);
            this.binding.itemName.setImeOptions(5);
        }

        public void onClick(View view) {
            int id = view.getId();
            if (id == R.id.checkboxButton) {
                try {
                    this.binding.itemName.setTag("Stop");
                    checkItemClick.checkItemClick(getPos(getAdapterPosition()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (id == R.id.removeItem) {
                try {
                    int pos = getPos(getAdapterPosition());
                    removeItem(pos, modelArrayList);
                    updateBodyText();
                    curPos = pos;
                    this.binding.itemName.setTag("Stop");
                    notifyItemRemoved(getAdapterPosition());
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }

        public void setData(String str, int i) {
            this.binding.itemName.setText(str);
            if (curPos == i) {
                this.binding.itemName.post(new Runnable() {
                    public void run() {
                        try {
                            if (BodyItemHolder.this.binding.itemName.requestFocus()) {
                                AppConstants.cursorPos(BodyItemHolder.this.binding.itemName);
                                imm.showSoftInput(BodyItemHolder.this.binding.itemName, 1);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                curPos = -1;
            }
            this.binding.itemName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View view, boolean z) {
                    try {
                        int pos = getPos(BodyItemHolder.this.getAdapterPosition());
                        Log.i("onTextChanged", "onFocusChange: " + z + pos);
                        if (z) {
                            BodyItemHolder.this.binding.itemName.addTextChangedListener(BodyItemHolder.this);
                            curPos = pos;
                            BodyItemHolder.this.binding.itemName.setTag("Change");
                            AppConstants.cursorPos(BodyItemHolder.this.binding.itemName);
                            BodyItemHolder.this.binding.removeItem.setVisibility(View.VISIBLE);
                            return;
                        }
                        BodyItemHolder.this.binding.removeItem.setVisibility(View.INVISIBLE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            this.binding.itemName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    try {
                        int pos = getPos(BodyItemHolder.this.getAdapterPosition());
                        if (keyEvent == null) {
                            if (i == 5 && modelArrayList.size() > 0) {
                                Log.i("onEditorAction", "onTextChanged: " + BodyItemHolder.this.binding.itemName.getText().toString().length() + "//" + BodyItemHolder.this.binding.itemName.getSelectionStart());
                                if (BodyItemHolder.this.binding.itemName.getText().toString().length() > BodyItemHolder.this.binding.itemName.getSelectionStart()) {
                                    BodyItemHolder.this.binding.itemName.setTag("Stop");
                                    modelArrayList.set(pos, BodyItemHolder.this.binding.itemName.getText().toString().substring(0, BodyItemHolder.this.binding.itemName.getSelectionStart()));
                                    notifyItemChanged(pos);
                                }
                                String substring = BodyItemHolder.this.binding.itemName.getText().toString().length() > BodyItemHolder.this.binding.itemName.getSelectionStart() ? BodyItemHolder.this.binding.itemName.getText().toString().substring(BodyItemHolder.this.binding.itemName.getSelectionStart(), BodyItemHolder.this.binding.itemName.getText().toString().length()) : "";
                                int i2 = pos + 1;
                                curPos = i2;
                                modelArrayList.add(i2, substring);
                                notifyItemInserted(BodyItemHolder.this.getAdapterPosition() + 1);
                                checkItemClick.addNewItemClick(BodyItemHolder.this.getAdapterPosition() + 1, substring, 0);
                            }
                            Log.i("onEditorAction", "onTextChanged: " + modelArrayList);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                }
            });
            this.binding.itemName.setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View view, int i, KeyEvent keyEvent) {
                    try {
                        if (keyEvent.getAction() != 0 || i != 67) {
                            return false;
                        }
                        int pos = getPos(BodyItemHolder.this.getAdapterPosition());
                        if ((modelArrayList.size() <= 1 || pos <= 1) && BodyItemHolder.this.binding.itemName.getText().toString().length() != 0) {
                            return false;
                        }
                        String str = modelArrayList.get(pos);
                        if (!removeItem(pos, modelArrayList)) {
                            return false;
                        }
                        notifyItemRemoved(BodyItemHolder.this.getAdapterPosition());
                        if (pos > 1) {
                            pos--;
                        }
                        curPos = pos;
                        BodyItemHolder.this.binding.itemName.setTag("Change");
                        if (str.length() > 0) {
                            modelArrayList.set(pos, modelArrayList.get(pos).concat(str));
                            notifyItemChanged(pos);
                        } else {
                            notifyItemChanged(pos);
                        }
                        BodyItemHolder.this.binding.itemName.requestFocus();
                        updateBodyText();
                        return false;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                }
            });
            this.binding.executePendingBindings();
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (this.binding.itemName.getTag() != null && !this.binding.itemName.getTag().equals("Stop")) {
                Log.i("onTextChanged", "afterTextChanged: " + editable.toString());
                try {
                    modelArrayList.set(getAdapterPosition(), editable.toString());
                    updateBodyText();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void onViewRecycled(@NonNull RecyclerView.ViewHolder viewHolder) {
        super.onViewRecycled(viewHolder);
    }

    class AddItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        BodyAddItemRawLayoutBinding binding;

        public AddItemHolder(@NonNull View view) {
            super(view);
            this.binding = (BodyAddItemRawLayoutBinding) DataBindingUtil.bind(view);
            this.binding.addNew.setOnClickListener(this);
        }

        public void onClick(View view) {
            if (view.getId() == R.id.addNew) {
                try {
                    curPos = modelArrayList.size() - 1;
                    modelArrayList.add(modelArrayList.size() - 1, "");
                    notifyItemInserted(curPos);
                    checkItemClick.addNewItemClick(curPos, "", 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class EditedDateHolder extends RecyclerView.ViewHolder {
        ModifiedDateLayoutBinding binding;

        public EditedDateHolder(@NonNull View view) {
            super(view);
            this.binding = (ModifiedDateLayoutBinding) DataBindingUtil.bind(view);
        }
    }

    class TitleViewHolder extends RecyclerView.ViewHolder {
        EditAddTitleHolderBinding binding;

        public TitleViewHolder(@NonNull View view) {
            super(view);
            this.binding = (EditAddTitleHolderBinding) DataBindingUtil.bind(view);
        }

        public void setData(String str) {
            this.binding.titleET.setText(str);
            this.binding.titleET.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable editable) {
                }

                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                    try {
                        modelArrayList.set(0, charSequence.toString());
                        checkItemClick.titleItemClick(charSequence.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }


    public boolean removeItem(int i, ArrayList<String> arrayList) {
        arrayList.remove(i);
        return true;
    }

    private class CTextWatcher implements TextWatcher {
        EditText itemName;
        private int position;
        private int viewType;

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        private CTextWatcher() {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            Log.i("onTextChanged", "onTextChanged: " + charSequence.toString());
            charSequence.toString();
            handler.removeCallbacks(input_finish_checker);
        }

        @Override
        public void afterTextChanged(Editable editable) {
            Log.i("onTextChanged", "afterTextChanged: " + this.position + this.itemName.getTag() + "///" + this.itemName.getText().toString() + "//");
            if (this.itemName.getTag() != null && !this.itemName.getTag().equals("Stop")) {
                ArrayList<String> arrayList = modelArrayList;
                if (this.viewType == 3) {
                    arrayList = checkedbodyItemArrayList;
                }
                try {
                    last_text_edit = System.currentTimeMillis();
                    input_finish_checker.setData(this.position, editable.toString(), arrayList);
                    if (editable.toString().length() > 0) {
                        delay = 5;
                        handler.postDelayed(input_finish_checker, delay);
                        return;
                    }
                    delay = 0;
                    handler.postDelayed(input_finish_checker, delay);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class RunnableClass implements Runnable {
        ArrayList<String> list;
        int position;
        String s;

        public RunnableClass() {
        }

        public void setData(int i, String str, ArrayList<String> arrayList) {
            this.position = i;
            this.s = str;
            this.list = arrayList;
        }

        public void run() {
            try {
                long currentTimeMillis = System.currentTimeMillis();
                long j = last_text_edit + delay;
                long j2 = 0;
                if (delay > 0) {
                    j2 = delay / 2;
                }
                if (currentTimeMillis > j - j2) {
                    Log.i("onTextChanged", "run: " + this.position + this.s + this.list);
                    this.list.set(this.position, this.s);
                    updateBodyText();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
