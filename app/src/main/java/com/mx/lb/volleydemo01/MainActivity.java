package com.mx.lb.volleydemo01;

import android.app.LoaderManager;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    private ImageView iv;
    private TextView tv;
    //private  CardView dd;
    //private FloatingActionButton dd;
    private LoaderManager loaderManager;


    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv= (ImageView) findViewById(R.id.iv);
        tv= (TextView) findViewById(R.id.tv);


        //1.创建一个RequestQueue队列
        RequestQueue queue= Volley.newRequestQueue(this);

        StringRequest stringRequest=new StringRequest("https://www.baidu.com/",
                new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                Log.d("TAG",s);
                tv.setText(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("Tag",volleyError.getMessage());
            }
        });


        JsonObjectRequest  jsonObjectRequest=new JsonObjectRequest("http://mobile.ximalaya.com/m/category_tag_menu",
                null,new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.d("TAG",jsonObject.toString());
                tv.setText(jsonObject.toString());
            }
        },new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError volleyError) {

                Log.d("TAG",volleyError.getMessage());
            }
        });


         ImageRequest imageRequest=new ImageRequest("https://assets-cdn.github.com/images/modules/open_graph/github-mark.png",
                new Response.Listener<Bitmap>() {

                    @Override
                    public void onResponse(Bitmap bitmap) {
                        iv.setImageBitmap(bitmap);
                    }
                }, 0, 0, Bitmap.Config.RGB_565,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        iv.setImageResource(android.R.drawable.btn_dropdown);
                    }
                });



        //2. 创建一个ImageLoader对象。

//        ImageLoader imageLoader=new ImageLoader(queue, new ImageLoader.ImageCache() {
//            @Override
//            public Bitmap getBitmap(String s) {
//                return null;
//            }
//
//            @Override
//            public void putBitmap(String s, Bitmap bitmap) {
//
//            }
//        });

        ImageLoader imageLoader=new ImageLoader(queue,new BitmapCache());
       // 3. 获取一个ImageListener对象。
        ImageLoader.ImageListener imageListener=ImageLoader.getImageListener(iv,android.R.drawable.alert_dark_frame,
                android.R.drawable.btn_dialog);

        //4. 调用ImageLoader的get()方法加载网络上的图片。
       imageLoader.get("http://avatar.csdn.net/F/F/5/1_lmj623565791.jpg",imageListener,200,200);









//        3.添加到请求队列中
        //queue.add(imageRequest);
    }


    class BitmapCache implements ImageLoader.ImageCache{
        private LruCache<String,Bitmap>mCache;
        public BitmapCache(){
               int maxSize=10*1024*1024;
            mCache=new LruCache<String,Bitmap>(maxSize){
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    return value.getRowBytes()*value.getHeight();
                }
            };

        }
        @Override
        public Bitmap getBitmap(String s) {
            return mCache.get(s);
        }
        @Override
        public void putBitmap(String s, Bitmap bitmap) {
                mCache.put(s,bitmap);
        }
    }


    class LooperThread extends Thread {
        public Handler mHandler;

        public void run() {
            Looper.prepare();

            mHandler = new Handler() {
                public void handleMessage(Message msg) {
                    // process incoming messages here
                }
            };

            Looper.loop();
        }
    }

}
