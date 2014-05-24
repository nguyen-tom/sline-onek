package com.sgroup.skara.util;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.sgroup.skara.R;
import com.sgroup.skara.model.Song;

public class DBUtil {
	
	private static DBUtil util = new DBUtil();
	private static DatabaseHelper helper = null;
	private static String lastupdate = null;
	private static Context context = null;

	public static void initialize(final Context context) throws Exception {
		DBUtil.context = context;

		if (helper == null) {
			final String dbName = context.getString(R.string.db_filename);
			int version = 1;

			try {
				version = Integer.parseInt(context
						.getString(R.string.db_version));
			} catch (final Exception e) {

			}

			helper = util.new DatabaseHelper(context,
					StrUtil.nvl(dbName).length() != 0 ? dbName : null, version);
			try {
				helper.getWritableDatabase();
			} catch (Exception e) {
				close();
				helperDestroy();
				throw e;
			}

			if (helper.initialized == null) {
				helper.initialized = true;
			}
		}
	}

	public static void helperDestroy() {
		helper = null;
	}

	public static boolean isInitialized() {
		if (helper != null) {
			return helper.isInitialized();
		}
		return false;
	}
	public static void updateMaster() throws Exception{
		DBUtil.helper.updateMaster(DBUtil.getInstance(context));
	}

	public static void setContext(final Context context) {
		DBUtil.context = context;
	}

	public static SQLiteDatabase getInstance(final Context context)
			throws Exception {
		initialize(context);
		return helper.getWritableDatabase();
	}

	public static void close() {
		if (helper != null) {
			try {
				helper.close();
			} catch (Exception ignore) {
			}
		}
	}

	public static SimpleDateFormat getSqlDateFormat() {
		if (helper != null) {
			return helper.SQL_DATE_FORMAT;
		}
		return null;
	}
	private static final String ARIANG_LIST_SQL = "select code, name, author, lyrics, author from m_ariang order by name COLLATE UNICODE ";

	public static List<Song> getAriangList() {
		return getListSong(ARIANG_LIST_SQL, null);
	}

	private static final String CALI_LIST_SQL = "select code,name, author, lyrics,author from m_cali order by name";
    
	public static String[][] getCaliforniaList() {
		return getListRev(CALI_LIST_SQL, null);
	}
	private static final String MUSIC_CORE_LIST_SQL = "select code,name, author, lyrics from m_music_core order by name";

	public static String[][] getMusicCoreList() {
		return getListRev(CALI_LIST_SQL, null);
	}
	private static final String FAVORITE_LIST_SQL = "select code,name, author, lyrics from m_music_core order by name";

	public static String[][] getFavoriteList() {
		return getListRev(CALI_LIST_SQL, null);
	}

	private static final String SONG_IN_DEVICE_SQL = "select name, author, lyrics from %s where code=? order by name";

	public static String getSongFromCode(String id,String device ) {
		String result = null;
		String sql  = String.format(SONG_IN_DEVICE_SQL, device);
		String[][] temp = getListRev(sql, new String[] { id });
		if (temp.length > 0) {
			if (temp[0].length > 0)
				result = temp[0][0];
		}
		return result;
	}

	private static String[][] getListRev(final String sql, final String[] args) {
		String[][] result = null;
		if (helper != null) {
			try {
				final SQLiteDatabase db = helper.getReadableDatabase();
					final Cursor cursor = db.rawQuery(sql, args);
					cursor.moveToFirst();
					result = new String[cursor.getColumnCount()][];
					for (int c = 0; c < cursor.getColumnCount(); c++) {
						result[c] = new String[cursor.getCount()];
					}
					for (int r = 0; r < cursor.getCount(); r++) {
						for (int c = 0; c < cursor.getColumnCount(); c++) {
							result[c][r] = cursor.getString(c);
						}
						cursor.moveToNext();
					}
					cursor.close();
				
			} catch (final Exception e) {
				Log.e(DBUtil.class.getName(), "## " + e.getMessage(), e);
			}
		}
		return result;
	}
	private static List<Song> getListSong(final String sql, final String[] args) {
		List<Song> result = null;
		if (helper != null) {
			try {
				final SQLiteDatabase db = helper.getReadableDatabase();
				int limit = 0;
				result  = new ArrayList<Song>();
				while(limit + 500 <= 7800){
					String sqlLimit = sql + " LIMIT " + limit  + ", 500";
					final Cursor cursor = db.rawQuery(sqlLimit, args);
					cursor.moveToFirst();
					for (int r = 0; r < cursor.getCount(); r++) {
						    int i  = 0;
							Song song  = new Song(cursor.getString(i++),
									              cursor.getString(i++),
									              cursor.getString(i++),
									              cursor.getString(i++));
							result.add(song);
						    cursor.moveToNext();
					}
					cursor.close();
					limit  = limit + 500;
					Log.w("DBUtil","Limit :" + limit + " , Array Count :" + result.size());
				}
				
			} catch (final Exception e) {
				Log.e(DBUtil.class.getName(), "## " + e.getMessage(), e);
			}
		}
		return result;
	}

	private static String[][] getList(final String sql, final String[] args) {
		String[][] result = null;
		if (helper != null) {
			try {
				final SQLiteDatabase db = helper.getReadableDatabase();
				final Cursor cursor = db.rawQuery(sql, args);
				cursor.moveToFirst();
				result = new String[cursor.getCount()][];
				for (int r = 0; r < cursor.getCount(); r++) {
					result[r] = new String[cursor.getColumnCount()];
				}
				for (int r = 0; r < cursor.getCount(); r++) {
					for (int c = 0; c < cursor.getColumnCount(); c++) {
						result[r][c] = cursor.getString(c);
					}
					cursor.moveToNext();
				}
				cursor.close();
			} catch (final Exception e) {
				Log.e(DBUtil.class.getName(), "## " + e.getMessage(), e);
			}

		}
		return result;
	}
	
	class DatabaseHelper extends SQLiteOpenHelper {
		private Context context = null;

		private Boolean initialized;

		public boolean isInitialized() {
			return ((this.initialized != null) && this.initialized);
		}

		public DatabaseHelper(final Context context) {
			super(context, null, null, 1);
			this.context = context;
		}

		public DatabaseHelper(final Context context, final String fileName,
				final int version) {
			super(context, fileName, null, version);
			this.context = context;
		}

		@Override
		public void onCreate(final SQLiteDatabase db) {
			try {
				db.beginTransaction();
				for (int t = 0; t < CREATE_SQL.length; t++) {
					db.execSQL(DROP_SQL[t]);
					db.execSQL(CREATE_SQL[t]);
				}
				 db.setTransactionSuccessful(); //
			} catch (final Exception e) {
				Log.e(DBUtil.class.getName(), "## " + e.getMessage(), e);
				initialized = false;
			} finally {
				 db.endTransaction(); 
			}
		}

		@Override
		public void onUpgrade(final SQLiteDatabase db, final int oldVersion,
				final int newVersion) {
		}

		private final SimpleDateFormat SQL_DATE_FORMAT = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");

		private final int UPD_ALL = 0;
		private final int UPD_NAME = 1;
		private final int UPD_DATE = 2;
		private final String LASTUPDATE_API_NAME = "/MasterUpdateInfoAPI/";
		private final String[] LASTUPDATE_COLUMN = new String[] {
				"master_api_update_information.information.information_update",
				"master_api_update_information.information.master_api_info.master_api_name",
				"master_api_update_information.information.master_api_info.master_api_updatetime",

		};
		private final String[] API_NAME = new String[] { 
				"arirang_vi",
				"california_vi",
				"musicCore_vi", 
		};

		private final String[] DROP_SQL = new String[] {
				"drop table if exists m_ariang",
				"drop table if exists m_cali",
				"drop table if exists m_music_core",
				"drop table if exists m_favorite",
				"drop table if exists m_user",};

		private final String[] CREATE_SQL = new String[] {
				"create table m_ariang (id integer primary key, code text not null, name text not null, lyrics text not null,author text,type text,vol text )",
				"create table m_cali (id integer primary key, code text not null, name text not null, lyrics text not null,author text,type text,vol text )",
				"create table m_music_core (id integer primary key, code text not null, name text not null, lyrics text not null,author text,type text,vol text )",
				"create table m_favorite (id integer primary key, code text not null, name text not null, lyrics text not null,author text,type text,vol text )",
				"create table m_user (id integer primary key, name text not null, pass text not null, device text not null, language text not null)",};
		private final String[] INSERT_SQL = new String[] {
				"insert into m_ariang (code, name, lyrics, author, type, vol) values (?, ?, ?, ?, ?, ?)",
				"insert into m_cali (code, name, lyrics, author, type, vol) values (?, ?, ?, ?, ?, ?)",
				"insert into m_music_core (code, name, lyrics, author, type, vol) values (?, ?, ?, ?, ?, ?)",
				"insert into m_favorite (code, name, lyrics, author, type, vol) values (?, ?, ?, ?, ?, ?)",
				"insert into m_user (name, pass, device, language) values (?, ?, ?, ?)"};
		private final String[] SELECT_SQL = new String[] {
				"select max(id) from m_ariang ",};
		public boolean  checkExistDatabase(final SQLiteDatabase db){
			Cursor c = null;
			boolean exist = false;
			try {
				c = db.rawQuery(SELECT_SQL[0], null);
				if (c.moveToFirst()) {
					final String dt = c.getString(0);
					if (dt != null) {
						   exist = true;
						} else {
							exist = false;
						}
				}
			} catch (final Exception e) {
				
			} finally {
				if (c != null) {
					try {
						c.close();
					} catch (final Exception e) {
						
					}
				}
			}
			return exist;
		}
		public boolean updateMaster(final SQLiteDatabase db) {
			boolean isUpdated = false;
			//check table exist
			isUpdated = checkExistDatabase(db);
			if (!isUpdated) {
				SQLiteStatement stmt = null;
				try {
					if (!db.inTransaction())
						db.beginTransaction();
					for (int t = 0; t < API_NAME.length; t++) {
						   InputStream input = context.getAssets().open(API_NAME[t]);
						   Reader reader = new InputStreamReader(input, "UTF-8");
						   LineNumberReader buff = new LineNumberReader(reader); 
							if(input.available() > 0){
								int count = 0;
								db.execSQL(DROP_SQL[t]);
								db.execSQL(CREATE_SQL[t]);
								stmt = db.compileStatement(INSERT_SQL[t]);
								String line  = null;
								while((line = buff.readLine()) != null){
									String[] arrValue = line.split("#");
									if (arrValue.length > 3) {
												int j = 1;
												for(int i = 0; i < arrValue.length ;  ++i){
													stmt.bindString(j++, arrValue[i]);
													if(i >= 4) break;
												 }
												stmt.executeInsert();
												count++;
									}else{
										Log.d(DBUtil.class.getName()," line Error ="+ line);
									}
								}
								Log.d(DBUtil.class.getName()," count="+ count);
								stmt.close();
							} else {
								Log.w(DBUtil.class.getName(),"## xml result null. api="+ API_NAME[t]);
								return false;
							}
						}
					isUpdated = true;
					db.setTransactionSuccessful();
				} catch (final Exception e) {
					Log.e(DBUtil.class.getName(), "## " + e.getMessage(), e);
				} finally {
					if (stmt != null) {
						try {
							stmt.close();
						} catch (final Exception e) {
						}
					}
					db.endTransaction();
				}
	      }
			return isUpdated;
	  }
	}//end class helper
   
	

}
