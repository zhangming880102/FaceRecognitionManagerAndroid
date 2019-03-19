package com.zm.view;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zm.R;
import com.zm.bean.ManagerUserBean;
import com.zm.bean.RegisterItemBean;
import com.zm.utils.MyProperty;
import com.zm.utils.MyUtils;

public class ReviewViewInit extends ViewInit {
    String postJson;
    Handler handler;
    Runnable runnable;
    Dialog dialog;
    String response;
    String serv="UpdateRegisterLoginService";
    String serv_sign_list="RegisterListService";
    Runnable post_sign_list;
    Handler handler_sign_list;
    Bitmap bitmap;
    RegisterItemBean item;
    @Override
    public void init_buttons(){
        v.findViewById(R.id.review_accept_button).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                accept_register();
            }
        });

        v.findViewById(R.id.review_reject_button).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                reject_register();
            }
        });
        init_handler_for_post_sign_list();
        init_runnable_for_post_sign_list();
        runnable=new Runnable() {
            @Override
            public void run() {
                Log.i("postip","http://"+MyProperty.ip + ":" + MyProperty.port + "/" + MyProperty.service+"/" +serv);
                try {
                    response=doPost(postJson, "http://"+MyProperty.ip + ":" + MyProperty.port + "/" +MyProperty.service+"/" +serv);
                    Message msg=handler.obtainMessage();
                    handler.sendMessage(msg);
                }catch (Exception e){
                    Log.i("err",e.toString());
                }
            }
        };

        handler=new Handler(){
            @Override
            public void handleMessage(Message message){
                super.handleMessage(message);
                dialog.dismiss();
                if(outTime){
                    Log.i("chaoshi","chaoshi");
                    alert_string("服务器连接超时！请稍后重试");
                    return;
                }
                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd HH:mm:ss")
                        .create();
                RegisterItemBean lb=gson.fromJson(response,RegisterItemBean.class);
                if(lb==null||!(lb.isChecked())) {
                    String str="审核失败！";
                    if(lb!=null){
                        str+=lb.getErr_str();
                    }
                    alert_string(str);
                }else {
                    msg_string("审核成功");
                    new Thread(post_sign_list).start();
                    dialog=new ProgressDialog(ac);
                    dialog.show();
                }
            }
        };
    }

    void accept_register(){
        item.setReview_status(1);
        post_review();
    }
    void reject_register(){
        item.setReview_status(2);
        post_review();
    }
    void post_review(){
        Gson gson=new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        postJson=gson.toJson(item,RegisterItemBean.class);
        new Thread(runnable).start();
        dialog=new ProgressDialog(ac);
        dialog.show();
    }

    public void init_image(Bitmap bitmap, RegisterItemBean item){
        this.bitmap=bitmap;
        this.item=item;
        ImageView imageView=v.findViewById(R.id.review_image);
        imageView.setImageBitmap(bitmap);
        TextView tv=v.findViewById(R.id.review_text);
        tv.setText("姓名 "+item.getName()+"   账号 "+item.getUser());
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
                    new ReviewListInit().init(ac,ac.getReviewListView());
                    ac.setContentView(ac.getReviewListView());
                }
            }
        };
    }
}
