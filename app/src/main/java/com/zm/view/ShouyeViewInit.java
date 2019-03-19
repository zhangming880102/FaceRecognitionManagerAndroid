package com.zm.view;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zm.R;
import com.zm.bean.ManagerUserBean;
import com.zm.utils.BDLocation;
import com.zm.utils.MyProperty;


import java.util.ArrayList;

public class ShouyeViewInit extends ViewInit{

    public static final int STAGE_LOCATION=0;
    public static final int STAGE_CAMERA=1;
    public static final int STAGE_POST=2;

    Dialog dialog;
    BDLocation bdl;
    Handler handler;
    Runnable thread;

    Runnable thread_post;

    Runnable thread_camera;

    boolean camera_done=false;
    boolean img_success=false;
    boolean has_face=false;
    String err_str="";
    int count=0;
    int camera_scene;
    String response;
    String postJson;
    String serv_review_list="RegisterListService";
    String serv_sign_list="ManagerSignListService";
    int stage=-1;


    Runnable post_sign_list;
    Handler handler_sign_list;

    Runnable post_review_list;
    Handler handler_review_list;
    ManagerUserBean userBean;
    boolean info_checked;
    @Override
    public void init_buttons(){

        init_runnable_for_post_sign_list();
        init_handler_for_post_sign_list();
        init_runnable_for_post_review_list();
        init_handler_for_post_review_list();
        v.findViewById(R.id.shouye_button_edit).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                new EditAddressViewInit().init(ac,ac.getEditAddressView());
                ac.setContentView(ac.getEditAddressView());
            }
        });
        v.findViewById(R.id.shouye_button_review_list).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                new Thread(post_review_list).start();
                dialog=new ProgressDialog(ac);
                dialog.show();
            }
        });
        v.findViewById(R.id.shouye_button_sign_list).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                new Thread(post_sign_list).start();
                dialog=new ProgressDialog(ac);
                dialog.show();
            }
        });
        v.findViewById(R.id.shouye_button_fanhuidenglu).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    ac.setContentView(ac.getLoginView());
                }
            }
        );
    }

    void init_runnable_for_post_review_list(){
        post_review_list=new Runnable() {
            @Override
            public void run() {
                Gson gson=new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                postJson=gson.toJson(ac.getUser(),ManagerUserBean.class);
                try {
                    outTime=false;
                    response = doPost(postJson, "http://" + MyProperty.ip + ":" + MyProperty.port + "/" + MyProperty.service + "/" + serv_review_list);
                }catch (Exception e){
                    e.printStackTrace();
                }
                handler_review_list.sendMessage(handler_review_list.obtainMessage());
            }
        };
    }
    void init_handler_for_post_review_list(){
        handler_review_list=new Handler(){
            @Override
            public void handleMessage(Message msg){
                dialog.dismiss();
                Gson gson=new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                ManagerUserBean responseUser=gson.fromJson(response,ManagerUserBean.class);
                if(outTime) {
                    alert_string("服务器连接失败，请稍后重试！");
                }else if(responseUser==null || !responseUser.isChecked()){
                    alert_string("获取审核列表失败");
                }else{
                    ac.setUser(responseUser);
                    new ReviewListInit().init(ac,ac.getReviewListView());
                    ac.setContentView(ac.getReviewListView());
                }
            }
        };
    }

    void init_runnable_for_post_sign_list(){
        post_sign_list=new Runnable() {
            @Override
            public void run() {
                Gson gson=new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                postJson=gson.toJson(ac.getUser(),ManagerUserBean.class);
                try {
                    outTime=false;
                    response = doPost(postJson, "http://" + MyProperty.ip + ":" + MyProperty.port + "/" + MyProperty.service + "/" + serv_sign_list);
                }catch (Exception e){
                    e.printStackTrace();
                }
                handler_sign_list.sendMessage(handler_sign_list.obtainMessage());
            }
        };
    }
    void init_handler_for_post_sign_list(){
        handler_sign_list=new Handler(){
            @Override
            public void handleMessage(Message msg){
                dialog.dismiss();
                Gson gson=new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                ManagerUserBean responseUser=gson.fromJson(response,ManagerUserBean.class);
                if(outTime) {
                    alert_string("服务器连接失败，请稍后重试！");
                }else if(responseUser==null || !responseUser.isChecked()){
                    alert_string("获取审核列表失败");
                }else{
                    ac.setUser(responseUser);
                    new SignListInit().init(ac,ac.getSignListView());
                    ac.setContentView(ac.getSignListView());
                }
            }
        };
    }

}
