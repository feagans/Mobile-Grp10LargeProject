package com.test.mylifegoale.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.test.mylifegoale.R;
import com.test.mylifegoale.activities.VisionEditActivity;
import com.test.mylifegoale.base.roomDb.AppDatabase;
import com.test.mylifegoale.databinding.VisionHolderBinding;
import com.test.mylifegoale.model.VisionModel;
import com.test.mylifegoale.utilities.Constants;
import com.test.mylifegoale.utilities.SwipeAndDragHelper;

import java.util.ArrayList;

public class VisionAdapter extends RecyclerView.Adapter<VisionAdapter.VisionViewHolder> implements SwipeAndDragHelper.ActionCompletionContract {
    Context context;

    public ItemTouchHelper itemTouchHelper;
    ArrayList<VisionModel> visionModelsList;

    public VisionAdapter(Context context2, ArrayList<VisionModel> arrayList) {
        this.context = context2;
        this.visionModelsList = arrayList;
    }

    @NonNull
    public VisionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new VisionViewHolder(LayoutInflater.from(this.context).inflate(R.layout.vision_holder, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull final VisionViewHolder visionViewHolder, int i) {
        visionViewHolder.binding.imgDrag.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getActionMasked() != 0) {
                    return false;
                }
                itemTouchHelper.startDrag(visionViewHolder);
                return false;
            }
        });
        visionViewHolder.binding.setVisionModel(this.visionModelsList.get(i));
    }

    public int getItemCount() {
        ArrayList<VisionModel> arrayList = this.visionModelsList;
        if (arrayList != null) {
            return arrayList.size();
        }
        return 0;
    }

    public void onViewMoved(int i, int i2) {
        int ord = this.visionModelsList.get(i).getOrd();
        this.visionModelsList.get(i).setOrd(this.visionModelsList.get(i2).getOrd());
        AppDatabase.getAppDatabase(this.context).visionDao().updateVisionOrd(this.visionModelsList.get(i).getId(), this.visionModelsList.get(i).getOrd());
        this.visionModelsList.get(i2).setOrd(ord);
        AppDatabase.getAppDatabase(this.context).visionDao().updateVisionOrd(this.visionModelsList.get(i2).getId(), this.visionModelsList.get(i2).getOrd());
        new VisionModel();
        this.visionModelsList.remove(i);
        this.visionModelsList.add(i2, this.visionModelsList.get(i));
        notifyItemMoved(i, i2);
    }

    public void reallyMoved() {
        notifyDataSetChanged();
    }

    public void onViewSwiped(int i) {
        this.visionModelsList.remove(i);
        notifyItemRemoved(i);
    }

    public void setTouchHelper(ItemTouchHelper itemTouchHelper2) {
        this.itemTouchHelper = itemTouchHelper2;
    }

    public class VisionViewHolder extends RecyclerView.ViewHolder {
        VisionHolderBinding binding;

        public VisionViewHolder(@NonNull View view) {
            super(view);
            this.binding = (VisionHolderBinding) DataBindingUtil.bind(view);
            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    ((Activity) context).startActivityForResult(new Intent(context, VisionEditActivity.class).putExtra(Constants.EDIT_ADD_VISION_TAG, true).putExtra(Constants.VISION_DATA_TAG, visionModelsList.get(VisionViewHolder.this.getAdapterPosition())), 100);
                }
            });
        }
    }
}
