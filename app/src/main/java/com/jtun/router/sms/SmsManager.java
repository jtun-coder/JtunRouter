package com.jtun.router.sms;

import android.app.Activity;
import android.app.role.RoleManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.Telephony;
import android.provider.Telephony.Sms;

import com.jtun.router.util.KLog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SmsManager {
    public static void sendSms(String address,String body){
        android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
        List<String> messages = smsManager.divideMessage(body);
        for (String message : messages){
            smsManager.sendTextMessage(address,null,message,null,null);
        }
    }
    public static void defaultSmsPackage(Activity context){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            RoleManager roleManager = context.getSystemService(RoleManager.class);
            Intent intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_SMS);
            context.startActivityForResult(intent,42389);
        }else{
            String packageName = context.getPackageName();
            if(!Telephony.Sms.getDefaultSmsPackage(context).equals(packageName)){
                Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, packageName);
                context.startActivity(intent);
            }
        }

    }
    /**
     * 获取短信会话，获取最新一条短信
     * @param context 上下文
     * @return 短信会话
     */
    public static Collection<SMSConversation> getLatestSmsByContact(Context context) {
        Map<String, SMSConversation> latestSmsByContact = new HashMap<>();
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = Uri.parse("content://sms");
        try (Cursor cursor = contentResolver.query(uri, null, null, null, Sms.DATE + " DESC")) {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String address = cursor.getString(cursor.getColumnIndexOrThrow(Sms.ADDRESS));
                    String body = cursor.getString(cursor.getColumnIndexOrThrow(Sms.BODY));
                    String person = cursor.getString(cursor.getColumnIndexOrThrow(Sms.PERSON));
                    String data = cursor.getString(cursor.getColumnIndexOrThrow(Sms.DATE));
                    int type = cursor.getInt(cursor.getColumnIndexOrThrow(Sms.TYPE));
                    int threadId = cursor.getInt(cursor.getColumnIndexOrThrow(Sms.THREAD_ID));
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(Sms._ID));
                    int read = cursor.getInt(cursor.getColumnIndexOrThrow(Sms.READ));
                    SMSConversation smsConversation = latestSmsByContact.get(address);
                    if(smsConversation == null){
                        com.jtun.router.sms.Sms sms = new com.jtun.router.sms.Sms(id,address,person,body,data,type,read);
                        latestSmsByContact.put(address, new SMSConversation(address,sms,threadId));
                    }
                } while (cursor.moveToNext());
            }
        }

        return latestSmsByContact.values();
    }

    /**
     * 根据号码获取短信列表
     * @param context 上下文
     * @param phoneNum 手机号码
     * @return 短信列表
     */
    public static List<com.jtun.router.sms.Sms> getAddressSms(Context context, String phoneNum){
        List<com.jtun.router.sms.Sms> listSms = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = Uri.parse("content://sms");
        try (Cursor cursor = contentResolver.query(uri, null, "address = " + phoneNum, null, Sms.DATE + " DESC")) {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String address = cursor.getString(cursor.getColumnIndexOrThrow(Sms.ADDRESS));
                    String body = cursor.getString(cursor.getColumnIndexOrThrow(Sms.BODY));
                    String person = cursor.getString(cursor.getColumnIndexOrThrow(Sms.PERSON));
                    String data = cursor.getString(cursor.getColumnIndexOrThrow(Sms.DATE));
                    int type = cursor.getInt(cursor.getColumnIndexOrThrow(Sms.TYPE));
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(Sms._ID));
                    int read = cursor.getInt(cursor.getColumnIndexOrThrow(Sms.READ));
                    com.jtun.router.sms.Sms sms = new com.jtun.router.sms.Sms(id,address,person,body,data,type,read);
                    listSms.add(sms);
                } while (cursor.moveToNext());
            }
        }
        return listSms;
    }
    public static int markSmsAsRead(Context context, int threadId) {
        ContentResolver contentResolver = context.getContentResolver();
        // 构建更新条件
        String where = Sms.THREAD_ID + " = ?";
        String[] whereArgs = new String[]{String.valueOf(threadId)};
        // 构建更新值
        ContentValues values = new ContentValues();
        values.put(Sms.READ, 1); // 1 表示已读
        // 执行更新操作
        int rowsUpdated = contentResolver.update(Uri.parse("content://sms"), values, where, whereArgs);
        KLog.i("SmsManager","markSmsAsRead rowsUpdated : " + rowsUpdated);
        // rowsUpdated 将包含被更新的行数，你可以根据需要处理这个值
        // 例如，如果 rowsUpdated == 0，则表示没有找到匹配的短信来更新
        return rowsUpdated;
    }
    public static int deleteSmsList(Context context,List<Integer> smsIdList){
        ContentResolver contentResolver = context.getContentResolver();
        int sum = 0;
        // 构建更新条件
        String where = Sms._ID + " = ?";
        for (int smsId : smsIdList){
            String[] whereArgs = new String[]{String.valueOf(smsId)};
            int deleteResult = contentResolver.delete(Uri.parse("content://sms/"), where,whereArgs);
            KLog.i("SmsManager","deleteSmsList deleteResult : " + deleteResult);
            if(deleteResult > 0){
                sum ++;
            }
        }
        return sum;
    }
    public static int deleteSms(Context context,int smsId){
        ContentResolver contentResolver = context.getContentResolver();
        // 构建更新条件
        String where = Sms._ID + " = ?";
        String[] whereArgs = new String[]{String.valueOf(smsId)};
        int deleteResult = contentResolver.delete(Uri.parse("content://sms/"), where,whereArgs);
        KLog.i("SmsManager","deleteSms deleteResult : " + deleteResult);
        return deleteResult;
    }
    public static int deleteSmsByThreadId(Context context,int threadId){
        ContentResolver contentResolver = context.getContentResolver();
        // 构建更新条件
        String where = Sms.THREAD_ID + " = ?";
        String[] whereArgs = new String[]{String.valueOf(threadId)};
        int deleteResult = contentResolver.delete(Uri.parse("content://sms/"), where,whereArgs);
        KLog.i("SmsManager","deleteSms deleteResult : " + deleteResult);
        return deleteResult;
    }
}