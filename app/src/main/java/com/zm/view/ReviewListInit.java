package com.zm.view;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zm.R;
import com.zm.bean.RegisterItemBean;
import com.zm.utils.MyProperty;

import java.util.ArrayList;
import java.util.List;

public class ReviewListInit extends ViewInit {

    Runnable runnable;
    Handler handler;
    Bitmap bitmap;
    public RegisterItemBean item_select;
    String serv="DownloadImageService";
    Dialog dialog;
    String postJson;
    List<Button> buttons;
    @Override
    public void init_buttons(){
        v.findViewById(R.id.review_list_button_fanhui).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Log.i("ADFASDFASDF","hre");
                ac.setContentView(ac.getsView());
            }
        }
        );
        init_list();
        init_runnable();
    }
    void init_runnable(){
        runnable=new Runnable() {
            @Override
            public void run() {
                Gson gson=new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                postJson=gson.toJson(item_select,RegisterItemBean.class);
                bitmap=downLoadImage("http://"+MyProperty.ip + ":" + MyProperty.port + "/" +MyProperty.service+"/" +serv,postJson);
                Message msg=handler.obtainMessage();
                handler.sendMessage(msg);
            }
        };
        handler=new Handler() {
            @Override
            public void handleMessage(Message message) {
                super.handleMessage(message);
                dialog.dismiss();
                if (outTime) {
                    Log.i("chaoshi", "chaoshi");
                    alert_string("服务器连接超时！请稍后重试");
                    return;
                }else{
                    ReviewViewInit rvi=new ReviewViewInit();
                    rvi.init(ac,ac.getReviewView());
                    rvi.init_image(bitmap,item_select);
                    ac.setContentView(ac.getReviewView());
                }

                dialog.dismiss();
            }
        };
    }

    public void init_list(){
        LinearLayout reviewListView=(LinearLayout)((v.findViewById(R.id.review_list)));
        reviewListView.removeAllViews();

        if(ac.getUser()==null|| ac.getUser().getRegisterList()==null || ac.getUser().getRegisterList().size()==0){
            return;
        }
        List<RegisterItemBean> reviewList=ac.getUser().getRegisterList();
        int num=reviewList.size();
        LinearLayout.LayoutParams lp_list = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,100*num);
        reviewListView.setLayoutParams(lp_list);
        if(num>=10){
            LinearLayout scrolllinear=(LinearLayout)((v.findViewById(R.id.review_list_scrolllinear)));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,100*(num+1));
            scrolllinear.setLayoutParams(lp);
            ((v.findViewById(R.id.review_list_tianchong))).setVisibility(View.GONE);
        }else{
            LinearLayout tianchong=(v.findViewById(R.id.review_list_tianchong));
            tianchong.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams lp_tianchong = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,100*(10-num));
            tianchong.setLayoutParams(lp_tianchong);
        }
        buttons=new ArrayList<Button>();
        for(int i=0;i<num;i++){
            final RegisterItemBean item=reviewList.get(i);
            View oneSign=View.inflate(ac,R.layout.one_review_item,null);
            LinearLayout.LayoutParams lp_item = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,100);
            oneSign.setLayoutParams(lp_item);
            int newid=MyProperty.generateViewId();
            oneSign.setId(newid);
            Log.i("SIGNLISTID1",""+oneSign.getId());
            TextView timev=oneSign.findViewById(R.id.review_list_time);
            TextView addressv=oneSign.findViewById(R.id.review_list_dizhi);
            Button statusv=oneSign.findViewById(R.id.review_list_status);
            TextView idv=oneSign.findViewById(R.id.review_list_id);
            buttons.add(statusv);
            timev.setText(item.getUser());
            addressv.setText(item.getName());
            idv.setText(i+"");
            statusv.setOnClickListener(new Button.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        for(int j=0;j<buttons.size();j++){
                            if(buttons.get(j)==v){
                                Log.i("SIGNLISTID",""+j);
                                item_select=ac.getUser().getRegisterList().get(j);
                                new Thread(runnable).start();
                                dialog=new ProgressDialog(ac);
                                dialog.show();
                            }
                        }
                    }
            });
            reviewListView.addView(oneSign);
        }
        reviewListView.setVisibility(View.VISIBLE);
    }
}
