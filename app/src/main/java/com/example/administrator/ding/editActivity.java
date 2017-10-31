package com.example.administrator.ding;

/**
 * Created by Administrator on 2017/10/16.
 */
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.view.WindowManager;
import android.widget.ImageView;
import java.util.ArrayList;
import android.widget.Toast;
import java.util.List;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import static com.example.administrator.ding.R.id.machine_photo_img;
import static com.example.administrator.ding.R.id.photo;
import static com.example.administrator.ding.R.id.tv_show_location;

public class editActivity extends Activity {
    private String on_activity;
    private Spinner spinner;
    private List<String> data_list;
    private ArrayAdapter<String> arr_adapter;
    private ImageView settings_back,machine_photo_img,conserve;
    private EditText machine_name_edt,mac_edt,machine_type_edt,tv_show_location,mLocation;
    private TextView location,add_machine,fan,device_type,deviceName,deviceMac,device_number,machine_photo;
    private String lang,keep,name,id,locationName,locationId,time,device_name,device_mac,type,blCode,signAddress;
    private EditText uuid_edt,tv_show_mLocation,tv_show_du;
    private double BatteryT;//电池温度
    private Button photo;
    ArrayList<String> resultList = new ArrayList<String>();

    public editActivity() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_machine_activity);
        //location,add_machine,fan,device_type,deviceName,deviceMac,device_number,machine_photo;
        location=findViewById(R.id.location);
        add_machine=findViewById(R.id.add_machine);
        fan=findViewById(R.id.fan);
        device_type=findViewById(R.id.device_type);
        deviceName=findViewById(R.id.device_name);
        deviceMac=findViewById(R.id.device_mac);
        device_number=findViewById(R.id.device_number);
        machine_photo=findViewById(R.id.machine_photo);
        settings_back=findViewById(R.id.settings_back);
        machine_name_edt=findViewById(R.id.machine_name_edt);
        mac_edt=findViewById(R.id.mac_edt);
        machine_photo_img=findViewById(R.id.machine_photo_img);
        machine_type_edt=findViewById(R.id.machine_type_edt);
        tv_show_du=findViewById(R.id.tv_show_du);
        tv_show_location=findViewById(R.id.tv_show_location);
        mLocation=findViewById(R.id.tv_show_mLocation);
        photo=findViewById(R.id.photo);
        uuid_edt=findViewById(R.id.uuid_edt);
        spinner = (Spinner) findViewById(R.id.spinner);
        conserve=findViewById(R.id.conserve);
        //获取传递数据
        Bundle bundle = this.getIntent().getExtras();
        on_activity=bundle.getString("on");
        lang= bundle.getString("lang");
        keep= bundle.getString("keep");
        name = bundle.getString("name");
        id = bundle.getString("id");
        locationName = bundle.getString("locationName");
        locationId = bundle.getString("locationId");

        //数据
        data_list = new ArrayList<String>();
        if(lang.equals("english")){
            data_list.add("temperature");
            data_list.add("humidity");
            data_list.add("flow");
            data_list.add("pressure");
        }else{
            data_list.add("温度");
            data_list.add("湿度");
            data_list.add("流量");
            data_list.add("压力");
        }
        //适配器
        arr_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spinner.setAdapter(arr_adapter);

        //下拉列表监听事件
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        // 注册一个系统 BroadcastReceiver，作为访问电池计量之用这个不能直接在AndroidManifest.xml中注册
                        registerReceiver(mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
                        break;
                    case 1:tv_show_du.setText("  20rh%");break;
                    case 2:tv_show_du.setText("  50M");break;
                    case 3:tv_show_du.setText("  100PA");break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //拍照按钮监听
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });
        //判断对应页面，填写对应信息
        if(on_activity.equals("wifi")){
            device_name = bundle.getString("device_name");
            device_mac=bundle.getString("device_mac");
            machine_name_edt.setText(device_name);
            mac_edt.setText(device_mac);
            machine_type_edt.setText("wifi");
        }else if(on_activity.equals("bluetooth")){
            device_name = bundle.getString("device_name");
            device_mac=bundle.getString("device_mac");
            machine_name_edt.setText(device_name);
            mac_edt.setText(device_mac);
            machine_type_edt.setText("bluetooth");
        }else if(on_activity.equals("code")){
            device_name = bundle.getString("device_name");
            resultList = getIntent().getStringArrayListExtra("result");
            machine_name_edt.setText(device_name);
            machine_type_edt.setText("code");
        }else if(on_activity.equals("edit")){
            device_name = bundle.getString("machine_name_edt");
            type=bundle.getString("machine_type_edt");
            device_mac=bundle.getString("mac_edt");
            blCode=bundle.getString("uuid_edt");
            uuid_edt.setText(blCode);
            signAddress=bundle.getString("tv_show_mLocation");
            tv_show_location.setText(bundle.getString("tv_show_location"));
            mLocation.setText(signAddress);
            machine_name_edt.setText(device_name);
            mac_edt.setText(device_mac);
            machine_type_edt.setText(type);
        }
        //判断是否让屏幕常亮
        if(keep.equals("true")){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        time=bundle.getString("time");
        //  判断是否用英文显示
        //location,add_machine,fan,device_type,deviceName,deviceMac,device_number,machine_photo;
        if(lang.equals("english")){
            add_machine.setText("Set Information");
            fan.setText("back");
            location.setText("Location");
            device_type.setText("type:");
            deviceName.setText("name:");
            deviceMac.setText("mac:");
            device_number.setText("number:");
            machine_photo.setText("photo:");

        }
        //返回按钮事件
        settings_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(on_activity.equals("wifi")){
                    Intent intent=new Intent(editActivity.this,wifiActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("keep",keep);
                    bundle.putString("time",time);
                    bundle.putString("lang",lang);
                    bundle.putString("id",id);
                    bundle.putString("name",name);
                    bundle.putString("locationName",locationName);
                    bundle.putString("locationId",locationId);
                    bundle.putString("on",on_activity);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else if(on_activity.equals("bluetooth")){
                    Intent intent=new Intent(editActivity.this,BluetoothActivity.class);  Bundle bundle=new Bundle();
                    bundle.putString("keep",keep);
                    bundle.putString("time",time);
                    bundle.putString("lang",lang);
                    bundle.putString("id",id);
                    bundle.putString("name",name);
                    bundle.putString("locationName",locationName);
                    bundle.putString("locationId",locationId);
                    bundle.putString("on",on_activity);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else if(on_activity.equals("code")){
                    Intent intent=new Intent(editActivity.this,codeActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("keep",keep);
                    bundle.putString("time",time);
                    bundle.putString("lang",lang);
                    bundle.putString("id",id);
                    bundle.putString("name",name);
                    bundle.putString("locationName",locationName);
                    bundle.putString("locationId",locationId);
                    bundle.putString("on",on_activity);
                    intent.putExtras(bundle);
                    intent.putStringArrayListExtra("result", (ArrayList<String>) resultList);
                    startActivity(intent);
                }else if(on_activity.equals("edit")){
                    Intent intent=new Intent(editActivity.this,DeviceSaveActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("keep",keep);
                    bundle.putString("time",time);
                    bundle.putString("lang",lang);
                    bundle.putString("id",id);
                    bundle.putString("name",name);
                    bundle.putString("locationName",locationName);
                    bundle.putString("locationId",locationId);
                    bundle.putString("on",on_activity);
                    bundle.putString("blName",device_name);
                    bundle.putString("blMac",device_mac);
                    bundle.putString("blType",type);
                    bundle.putString("blCode",blCode);
                    bundle.putString("signAddress",signAddress);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
        //保存
        conserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(editActivity.this,DeviceListActivity.class);
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
        });
    }
//获取,处理拍照事件
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            String sdStatus = Environment.getExternalStorageState();
            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                Log.i("TestFile",
                        "SD card is not avaiable/writeable right now.");
                return;
            }
            new DateFormat();
            String name = DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".png";
            System.out.println("路径："+name);
            Toast.makeText(this, name, Toast.LENGTH_LONG).show();
            Bundle bundle = data.getExtras();
            Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式

            FileOutputStream b = null;
            File file = new File("/sdcard/Image/");
            file.mkdirs();// 创建文件夹
            String fileName = "/sdcard/Image/"+name;

            try {
                b = new FileOutputStream(fileName);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, b);// 把数据写入文件
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    b.flush();
                    b.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try
            {
                machine_photo_img.setImageBitmap(bitmap);// 将图片显示在ImageView里
            }catch(Exception e)
            {
                Log.e("error", e.getMessage());
            }

        }
    }
    /* 创建广播接收器 */
    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver()
    {
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            /*
             * 如果捕捉到的action是ACTION_BATTERY_CHANGED， 就运行onBatteryInfoReceiver()
             */
            if (Intent.ACTION_BATTERY_CHANGED.equals(action))
            {
                BatteryT = intent.getIntExtra("temperature", 0);  //电池温度
                tv_show_du.setText( " "+(BatteryT*0.1) + "℃");
            }
        }
    };
    //所有手机返回键事件
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(on_activity.equals("wifi")){
            Intent intent=new Intent(editActivity.this,wifiActivity.class);
            Bundle bundle=new Bundle();
            bundle.putString("keep",keep);
            bundle.putString("time",time);
            bundle.putString("lang",lang);
            bundle.putString("id",id);
            bundle.putString("name",name);
            bundle.putString("locationName",locationName);
            bundle.putString("locationId",locationId);
            bundle.putString("on",on_activity);
            intent.putExtras(bundle);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }else if(on_activity.equals("bluetooth")){
            Intent intent=new Intent(editActivity.this,BluetoothActivity.class);  Bundle bundle=new Bundle();
            bundle.putString("keep",keep);
            bundle.putString("time",time);
            bundle.putString("lang",lang);
            bundle.putString("id",id);
            bundle.putString("name",name);
            bundle.putString("locationName",locationName);
            bundle.putString("locationId",locationId);
            bundle.putString("on",on_activity);
            intent.putExtras(bundle);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }else if(on_activity.equals("code")){
            Intent intent=new Intent(editActivity.this,codeActivity.class);
            Bundle bundle=new Bundle();
            bundle.putString("keep",keep);
            bundle.putString("time",time);
            bundle.putString("lang",lang);
            bundle.putString("id",id);
            bundle.putString("name",name);
            bundle.putString("locationName",locationName);
            bundle.putString("locationId",locationId);
            bundle.putString("on",on_activity);
            intent.putExtras(bundle);
            intent.putStringArrayListExtra("result", (ArrayList<String>) resultList);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }else  if(on_activity.equals("edit")){
            Intent intent=new Intent(editActivity.this,DeviceSaveActivity.class);
            Bundle bundle=new Bundle();
            bundle.putString("keep",keep);
            bundle.putString("time",time);
            bundle.putString("lang",lang);
            bundle.putString("id",id);
            bundle.putString("name",name);
            bundle.putString("locationName",locationName);
            bundle.putString("locationId",locationId);
            bundle.putString("on",on_activity);
            bundle.putString("blName",device_name);
            bundle.putString("blMac",device_mac);
            bundle.putString("blType",type);
            bundle.putString("blCode",blCode);
            bundle.putString("signAddress",signAddress);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        return true;
    }
}
