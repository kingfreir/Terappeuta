package pms.testing;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.graphics.BitmapCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.NotificationTarget;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Array;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import pms.testing.bitmap.BitmapHelper;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean useBigPicture = true;
        String intro, message;
        int img_id;

        System.out.println("id: "+ intent.getExtras().get("id"));
        intro = context.getResources().getString(R.string.intro_message);
        message = getMessage(context);

        Bitmap icon = BitmapFactory.decodeResource(context.getResources(),R.mipmap.logo_terappeuta);

        img_id = getImage(context);
        Bitmap image_small = BitmapHelper.decodeSampledBitmapFromResource(context.getResources(),img_id,512,256);


        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_announcement_black_24dp)
                        .setContentTitle("Terappeuta")
                        .setContentText(intro)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setAutoCancel(true);

        if(useBigPicture) {
            mBuilder.setStyle(new NotificationCompat.BigPictureStyle()
                    .setBigContentTitle(intro)
                    .bigPicture(image_small)
                    .bigLargeIcon(icon)
                    .setSummaryText(message));
        }else {
            mBuilder.setStyle(new NotificationCompat.BigTextStyle()
                    .setBigContentTitle(intro)
                    .bigText(message));
        }

        Intent result = new Intent(context,PopupActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(PopupActivity.class);
        stackBuilder.addNextIntent(result);

        PendingIntent pIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pIntent);

        Notification notif = mBuilder.build();

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1,notif);

        String img_path = "ID"+img_id;

        storeNewEntry(context,message,img_path);

        SharedPreferences preferences = context.getSharedPreferences("prefs",0);

        AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent alarm_intent = new Intent(context,AlarmReceiver.class);

        if((Boolean)intent.getExtras().get("isMorningAlarm")){

            int id = preferences.getInt("alarm_id_one",0);

            int hourOfDay = preferences.getInt("hour_one",0);
            int minute = preferences.getInt("minute_one",0);

            intent.putExtra("id",id);
            intent.putExtra("isMorningAlarm",true);
            PendingIntent p_intent = PendingIntent.getBroadcast(context, id, alarm_intent, 0);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());

            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);

            calendar.add(Calendar.DATE,1);


            System.out.println("Alarm received and set for: "+calendar.getTime());
            manager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), p_intent);
        }else{
            int id = preferences.getInt("alarm_id_two",0);

            int hourOfDay = preferences.getInt("hour_two",0);
            int minute = preferences.getInt("minute_two",0);

            intent.putExtra("id",id);
            intent.putExtra("isMorningAlarm",false);
            PendingIntent p_intent = PendingIntent.getBroadcast(context, id, alarm_intent, 0);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());

            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);

            calendar.add(Calendar.DATE,1);

            System.out.println("Alarm received and set for: "+calendar.getTime());
            manager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), p_intent);
        }
    }

    private String getMessage(Context context){
        int last = context.getSharedPreferences("prefs",0).getInt("last_message",-1);
        String[] array = context.getResources().getStringArray(R.array.messages);
        int pos;
        do {
            pos = new Random().nextInt(array.length);
        }while(pos==last);
        context.getSharedPreferences("prefs",0).edit().putInt("last_message",pos).apply();
        return array[pos];
    }

    private int getImage(Context context){
        int size = R.raw.class.getFields().length-3;
        int last = context.getSharedPreferences("prefs",0).getInt("last_image",-1);
        ArrayList<Integer> id = new ArrayList<>();
        for(int i = 0;i<size;i++){
            id.add(context.getResources().getIdentifier("img"+i,"raw",context.getPackageName()));
        }
        int rand;
        do {
            rand = new Random().nextInt(size);
        }while(rand==last);
        context.getSharedPreferences("prefs",0).edit().putInt("last_image",rand).apply();
        return id.get(rand);
    }

    private void storeNewEntry(Context context, String message, String img_path){
        DbHelper helper = new DbHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NotifReaderContract.NotifEntry.MESSAGE,message);
        values.put(NotifReaderContract.NotifEntry.IMAGE_PATH,img_path);

        Calendar c = Calendar.getInstance();
        String date = String.format(Locale.getDefault(),"%1$tA %1$tb %1$td %1$tY at %1$tH:%1$tM", c);

        values.put(NotifReaderContract.NotifEntry.DATE,date);
        db.insert(NotifReaderContract.NotifEntry.TABLE_NAME,null,values);

        helper.close();
    }
}
