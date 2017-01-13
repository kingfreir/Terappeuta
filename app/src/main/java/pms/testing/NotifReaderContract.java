package pms.testing;

import android.provider.BaseColumns;

/**
 * Created by ricar on 12/01/2017.
 */

public final class NotifReaderContract{
    private NotifReaderContract(){}

    public static class NotifEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String MESSAGE = "msg";
        public static final String IMAGE_PATH = "img_path";
        public static final String DATE = "date";
    }
}
