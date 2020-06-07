package com.example.absenku;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    public static final String SP_ABSENKU = "spAbsenku";

    public static final String SP_ID = "spID";
    public static final String SP_UserType = "spUserType";
    public static final String SP_IDNODOT = "spIDNODOT";
    public static final String SP_NAMA = "spNAMA";

    public static final String SP_SUDAH_LOGIN = "spSudahLogin";

    SharedPreferences sp;
    SharedPreferences.Editor spEditor;

    public SharedPrefManager(Context context){
        sp = context.getSharedPreferences(SP_ABSENKU, Context.MODE_PRIVATE);
        spEditor = sp.edit();
    }

    public void saveSPString(String keySP, String value){
        spEditor.putString(keySP, value);
        spEditor.commit();
    }

    public void saveSPInt(String keySP, int value){
        spEditor.putInt(keySP, value);
        spEditor.commit();
    }

    public void saveSPBoolean(String keySP, boolean value){
        spEditor.putBoolean(keySP, value);
        spEditor.commit();
    }

    public String getSPID(){
        return sp.getString(SP_ID, "");
    }

    public String getSPUserType(){
        return sp.getString(SP_UserType, "");
    }

    public Boolean getSPSudahLogin(){
        return sp.getBoolean(SP_SUDAH_LOGIN, false);
    }

    public String getSPIDNODOT(){
        return sp.getString(SP_IDNODOT,"");
    }

    public String getSPNama(){
        return  sp.getString(SP_NAMA,"");
    }
}
