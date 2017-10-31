package com.example.administrator.ding;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/16.
 */
@SuppressLint("SdCardPath")
public class DeviceSaveActivity extends Activity {
    private Spinner spinner;
    private List<String> data_list;
    private ArrayAdapter<String> arr_adapter;
    private ImageView settings_back,machine_photo_img;
    private TextView location,add_machine,fan,device_type,device_name,device_mac,device_number,machine_photo,edit;
    private EditText tv_show_location,machine_name_edt,machine_type_edt,mac_edt,uuid_edt,tv_show_mLocation,tv_show_du;
    String name,id,locationName,locationId,longitude,latitude, blMac,blType,blName,blcode,signAddress;
    private double BatteryT;//电池温度
    private Button photo;
    String keep,lang,time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_machine_activity_lyt);
        spinner = (Spinner) findViewById(R.id.spinner);
        settings_back = (ImageView) findViewById(R.id.settings_back);
        location = (TextView) findViewById(R.id.location);
        tv_show_location = (EditText) findViewById(R.id.tv_show_location);
        machine_name_edt = (EditText) findViewById(R.id.machine_name_edt);
        machine_type_edt = (EditText) findViewById(R.id.machine_type_edt);
        mac_edt = (EditText) findViewById(R.id.mac_edt);
        uuid_edt = (EditText) findViewById(R.id.uuid_edt);
        tv_show_mLocation= (EditText) findViewById(R.id.tv_show_mLocation);
        tv_show_du=findViewById(R.id.tv_show_du);
        photo=findViewById(R.id.photo);
        machine_photo_img=findViewById(R.id.machine_photo_img);
        add_machine=findViewById(R.id.add_machine);
        fan=findViewById(R.id.fan);
        edit=findViewById(R.id.edit);
        //location,device_type,device_name,device_mac,device_number,machine_photo;
        location=findViewById(R.id.location);
        device_type=findViewById(R.id.device_type);
        device_name=findViewById(R.id.device_name);
        device_mac=findViewById(R.id.device_mac);
        device_number=findViewById(R.id.device_number);
        machine_photo=findViewById(R.id.machine_photo);
        //获取信息
        Bundle bundle = this.getIntent().getExtras();
        keep= bundle.getString("keep");
        name = bundle.getString("name");
        time = bundle.getString("time");
        lang = bundle.getString("lang");
        id = bundle.getString("id");
        blMac = bundle.getString("blMac");
        blType = bundle.getString("blType");
        blName = bundle.getString("blName");
        locationName = bundle.getString("locationName");
        locationId = bundle.getString("locationId");
        blcode = bundle.getString("blCode");
        signAddress = bundle.getString("signAddress");
        //判断是否让屏幕常亮
        if(keep.equals("true")){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
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
        //返回事件
        settings_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DeviceSaveActivity.this,DeviceListActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("name", name);
                bundle.putString("id", id);
                bundle.putString("locationId", locationId);
                bundle.putString("locationName", locationName);
                bundle.putString("lang", lang);
                bundle.putString("time", time);
                bundle.putString("keep", keep);
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


//        //拍照按钮监听,有点问题，未完全实现
//        photo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // TODO Auto-generated method stub
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(intent, 0);
//            }
//        });
        //判断是否英文显示   //location,device_type,device_name,device_mac,device_number,machine_photo;
        if(lang.equals("english")){
            add_machine.setText("Set Information");
            fan.setText("back");
            location.setText("Location");
            device_type.setText("type:");
            device_name.setText("name:");
            device_mac.setText("mac:");
            device_number.setText("number:");
            machine_photo.setText("photo:");
        }

        //判断是否让屏幕常亮
        if(keep!=null){
            if(keep.equals("true")){
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        }

        //blMac,blType,blName
//         longitude = bundle.getString("longitude");
//        latitude = bundle.getString("latitude");

        System.out.println("设备列表    经度："+longitude+"  "+"纬度："+latitude);
        if(longitude==null||longitude.equals("null")){
            tv_show_location.setText("请更新再获取...");
        }else{
            tv_show_location.setText("经度："+longitude+"\n"+"纬度："+latitude);
        }
        machine_name_edt.setText(blName);
        machine_type_edt.setText(blType);
        mac_edt.setText(blMac);
        uuid_edt.setText(blcode);
        //System.out.println(signAddress);
        if(signAddress==null|| signAddress.equals("null")){
            tv_show_mLocation.setText("请更新...");
        }else{
            tv_show_mLocation.setText(signAddress);
        }
       // machine_name_edt,machine_type_edt,mac_edt,uuid_edt
        //编辑
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DeviceSaveActivity.this,editActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("name", name);
                bundle.putString("id", id);
                bundle.putString("locationId", locationId);
                bundle.putString("locationName", locationName);
                bundle.putString("lang", lang);
                bundle.putString("machine_name_edt",blName);
                bundle.putString("machine_type_edt",blType);
                bundle.putString("mac_edt",blMac);
                bundle.putString("uuid_edt",blcode);
                if(signAddress==null|| signAddress.equals("null")){
                    bundle.putString("tv_show_mLocation","请更新...");
                }else{
                    bundle.putString("tv_show_mLocation",signAddress);
                }
                if(longitude==null||longitude.equals("null")){
                    bundle.putString("tv_show_location","请更新再获取...");
                }else{
                    bundle.putString("tv_show_location","经度："+longitude+"\n"+"纬度："+latitude);
                }
                bundle.putString("time", time);
                bundle.putString("keep", keep);
                bundle.putString("on","edit");
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
//手机返回键监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent=new Intent(DeviceSaveActivity.this,DeviceListActivity.class);
            Bundle bundle=new Bundle();
            bundle.putString("name", name);
            bundle.putString("id", id);
            bundle.putString("locationId", locationId);
            bundle.putString("locationName", locationName);
            bundle.putString("lang", lang);
            bundle.putString("time", time);
            bundle.putString("keep", keep);
            intent.putExtras(bundle);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return true;
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
//    @SuppressLint("SdCardPath")
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        // TODO Auto-generated method stub
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK) {
//            String sdStatus = Environment.getExternalStorageState();
//            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
//                Log.i("TestFile",
//                        "SD card is not avaiable/writeable right now.");
//                return;
//            }
//            new DateFormat();
//            String name = DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
//            System.out.println("路径："+name);
//            Toast.makeText(this, name, Toast.LENGTH_LONG).show();
//            Bundle bundle = data.getExtras();
//            Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
//
//            FileOutputStream b = null;
//            File file = new File("/sdcard/Image/");
//            file.mkdirs();// 创建文件夹
//            String fileName = "/sdcard/Image/"+name;
//
//            try {
//                b = new FileOutputStream(fileName);
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } finally {
//                try {
//                    b.flush();
//                    b.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            try
//            {
//                machine_photo_img.setImageBitmap(bitmap);// 将图片显示在ImageView里
//            }catch(Exception e)
//            {
//                Log.e("error", e.getMessage());
//            }
//
//        }
//    }
   
}
