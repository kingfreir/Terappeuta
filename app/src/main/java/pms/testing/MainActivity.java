package pms.testing;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.GregorianCalendar;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private PendingIntent pendingIntent;
    private AlarmManager manager;
    private Calendar calendar;

    private TextView text;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = (TextView)findViewById(R.id.hour_text);
        image = (ImageView)findViewById(R.id.image_view);

        int h = getSharedPreferences("prefs",0).getInt("hour",-1);
        int m = getSharedPreferences("prefs",0).getInt("minute",-1);
        if(h!=-1){
            text.setText(h+":"+m);
        }else{
            text.setText("--:--");
        }
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(image);
        Glide
                .with(getApplicationContext())
                .load(R.raw.breathe_azulverde)
                .into(imageViewTarget);
    }

    public void cancelAlarm(View view){
        SharedPreferences preferences = getSharedPreferences("prefs",0);
        int id = preferences.getInt("alarm_id",0);

        AlarmManager manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        if(id!=0){
            Intent intent = new Intent(this,AlarmReceiver.class);
            PendingIntent cancelIntent  = PendingIntent.getBroadcast(this,id,intent,0);
            manager.cancel(cancelIntent);
            Toast.makeText(this,"Alarm canceled.",Toast.LENGTH_SHORT).show();
            text.setText("--:--");
            preferences.edit().putInt("alarm_id",0);
        }else{
            Toast.makeText(this,"No alarm set.",Toast.LENGTH_SHORT).show();
        }

    }

    public void startAlarm(View view){
        DialogFragment dialog = new TimePickerFragment();
        dialog.show(getSupportFragmentManager(),"timePicker");
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            Context context = view.getContext();

            SharedPreferences preferences = getActivity().getSharedPreferences("prefs",0);
            SharedPreferences.Editor edt = preferences.edit();

            Intent intent = new Intent(context,AlarmReceiver.class);

            AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

            int chk_id = preferences.getInt("alarm_id",0);
            if(chk_id!=0){
                PendingIntent cancel_intent = PendingIntent.getBroadcast(context,chk_id,intent,0);
                manager.cancel(cancel_intent);
            }

            //random id for multiple alarm intents
            final int id = (int) System.currentTimeMillis();
            edt.putInt("alarm_id",id);
            edt.putInt("hour",hourOfDay);
            edt.putInt("minute",minute);
            edt.commit();
            PendingIntent p_intent  = PendingIntent.getBroadcast(context,id,intent,0);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());

            calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
            calendar.set(Calendar.MINUTE,minute);

            //Repeat alarm every day
            manager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY,p_intent);

            TextView text = (TextView)getActivity().findViewById(R.id.hour_text);
            text.setText(hourOfDay+":"+minute);
        }
    }
}
