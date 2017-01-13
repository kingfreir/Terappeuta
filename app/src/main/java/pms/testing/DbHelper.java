package pms.testing;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by ricar on 12/01/2017.
 */

public class DbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "NotificationReader.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE "+ NotifReaderContract.NotifEntry.TABLE_NAME + " (" +
                    NotifReaderContract.NotifEntry._ID + " INTEGER PRIMARY KEY," +
                    NotifReaderContract.NotifEntry.MESSAGE + " TEXT," +
                    NotifReaderContract.NotifEntry.IMAGE_PATH + " TEXT," +
                    NotifReaderContract.NotifEntry.DATE + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + NotifReaderContract.NotifEntry.TABLE_NAME;

    public DbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        onUpgrade(db, oldVersion, newVersion);
    }
}
