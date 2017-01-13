package pms.testing;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pms.testing.bitmap.BitmapHelper;

/**
 * Created by ricar on 29/12/2016.
 */

public class PopupActivity extends ActionBarActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_layout);

        mRecyclerView = (RecyclerView)findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerViewAdapter(getDataSet());
        mRecyclerView.setAdapter(mAdapter);

        SharedPreferences prefs = getSharedPreferences("prefs",0);

    }

    @Override
    protected void onResume() {
        super.onResume();
        ((RecyclerViewAdapter) mAdapter).setOnItemClickListener(new RecyclerViewAdapter
                .MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                //do something when clicked
            }
        });
    }

    private ArrayList<DataObject> getDataSet() {
        ArrayList results = new ArrayList<DataObject>();

        DbHelper helper = new DbHelper(this);

        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from entry order by "
                + NotifReaderContract.NotifEntry._ID +" desc",null);

        int i = 0;
        while(cursor.moveToNext()){
            String message = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            NotifReaderContract.NotifEntry.MESSAGE
                    )
            );

            String date = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            NotifReaderContract.NotifEntry.DATE
                    )
            );

            String image_path = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            NotifReaderContract.NotifEntry.IMAGE_PATH
                    )
            );

            Bitmap img_small = BitmapFactory.decodeResource(getResources(),R.mipmap.logo_terappeuta);

            if(image_path.contains("ID")){
                String number = image_path.replaceAll("ID","");
                int pointer = Integer.parseInt(number);
                img_small = BitmapHelper.decodeSampledBitmapFromResource(getResources(),pointer,400,200);
            }else{
                try {
                    img_small = Glide.with(this)
                            .load(Uri.fromFile(new File(image_path)))
                            .asBitmap()
                            .into(512, 250)
                            .get();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            results.add(new DataObject(message,date,img_small));
            i++;
            if(i==20){break;}
        }

        cursor.close();
        return results;
    }
}
