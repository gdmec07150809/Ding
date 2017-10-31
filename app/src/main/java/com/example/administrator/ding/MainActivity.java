package com.example.administrator.ding;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {
    private TextView come,add_machine,fan,interval,screen,ding_new;
    private RadioGroup group,language_group;
    String CheckButton="WIFI";
    private CheckBox ch_box;
    String keep;
    private RadioButton btn1,btn2,btn3,btn4,btn5,btn6,china;
    private EditText edittime;
    String lang;
    String time;
    String name, id, locationName, locationId;
    private ImageView setting_back;

    private long clickTime=0;
    //手机返回键的监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent=new Intent(MainActivity.this,DeviceListActivity.class);
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
        }
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.label);
        come= (TextView) findViewById(R.id.come);
        group = (RadioGroup) findViewById(R.id.group);
        add_machine= (TextView) findViewById(R.id.add_machine);
        fan= (TextView) findViewById(R.id.fan);
        btn1= (RadioButton) findViewById(R.id.button1);
        btn2= (RadioButton) findViewById(R.id.button2);
        btn3= (RadioButton) findViewById(R.id.button3);
        btn4= (RadioButton) findViewById(R.id.button4);
        btn5= (RadioButton) findViewById(R.id.button5);
        btn6= (RadioButton) findViewById(R.id.button6);
        //获取数据
        Bundle bundle = this.getIntent().getExtras();
        lang= bundle.getString("lang");
        keep= bundle.getString("keep");
        name = bundle.getString("name");
        id = bundle.getString("id");
        locationName = bundle.getString("locationName");
        locationId = bundle.getString("locationId");
        //判断是否让屏幕常亮
        if(keep.equals("true")){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        time=bundle.getString("time");
        //  判断是否用英文显示
        if(getIntent().getStringExtra("lang")!=null){
            lang=getIntent().getStringExtra("lang");
            if(lang.equals("english")){
                add_machine.setText("Tag Format");
                come.setText("confirm");
                btn4.setText("Rfid(need NFC)：");
                btn5.setText("ZigBee(need ZigBee)：");
                btn6.setText("QR/Bar Code：");
                fan.setText("back");

            }
        }

//确定事件
        come.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CheckButton.equals("WIFI")){
                    Intent intent=new Intent(MainActivity.this,wifiActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("keep",keep);
                    bundle.putString("time",time);
                    bundle.putString("lang",lang);
                    bundle.putString("id",id);
                    bundle.putString("name",name);
                    bundle.putString("locationName",locationName);
                    bundle.putString("locationId",locationId);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    //android.os.Process.killProcess(android.os.Process.myPid());//按返回按钮时,结束本Activity的运行
                }else if(CheckButton.equals("二维码/条形码")){
                    Intent intent=new Intent(MainActivity.this,codeActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("keep",keep);
                    bundle.putString("lang",lang);
                    bundle.putString("time",time);
                    bundle.putString("id",id);
                    bundle.putString("name",name);
                    bundle.putString("locationName",locationName);
                    bundle.putString("locationId",locationId);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    //android.os.Process.killProcess(android.os.Process.myPid());//按返回按钮时,结束本Activity的运行
                }else if(CheckButton.equals("Bluetooth")){
                    Intent intent=new Intent(MainActivity.this,BluetoothActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("keep",keep);
                    bundle.putString("lang",lang);
                    bundle.putString("time",time);
                    bundle.putString("id",id);
                    bundle.putString("name",name);
                    bundle.putString("locationName",locationName);
                    bundle.putString("locationId",locationId);
                    intent.putExtras(bundle);
                    startActivity(intent);
                   // android.os.Process.killProcess(android.os.Process.myPid());//按返回按钮时,结束本Activity的运行
                } else{
                    Toast.makeText(MainActivity.this,"抱歉此功能暂未开发",Toast.LENGTH_SHORT).show();
                }
            }
        });

        setting_back= (ImageView) findViewById(R.id.settings_back);
        //添加返回按钮的监听
        setting_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,DeviceListActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("keep",keep);
                bundle.putString("time",time);
                bundle.putString("lang",lang);
                bundle.putString("id",id);
                bundle.putString("name",name);
                bundle.putString("locationName",locationName);
                bundle.putString("locationId",locationId);
                intent.putExtras(bundle);
                startActivity(intent);
                //android.os.Process.killProcess(android.os.Process.myPid());//按返回按钮时,结束本Activity的运行
            }
        });
        // 单选按钮组监听事件
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // 根据ID判断选择的按钮
                if (checkedId == R.id.button1) {
                    CheckButton="GPRS";
                } else if (checkedId == R.id.button2) {
                    CheckButton="WIFI";
                }else if (checkedId == R.id.button3) {
                    CheckButton="Bluetooth";
                }else if (checkedId == R.id.button4) {
                    CheckButton="Rfid";
                }else if (checkedId == R.id.button5) {
                    CheckButton="ZigBee";
                }else if (checkedId == R.id.button6) {
                    CheckButton="二维码/条形码";
                }
            }
        });
    }
}
