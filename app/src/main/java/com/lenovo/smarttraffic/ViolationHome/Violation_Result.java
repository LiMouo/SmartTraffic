package com.lenovo.smarttraffic.ViolationHome;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.TolssHome.MySqLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Violation_Result extends AppCompatActivity {

    private RecyclerView RV_left,RV_right;
    private SQLiteDatabase db;
    private String FirstNumber;
    private ViolationResultAdapter adapter_right,adapter_left;
    private List<Map> LeftInfo = new ArrayList<>();
    private List<Map> RightInfo = new ArrayList<>();
    private int ClickItem = 0;
    private List<Map> AllCodeInfo = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitView();
        InitData();
    }

    private void InitView() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_violation__result);
        RV_left = findViewById(R.id.RV_left);
        RV_right = findViewById(R.id.RV_right);
        FirstNumber = getIntent().getExtras().getString("carnumber");
        db = MySqLiteOpenHelper.getInstance(this).getWritableDatabase();
    }
    private void InitData() {
        LinearLayoutManager left_Manger = new LinearLayoutManager(this);
        RV_left.setLayoutManager(left_Manger);
        adapter_left = new ViolationResultAdapter(this,false,LeftInfo);

        RV_right.setLayoutManager(new LinearLayoutManager(this));
        adapter_right = new ViolationResultAdapter(this,true,RightInfo);
        /*左边每一项*/
        adapter_left.setOnItemClick((v,position) -> {
            RV_left.getViewTreeObserver().addOnDrawListener(() -> {//为RV添加onDraw监听事件
                for (int i = 1; i < left_Manger.getItemCount(); i++) {//遍历所有item项
                    View view = left_Manger.findViewByPosition(i);//findViewByPosition 找到所有item的position项
                    if( view != null )//RV独特有的回收机制，在item不可见时，ViewHolder会被回收，会拿到一个null
                        view.findViewById(R.id.L_root).setBackground(getDrawable(R.drawable.violation_result_item_board));//为所有item设置边框加白背景色
                    v.setBackground(getDrawable(R.drawable.violation_result_item_background));//为点击的item设置边框加灰背景色
                }
            });
            ClickItem = position;
            RightInfo.clear();
            /*RightInfo.addAll(AllCodeInfo.get(position));*/
            adapter_right.notifyDataSetChanged();
//            RightInfo = AllRightInfo.get(position);
            /*不能用此类方法赋值、hashCode不会改变。   RightInfo = AllRightInfo.get(position);
            hashCode返回的并不一定是对象的（虚拟）内存地址、具体取决于运行时库和JVM的具体实现。但是在程序的一次执行过程中、对同一个对象必须一致地返回同一个整数。
            对象一样、notifyDataSetChanged 就不会刷新*/
        });
        /*减号图标*/
        adapter_left.setOnReduceClick(position -> {
            if(ClickItem == position){//点击的减号项与选中的项一致
                String number = LeftInfo.get(position).get("carnumber").toString();
                db.delete("Violation","carnumber='"+number+"'",null);
                LeftInfo.remove(position);
                AllCodeInfo.remove(position);
                adapter_left.RemoveItem(position);
                if(LeftInfo.size() <= 0){
                    Toast.makeText(this,"已无历史记录,请重新查询!",Toast.LENGTH_LONG).show();
                    finish();
                }
            }else{
                Toast.makeText(this,"先选中数据好吗",Toast.LENGTH_LONG).show();
            }
        });
        /*加号图标*/
        adapter_left.setOnPlusClick(() -> {
            Intent intent = new Intent(this,ViolationActivity.class).putExtras(new Bundle());
            startActivityForResult(intent,1);
            overridePendingTransition(R.anim.gradual_in,R.anim.gradual_out);
        });
        /*右边列表项点击*/
        adapter_right.setOnItemClick((v,position)-> Toast.makeText(this, "???", Toast.LENGTH_SHORT).show());

        RV_left.setAdapter(adapter_left);
        RV_right.setAdapter(adapter_right);
        RV_left.setOverScrollMode(View.OVER_SCROLL_NEVER);//取消 上滑到顶、下拉到底 的效果
        RV_right.setOverScrollMode(View.OVER_SCROLL_NEVER);
    }
}
