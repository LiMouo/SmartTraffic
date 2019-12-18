package com.lenovo.smarttraffic.BusQueryCatalog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.lenovo.smarttraffic.R;

import java.util.List;
import java.util.Map;

public class BusQueryAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private String[] GroupName;
    private List<Map>[] ChildData;

    public BusQueryAdapter(Context mContext, String[] GroupName, List<Map>[] ChildData) {
        this.mContext = mContext;
        this.GroupName = GroupName;
        this.ChildData = ChildData;
    }

    @Override
    // 获取分组的个数
    public int getGroupCount() {
        return GroupName.length;
    }

    @Override
    //获取指定分组中的子选项的个数
    public int getChildrenCount(int i) {
        return ChildData[i].size();
    }

    @Override
    //获取指定的分组数据
    public Object getGroup(int i) {
        return GroupName[i];
    }

    @Override
    //获取指定分组中的指定子选项数据
    public Object getChild(int groupPosition, int childPosition) { //子项
        return ChildData[groupPosition].get(childPosition);
    }

    @Override
    //获取指定分组的ID, 这个ID必须是唯一的
    public long getGroupId(int i) {
        return i;
    }

    @Override
    //获取子选项的ID, 这个ID必须是唯一的
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    //分组和子选项是否持有稳定的ID, 就是说底层数据的改变会不会影响到它们
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    @Override
    /**
     *
     * 获取显示指定组的视图对象
     *
     * @param groupPosition 组位置
     * @param isExpanded 该组是展开状态还是伸缩状态
     * @param convertView 重用已有的视图对象
     * @param parent 返回的视图对象始终依附于的视图组
     */
    // 获取显示指定分组的视图
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        if (view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.bus_one_item,viewGroup,false);
            TextView title = view.findViewById(R.id.label_group);
            title.setText(GroupName[i]);
        }
        return view;
    }

    /**
     * 获取一个视图对象，显示指定组中的指定子元素数据。
     *
     * @param groupPosition 组位置
     * @param childPosition 子元素位置
     * @param isLastChild   子元素是否处于组中的最后一个
     * @param convertView   重用已有的视图(View)对象
     * @param parent        返回的视图(View)对象始终依附于的视图组
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.bus_tow_item,parent,false);
        //车号人
        TextView T_person = convertView.findViewById(R.id.T_person);
        //时间
        TextView T_time = convertView.findViewById(R.id.T_time);
        //距离
        TextView T_number = convertView.findViewById(R.id.T_number_tem);
        T_person.setText(ChildData[groupPosition].get(childPosition).get("carId")+"号("+ChildData[groupPosition].get(childPosition).get("person")+"人)");
        T_number.setText("距离站台"+ChildData[groupPosition].get(childPosition).get("distance")+"米");
        T_time.setText(ChildData[groupPosition].get(childPosition).get("time")+"分钟到达");
        return convertView;
    }


    @Override
    public boolean hasStableIds() {
        return true;
    }
}
