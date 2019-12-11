package com.lenovo.smarttraffic.ViolationHome;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lenovo.smarttraffic.R;

import java.util.List;
import java.util.Map;

public class ViolationAdapter extends RecyclerView.Adapter {
    private Context context;
    private boolean isRight;
    private List<Map> CarInfo;
    private String TAG = "Violation_Result_2";

    public ViolationAdapter(Context context, boolean isRight, List<Map> CarInfo) {
        this.context = context;
        this.isRight = isRight;
        this.CarInfo = CarInfo;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (!isRight){
            if (viewType == 0){
                Log.e(TAG,  "Title_left" );
                return new Title(LayoutInflater.from(context).inflate(R.layout.violation_result_title,parent,false));
            }else {
                Log.e(TAG,  "leftLayout" );
                return new LeftViewHolder(LayoutInflater.from(context).inflate(R.layout.violation_result_left_container,parent,false));
            }
        }else {
            if (viewType==0){
                Log.e(TAG, "Title_right" );
                return new Title(LayoutInflater.from(context).inflate(R.layout.violation_result_title,parent,false));
            }else {
                Log.e(TAG, "RightLayout");
                return new RightViewHolder(LayoutInflater.from(context).inflate(R.layout.violation_result_right_container,parent,false));
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (!isRight){
            if (position == 0){
                Title t_view = ((Title) holder);
                t_view.T_title.setText("汽车资料卡片");
                t_view.I_icon.setImageResource(R.drawable.plus);
                /*设置加号点击监听*/
            }else {
                LeftViewHolder view = ((LeftViewHolder) holder);
                view.T_number.setText("Test");
                view.T_car_id.setText("鲁10001");
                view.T_money.setText("100元");
                view.T_reduce.setText("扣分 1 ");
            }
        }else {
            if (position == 0){
                ((Title)holder).T_title.setText("违章详情");
            }
        }
       /* LeftViewHolder view = (LeftViewHolder) holder;
        */
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    class LeftViewHolder extends RecyclerView.ViewHolder {
        private TextView T_car_id,T_number,T_reduce,T_money;
        private ImageView I_icon;
        private LinearLayout L_root;
        public LeftViewHolder(View itemView) {
            super(itemView);
            L_root = itemView.findViewById(R.id.L_root);//左边的布局
            T_car_id = itemView.findViewById(R.id.t_car_id);//左边车牌号
            T_number = itemView.findViewById(R.id.t_number);//左边未处理违章
            T_reduce = itemView.findViewById(R.id.t_reduce);//左边扣分
            T_money = itemView.findViewById(R.id.t_money);//左边罚款
            I_icon = itemView.findViewById(R.id.i_icon);//左边图片
        }
    }

    class Title extends RecyclerView.ViewHolder {
        private TextView T_title;
        private ImageView I_icon;
        public Title(@NonNull View itemView) {
            super(itemView);
            T_title = itemView.findViewById(R.id.t_title);
            I_icon = itemView.findViewById(R.id.i_icon);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return  0;
        }else {
            return 1;
        }
    }

    private class RightViewHolder extends RecyclerView.ViewHolder {
        private TextView T_time,T_road,T_reduce,T_money,T_info;
        private LinearLayout L_root;
        public RightViewHolder(View itemView) {
            super(itemView);
            T_time = itemView.findViewById(R.id.t_time);
            T_road = itemView.findViewById(R.id.t_road);
            T_reduce = itemView.findViewById(R.id.t_reduce);
            T_money = itemView.findViewById(R.id.t_money);
            T_info = itemView.findViewById(R.id.t_info);
            L_root =  itemView.findViewById(R.id.L_root);
        }
    }
}
