package com.sgroup.skara.util;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.sgroup.skara.R;
import com.sgroup.skara.listener.DBListener;
import com.sgroup.skara.listener.LoadingDataListener;

public class DBWorking extends AsyncTask<Void, Integer, Void> {

	private static final String TAG  = "DataLoading";
	public static final String DATA_FAVORITE_FOLDER_PATH = "AkaraFv";
	public static final String DATA_FAVORITE_FILE = "AkaraFvr";
	private DBListener ldListener;
	private Context context;
	
	public DBWorking(Context context,DBListener listerner){
		this.context  = context;
		this.ldListener  = listerner;
	}
	@Override
	protected void onPreExecute() {
	}
	
	@Override
	protected Void doInBackground(Void... param) {
		try {
			DBUtil.updateMaster();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}
	@Override
	protected void onPostExecute(Void returnBean) {
		
			ldListener.loadedDB();
		
	}
}
	
	