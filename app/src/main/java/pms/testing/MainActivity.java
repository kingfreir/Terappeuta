package pms.testing;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.GregorianCalendar;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView text_one, text_two;
    private Switch switch_one, switch_two;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text_one = (TextView)findViewById(R.id.hour_one);
        text_two = (TextView)findViewById(R.id.hour_two);

        final SharedPreferences preferences = getSharedPreferences("prefs",0);

        switch_one = (Switch) findViewById(R.id.switch_one);
        switch_one.setChecked(preferences.getBoolean("switch_one_state",true));
        switch_one.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                AlarmManager manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(getApplicationContext(),AlarmReceiver.class);

                preferences.edit().putBoolean("switch_one_state",b).commit();

                int id = preferences.getInt("alarm_id_one",0);
                System.out.println("one "+id);

                if(b){
                    //alarm on
                    int hourOfDay = preferences.getInt("hour_one",0);
                    int minute = preferences.getInt("minute_one",0);

                    if(id!=0) {
                        intent.putExtra("id",id);
                        intent.putExtra("isMorningAlarm",true);
                        PendingIntent p_intent = PendingIntent.getBroadcast(getApplicationContext(), id, intent, 0);

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(System.currentTimeMillis());

                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);

                        if(calendar.getTimeInMillis()<System.currentTimeMillis()){
                            calendar.add(Calendar.DATE,1);
                        }

                        System.out.println("Alarm set for: "+calendar.getTime());
                        manager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), p_intent);

                        Toast.makeText(MainActivity.this,"Morning alarm was set!",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(MainActivity.this, "No morning alarm was set!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    //alarm off
                    PendingIntent cancel_intent = PendingIntent.getBroadcast(getApplicationContext(),id,intent,0);
                    manager.cancel(cancel_intent);
                    Toast.makeText(MainActivity.this,"Morning alarm canceled.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        switch_two = (Switch) findViewById(R.id.switch_two);
        switch_two.setChecked(preferences.getBoolean("switch_two_state",true));
        switch_two.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                AlarmManager manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(getApplicationContext(),AlarmReceiver.class);

                preferences.edit().putBoolean("switch_two_state",b).commit();

                int id = preferences.getInt("alarm_id_two",0);
                System.out.println("two "+id);

                if(b){
                    //alarm on
                    int hourOfDay = preferences.getInt("hour_two",0);
                    int minute = preferences.getInt("minute_two",0);

                    if(id!=0) {
                        intent.putExtra("id",id);
                        intent.putExtra("isMorningAlarm",false);
                        PendingIntent p_intent = PendingIntent.getBroadcast(getApplicationContext(), id, intent, 0);

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(System.currentTimeMillis());

                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);

                        if(calendar.getTimeInMillis()<System.currentTimeMillis()){
                            calendar.add(Calendar.DATE,1);
                        }

                        System.out.println("Alarm set for: "+calendar.getTime());
                        manager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), p_intent);

                        Toast.makeText(MainActivity.this,"Evening alarm was set!",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(MainActivity.this, "No evening alarm was set!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    //alarm off
                    PendingIntent cancel_intent = PendingIntent.getBroadcast(getApplicationContext(),id,intent,0);
                    manager.cancel(cancel_intent);
                    Toast.makeText(MainActivity.this,"Evening alarm canceled.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        ImageView image = (ImageView)findViewById(R.id.image_view);

        checkAlarms();

        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(image);
        Glide
                .with(getApplicationContext())
                .load(R.raw.breathe_azulverde)
                .into(imageViewTarget);
    }

    public void showNotifications(View view){
        Intent intent = new Intent(this,PopupActivity.class);
        startActivity(intent);
    }
    public void showTimePicker(View view){
        DialogFragment dialog = new TimePickerFragment();
        Boolean isHourOne = view.getId()==R.id.hour_one;
        Boolean switchState = (isHourOne)?switch_one.isChecked():switch_two.isChecked();

        Bundle bundle = new Bundle();
        bundle.putBoolean("isHourOne",isHourOne);
        bundle.putBoolean("switchState",switchState);

        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(),"timePicker");
    }

    public void checkAlarms(){
        int h, m;
        String text;

        h = getSharedPreferences("prefs",0).getInt("hour_one",-1);
        m = getSharedPreferences("prefs",0).getInt("minute_one",-1);

        text=(h!=-1)?String.format(Locale.getDefault(),"%02d:%02d",h,m):"--:--";
        text_one.setText(text);

        h = getSharedPreferences("prefs",0).getInt("hour_two",-1);
        m = getSharedPreferences("prefs",0).getInt("minute_two",-1);

        text=(h!=-1)?String.format(Locale.getDefault(),"%02d:%02d",h,m):"--:--";
        text_two.setText(text);

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

            int chk_id;
            final int id;

            if((Boolean)getArguments().get("isHourOne")) {
                chk_id = preferences.getInt("alarm_id_one", 0);

                id = (int) System.currentTimeMillis();

                System.out.println("one set"+id);

                edt.putInt("alarm_id_one",id);
                edt.putInt("hour_one",hourOfDay);
                edt.putInt("minute_one",minute);
                edt.commit();

                TextView text = (TextView)getActivity().findViewById(R.id.hour_one);
                text.setText(String.format(Locale.getDefault(),"%02d:%02d",hourOfDay,minute));

                intent.putExtra("isMorningAlarm",true);
            }else{
                chk_id = preferences.getInt("alarm_id_two", 0);

                id = (int) System.currentTimeMillis();

                System.out.println("two set "+id);

                edt.putInt("alarm_id_two",id);
                edt.putInt("hour_two",hourOfDay);
                edt.putInt("minute_two",minute);
                edt.commit();

                TextView text = (TextView)getActivity().findViewById(R.id.hour_two);
                text.setText(String.format(Locale.getDefault(),"%02d:%02d",hourOfDay,minute));

                intent.putExtra("isMorningAlarm",false);
            }
            if(chk_id!=0){
                PendingIntent cancel_intent = PendingIntent.getBroadcast(context,chk_id,intent,0);
                manager.cancel(cancel_intent);
            }

            if((Boolean)getArguments().get("switchState")) {
                intent.putExtra("id",id);
                PendingIntent p_intent = PendingIntent.getBroadcast(context, id, intent, 0);

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());

                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);

                if(calendar.getTimeInMillis()<System.currentTimeMillis()){
                    calendar.add(Calendar.DATE,1);
                }

                System.out.println("Alarm set for: "+calendar.getTime());
                manager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), p_intent);

                if((Boolean)getArguments().get("isHourOne")) {
                    Toast.makeText(context, "Morning alarm was set!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Evening alarm was set!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
