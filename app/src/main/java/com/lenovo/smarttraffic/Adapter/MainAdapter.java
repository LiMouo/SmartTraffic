package com.lenovo.smarttraffic.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lenovo.smarttraffic.R;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViaewHolder> {
    private Context mContext;

    public MainAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MainAdapter.MainViaewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainViaewHolder(LayoutInflater.from(mContext).inflate(R.layout.main_content,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MainViaewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    class MainViaewHolder extends RecyclerView.ViewHolder {

        public MainViaewHolder(View itemView) {
            super(itemView);
        }
    }
}
