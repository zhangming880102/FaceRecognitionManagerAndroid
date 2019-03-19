package com.zm.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zm.R;
import com.zm.activity.ShouyeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ViewInit{
    ShouyeActivity ac;
    View v;
    Toast toast;
    boolean outTime;
    static int REQUEST_IMAGE_CAPTURE=1;
    Uri imageUri;
    boolean isProcess=false;
    SurfaceView cameraView;

    public ViewInit(){

    }
    public void init(ShouyeActivity ac, View v){
        this.ac=ac;
        this.v=v;
        toast=new Toast(ac);
        init_buttons();
        init_sizes();
    }
    public void init_sizes(){

    }
    public void init_buttons(){

    }

    public void alert_string(String str){
        TextView toastlable2=(ac.getToastView()).findViewById(R.id.my_toast_label_2);
        toastlable2.setText(str);
        toast.setGravity(Gravity.BOTTOM,0,0);
        toast.setView(ac.getToastView());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    public void msg_string(String str){
        TextView toastlable2=(ac.getToastViewSuccess()).findViewById(R.id.my_toast_suc_label_2);
        toastlable2.setText(str);
        toast.setGravity(Gravity.BOTTOM,0,0);
        toast.setView(ac.getToastViewSuccess());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    public String doPost(String jsonString, String urlpath)
            throws Exception {
        outTime=false;
        String result = null;
        try {
            URL url = new URL(urlpath);
            HttpURLConnection connection = (HttpURLConnection) (url.openConnection());
            byte[] data = jsonString.getBytes();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-javascript;charset=UTF-8");
            connection.setRequestProperty("Content-Length", String.valueOf(data.length));
            connection.setConnectTimeout(5 * 1000);
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(data);
            outputStream.flush();
            outputStream.close();

            if (connection.getResponseCode() == 200) {
                InputStream in = connection.getInputStream();
                BufferedReader bw = new BufferedReader(new InputStreamReader(in, "utf-8"));
                String re = "";
                String line = "";
                while ((line = bw.readLine()) != null) {
                    re += line;
                }
                Log.i("re", re);
                return re;
            /*
            String sb = "";
            byte[] buffer = new byte[1024];
            int n;
            while ((n=in.read(buffer, 0, 1024)) != -1) {
                Log.i("response","n"+n);

                Log.i("i",""+(char)buffer[11]);
                Log.i("sb",new String(buffer,0,n));
            }
            in.close();
            Log.i("response", sb.toString() + "");
            */

            } else {
                outTime = true;
            }
        }catch (Exception e){
            outTime=true;
            //e.printStackTrace();
        }
        return "";
    }

    public Bitmap downLoadImage(String urlpath, String jsonString){
        Bitmap img = null;
        outTime=false;
        String result = null;
        try {
            URL url = new URL(urlpath);
            HttpURLConnection connection = (HttpURLConnection) (url.openConnection());
            byte[] data = jsonString.getBytes();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-javascript;charset=UTF-8");
            connection.setRequestProperty("Content-Length", String.valueOf(data.length));
            connection.setConnectTimeout(5 * 1000);
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(data);
            outputStream.flush();
            outputStream.close();

            if (connection.getResponseCode() == 200) {
                InputStream is = connection.getInputStream();
                img = BitmapFactory.decodeStream(is);
                is.close();
            }
        } catch (IOException e) {
            outTime=true;
            e.printStackTrace();
        }
        if(img==null){
            outTime=true;
        }
        return img;
    }
}
