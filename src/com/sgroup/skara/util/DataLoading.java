package com.sgroup.skara.util;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;

import com.sgroup.skara.R;
import com.sgroup.skara.database.SharedPreferencesDB;
import com.sgroup.skara.listener.LoadingDataListener;
import com.sgroup.skara.model.Section;
import com.sgroup.skara.model.Song;
import com.sgroup.skara.model.UserOption;


public class DataLoading extends AsyncTask<UserOption, Integer, Section> {

	private static final String TAG  = "DataLoading >>";
	public static final String DATA_FAVORITE_FOLDER_PATH = "AkaraFv";
	public static final String DATA_FAVORITE_FILE = "AkaraFvr";
	private LoadingDataListener ldListener;
	private int deviceID;
	private int languageID;
	private Context context;
	private FileUtil fileUtil;
	private UserOption userOption;
	private String charFilter;
	
	public DataLoading(Context context,LoadingDataListener listerner,String charFilter){
		this.context  = context;
		this.ldListener  = listerner;
		this.charFilter = charFilter;
	}
	@Override
	protected void onPreExecute() {
		fileUtil = new FileUtil();
	}
	
	@Override
	protected Section doInBackground(UserOption... param) {
		UserOption userOption  = param[0];
		Section section = null;
		  if(userOption == null) userOption = new UserOption();
		  if(userOption != null){
		  
	       try {
			//String mSections = "123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
				List<Song> arrStr  = DBUtil.getSectionAnphabe(
						UserOption.LIST_DEVICE[userOption.getDevice()],
						charFilter);
				section = new Section();
				section.setLsSong(arrStr);
				
			
			//section = fileUtil.getSection(context, userOption,share.getEndChar(),);
			//share.setEndchar(section.getEndChar());
		} catch (Exception e) {
			e.printStackTrace();
		}
		  }
		return section;
	}
	@Override
	protected void onPostExecute(Section returnBean) {
		if(returnBean != null){
			ldListener.callBack(returnBean);
		}else {
            Resources resource = context.getResources();
			ldListener.error(resource.getString(R.string.error_loading));
		}
	}
	
	
	

}
