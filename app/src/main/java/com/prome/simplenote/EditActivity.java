package com.prome.simplenote;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import me.yokeyword.fragmentation_swipeback.SwipeBackActivity;
import skin.support.SkinCompatManager;

public class EditActivity extends SwipeBackActivity {

    public static final int EDIT_MODE_NEW=0;
    public static final int EDIT_MODE_EDIT=1;

    private int mode;
    private String name;
    private NotePublic notePublic;

    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Toolbar toolbar=(Toolbar)findViewById(R.id.edit_layout_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null) actionBar.setDisplayHomeAsUpEnabled(true);
        loadSettings();
        init();
    }

    public void init(){
        editText=(EditText)findViewById(R.id.edit_layout_edittext);
        Intent intent=getIntent();
        mode=intent.getIntExtra("mode",EDIT_MODE_NEW);
        if (mode==EDIT_MODE_EDIT){
            setTitle("编辑");
            name=intent.getStringExtra("name");
            List<NotePublic> notePublics= DataSupport.where("name=?",name).find(NotePublic.class);
            notePublic=notePublics.get(0);
            editText.setText(notePublic.getContent());
        }else {
            setTitle("新建");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_edit_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_edit_mode:
                if (editText.isFocusable()){
                    editModeView(editText,item);
                }else {
                    editModeEdit(editText,item);
                }
                break;
            case R.id.menu_edit_save:
                String content=editText.getText().toString();
                if (content.equals("")){
                    Snackbar.make(editText,"还没有输入任何内容噢！",Snackbar.LENGTH_SHORT).show();
                    break;
                }
                //获取当前时间作为note的时间
                Calendar calendar=Calendar.getInstance();
                calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
                String month=String.valueOf(calendar.get(Calendar.MONTH)+1)+"月";
                String day=String.valueOf(calendar.get(Calendar.DATE))+"日";
                String hour;
                if (calendar.get(Calendar.AM_PM) == 0) {
                    hour = String.valueOf(calendar.get(Calendar.HOUR))+"点";
                }
                else {
                    hour = "下午"+String.valueOf(calendar.get(Calendar.HOUR))+"点";
                }
                String minute=String.valueOf(calendar.get(Calendar.MINUTE))+"分";
                if (mode==EDIT_MODE_NEW){
                    saveNoteAsNew(content,month,day,hour,minute);
                }else {
                    saveNoteAsEdit(notePublic,content,month,day,hour,minute);
                }
                break;
            case R.id.menu_edit_set:

                break;
        }
        return true;
    }

    private void saveNoteAsNew(String text,String month,String day,String hour,String minute){
        //获取long类型的时间作为note的id
        Date date=new Date();
        long time=date.getTime();
        String id=String.valueOf(time);
        //保存
        NotePublic notePublic=new NotePublic();
        notePublic.setName(id);
        notePublic.setContent(text);
        notePublic.setDate(month+day+"    "+hour+minute);
        if (notePublic.save()){
            Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
            finish();
        }else {
            Snackbar.make(editText,"保存失败",Snackbar.LENGTH_SHORT).show();
        }
    }

    public void saveNoteAsEdit(NotePublic notePublic,String text,String month,String day,String hour,String minute){
        notePublic.setContent(text);
        notePublic.setDate(month+day+"    "+hour+minute);
        if (notePublic.save()){
            Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
            finish();
        }else {
            Snackbar.make(editText,"保存失败",Snackbar.LENGTH_SHORT).show();
        }
    }

    private void result(String name){
        Intent intent=new Intent();
        intent.putExtra("name",name);
        setResult(RESULT_OK,intent);
        finish();
    }

    private void editModeView(EditText editText, MenuItem item){
        editText.setFocusable(false);
        editText.setFocusableInTouchMode(false);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(),0);
        item.setTitle("查看模式");
    }
    private void editModeEdit(EditText editText,MenuItem item){
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
        item.setTitle("编辑模式");
    }

    //加载设置
    @SuppressLint("NewApi")
    public void loadSettings() {
        SharedPreferences sharedPreferences = getSharedPreferences("com.prome.simplenote_preferences", MODE_PRIVATE);
        String theme = sharedPreferences.getString("theme", "颐堤蓝");
        Boolean nightMode=sharedPreferences.getBoolean("nightMode",false);
        //加载主题
        //加载夜间模式
        if (nightMode){
            setStatusBarColor(getColor(R.color.colorPrimaryDark_night));
        }else {
            switch (theme){
                case "颐堤蓝":
                    setStatusBarColor(getColor(R.color.colorPrimaryDark));
                    break;
                case "知乎蓝":
                    setStatusBarColor(getColor(R.color.colorPrimaryDark_blue));
                    break;
                case "姨妈红":
                    setStatusBarColor(getColor(R.color.colorPrimaryDark_red));
                    break;
                case "哔哩粉":
                    setStatusBarColor(getColor(R.color.colorPrimaryDark_pin));
                    break;
                case "纯洁白":
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
}
