package com.mentalmachines.ttime;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.mentalmachines.ttime.objects.Alert;
import com.mentalmachines.ttime.objects.Route;

import java.util.ArrayList;

/**
 * Created by CaptofOuterSpace on 1/23/2016.
 */
public class DBHelper extends SQLiteOpenHelper {
    public static final String TAG = "DBAdapter";
    public static final int DB_VERSION = 1;
    public static final String DBNAME = "ttimedb.sqlite3";
    public static final String DB_ROUTE_TABLE = "route_table";
    public static final String STOPS_INB_TABLE = "stops_inbound";
    public static final String STOPS_OUT_TABLE = "stops_outbound";
    public static final String FAVS_TABLE = "favorites_table";

    String CREATE_FAVS_TABLE  = TABLE_PREFIX + FAVS_TABLE + "("
            + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_ROUTE_ID + " TEXT unique not null, "
            + KEY_ROUTE_NAME + " TEXT unique not null);";

    //Route table keys
    public static final String KEY_ROUTE_MODE = "mode";
    public static final String KEY_ROUTE_MODE_NM = "mode_name";
    public static final String KEY_ROUTE_ID = "route_id";
    public static final String KEY_ROUTE_NAME = "route_name";
    //public static final String KEY_ROUTE_TYPE = "route_type";
    public static final String KEY_ROUTE = "route";
    public static final String BUS_MODE = "Bus";
    public static final String SUBWAY_MODE = "Subway";

    String CREATE_DB_TABLE_ROUTE  = TABLE_PREFIX + DB_ROUTE_TABLE + "("
            + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_ROUTE_MODE + " TEXT not null, "
            + KEY_ROUTE_ID + " TEXT unique not null, "
            + KEY_ROUTE_NAME + " TEXT unique not null);";

    //Stop keys, some duplication with routes
    public static final String KEY_DIR = "direction";
    public static final String KEY_DIR_NM = "direction_name";
    public static final String KEY_DIR_ID = "direction_id";
    //Inbound, Outbound, Northbound and Southbound
    public static final String STOP = "stop";
    public static final String KEY_STOPID = "stop_id";
    public static final String KEY_STOPNM = "stop_name";
    public static final String KEY_STOPLT = "stop_lat";
    public static final String KEY_STOPLN = "stop_lon";

    public static final String KEY_TRIP = "trip";
    public static final String KEY_VEHICLE = "vehicle";
    public static final String KEY_TRIP_SIGN = "trip_headsign";
    public static final String KEY_HOUR = "hour";
    public static final String KEY_MINUTE = "minute";
    public static final String KEY_DTIME = "sch_dep_dt";

    String SCHEDULE_COLS = "(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_ROUTE_ID + " TEXT not null,"
            + KEY_STOPID + " TEXT not null,"
            + KEY_DIR_ID + " NUMERIC not null,"
            + KEY_TRIP_SIGN + " TEXT,"
            + KEY_HOUR + " TEXT not null,"
            + KEY_MINUTE + " TEXT not null);";

    public static final String TABLE_PREFIX = "create table if not exists ";
    public static final String WEEKDAY_TABLE_BUS_IN = "weekday_table_b_in";
    public static final String WEEKDAY_TABLE_BUS_OUT = "weekday_table_b_out";
    public static final String WEEKDAY_TABLE_4BUS_IN = "weekday_table_4b_in";
    public static final String WEEKDAY_TABLE_4BUS_OUT = "weekday_table_4b_out";

    public static final String WEEKDAY_TABLE_SUB_IN = "weekday_table_sub_in";
    public static final String WEEKDAY_TABLE_SUB_OUT = "weekday_table_sub_out";

    public static final String SATURDAY_TABLE_BUS_IN = "sat_table_bus_in";
    public static final String SATURDAY_TABLE_BUS_OUT = "sat_table_bus_out";
    public static final String SATURDAY_TABLE_4BUS_IN = "sat_table_4bus_in";
    public static final String SATURDAY_TABLE_4BUS_OUT = "sat_table_4bus_out";

    public static final String SATURDAY_TABLE_SUB_IN = "sat_table_sub_in";
    public static final String SATURDAY_TABLE_SUB_OUT = "sat_table_sub_out";

    public static final String SUNDAY_TABLE_BUS_IN = "sun_table_bus_in";
    public static final String SUNDAY_TABLE_BUS_OUT = "sun_table_bus_out";
    public static final String SUNDAY_TABLE_4BUS_IN = "sun_table_4bus_in";
    public static final String SUNDAY_TABLE_4BUS_OUT = "sun_table_4bus_out";

    public static final String SUNDAY_TABLE_SUB_IN = "sun_table_sub_in";
    public static final String SUNDAY_TABLE_SUB_OUT = "sun_table_sub_out";

    private static final String RTINDEX = "CREATE INDEX RTINDEX ON " + DB_ROUTE_TABLE + "("
            + KEY_ROUTE_ID + ");";
    private static final String STOP_IN_DEX = "CREATE UNIQUE INDEX STOP_IN_DEX ON " + STOPS_INB_TABLE + "("
            + KEY_ROUTE_ID + "," + KEY_STOPID + ");";
    private static final String STOP_OUT_DEX = "CREATE UNIQUE INDEX STOP_OUT_DEX ON " + STOPS_OUT_TABLE + "("
            + KEY_ROUTE_ID + "," + KEY_STOPID + ");";
    //these are created AFTER tables are loaded to speed through loading up these big tables
    public static final String WEEKDAY_DEX_B_IN = "CREATE UNIQUE INDEX WDAY_DEX_B_IN ON " + WEEKDAY_TABLE_BUS_IN + "("
            + KEY_ROUTE_ID + "," + KEY_STOPID + "," + KEY_HOUR + ");";
    public static final String WEEKDAY_DEX_B_OUT = "CREATE UNIQUE INDEX WDAY_DEX_B_OUT ON " + WEEKDAY_TABLE_BUS_OUT + "("
            + KEY_ROUTE_ID + "," + KEY_STOPID + "," + KEY_HOUR + ");";
    public static final String WEEKDAY_DEX_B4_IN = "CREATE UNIQUE INDEX WDAY_DEX_B_IN ON " + WEEKDAY_TABLE_4BUS_IN + "("
            + KEY_ROUTE_ID + "," + KEY_STOPID + "," + KEY_HOUR + ");";
    public static final String WEEKDAY_DEX_B4_OUT = "CREATE UNIQUE INDEX WDAY_DEX_B_OUT ON " + WEEKDAY_TABLE_4BUS_OUT + "("
            + KEY_ROUTE_ID + "," + KEY_STOPID + "," + KEY_HOUR + ");";
    public static final String WEEKDAY_DEX_SUB_IN = "CREATE UNIQUE INDEX WDAY_DEX_SUB_IN ON " + WEEKDAY_TABLE_SUB_IN + "("
            + KEY_ROUTE_ID + "," + KEY_STOPID + "," + KEY_HOUR + ");";
    public static final String WEEKDAY_DEX_SUB_OUT = "CREATE UNIQUE INDEX WDAY_DEX_SUB_OUT ON " + WEEKDAY_TABLE_SUB_OUT + "("
            + KEY_ROUTE_ID + "," + KEY_STOPID + "," + KEY_HOUR + ");";

    public static final String SATURDAY_DEX_B_IN = "CREATE UNIQUE INDEX SAT_DEX_INB ON " + SATURDAY_TABLE_BUS_IN + "("
            + KEY_ROUTE_ID + "," + KEY_STOPID + "," + KEY_HOUR + ");";
    public static final String SATURDAY_DEX_B_OUT = "CREATE UNIQUE INDEX SAT_DEX_OUTB ON " + SATURDAY_TABLE_BUS_OUT + "("
            + KEY_ROUTE_ID + "," + KEY_STOPID + "," + KEY_HOUR + ");";
    public static final String SATURDAY_DEX_4B_IN = "CREATE UNIQUE INDEX SAT_DEX_INB ON " + SATURDAY_TABLE_4BUS_IN + "("
            + KEY_ROUTE_ID + "," + KEY_STOPID + "," + KEY_HOUR + ");";
    public static final String SATURDAY_DEX_4B_OUT = "CREATE UNIQUE INDEX SAT_DEX_OUTB ON " + SATURDAY_TABLE_4BUS_OUT + "("
            + KEY_ROUTE_ID + "," + KEY_STOPID + "," + KEY_HOUR + ");";
    public static final String SATURDAY_DEX_SUB_IN = "CREATE UNIQUE INDEX SAT_DEX_IN ON " + SATURDAY_TABLE_SUB_IN + "("
            + KEY_ROUTE_ID + "," + KEY_STOPID + "," + KEY_HOUR + ");";
    public static final String SATURDAY_DEX_SUB_OUT = "CREATE UNIQUE INDEX SAT_DEX_OUT ON " + SATURDAY_TABLE_SUB_OUT + "("
            + KEY_ROUTE_ID + "," + KEY_STOPID + "," + KEY_HOUR + ");";

    public static final String SUN_DEX_B_IN = "CREATE UNIQUE INDEX SUN_DEX_INB ON " + SUNDAY_TABLE_BUS_IN + "("
            + KEY_ROUTE_ID + "," + KEY_STOPID + "," + KEY_HOUR + ");";
    public static final String SUN_DEX_B_OUT = "CREATE UNIQUE INDEX SUN_DEX_OUTB ON " + SUNDAY_TABLE_BUS_OUT + "("
            + KEY_ROUTE_ID + "," + KEY_STOPID + "," + KEY_HOUR + ");";
    public static final String SUN_DEX_B4_IN = "CREATE UNIQUE INDEX SUN_DEX_INB ON " + SUNDAY_TABLE_4BUS_IN + "("
            + KEY_ROUTE_ID + "," + KEY_STOPID + "," + KEY_HOUR + ");";
    public static final String SUN_DEX_B4_OUT = "CREATE UNIQUE INDEX SUN_DEX_OUTB ON " + SUNDAY_TABLE_4BUS_OUT + "("
            + KEY_ROUTE_ID + "," + KEY_STOPID + "," + KEY_HOUR + ");";
    public static final String SUN_DEX_SUB_IN = "CREATE UNIQUE INDEX SUN_DEX_IN ON " + SUNDAY_TABLE_SUB_IN + "("
            + KEY_ROUTE_ID + "," + KEY_STOPID + "," + KEY_HOUR + ");";
    public static final String SUN_DEX_SUB_OUT = "CREATE UNIQUE INDEX SUN_DEX_OUT ON " + SUNDAY_TABLE_SUB_OUT + "("
            + KEY_ROUTE_ID + "," + KEY_STOPID + "," + KEY_HOUR + ");";


    public static final String KEY_ALERTS= "alerts";
    public static final String KEY_ALERT_ID = "alert_id";
    public static final String KEY_EFFECT_NAME = "effect_name";
    public static final String KEY_EFFECT = "effect";
    //public static final String KEY_CAUSE_NAME = "cause_name";
    //these are the same...
    public static final String KEY_CAUSE = "cause";
    //public static final String KEY_HEADER_TEXT = "header_text";
    public static final String KEY_SHORT_HEADER_TEXT = "short_header_text";
    public static final String KEY_DESCRIPTION_TEXT = "description_text";
    public static final String KEY_SEVERITY = "severity";
    public static final String KEY_CREATED_DT = "created_dt";
    public static final String KEY_LAST_MODIFIED_DT = "last_modified_dt";
    public static final String KEY_SERVICE_EFFECT_TEXT = "service_effect_text";
    public static final String KEY_TIMEFRAME_TEXT = "timeframe_text";
    public static final String KEY_ALERT_LIFECYCLE = "alert_lifecycle";
    public static final String KEY_EFFECT_PERIOD_START = "effect_start";
    public static final String KEY_EFFECT_PERIOD_END = "effect_end";
    public static final String KEY_EFFECT_PERIODS = "effect_periods";

    public static final String DB_ALERTS_TABLE = "alerts";

    String CREATE_DB_TABLE_ALERTS = "create table " + DB_ALERTS_TABLE + "("
            + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_ALERT_ID + " TEXT not null,"
            + KEY_EFFECT + " TEXT not null,"
            + KEY_EFFECT_NAME + " int not null,"
            + KEY_CAUSE + " TEXT,"
            + KEY_SHORT_HEADER_TEXT + " TEXT,"
            + KEY_DESCRIPTION_TEXT + " TEXT,"
            + KEY_SEVERITY + " TEXT,"
            + KEY_CREATED_DT + " TEXT not null,"
            + KEY_LAST_MODIFIED_DT + " TEXT not null,"
            + KEY_SERVICE_EFFECT_TEXT + " TEXT,"
            + KEY_TIMEFRAME_TEXT + " TEXT,"
            + KEY_ALERT_LIFECYCLE + " TEXT ,"
            + KEY_EFFECT_PERIOD_START + " TEXT,"
            + KEY_EFFECT_PERIOD_END + " TEXT);";

    public static final String STOPS_COLS = "("
            + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_ROUTE_ID + " TEXT not null,"
            + KEY_STOPID + " TEXT not null,"
            + KEY_STOPNM + " TEXT not null,"
            + KEY_ALERT_ID + " NUMERIC,"
            + KEY_STOPLT + " NUMERIC not null,"
            + KEY_STOPLN + " NUMERIC not null);";

    public static final String KEY_PREAWAY = "pre_away";
    public static final String KEY_SCH_TIME = "sch_arr_dt";
    //Property of “trip.” String representation of an integer.
    //Scheduled arrival time at the stop for the trip, in epoch time
    //Example: “1361989260”
    public static final String PRED_TIME = "pre_dt";


    // alert ids are saved to the stop table
    // parse alerts in reverse chron order, save alert id to stop table
    //check the stop table alert is still valid, else look for other alert id for stop


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion
                + " to "
                + newVersion + ", which may destroy all old data");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB_TABLE_ROUTE);
        db.execSQL(CREATE_DB_TABLE_ALERTS);
        db.execSQL(CREATE_FAVS_TABLE);

        db.execSQL(TABLE_PREFIX + STOPS_INB_TABLE + STOPS_COLS);
        db.execSQL(TABLE_PREFIX + STOPS_OUT_TABLE + STOPS_COLS);
        db.execSQL(RTINDEX);
        db.execSQL(STOP_IN_DEX);
        db.execSQL(STOP_OUT_DEX);
        db.execSQL(TABLE_PREFIX + WEEKDAY_TABLE_BUS_IN + SCHEDULE_COLS);
        db.execSQL(TABLE_PREFIX + WEEKDAY_TABLE_BUS_OUT + SCHEDULE_COLS);
        db.execSQL(TABLE_PREFIX + WEEKDAY_TABLE_4BUS_IN + SCHEDULE_COLS);
        db.execSQL(TABLE_PREFIX + WEEKDAY_TABLE_4BUS_OUT + SCHEDULE_COLS);
        db.execSQL(TABLE_PREFIX + WEEKDAY_TABLE_SUB_IN + SCHEDULE_COLS);
        db.execSQL(TABLE_PREFIX + WEEKDAY_TABLE_SUB_OUT + SCHEDULE_COLS);

        db.execSQL(TABLE_PREFIX + SATURDAY_TABLE_BUS_IN + SCHEDULE_COLS);
        db.execSQL(TABLE_PREFIX + SATURDAY_TABLE_BUS_OUT + SCHEDULE_COLS);
        db.execSQL(TABLE_PREFIX + SATURDAY_TABLE_4BUS_IN + SCHEDULE_COLS);
        db.execSQL(TABLE_PREFIX + SATURDAY_TABLE_4BUS_OUT + SCHEDULE_COLS);

        db.execSQL(TABLE_PREFIX + SATURDAY_TABLE_SUB_IN + SCHEDULE_COLS);
        db.execSQL(TABLE_PREFIX + SATURDAY_TABLE_SUB_OUT + SCHEDULE_COLS);

        db.execSQL(TABLE_PREFIX + SUNDAY_TABLE_BUS_IN + SCHEDULE_COLS);
        db.execSQL(TABLE_PREFIX + SUNDAY_TABLE_BUS_OUT + SCHEDULE_COLS);
        db.execSQL(TABLE_PREFIX + SUNDAY_TABLE_4BUS_IN + SCHEDULE_COLS);
        db.execSQL(TABLE_PREFIX + SUNDAY_TABLE_4BUS_OUT + SCHEDULE_COLS);
        db.execSQL(TABLE_PREFIX + SUNDAY_TABLE_SUB_IN + SCHEDULE_COLS);
        db.execSQL(TABLE_PREFIX + SUNDAY_TABLE_SUB_OUT + SCHEDULE_COLS);
    }

    public DBHelper(Context context) {
        super(context, DBNAME, null, DB_VERSION);
    }

    //Some utilities
    public static String[] makeArrayFromCursor(Cursor c, int colIndex) {
        if(c.getCount() > 0 && c.moveToFirst()) {
            final String[] tmp = new String[c.getCount()];
            for(int dex = 0; dex < tmp.length; dex++) {
                tmp[dex] = c.getString(colIndex);
                c.moveToNext();
            }
            return tmp;
        }
        Log.w(TAG, "bad cursor, no array");
        return null;
    }

    public static boolean checkFavorite(String routeNm) {
        final SQLiteDatabase db = TTimeApp.sHelper.getReadableDatabase();
        final boolean isFavorite = checkFavorite(db, routeNm);
        return isFavorite;
    }

    public static boolean checkFavorite(SQLiteDatabase db, String routeNm) {
        final Cursor c = db.query(FAVS_TABLE, null, KEY_ROUTE_NAME + " like '" + routeNm + "'", null, null, null, null);
        final boolean returnVal;
        if(c.getCount() == 0) {
            returnVal = false;
        } else {
            returnVal = true;
        }
        c.close();
        return returnVal;
    }

    public static boolean setFavorite(String routeNm, String routeId) {
        final SQLiteDatabase db = TTimeApp.sHelper.getWritableDatabase();
        final boolean isFavorite;
        final Cursor c = db.query(FAVS_TABLE, null, KEY_ROUTE_NAME + " like '" + routeNm + "'", null, null, null, null);
        if(c.getCount() > 0) {
            //not a favorite and found in the table
            Log.i(TAG, "dropping favorite " + routeNm + db.delete(
                    FAVS_TABLE, KEY_ROUTE_NAME + " like '" + routeNm + "'", null));
            isFavorite = false;
        } else {
            final ContentValues cv = new ContentValues();
            cv.put(KEY_ROUTE_NAME, routeNm);
            cv.put(KEY_ROUTE_ID, routeId);
            Log.i(TAG, "adding favorite " + routeNm + " row:" + db.insert(
                    FAVS_TABLE, "_id", cv));
            cv.clear();
            isFavorite = true;
        }

        c.close();
        return isFavorite;
    }

    public static String getRouteName(SQLiteDatabase db, String routeID) {
        final Cursor c = db.query(DBHelper.DB_ROUTE_TABLE, new String[]{DBHelper.KEY_ROUTE_NAME},
                Route.routeStopsWhereClause + "'" + routeID + "'", null,
                null, null, null);
        if(c.moveToFirst()) {
            final String name = c.getString(0);
            c.close();
            return name;
        }
        return null;
    }

    //This junk manages db concurrency, many reads and one write
    //volatile means one copy in Global memory
    /*private volatile static DBHelper instance;

    *//**
     * DBHelper is a singleton, the app has one db connection, DBHelper
     * The application class instantiates so, no other write, no need to synchronize
     * Seem to have issues clisng cursors!
     * Whenever getHelper is called after the app is launched will already have created the helper instance
     * @param context
     * @return
     *//*
    public static DBHelper getHelper(Context context){
        if(instance == null) {
            instance = new DBHelper(context);
        }
        return instance;
    }*/

    final static String[] mAlertProjection = new String[]{
                    DBHelper.KEY_ALERT_ID,
                    DBHelper.KEY_EFFECT_NAME, DBHelper.KEY_EFFECT,
                    DBHelper.KEY_CAUSE, DBHelper.KEY_SHORT_HEADER_TEXT,
                    DBHelper.KEY_DESCRIPTION_TEXT, DBHelper.KEY_SEVERITY,
                    DBHelper.KEY_CREATED_DT, DBHelper.KEY_LAST_MODIFIED_DT,
                    DBHelper.KEY_TIMEFRAME_TEXT,
                    DBHelper.KEY_ALERT_LIFECYCLE, DBHelper.KEY_EFFECT_PERIOD_START,
                    DBHelper.KEY_EFFECT_PERIOD_END
            };

    public static ArrayList<Alert> getAllAlerts(){
        SQLiteDatabase db = TTimeApp.sHelper.getReadableDatabase();
        Cursor alertsCursor = db.query(DBHelper.DB_ALERTS_TABLE, mAlertProjection,
                null, null, null, null, null, null);

        ArrayList<Alert> alerts = new ArrayList<Alert>();
        if (alertsCursor.getCount() > 0 && alertsCursor.moveToFirst()) {
            do {
                Alert a = new Alert();
                a.alert_id = alertsCursor.getString(alertsCursor.getColumnIndex(DBHelper.KEY_ALERT_ID));
                a.effect_name = alertsCursor.getInt(alertsCursor.getColumnIndex(DBHelper.KEY_EFFECT_NAME));
                a.effect = alertsCursor.getString(alertsCursor.getColumnIndex(DBHelper.KEY_EFFECT));
                a.cause = alertsCursor.getString(alertsCursor.getColumnIndex(DBHelper.KEY_CAUSE));
                a.description_text = alertsCursor.getString(alertsCursor.getColumnIndex(DBHelper.KEY_DESCRIPTION_TEXT));
                a.short_header_text= alertsCursor.getString(alertsCursor.getColumnIndex(DBHelper.KEY_SHORT_HEADER_TEXT));
                a.severity= alertsCursor.getString(alertsCursor.getColumnIndex(DBHelper.KEY_SEVERITY));
                a.created_dt= alertsCursor.getString(alertsCursor.getColumnIndex(DBHelper.KEY_CREATED_DT));
                a.last_modified_dt= alertsCursor.getString(alertsCursor.getColumnIndex(DBHelper.KEY_LAST_MODIFIED_DT));
                a.timeframe_text= alertsCursor.getString(alertsCursor.getColumnIndex(DBHelper.KEY_TIMEFRAME_TEXT));
                a.alert_lifecycle= alertsCursor.getString(alertsCursor.getColumnIndex(DBHelper.KEY_ALERT_LIFECYCLE));
                a.effect_start = alertsCursor.getString(alertsCursor.getColumnIndex(DBHelper.KEY_EFFECT_PERIOD_START));
                a.effect_end= alertsCursor.getString(alertsCursor.getColumnIndex(DBHelper.KEY_EFFECT_PERIOD_END));
                alerts.add(a);
            } while (alertsCursor.moveToNext());
            alertsCursor.close();
        }
        return alerts;
    }
}