package com.lenovo.smarttraffic.TolssHome;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toolbar;

import com.lenovo.smarttraffic.BusQueryCatalog.BusQueryActivity;
import com.lenovo.smarttraffic.DataAnalysis_15.DataAnalysisActivity;
import com.lenovo.smarttraffic.LifeAssistant_14.LifeAssistantActivity;
import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.TrafficHome.TrafficActivity;
import com.lenovo.smarttraffic.ViolationHome.ViolationActivity;

/**
 * @author glsite.com
 * @version $Rev$
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public class ToolbarMaster extends LinearLayout {
    private static Toolbar toolbar;
    private static TextView title;
    private static Context mContext;
    private static Button btn_account, btn_inMoney , btn_Record ;

    public ToolbarMaster(final Context context,AttributeSet attrs) {
        super(context,attrs);
        this.mContext = context;
        LayoutInflater.from(context).inflate(R.layout.toolbar_layout, this);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.menu);
        title = findViewById(R.id.toolbar_title);
    }

    public static void MenuCreate() {
        toolbar.setNavigationOnClickListener(view -> {
            //创建弹出式菜单对象 最低版本11
            PopupMenu popupMenu = new PopupMenu(mContext, view);
            //获取菜单填充器
            MenuInflater menuInflater = popupMenu.getMenuInflater();
            //填充菜单
            menuInflater.inflate(R.menu.nav_menu,popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.item_4:
                            mContext.startActivity(new Intent(mContext,BusQueryActivity.class));
                            break;
                        case R.id.item_5:
                            mContext.startActivity(new Intent(mContext, ViolationActivity.class));
                            break;
                        case R.id.item_6:
                            mContext.startActivity(new Intent(mContext, TrafficActivity.class));
                            break;
                        case R.id.item_7:
                            mContext.startActivity(new Intent(mContext, LifeAssistantActivity.class));
                            break;
                        case R.id.item_8:
                            mContext.startActivity(new Intent(mContext, DataAnalysisActivity.class));
                            break;
                        default:
                            break;
                    }
                    return false;
                }
            });
            popupMenu.show();
        });
    }

    public static void setTitle(String T) {
        title.setText(T); /*设置Title*/
    }
}
