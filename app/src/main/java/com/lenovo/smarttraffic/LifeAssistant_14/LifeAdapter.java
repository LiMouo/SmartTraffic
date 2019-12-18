package com.lenovo.smarttraffic.LifeAssistant_14;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lenovo.smarttraffic.R;

import java.util.List;

/**
 * @author glsite.com
 * @version $Rev$
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public class LifeAdapter extends RecyclerView.Adapter<LifeAdapter.LifeViewHolder>{
    private Context mContext;
    private String[] name = {"紫外线指数","感冒指数","穿衣指数","运动指数","空气污染指数"};
    private String[] temperature;

    public LifeAdapter(Context context, String[] temperature) {
        this.mContext = context;
        this.temperature = temperature;
    }

    @NonNull
    @Override
    public LifeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_life_image,parent,false);
        return new LifeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LifeViewHolder holder, int position) {
        for (int i = 0; i < position; i++) {
            holder.mText_1.setText(name[position]);
        }
        holder.mUV.setText("120");
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class LifeViewHolder extends RecyclerView.ViewHolder {
        private TextView mUV,mText_1,mText_2,T_number;
        private View mImage;

        public LifeViewHolder(View itemView) {
            super(itemView);
            mUV = itemView.findViewById(R.id.number_uv);
            mImage = itemView.findViewById(R.id.image);
            mText_1 = itemView.findViewById(R.id.text_1);
            mText_2 = itemView.findViewById(R.id.text_2);
            T_number = itemView.findViewById(R.id.T_number);
        }
    }
}
