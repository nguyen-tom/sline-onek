package com.sgroup.skara.database;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Save and load preferences for loging in SqaureClip
 * @author trananh
 *
 */
public class SharedPreferencesDB{

	private SharedPreferences 	dataStorage;
	private static final String FILENAME 					= "config";
	private static final String PREF_DEVICE					= "device";
	private static final String PREF_LANGUAGE				= "language";
	private static final String PREF_ENDCHAR                = "endChar";
	private static final int BLANK 						    = -1;
	
	/**
	 * Constructor
	 */
	public SharedPreferencesDB(Context context) {
		//--- declare the storage
		dataStorage = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE); // need to check more for private mode and encryption
	}
	
	/**
	 * Clear all user's login information. May use in logout process.
	 */
	public void clearPreferences(){
		this.setDevice(BLANK);
		this.setLanguage(BLANK);
		
	}
	
	
	public void setDevice(int loginType){
		setPreferenceValue(PREF_DEVICE, loginType);
	}
	public void setLanguage(int language){
		setPreferenceValue(PREF_LANGUAGE, language);
	}
	public int getLanguage(){
		return getPreferenceIntValue(PREF_LANGUAGE);
	}
	public int getDevice(){
		return getPreferenceIntValue(PREF_DEVICE);
	}
	public void setEndchar(int value){
		setPreferenceValue(PREF_ENDCHAR, value);
	}
	public int getEndChar(){
		return getPreferenceIntValue(PREF_ENDCHAR);
	}

	
	/**
	 * 
	 * @param preferenceKey
	 * @return
	 */
	private String getPreferenceValue(String preferenceKey){
		String prefVal ="";
		try{
			prefVal = dataStorage.getString(preferenceKey, "");
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return prefVal;
	}
	
	private int getPreferenceIntValue(String preferenceKey){
		int prefVal = 0;
		try{
			prefVal = dataStorage.getInt(preferenceKey, 0);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return prefVal;
	}
	private long getPreferenceLongValue(String preferenceKey){
		long prefVal = 0;
		try{
			prefVal = dataStorage.getLong(preferenceKey,0);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return prefVal;
	}
	
	private boolean getPreferenceBooleanValue(String preferenceKey){
		boolean result = false;
		try{
			result = dataStorage.getBoolean(preferenceKey, true);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * Method set preference value
	 * Save String value
	 */
	private void setPreferenceValue(String preferenceKey, String value){
		try{
			SharedPreferences.Editor editor = dataStorage.edit();
			editor.putString(preferenceKey, value);
			editor.commit();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	/**
	 * Save int value
	 */
	private void setPreferenceValue(String preferenceKey, int value) {
		try{
			SharedPreferences.Editor editor = dataStorage.edit();
			editor.putInt(preferenceKey, value);
			editor.commit();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	private void setPreferenceValue(String preferenceKey, long value) {
		try{
			SharedPreferences.Editor editor = dataStorage.edit();
			editor.putLong(preferenceKey, value);
			editor.commit();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	/**
	 * Save boolean value
	 */
	private void setPreferenceValue(String preferenceKey, boolean value) {
		try{
			SharedPreferences.Editor editor = dataStorage.edit();
			editor.putBoolean(preferenceKey, value);
			editor.commit();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

}
