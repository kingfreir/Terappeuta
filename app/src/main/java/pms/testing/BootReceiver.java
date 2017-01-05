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
        SharedPreferences prefs = context.getSharedPreferences("prefs",0);

        Intent alarmIntent = new Intent(context,AlarmReceiver.class);
        AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        int id = prefs.getInt("alarm_id",0);
        int h = prefs.getInt("hour",0);
        int m = prefs.getInt("min",0);

        PendingIntent pintent = PendingIntent.getBroadcast(context,id,alarmIntent,0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        calendar.set(Calendar.HOUR_OF_DAY,h);
        calendar.set(Calendar.MINUTE,m);

        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,pintent);
    }
}
