package com.zm.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;


import com.zm.R;
import com.zm.bean.ManagerUserBean;
import com.zm.utils.MyProperty;
import com.zm.view.LoginViewInit;
import com.zm.view.MainInit;
import com.zm.view.RegisterViewInit;
import com.zm.view.ShouyeViewInit;
import com.zm.view.ToastViewInit;
import com.zm.view.ToastViewSuccessInit;
import java.util.ArrayList;

public class ShouyeActivity extends Activity {

    View mainView;
    View sView;
    View loginView;
    View registerView;
    View toastView;
    View toastViewSuccess;
    View signListView;
    View reviewListView;
    View editAddressView;
    View reviewView;
    static Handler handler;
    static Runnable handRunnable;
    static Activity ac;

    String postJson;
    boolean outtime;
    Dialog dialog;

    ManagerUserBean user;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        ac=this;
        propertySize();
        ac.setContentView(R.layout.activity_main);
        check_permissions();
        this.mainView=View.inflate(this,R.layout.activity_main,null);
        this.loginView=View.inflate(this,R.layout.login,null);
        this.registerView=View.inflate(this,R.layout.register,null);
        this.sView=View.inflate(this,R.layout.shouye_layout,null);
        toastView=View.inflate(this,R.layout.mytoast,null);
        toastViewSuccess=View.inflate(this,R.layout.mytoast_success,null);
        signListView=View.inflate(this,R.layout.sign_list,null);
        reviewListView=View.inflate(this,R.layout.review_list,null);
        editAddressView=View.inflate(this,R.layout.edit_address,null);
        reviewView=View.inflate(this,R.layout.review,null);
        new ShouyeViewInit().init(this,sView);
        new LoginViewInit().init(this,loginView);
        new ToastViewInit().init(this,toastView);
        new ToastViewSuccessInit().init(this,toastViewSuccess);
        new RegisterViewInit().init(this,registerView);
        new MainInit().init(this,mainView);

    }

    void check_permissions(){
        String[] perms=new String[]{Manifest.permission.INTERNET,Manifest.permission.ACCESS_FINE_LOCATION};
        ArrayList<String> need_perms=new ArrayList<String>();
        for(int i=0;i<perms.length;i++) {
            if (ContextCompat.checkSelfPermission(this,perms[i])
                    != PackageManager.PERMISSION_GRANTED) {
                need_perms.add(perms[i]);
            }
        }
        if(need_perms.size()>0){
            String[] need_perms_arr=new String[need_perms.size()];
            for(int i=0;i<need_perms.size();i++){
                need_perms_arr[i]=need_perms.get(i);
            }
            ActivityCompat.requestPermissions(this,
                    need_perms_arr,123);
        }
    }
    private void propertySize(){
        DisplayMetrics dm=new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;
        int height=dm.heightPixels;
        int statbarheight=0;
        MyProperty.width=width;
        MyProperty.height=height;
        MyProperty.statbarheight=statbarheight;
        Log.i("width",width+"");
        float scaleX=(float)width/360;
        float scaleY=(float)(height-statbarheight)/(640-25);
        MyProperty.scale=scaleX<scaleY?scaleX:scaleY;
        MyProperty.scaleX=scaleX;
        MyProperty.scaleY=scaleY;
        Log.i("scale",MyProperty.scale+"\t"+scaleX+"\t"+scaleY+"\t"+height+"\t"+statbarheight);
    }


    public View getsView() {
        return sView;
    }

    public void setsView(View sView) {
        this.sView = sView;
    }

    public View getLoginView() {
        return loginView;
    }

    public void setLoginView(View loginView) {
        this.loginView = loginView;
    }

    public View getRegisterView() {
        return registerView;
    }

    public void setRegisterView(View registerView) {
        this.registerView = registerView;
    }

    public View getToastView() {
        return toastView;
    }

    public void setToastView(View toastView) {
        this.toastView = toastView;
    }

    public View getToastViewSuccess() {
        return toastViewSuccess;
    }

    public void setToastViewSuccess(View toastViewSuccess) {
        this.toastViewSuccess = toastViewSuccess;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults!=null && grantResults.length!=0){
            for(int i=0;i<grantResults.length;i++){
                if(grantResults[i]!= PackageManager.PERMISSION_GRANTED){
                    finish();
                }
            }
        }
    }

    public View getSignListView() {
        return signListView;
    }

    public void setSignListView(View signListView) {
        this.signListView = signListView;
    }

    public ManagerUserBean getUser() {
        return user;
    }

    public void setUser(ManagerUserBean user) {
        this.user = user;
    }

    public View getEditAddressView() {
        return editAddressView;
    }

    public void setEditAddressView(View editAddressView) {
        this.editAddressView = editAddressView;
    }

    public View getReviewView() {
        return reviewView;
    }

    public View getReviewListView() {
        return reviewListView;
    }
}
