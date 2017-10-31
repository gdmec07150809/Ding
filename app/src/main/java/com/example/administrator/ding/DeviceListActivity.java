package com.example.administrator.ding;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2017/9/22.
 */

public class DeviceListActivity extends Activity {
    private ImageView ble;
    String name, id, locationName, locationId,blMac,blType,blName;
    public String longitude;
    public String latitude;
    private TextView userName,title;
    public static final int SHOW_RESPONSE = 0;
    private ListView wifi_listView;
    private LocationManager locationManager;
    private String provider;
    private Location location;
    private static Toast t1 = null;
    private Button syn,new_device,exit;
    private ListView list1;

    JSONObject object = null;
    String response=null;
    JSONArray jsonArray = null;
    Object str[]=null;
    String [] strNew=null;
    String [] Codes=null;
    private long clickTime=0;
    private Spinner Right;
    private List<String> data_list;
    private ArrayAdapter<String> arr_adapter;
    boolean isFlag=true;
    boolean isF=true;
    boolean isShow=false;

    String keep="false";
    String lang="chinese";
    String time="10";
    private ImageView img;
    //重写onKeyDown方法,实现双击退出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if ((System.currentTimeMillis() - clickTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再次点击退出",  Toast.LENGTH_SHORT).show();
            clickTime = System.currentTimeMillis();
        } else {
            Log.e(TAG, "exit application");
            this.finish();
            System.exit(0);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_list);

        ble = findViewById(R.id.ble);
        userName = findViewById(R.id.userName);
        Right=findViewById(R.id.Right);
        title=findViewById(R.id.title);
        new_device=findViewById(R.id.new_device);
        exit=findViewById(R.id.exit);
        syn=findViewById(R.id.syn);
        img=findViewById(R.id.img);
        //获取数据
        Bundle bundle = this.getIntent().getExtras();
        name = bundle.getString("name");
        id = bundle.getString("id");
        locationName = bundle.getString("locationName");
        locationId = bundle.getString("locationId");
        //获取刷新时间
        if( bundle.getString("time")!=null){
            time= bundle.getString("time");
        }
        if(bundle.getString("keep")!=null){
            keep= bundle.getString("keep");
        }

        //判断是否让屏幕常亮
        if(keep!=null){
            if(keep.equals("true")){
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        }

        //获取语言

        if(bundle.getString("lang")!=null){
            lang=bundle.getString("lang");
        }
        if(lang.equals("english")){
            title.setText("Device List");
            new_device.setText("Add");
            syn.setText("Synchro");
            exit.setText("Exit");
        }
        if(locationName.length()>4){
            locationName=locationName.substring(0,4);
        }
        userName.setText("  " + locationName + " \n" + name);
//        if(longitude==null){
//            Toast.makeText(this, "正在获取位置,请打开GPS...", Toast.LENGTH_SHORT).show();
//        }
        //数据
        data_list = new ArrayList<String>();

            data_list.clear();
        if(lang.equals("english")){
            data_list.add("Sort by name");
            data_list.add("Sort by SN");
        }else {
            data_list.add("按名称排序");
            data_list.add("按SN排序");
        }

        //适配器
        arr_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice );
        //加载适配器
        Right.setAdapter(arr_adapter);
        img.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Right.performClick();
            }
        });
        //下拉列表监听事件
        Right.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        TextView tv1=(TextView) view;
                        tv1.setTextSize(18.0f); //设置大小
                        isFlag=false;
                        isF=true;
                        Sort_handlerTimer.sendEmptyMessageDelayed(0, 100);//启动handler，进入设备列表界面1秒后执行
                        break;
                    case 1:
                        TextView tv2=(TextView) view;
                        tv2.setTextSize(18.0f);
                        //设置大小informationSort();
                        isFlag=true;
                        isF=false;
                        Sort_handlerTimer.sendEmptyMessageDelayed(0, 100);//启动handler，进入设备列表界面1秒后执行
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        wifi_listView=findViewById(R.id.listView);

        //sendRequestWithHttpClient(locationId);
        //System.out.println(jsonArray);
      //  handlerTimer.sendEmptyMessageDelayed(0, 1000);//启动handler，进入设备列表界面1秒后执行

        //退出
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.exit(0);
            }
        });
        //立刻同步
        syn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                syn.setBackgroundResource(R.drawable.button_shape_normal);
                Immediately_handlerTimer.sendEmptyMessageDelayed(0, 100);//启动handler，进入设备列表界面1秒后执行
            }
        });
        //添加list列表项的监听
        wifi_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(DeviceListActivity.this,DeviceSaveActivity.class);
                //将name,id,locationId,locationName等的值传给下一个Activity
                Bundle bundle=new Bundle();
                bundle.putString("name", name);
                bundle.putString("id", id);
                bundle.putString("locationId", locationId);
                bundle.putString("locationName", locationName);
                bundle.putString("time",time);
                bundle.putString("keep",keep);
                bundle.putString("lang",lang);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);//销毁中间的Activity

//                System.out.println("序号： "+i);
//                System.out.println("每一项： "+ str[i]);

                String ss= str[i]+"";

                try {
                    JSONObject obj=new JSONObject(ss);
                    bundle.putString("blMac", obj.getString("blMac"));
                    bundle.putString("blType", obj.getString("blType"));
                    bundle.putString("blName", obj.getString("blName"));
                    bundle.putString("blCode", obj.getString("blCode"));
                    bundle.putString("signAddress", obj.getString("signAddress"));
                    bundle.putString("keey",keep);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                intent.putExtras(bundle);
                startActivity(intent);//跳转
            }
        });


        //添加新设备
        new_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DeviceListActivity.this, MainActivity.class);
                //用Bundle携带数据
                Bundle bundle = new Bundle();
                //传递name参数为tinyphp
                //blMac,blType,blName
                //将name，id，locationName，locationId，latitude，longitude的值传给下一个Activity
                bundle.putString("name", name);
                bundle.putString("id", id);
                bundle.putString("locationName", locationName);
                bundle.putString("locationId", locationId);
                bundle.putString("keep",keep);
                bundle.putString("time",time);
                bundle.putString("lang",lang);


                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                //t1.cancel();
                startActivity(intent);

            }
        });
        //添加进入搜索列表的按钮监听
        ble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent  intent=new Intent(DeviceListActivity.this,DeviceScanActivity.class);
                Intent intent = new Intent(DeviceListActivity.this, SetActivity.class);
                //用Bundle携带数据
                Bundle bundle = new Bundle();
                //传递name参数为tinyphp
                //blMac,blType,blName
                //将name，id，locationName，locationId，latitude，longitude的值传给下一个Activity
                bundle.putString("name", name);
                bundle.putString("id", id);
                bundle.putString("locationName", locationName);
                bundle.putString("locationId", locationId);
                bundle.putString("lang",lang);
                bundle.putString("keep", keep);
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                //t1.cancel();
                startActivity(intent);
               
            }
        });

    }
        //发送请求获取blMAC
    private void sendRequestWithHttpClient(final String locationId) {
        // Toast.makeText(wifiActivity.this,"正在获取要配对的blMAC,请稍等...",Toast.LENGTH_SHORT).show();
        new Thread(new Runnable() {

            @Override
            public void run() {
                //用HttpClient发送请求，分为五步
                //第一步：创建HttpClient对象
                HttpClient httpCient = new DefaultHttpClient();
                //第二步：创建代表请求的对象,参数是访问的服务器地址blMac=12:34:56:78:9A:BC&userId=1001&userName=%E5%BC%A0%E4%B8%89
                HttpGet httpGet = new HttpGet("http://www.ding-new.com/wifiBlutooth/blutoothList.do?locationId="+locationId);
                  //HttpGet httpGet = new HttpGet("http://www.java-go.cn/wifiBlutooth/blutoothList.do?locationId=" + locationId);
               //HttpGet httpGet = new HttpGet("http://192.168.1.102:8080/EasyCCC/wifiBlutooth/blutoothList.do?locationId="+locationId);


                try {
                    //第三步：执行请求，获取服务器发还的相应对象
                    HttpResponse httpResponse = httpCient.execute(httpGet);
                    //第四步：检查相应的状态是否正常：检查状态码的值是200表示正常
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        //第五步：从相应对象当中取出数据，放到entity当中
                        HttpEntity entity = httpResponse.getEntity();

                        String response = EntityUtils.toString(entity, "utf-8");//将entity当中的数据转换为字符串

                        //在子线程中将Message对象发出去
                        Message message = new Message();
                        message.what = SHOW_RESPONSE;
                        message.obj = response.toString();
                        System.out.println("请求！！！");
                        handler.sendMessage(message);
                    } else {
                        Toast.makeText(DeviceListActivity.this, "请求不成功", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }).start();//这个start()方法不要忘记了

    }

    //获取需要配对的blMac
    private Handler handler = new Handler() {

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_RESPONSE:
                    //object1= (JSONObject) msg.obj;
                     response = (String) msg.obj;
                    System.out.println(response);
                    try {
                        jsonArray = new JSONArray(response);
                        str=new Object[jsonArray.length()];
                        strNew=new String[jsonArray.length()];
                        Codes=new String[jsonArray.length()];
                        System.out.println("jsonArray:" + jsonArray);
                        //将获取回来的数据进行解析，正常执行
                        for (int i = 0; i < jsonArray.length(); i++) {
                            object = new JSONObject(jsonArray.get(i) + "");
                            System.out.println("_________________________");
                            strNew[i]=object.getString("blName");
                            Codes[i]=object.getString("blCode");
                            str[i]=object;
                        }
                        //按Name排序时执行
                        if(isFlag==false){
                            System.out.println("排序Name");
                            Arrays.sort(strNew);
                            for(int j=0;j<strNew.length;j++){
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    object = new JSONObject(jsonArray.get(i) + "");
                                    System.out.println("_________________________");

                                    if(strNew[j].equals(object.getString("blName"))){
                                        str[j]=object;
                                    }
                                }
                            }
                        }
                        //按blCode排序时执行
                        if(isF==false){
                            System.out.println("排序blCode");
                            Arrays.sort(Codes);
                            for(int j=0;j<Codes.length;j++){
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    object = new JSONObject(jsonArray.get(i) + "");
                                    System.out.println("_________________________");
                                    if(Codes[j].equals(object.getString("blCode"))){
                                        str[j]=object;
                                    }
                                }
                            }
                        }
                        if(str!=null){
                            wifi_listView.setAdapter(new MyAdapter(DeviceListActivity.this,str));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

            }
        }

    };

    //排序启动定时器
    private Handler Sort_handlerTimer = new Handler(){
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        public void handleMessage(Message msg) {
            System.out.println("调用定时器");
            sendRequestWithHttpClient(locationId);//开始发送请求

        }
    };
    //立刻启动定时器
    private Handler Immediately_handlerTimer = new Handler(){
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        public void handleMessage(Message msg) {
            System.out.println("调用定时器");
            isFlag=false;
            isF=true;
            sendRequestWithHttpClient(locationId);//开始发送请求


        }
    };
    //自动启动定时器
    private Handler handlerTimer = new Handler(){
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        public void handleMessage(Message msg) {
            System.out.println("调用定时器");
            isFlag=true;
            isF=true;
            sendRequestWithHttpClient(locationId);//开始发送请求
            handlerTimer.sendEmptyMessageDelayed(1,1000*60);//60秒后再次执行

        }
    };
    //列表适配器
    public class MyAdapter extends BaseAdapter
    {
        private LayoutInflater mInflater;
        private Context mContext;
        private Object[] str;

        public MyAdapter(Context context, Object[] str)
        {
            mInflater = LayoutInflater.from(context);
            this.mContext = context;
            this.str = str;
        }

        @Override
        public int getCount()
        {
            return str.length;
        }

        @Override
        public Object getItem(int position)
        {
            return str;
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            ViewHolder viewHolder = null;
            if (convertView == null)
            {
                convertView = mInflater.inflate(R.layout.product_item,null);
                viewHolder = new ViewHolder();
                viewHolder.mImageView = (ImageView) convertView
                        .findViewById(R.id.list1);

                viewHolder.star = (ImageView) convertView
                        .findViewById(R.id.star);
                viewHolder.Mac = (TextView) convertView
                        .findViewById(R.id.Mac);
                viewHolder.SN = (TextView) convertView
                        .findViewById(R.id.SN);
                viewHolder.name = (TextView) convertView
                        .findViewById(R.id.name);
                viewHolder.strong = (TextView) convertView
                        .findViewById(R.id.strong);
                convertView.setTag(viewHolder);
            } else
            {
                viewHolder = (ViewHolder) convertView.getTag();
            }
                    System.out.println((JSONObject)str[position]);
            String ss= str[position]+"";
            try {
                JSONObject obj=new JSONObject(ss);
               // blMac,blType,blName
                //进行渲染列表
                blMac=obj.getString("blMac");
                blType=obj.getString("blType");
                blName=obj.getString("blName");
                viewHolder.Mac.setText(obj.getString("blMac"));
                viewHolder.SN.setText(obj.getString("blCode"));
                viewHolder.name.setText(obj.getString("blName"));
                System.out.println(obj.getString("blArriveFlag"));
               if( obj.getString("blArriveFlag").equals("Y")){
                   System.out.println("Y 进来");
                   viewHolder.star.setImageDrawable( getResources().getDrawable(R.drawable.select_yes) );
                   //viewHolder.star.setBackgroundResource(R.drawable.select_yes);
                   viewHolder.strong.setText("已链接");
               }else{
                   viewHolder.star.setImageDrawable( getResources().getDrawable(R.drawable.delete) );
                   viewHolder.strong.setText("未链接");
                   //viewHolder.star.setBackgroundResource(R.drawable.delete);
               }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return convertView;
        }
        private final class ViewHolder
        {
            TextView Mac,SN,name,strong;
            ImageView mImageView,star;
        }
    }
}

