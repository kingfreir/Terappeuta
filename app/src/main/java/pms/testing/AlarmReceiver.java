package pms.testing;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.graphics.BitmapCompat;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.bumptech.glide.request.target.NotificationTarget;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String intro, message;

        intro = context.getResources().getString(R.string.intro_message);
        message = getMessages(context);

        System.out.println(intro+" "+message);

        Bitmap icon = BitmapFactory.decodeResource(context.getResources(),R.mipmap.logo_terappeuta);
        Bitmap image = BitmapFactory.decodeResource(context.getResources(),R.raw.space_rock);
        Bitmap resize = Bitmap.createScaledBitmap(image,120,120,false);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_announcement_black_24dp)
                        .setContentTitle("Terappeuta")
                        .setContentText(message)
                        .setStyle(new NotificationCompat.BigPictureStyle()
                            .bigPicture(resize)
                                .bigLargeIcon(icon)
                            .setSummaryText(message)
                            .setBigContentTitle(intro))
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        Intent result = new Intent(context,PopupActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(PopupActivity.class);
        stackBuilder.addNextIntent(result);

        PendingIntent pIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pIntent);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1,mBuilder.build());
    }

    private String getMessages(Context context){
        String[] array = context.getResources().getStringArray(R.array.messages);
        ArrayList<String> messages = new ArrayList<>();
        messages = (ArrayList) Arrays.asList(array);
        return messages.get(1);
    }
}
