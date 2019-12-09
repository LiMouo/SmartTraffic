package com.lenovo.smarttraffic;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.lenovo.smarttraffic.BusQueryCatalog.BusQueryActivity;
import com.lenovo.smarttraffic.TestTools.DrawerHome.DrawerLayoutActivity;
import com.lenovo.smarttraffic.ui.activity.BaseActivity;
import com.lenovo.smarttraffic.ui.activity.Item1Activity;
import com.lenovo.smarttraffic.ui.activity.LoginActivity;
import com.lenovo.smarttraffic.ui.fragment.DesignFragment;
import com.lenovo.smarttraffic.ui.fragment.MainContentFragment;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author Amoly
 * @date 2019/4/11.
 * description：
 */
public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private Toolbar mToolbar;
    private DrawerLayout mDrawer;
    private MainContentFragment mMainContent;
    private DesignFragment mDesignFragment;
    private static final String POSITION = "position";
    private static final String SELECT_ITEM = "bottomItem";
    private static final int FRAGMENT_MAIN = 0;
    private static final int FRAGMENT_DESIGN = 1;
    private BottomNavigationView bottom_navigation;
    //    private int position;

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        if (savedInstanceState != null) {
            //使用fragmentation加载根组件
            loadMultipleRootFragment(R.id.container, 0, mMainContent, mDesignFragment);
            // 恢复 recreate 前的位置
            showFragment(savedInstanceState.getInt(POSITION));
            bottom_navigation.setSelectedItemId(savedInstanceState.getInt(SELECT_ITEM));
        } else {
            showFragment(FRAGMENT_MAIN);
        }
    }

    private void showFragment(int index) {
//        position = index;
        /*beginTransaction 在与此FragmentManager相关的Fragment上开始一系列编辑操作。  打开事务
        getSupportFragmentManager 返回用于与与此活动关联的片段进行交互的FragmentManager。 获取fragment管理器
        ft 进行碎片
        */
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        hideFragment(ft);
        switch (index) {
            //0
            case FRAGMENT_MAIN:
                mToolbar.setTitle(R.string.title_main);
                if (mMainContent == null) {
                    //如果页面实例为空，就创建一个新的
                    mMainContent = MainContentFragment.getInstance();
                    ft.add(R.id.container, mMainContent, MainContentFragment.class.getName());
                } else {
                    ft.show(mMainContent);
                }
                break;
                //1
            case FRAGMENT_DESIGN:
                mToolbar.setTitle(R.string.creative_design);
                if (mDesignFragment == null) {
                    mDesignFragment = DesignFragment.getInstance();
                    ft.add(R.id.container, mDesignFragment, DesignFragment.class.getName());
                } else {
                    ft.show(mDesignFragment);
                }
                break;
                default:
                    break;
        }
        ft.commit();

    }

    private void hideFragment(FragmentTransaction ft) {
        // 如果不为空，就先隐藏起来
        //隐藏，然后显示用户进行的操作
        if (mMainContent != null) {
            ft.hide(mMainContent);
        }
        if (mDesignFragment != null) {
            ft.hide(mDesignFragment);
        }
    }

    private void initView() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //标题
        mToolbar = findViewById(R.id.toolbar);
        //侧滑菜单布局
        mDrawer = findViewById(R.id.drawer_layout);
        //<!--侧滑布局控件-->
        NavigationView navigationView = findViewById(R.id.nav_view);
        //主页面，创意设计
        bottom_navigation = findViewById(R.id.bottom_navigation);
        //头像 圆形图像视图
        CircleImageView imageView = navigationView.getHeaderView(0).findViewById(R.id.ivAvatar);
        //设置标题
        setSupportActionBar(mToolbar);
        //头像点击事件
        imageView.setOnClickListener(this);
        /*设置选择item监听*/
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initData() {
        //设置
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //将抽屉指示器/功能的状态与链接的DrawerLayout同步。
        toggle.syncState();
        //将指定的侦听器添加到将通知抽屉事件的侦听器列表。
        mDrawer.addDrawerListener(toggle);

        bottom_navigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_main:
                    showFragment(FRAGMENT_MAIN);
                    break;
                case R.id.action_creative:
                    showFragment(FRAGMENT_DESIGN);
                    break;
                    default:
                        break;
            }
            return true;
        });

    }

    @Override
    public void onBackPressedSupport() {
        //打开或关闭左边的菜单
        /* isDrawerOpen 检查给定的抽屉视图当前是否处于打开状态。
        * 抽屉必须处于完全可见的状态才能被视为“打开”状态。如果没有给定重力的抽屉，此方法*将返回false。
        * GravityCompat 兼容性填充程序，用于从中访问更新的功能
        *
        * closeDrawer 通过使指定的动画不在视线范围内来关闭它
        * */
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
//            super.onBackPressedSupport();
            showExitDialog();
        }
    }

    /**是否退出项目*/
    private void showExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("确定退出吗");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", (dialogInterface, i) -> InitApp.getInstance().exitApp());
        builder.show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        /*设置选中item事件*/
        int id = item.getItemId();
        String string = null;
        switch (id) {
            case R.id.nav_account:
                string = "个人";
                break;
            case R.id.item_1:
                string = "item1";
                startActivity(new Intent(this, Item1Activity.class));
                break;
            case R.id.item_2:
                string = "item2";
                break;
            case R.id.item_3:
                string = "item3";
                break;
            case R.id.item_4:
                string = "第十题";
                startActivity(new Intent(this, BusQueryActivity.class));
                break;
            case R.id.nav_setting:
                string = "设置";
                break;
            case R.id.nav_about:
                string = "关于";
                break;
                default:
                    break;
        }
        if (!TextUtils.isEmpty(string)) {
            {
                Toast.makeText(InitApp.getInstance(), "你点击了" + "\"" + string + "\"", Toast.LENGTH_SHORT).show();
            }
        }
        //mDrawer.closeDrawer(GravityCompat.START);
        mDrawer.closeDrawers();
        return true;
    }

    @Override
    public void onClick(View view) {
        /*点击头像跳转登录界面*/
        if (view.getId() == R.id.ivAvatar) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        mDrawer.closeDrawer(GravityCompat.START);
    }
}
