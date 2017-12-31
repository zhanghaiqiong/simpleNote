package com.prome.simplenote;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import skin.support.SkinCompatManager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String TAG="prome";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //加载设置
        loadSettings();
        //悬浮按钮被点击
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,EditActivity.class);
                intent.putExtra("mode",EditActivity.EDIT_MODE_NEW);
                startActivityForResult(intent,1);
            }
        });
        //加载碎片
        replaceFragment(new NotePublicActivity());
        //配置侧滑
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        //配置侧滑菜单点击事件
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_note_public);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //加载设置
        loadSettings();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }**/

    //侧滑菜单被点击
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case R.id.nav_note_public:
                setTitle("开放");
                replaceFragment(new NotePublicActivity());
                break;
            case R.id.nav_note_private:
                setTitle("私密");
                replaceFragment(new NotePrivateActivity());
                break;
            case R.id.nav_else_set:
                startActivity(new Intent(MainActivity.this,SettingsActivity.class));
                break;
            case R.id.nav_else_help:

                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //加载设置
        @SuppressLint("NewApi")
        public void loadSettings() {
            SharedPreferences sharedPreferences = getSharedPreferences("com.prome.simplenote_preferences", MODE_PRIVATE);
            String theme = sharedPreferences.getString("theme", "颐堤蓝");
            Boolean nightMode=sharedPreferences.getBoolean("nightMode",false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
                localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
            }
            //加载主题
            //加载夜间模式
            if (nightMode){
                SkinCompatManager.getInstance().loadSkin("night", SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN);
                setStatusBarColor(getColor(R.color.colorPrimaryDark_night));
            }else {
                switch (theme){
                    case "颐堤蓝":
                        SkinCompatManager.getInstance().loadSkin("", SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN);
                        setStatusBarColor(getColor(R.color.colorPrimaryDark));
                        break;
                    case "知乎蓝":
                        SkinCompatManager.getInstance().loadSkin("blue", SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN);
                        setStatusBarColor(getColor(R.color.colorPrimaryDark_blue));
                        break;
                    case "姨妈红":
                        SkinCompatManager.getInstance().loadSkin("red", SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN);
                        setStatusBarColor(getColor(R.color.colorPrimaryDark_red));
                        break;
                    case "哔哩粉":
                        SkinCompatManager.getInstance().loadSkin("pin", SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN);
                        setStatusBarColor(getColor(R.color.colorPrimaryDark_pin));
                        break;
                    case "纯洁白":
                        SkinCompatManager.getInstance().loadSkin("write",SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN);
                        setStatusBarColor(getColor(R.color.colorPrimaryDark_write));
                        break;
                }
            }
    }

    //设置状态栏颜色函数
    public void setStatusBarColor(int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);

        }
    }

    //加载碎片函数
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.replace(R.id.main_layout_FrameLayout,fragment);
        transaction.commit();
    }
}
