package pms.testing;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.Calendar;

/**
 * Created by ricar on 05/01/2017.
 */

public class BootReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent){
        int id, h, m;
        boolean switch_state;

        SharedPreferences prefs = context.getSharedPreferences("prefs",0);

        Intent alarmIntent = new Intent(context,AlarmReceiver.class);
        AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        id = prefs.getInt("alarm_id_one",0);
        h = prefs.getInt("hour_one",0);
        m = prefs.getInt("minute_one",0);

        switch_state = prefs.getBoolean("switch_one_state",true);

        if(id!=0 && switch_state) {
            PendingIntent pintent = PendingIntent.getBroadcast(context, id, alarmIntent, 0);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());

            calendar.set(Calendar.HOUR_OF_DAY, h);
            calendar.set(Calendar.MINUTE, m);

            manager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pintent);
        }

        id = prefs.getInt("alarm_id_two",0);
        h = prefs.getInt("hour_two",0);
        m = prefs.getInt("minute_two",0);

        switch_state = prefs.getBoolean("switch_two_state",true);

        if(id!=0 && switch_state) {
            PendingIntent pintent = PendingIntent.getBroadcast(context, id, alarmIntent, 0);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());

            calendar.set(Calendar.HOUR_OF_DAY, h);
            calendar.set(Calendar.MINUTE, m);

            manager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pintent);
        }

    }
}
