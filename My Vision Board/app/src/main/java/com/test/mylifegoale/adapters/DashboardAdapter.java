package com.test.mylifegoale.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.test.mylifegoale.R;
import com.test.mylifegoale.databinding.DashboardHolderBinding;
import com.test.mylifegoale.model.VisionModel;

import java.util.ArrayList;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.DashboardViewHolder> {
    Context context;
    DashBoardItemClick recyclerClick;
    String type;
    ArrayList<VisionModel> visionModels;

    public interface DashBoardItemClick {
        void onItemClick(VisionModel visionModel, String str);
    }

    public DashboardAdapter(String str, ArrayList<VisionModel> arrayList, Context context2, DashBoardItemClick dashBoardItemClick) {
        type = str;
        visionModels = arrayList;
        context = context2;
        recyclerClick = dashBoardItemClick;
    }

    @NonNull
    public DashboardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new DashboardViewHolder(LayoutInflater.from(context).inflate(R.layout.dashboard_holder, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull DashboardViewHolder dashboardViewHolder, int i) {
        Log.i("onBindViewHolder", "onBindViewHolder: " + visionModels.get(i).getCatTitle());
        dashboardViewHolder.binding.setVisionModel(visionModels.get(i));
    }

    public int getItemCount() {
        ArrayList<VisionModel> arrayList = visionModels;
        if (arrayList != null) {
            return arrayList.size();
        }
        return 0;
    }

    public class DashboardViewHolder extends RecyclerView.ViewHolder {
        DashboardHolderBinding binding;

        public DashboardViewHolder(@NonNull View view) {
            super(view);
            binding = (DashboardHolderBinding) DataBindingUtil.bind(view);
            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    recyclerClick.onItemClick(visionModels.get(getAdapterPosition()), type);
                }
            });
        }
    }
}
