package com.menethil.logmylife.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 数据库DB操作类
 * 
 * @author LinLin
 */
public class DBHelper {
	public static final String TAG = "logmylife.db.DBHelper";

	// 本类的单态模式
	private static DBHelper dbHelper = null;

	// 数据库名称
	private static final String DATABASE_NAME = "logmylife.sqlite";
	// 数据库版本号
	private static final int DATABASE_VERSION = 1;
	// 防火应用程序列表，表名
	private static final String APPS_TABLE = "apps";
	// 防火应用程序日志，表名
	private static final String LOGS_TABLE = "logs";

	/**
	 * 防火应用程序，表里的“字段”
	 * 
	 * @author LinLin
	 */
	public class Apps {
		public static final String ID = "_id"; // id
		public static final String UID = "uid"; // uid应用程序在android系统里的唯一标识
		public static final String PACKAGE = "package"; // 软件包名
		public static final String NAME = "name"; // 软件名称
		public static final String LAST_CALLED_NUM = "last_called_num"; // 最后一次吸费的电话号码
		public static final String ALLOW = "allow"; // 是否允许(1单次允话，2总是允许，3单次禁止，4总是禁止，5超时)
		// 用逗号区分开来,比如:1,2,3代表三种吸费类型
		public static final String EXES_TYPE = "exes_type"; // 是什么类型的吸费，比如：SMS，电话,net等等
	}

	/**
	 * 防火应用程序日志，表里的“字段”
	 * 
	 * @author LinLin
	 */
	public class Logs {
		public static final String ID = "_id";
		public static final String APP_ID = "app_id";
		public static final String DATE = "date";
		public static final String ALLOW = "allow";
		public static final String CONTENT = "content";
		public static final String PHONE_NUM = "phone_num";
	}

	/**
	 * 防火权限的类型（6种）
	 * 
	 * @author LinLin
	 */
	public class AllowType {
		public static final int ASK = 0; // 1询问
		public static final int TIMEOUT = ASK + 1; // 2超时
		public static final int SINGLE_ALLOW = TIMEOUT + 1; // 3单次允许
		public static final int SINGLE_DENY = SINGLE_ALLOW + 1; // 4单次拒绝
		public static final int ALLOW = SINGLE_DENY + 1; // 5总是允许
		public static final int DENY = ALLOW + 1; // 6总是禁止

	}

	@SuppressWarnings("unused")
	private Context mContext;
	private SQLiteDatabase mDB;

	public static DBHelper getInstance(Context context) {
		if (dbHelper == null) {
			dbHelper = new DBHelper(context);
		}
		return dbHelper;
	}

	private DBHelper(Context context) {
		this.mContext = context;
		DBOpenHelper dbOpenHelper = new DBOpenHelper(context);
		this.mDB = dbOpenHelper.getWritableDatabase();
	}

	/**
	 * 清除日志
	 */
	public void clearLog() {
		this.mDB.delete(LOGS_TABLE, null, null);
	}

	/**
	 * 删除AppDetail by _id，并且也删除相对应的日志
	 * 
	 * @param _id
	 */
	public void deleteById(long id) {
		Log.d(TAG, "Deleting from logs table where app_id=" + id);
		this.mDB.delete(LOGS_TABLE, "app_id=?", new String[] { Long
				.toString(id) });
		Log.d(TAG, "Deleting from apps table where _id=" + id);
		this.mDB
				.delete(APPS_TABLE, "_id=?", new String[] { Long.toString(id) });
	}

	/**
	 * 删除AppDetail by UID
	 * 
	 * @param uid
	 */
	public void deleteByUid(int uid) {
		Cursor cursor = this.mDB.query(APPS_TABLE, new String[] { Apps.ID },
				"uid=?", new String[] { Integer.toString(uid) }, null, null,
				null);
		if (cursor.moveToFirst()) {
			Log.d(TAG, "_id found, deleting logs");
			long id = cursor.getLong(cursor.getColumnIndex(Apps.ID));
			this.mDB.delete(LOGS_TABLE, "_id=?", new String[] { Long
					.toString(id) });
		}
		this.mDB.delete(APPS_TABLE, "uid=?", new String[] { Integer
				.toString(uid) });
		cursor.close();
	}

	/**
	 * 返回全部的被过滤的项
	 * 
	 * @return Cursor
	 */
	public Cursor getAllAppsForCursor() {
		Cursor cursor = this.mDB.query(APPS_TABLE, new String[] { Apps.ID,
				Apps.UID, Apps.PACKAGE, Apps.NAME, Apps.ALLOW, Apps.EXES_TYPE,
				Apps.LAST_CALLED_NUM }, null, null, null, null,
				"allow DESC, name ASC");

		if (cursor == null) {
			return null;
		}
		return cursor;
	}

	/**
	 * 关闭数据库
	 */
	@SuppressWarnings("unused")
	private void close() {
		if (this.mDB.isOpen()) {
			this.mDB.close();
		}
	}

	// ////////////////////////////// 内部类 ///////////////////////////////
	/**
	 * SQLite数据库的操作帮作类DB
	 * 
	 * @author LinLin
	 */
	private static class DBOpenHelper extends SQLiteOpenHelper {
		// 创建防火应用程序列表，SQL语句
		private static final String CREATE_APPS = "CREATE TABLE IF NOT EXISTS "
				+ APPS_TABLE
				+ " (_id INTEGER, uid INTEGER, package TEXT, name TEXT, last_called_num TEXT, "
				+ "allow INTEGER, exes_type TEXT," + " PRIMARY KEY (_id) );";

		// 创建防火应用程序日志，SQL语句
		private static final String CREATE_LOGS = "CREATE TABLE IF NOT EXISTS "
				+ LOGS_TABLE
				+ " (_id INTEGER, app_id INTEGER, date INTEGER, allow INTEGER, content TEXT, phone_num TEXT, "
				+ "PRIMARY KEY (_id));";

		@SuppressWarnings("unused")
		private Context mContext;

		DBOpenHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			mContext = context;
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_APPS); // 创建防火应用程序列表，SQL语句
			db.execSQL(CREATE_LOGS); // 创建防火应用程序日志，SQL语句
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// 数据数升级
		}
	}

}
