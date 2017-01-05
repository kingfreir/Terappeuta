package pms.testing;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

/**
 * Created by ricar on 29/12/2016.
 */

public class PopupActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(1);

        //AlertDialog dialog = new AlertDialog();
        Toast.makeText(this,"Hello again!",Toast.LENGTH_LONG).show();

        SharedPreferences prefs = getSharedPreferences("prefs",0);

        setContentView(R.layout.popup_layout);
    }
}
