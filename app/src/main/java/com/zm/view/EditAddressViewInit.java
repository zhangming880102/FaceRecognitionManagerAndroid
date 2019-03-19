package com.zm.view;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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

import org.w3c.dom.Text;


public class EditAddressViewInit extends ViewInit {
    public static final int STAGE_LOCATION=0;
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
    String serv="EditAddressService";
    int stage=-1;


    Runnable post_sign_list;
    Handler handler_sign_list;
    ManagerUserBean userBean;
    boolean info_checked=false;
    int using=0;

    @Override
    public void init_buttons(){

        init_edit_text();
        init_runnable_for_location();
        init_runnable_for_post();
        init_handler();

        v.findViewById(R.id.edit_button_fanhui).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                ac.setContentView(ac.getsView());
            }
        });
        v.findViewById(R.id.edit_xiugai_button).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                stage=STAGE_LOCATION;
                using=1;
                checkUserInfo();
                if(!info_checked){
                    stage=-1;
                    return;
                }
                local_address();
            }
        });

        v.findViewById(R.id.edit_tingyong_button).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                stage=STAGE_LOCATION;
                using=0;
                checkUserInfo();
                if(!info_checked){
                    stage=-1;
                    return;
                }
                local_address();
            }
        });
    }
    void init_edit_text(){
        if(ac.getUser()==null ){
            return;
        }
        ((EditText) (v.findViewById(R.id.edit_input_xuexiao))).setText(MyUtils.nullStr(ac.getUser().getCompany_name()));
        ((EditText) (v.findViewById(R.id.edit_input_banji))).setText(MyUtils.nullStr(ac.getUser().getDepartment_name()));
        ((EditText) (v.findViewById(R.id.edit_input_week_from))).setText(MyUtils.nullStr(ac.getUser().getWeek_from()+""));
        ((EditText) (v.findViewById(R.id.edit_input_week_to))).setText(MyUtils.nullStr(ac.getUser().getWeek_to()+""));
        ((EditText) (v.findViewById(R.id.edit_input_morning_h))).setText(MyUtils.nullStr(ac.getUser().getMorning_h()+""));
        ((EditText) (v.findViewById(R.id.edit_input_morning_m))).setText(MyUtils.nullStr(ac.getUser().getMorning_m()+""));
        ((EditText) (v.findViewById(R.id.edit_input_evening_h))).setText(MyUtils.nullStr(ac.getUser().getEvening_h()+""));
        ((EditText) (v.findViewById(R.id.edit_input_evening_m))).setText(MyUtils.nullStr(ac.getUser().getEvening_m()+""));
        if(ac.getUser().getUse_status()==0){
            v.findViewById(R.id.edit_tingyong_button).setVisibility(View.GONE);
            ((TextView)v.findViewById(R.id.edit_xiugai_button)).setText("启用");
        }else{
            v.findViewById(R.id.edit_tingyong_button).setVisibility(View.VISIBLE);
            ((TextView)v.findViewById(R.id.edit_xiugai_button)).setText("修改");
        }

    }
    void post_user(){
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
        postJson=gson.toJson(userBean,ManagerUserBean.class);
        outTime=false;
        new Thread(thread_post).start();
    }
    void init_handler(){
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                Log.i("STAGE_STATUS",stage+"");
                if(stage==STAGE_LOCATION) {
                    dialog.dismiss();
                    if (!bdl.getPos().isReceived()) {
                        alert_string("定位失败，请检查定位是否打开");
                        stage=-1;
                    } else {
                        msg_string("定位成功");
                        userBean.setSign_addrees(bdl.getPos().getAddress());
                        userBean.setSign_latitude(bdl.getPos().getLatitude());
                        userBean.setSign_longtitude(bdl.getPos().getLongtitude());
                        stage=STAGE_POST;
                        post_user();
                    }
                }else if(stage==STAGE_POST) {
                    stage = -1;
                    dialog.dismiss();
                    if (outTime) {
                        alert_string("服务器连接超时！请稍后重试");
                        return;
                    }
                    Gson gson = new GsonBuilder()
                            .setDateFormat("yyyy-MM-dd HH:mm:ss")
                            .create();
                    ManagerUserBean lb = gson.fromJson(response, ManagerUserBean.class);
                    if (lb == null || !(lb.isChecked())) {
                        String str = "修改失败！";
                        if (lb != null) {
                            str += lb.getErr_str();
                        }
                        alert_string(str);
                    } else {
                        ac.setUser(lb);
                        msg_string("修改成功!");
                        TextView signinfo = ac.getsView().findViewById(R.id.sign_info_text);
                        signinfo.setText(lb.getCompany_name() + lb.getDepartment_name() + "\n" + lb.getDepartment_address());
                        ac.setContentView(ac.getsView());
                        String key[] = new String[]{MyProperty.USER_SHARE_KEY, MyProperty.PASSWORD_SHARE_KEY};
                        String value[] = new String[]{lb.getUser(), lb.getPassword()};
                        MyUtils.saveShareInfo(ac, key, value, MyProperty.SHARE_NAME);
                    }
                }
            }
        };
    }

    void init_runnable_for_location(){
        thread=new Runnable() {
            @Override
            public void run() {

                if(!bdl.getPos().isReceived()) {
                    count++;
                    if (count > 10) {
                        handler.sendMessage(handler.obtainMessage());
                    }else{
                        handler.postDelayed(thread,300);
                    }
                }else{
                    handler.sendMessage(handler.obtainMessage());
                }
            }
        };

    }

    void init_runnable_for_post(){
        thread_post=new Runnable() {
            @Override
            public void run() {
                Gson gson=new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                postJson=gson.toJson(userBean,ManagerUserBean.class);
                try {
                    outTime=false;
                    response = doPost(postJson, "http://" + MyProperty.ip + ":" + MyProperty.port + "/" + MyProperty.service + "/" + serv);
                }catch (Exception e){
                    e.printStackTrace();
                }
                handler.sendMessage(handler.obtainMessage());
            }
        };
    }

    void checkUserInfo(){
        info_checked=false;

        String xuexiao=((EditText) (v.findViewById(R.id.edit_input_xuexiao))).getText().toString().trim();
        String banji=((EditText) (v.findViewById(R.id.edit_input_banji))).getText().toString().trim();
        String week_from=((EditText) (v.findViewById(R.id.edit_input_week_from))).getText().toString().trim();
        String week_to=((EditText) (v.findViewById(R.id.edit_input_week_to))).getText().toString().trim();
        String morning_h=((EditText) (v.findViewById(R.id.edit_input_morning_h))).getText().toString().trim();
        String morning_m=((EditText) (v.findViewById(R.id.edit_input_morning_m))).getText().toString().trim();
        String evening_h=((EditText) (v.findViewById(R.id.edit_input_evening_h))).getText().toString().trim();
        String evening_m=((EditText) (v.findViewById(R.id.edit_input_evening_m))).getText().toString().trim();

        if(xuexiao==null ||xuexiao.trim().length()==0){
            alert_string("公司不能为空！");
            return;
        }

        if(banji==null ||banji.trim().length()==0){
            alert_string("部门不能为空！");
            return;
        }

        if(week_from==null ||week_from.trim().length()==0||week_to==null ||week_to.trim().length()==0){
            alert_string("签到周期不能为空！");
            return;
        }
        if(morning_h==null ||morning_h.trim().length()==0||morning_m==null ||morning_m.trim().length()==0){
            alert_string("签到时间不能为空！");
            return;
        }
        if(evening_h==null ||evening_h.trim().length()==0||evening_m==null ||evening_m.trim().length()==0){
            alert_string("签退时间不能为空！");
            return;
        }
        if(!MyUtils.checkNumber(week_from,1,7) ||!MyUtils.checkNumber(week_to,1,7)){
            alert_string("签到周期只能为1-7！");
            return;
        }
        int week_from_int=Integer.parseInt(week_from);
        int week_to_int=Integer.parseInt(week_to);
        if(week_to_int< week_from_int){
            alert_string("签到周期有误");
            return;
        }
        if(!MyUtils.checkNumber(morning_h,0,23) ||!MyUtils.checkNumber(morning_m,0,59)){
            alert_string("签到时间有误,0-23,0-59");
            return;
        }
        if(!MyUtils.checkNumber(evening_h,0,23) ||!MyUtils.checkNumber(evening_m,0,59)){
            alert_string("签退时间有误,0-23,0-59");
            return;
        }

        int morning_h_int=Integer.parseInt(morning_h);
        int morning_m_int=Integer.parseInt(morning_m);
        int evening_h_int=Integer.parseInt(evening_h);
        int evening_m_int=Integer.parseInt(evening_m);

        if(evening_h_int <morning_h_int){
            alert_string("签退时间应晚于签到时间");
            return;
        }else if(evening_h==morning_h && evening_m_int <morning_m_int){
            alert_string("签退时间应晚于签到时间");
            return;
        }

        userBean=ac.getUser();

        userBean.setDepartment_name(banji);
        userBean.setCompany_name(xuexiao);
        userBean.setEvening_h(evening_h_int);
        userBean.setEvening_m(evening_m_int);
        userBean.setWeek_from(week_from_int);
        userBean.setWeek_to(week_to_int);
        userBean.setMorning_h(morning_h_int);
        userBean.setMorning_m(morning_m_int);
        userBean.setUse_status(using);

        info_checked=true;
    }

    void local_address(){
        bdl=new BDLocation(ac);
        bdl.checkPos(ac);
        count=0;
        dialog = new ProgressDialog(ac);
        dialog.show();
        stage=STAGE_LOCATION;
        handler.post(thread);
    }
}
