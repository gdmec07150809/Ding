package com.example.administrator.ding;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/10/16.
 */

public class SetActivity extends MainActivity {
        private TextView come,add_machine,Label,interval,screen,ding_new;
        private RadioGroup group,language_group;
        private CheckBox ch_box;
        String keep="false";
        private RadioButton btn1,btn2,btn3,btn4,btn5,btn6,china;
        private EditText edittime;
        String language="china";
        String lang;
        String time;
        String name,id,locationName,locationId;
        private static final String fileName = "sharedfile";// 定义保存的文件的名称
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

            if (keyCode == KeyEvent.KEYCODE_BACK) {
                Intent intent=new Intent(SetActivity.this,DeviceListActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("keep",keep);
                bundle.putString("time",time);
                bundle.putString("lang",lang);
                bundle.putString("id",id);
                bundle.putString("name",name);
                bundle.putString("locationName",locationName);
                bundle.putString("locationId",locationId);
                intent.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                android.os.Process.killProcess(android.os.Process.myPid());//按返回按钮时,结束本Activity的运行
            }
            return true;

    }

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.set);
            come= (TextView) findViewById(R.id.come);
            interval= (TextView) findViewById(R.id.interval);
            group = (RadioGroup) findViewById(R.id.group);
            language_group = (RadioGroup) findViewById(R.id.language_group);
            ch_box= (CheckBox) findViewById(R.id.ch_box);
            edittime= (EditText) findViewById(R.id.edittime);
            add_machine= (TextView) findViewById(R.id.add_machine);
            screen= (TextView) findViewById(R.id.screen);
            ding_new= (TextView) findViewById(R.id.ding_new);

            china= (RadioButton) findViewById(R.id.china);
            //获取缓存
             SharedPreferences share = super.getSharedPreferences(fileName,
                MODE_PRIVATE);
             edittime.setText(share.getString("timeStr",10+""));// 如果没有值，则显示“10”
             keep=share.getString("keep",false+"");
            if(keep.equals("true")){
            ch_box.setChecked(true);
             }
            Bundle bundle = this.getIntent().getExtras();
            name = bundle.getString("name");
            id = bundle.getString("id");
            locationName = bundle.getString("locationName");
            locationId = bundle.getString("locationId");
            //  判断是否用英文显示
            if(getIntent().getStringExtra("lang")!=null){
                lang=getIntent().getStringExtra("lang");
                if(lang.equals("english")){
                    add_machine.setText("Setting");
                    come.setText("confirm");
                    interval.setText("Search interval");
                    screen.setText("The screen is always on");
                    china.setText("Chinese：");
                    ding_new.setText("ding_new v0.1");
                }
            }
        //确定
            come.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(SetActivity.this,DeviceListActivity.class);
                    Bundle bundle=new Bundle();
                    //传递name参数为tinyphp
//                bundle.putString("longitude", longitude);
//                bundle.putString("latitude", latitude);
                    //将name，id，locationId，locationName的值传到下个Activity
                    time=edittime.getText().toString();
                    bundle.putString("name", name);
                    bundle.putString("id", id);
                    bundle.putString("locationId", locationId);
                    bundle.putString("locationName", locationName);
                    bundle.putString("time", time);
                    bundle.putString("keep", keep);
                    bundle.putString("lang", language);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    android.os.Process.killProcess(android.os.Process.myPid());//按返回按钮时,结束本Activity的运行
                }
            });

            // 单选按钮组监听事件
            language_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    // 根据ID判断选择的按钮
                    if (checkedId == R.id.china) {
                        language="china";
                    } else if (checkedId == R.id.english) {
                        language="english";
                    }
                }
            });
            //屏幕常亮复选框事件
            ch_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {
                    // TODO Auto-generated method stub
                    if(isChecked){
                        keep="true";
                        Toast.makeText(SetActivity.this,"保持常亮",Toast.LENGTH_SHORT).show();
                        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    }else {
                        keep="false";
                        Toast.makeText(SetActivity.this,"取消常亮",Toast.LENGTH_SHORT).show();
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    }
                }
            });
        }

}
