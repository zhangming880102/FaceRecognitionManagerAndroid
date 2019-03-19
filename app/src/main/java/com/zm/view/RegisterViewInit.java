package com.zm.view;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zm.R;
import com.zm.bean.ManagerUserBean;
import com.zm.utils.BDLocation;
import com.zm.utils.MyProperty;
import com.zm.utils.MyUtils;

import java.util.ArrayList;

public class RegisterViewInit extends ViewInit {

    final static int STAGE_POST=1;
    final static int STAGE_LOCAL=0;
    Uri imageUri;
    String postJson;
    Handler handler;
    boolean outtime;
    Dialog dialog;
    String response;
    String serv="ManagerRegisterService";
    ManagerUserBean userBean;
    boolean info_checked=false;

    Runnable post_runnable;
    Runnable local_runnable;

    int count=0;
    boolean img_success=false;
    String err_str="";
    boolean camera_done=false;
    boolean has_face;
    int stage;
    BDLocation bdl;
    @Override
    public void init_buttons() {
        init_post_runnable();
        init_local_runnable();
        init_handler();
        init_caiji_button();
    }
    void init_local_runnable(){
        local_runnable=new Runnable() {
            @Override
            public void run() {
                if(!bdl.getPos().isReceived()) {
                    count++;
                    if (count > 10) {
                        handler.sendMessage(handler.obtainMessage());
                    }else{
                        handler.postDelayed(local_runnable,300);
                    }
                }else{
                    handler.sendMessage(handler.obtainMessage());
                }
            }
        };
    }
    void init_handler(){

        handler=new Handler(){
            @Override
            public void handleMessage(Message message){
                super.handleMessage(message);
                if(stage==STAGE_LOCAL){
                    dialog.dismiss();
                    if (!bdl.getPos().isReceived()) {
                        alert_string("定位失败，请检查定位是否打开");
                        stage=-1;
                    } else {
                        msg_string("定位成功");
                        userBean.setDepartment_address(bdl.getPos().getAddress());
                        userBean.setDepartment_latitude(bdl.getPos().getLatitude());
                        userBean.setDepartment_longtitude(bdl.getPos().getLongtitude());
                        stage=STAGE_POST;
                        post_user();
                    }
                }else if(stage==STAGE_POST) {
                    stage = -1;
                    if (outtime) {
                        alert_string("服务器连接超时！请稍后重试");
                        return;
                    }
                    Gson gson = new GsonBuilder()
                            .setDateFormat("yyyy-MM-dd HH:mm:ss")
                            .create();
                    ManagerUserBean lb = gson.fromJson(response, ManagerUserBean.class);
                    if (lb == null || !(lb.isRegistered())) {
                        String str = "注册失败！";
                        if (lb != null) {
                            str += lb.getErr_str();
                        }
                        alert_string(str);
                    } else {
                        ac.setUser(lb);
                        msg_string("注册成功!");
                        TextView signinfo = ac.getsView().findViewById(R.id.sign_info_text);
                        signinfo.setText(lb.getCompany_name() + lb.getDepartment_name() + "\n" + lb.getDepartment_address());
                        String key[] = new String[]{MyProperty.USER_SHARE_KEY, MyProperty.PASSWORD_SHARE_KEY};
                        String value[] = new String[]{lb.getUser(), lb.getPassword()};
                        MyUtils.saveShareInfo(ac, key, value, MyProperty.SHARE_NAME);
                        ac.setContentView(ac.getsView());
                    }
                }
            }
        };
    }
    void init_post_runnable(){

       post_runnable=new Runnable() {
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
    }

    void post_user(){
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
        postJson=gson.toJson(userBean,ManagerUserBean.class);
        outtime=false;
        new Thread(post_runnable).start();
    }

    void checkUserInfo(){
        info_checked=false;

        String user=((EditText) (v.findViewById(R.id.register_input_zhanghao))).getText().toString();
        String password=((EditText) (v.findViewById(R.id.register_input_mima))).getText().toString();
        String xuexiao=((EditText) (v.findViewById(R.id.register_input_xuexiao))).getText().toString();
        String banji=((EditText) (v.findViewById(R.id.register_input_banji))).getText().toString();
        String name=((EditText) (v.findViewById(R.id.register_input_name))).getText().toString();

        if(user==null || user.trim().length()==0){
            alert_string("账号不能为空！");
            return;
        }
        if(password==null ||password.trim().length()==0){
            alert_string("密码不能为空！");
            return;
        }

        if(xuexiao==null ||xuexiao.trim().length()==0){
            alert_string("公司不能为空！");
            return;
        }

        if(banji==null ||banji.trim().length()==0){
            alert_string("部门不能为空！");
            return;
        }

        if(name==null ||name.trim().length()==0){
            alert_string("姓名不能为空！");
            return;
        }
        user=user.trim();
        password=password.trim();
        xuexiao=xuexiao.trim();
        banji=banji.trim();
        name=name.trim();

        userBean=new ManagerUserBean();
        userBean.setUser(user);
        userBean.setPassword(password);
        userBean.setDepartment_name(banji);
        userBean.setCompany_name(xuexiao);
        userBean.setName(name);
        info_checked=true;
    }
    void local_address(){
        bdl=new BDLocation(ac);
        bdl.checkPos(ac);
        count=0;
        dialog = new ProgressDialog(ac);
        dialog.show();
        stage=STAGE_LOCAL;
        handler.post(local_runnable);
    }

    void init_caiji_button(){
        (v.findViewById(R.id.caiji_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stage=STAGE_LOCAL;
                checkUserInfo();
                if(!info_checked){
                    stage=-1;
                    return;
                }
                local_address();
            }
        });
    }

}
