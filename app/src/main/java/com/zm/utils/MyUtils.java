package com.zm.utils;

import android.app.Activity;
import android.content.SharedPreferences;

import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;


public class MyUtils {
    public static void saveShareInfo(Activity ac,String[] keys,String[] values,String name) {
        SharedPreferences sharedPreferences = ac.getSharedPreferences(name, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for(int i=0;i<keys.length;i++){
            editor.putString(keys[i],values[i]);
        }
        editor.commit();
    }
    public static void getShareInfo(Activity ac,String[] keys,String[] values,String name){
        SharedPreferences sharedPreferences = ac.getSharedPreferences(name,MODE_PRIVATE);
        for(int i=0;i<keys.length;i++){
            values[i]=sharedPreferences.getString(keys[i],"");
        }
    }
    public static String nullStr(String str){
        if(str==null){
            return "";
        }else{
            return str;
        }
    }
    public static boolean checkNumber(String val,int from,int to){
        Pattern pattern=Pattern.compile("[^\\d]");
        if(pattern.matcher(val).find()){
            return false;
        }
        int v=Integer.parseInt(val);
        if(v<from||v>to){
            return false;
        }
        return true;
    }
}
