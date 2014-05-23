package com.sgroup.skara.model;

import com.sgroup.skara.util.StrUtil;


public class Song {
	
	public static int RESULF_FIND_NONE = 0;
	public static int RESULF_FIND_EXACT = 1;
	public static int RESULF_FIND_CONTANT = 2;
	
	public static final int NUM_SEARCH_FIELD = 4;
	public static final int ID_FIELD_TEN = 0;
	public static final int ID_FIELD_LOI = 1;
	public static final int ID_FIELD_NHACSI = 2;
	public static final int ID_FIELD_CASI = 3;
	
	public String maso;
	public String ten;
	public String nhacsi;
	public String casi;
	public String loi;
	public String vol;
	private int device;
	private int language;
	
	public String[] fieldForSearch;
	
	public String[][] unicode = {{"a", "??", "??", "???", "??", "???", "??", "???", "???", "???", "???", "???", "??", "???", "???", "???", "???", "???"}, 
								{"d", "??"}, 
								{"e", "??", "??", "???", "???", "???", "??", "???", "???", "???", "???", "???"}, 
								{"i", "??", "??", "???", "??", "???"}, 
								{"o", "??", "??", "???", "??", "???", "??", "???", "???", "???", "???", "???", "??", "???", "???", "???", "???", "???"}, 
								{"u", "??", "??", "???", "??", "???", "??", "???", "???", "???", "???", "???"}, 
								{"y", "??", "???", "???", "???", "???"}};
	
	public Song(String data)
	{
	  if(!StrUtil.isNullString(data)){
		  String[] arr= data.split("#");
			if (arr.length>=4)
			{
				maso=arr[0];
				ten=arr[1];
				loi=arr[2];
				nhacsi=arr[3];
				
				fieldForSearch= new String[3];
				fieldForSearch[ID_FIELD_TEN] = SimplifiedString(ten);
				fieldForSearch[ID_FIELD_LOI]    = SimplifiedString(loi);
				fieldForSearch[ID_FIELD_NHACSI] = SimplifiedString(nhacsi);
				//fieldForSearch[ID_FIELD_CASI] = SimplifiedString(casi);
			}
			else
			{
				maso="";
				android.util.Log.w("My trace", ">>>"+data);	
			}
	  }
		
	}
	public Song(String maso,String ten,String loi,String nhacsi){
		this.maso  = maso;
		this.ten   = ten;
		this.loi   = loi;
		this.nhacsi = nhacsi;
	}
	
	public String SimplifiedString(String str)
	{
		str=str.toLowerCase();
		for (int i=0; i<7; i++)
		{
			String[] arr= unicode[i];
			for (int j=1; j<arr.length; j++)
			{
				str = str.replace(arr[j], arr[0]);
			}
		}
		return str;
	}
	
	public int Compare(String key, int[] field)
	{
		String str = SimplifiedString(key);
		for (int i=0; i<field.length; i++)
		{
			if (str.compareTo(fieldForSearch[field[i]]) == 0)
				return RESULF_FIND_EXACT;
			if (fieldForSearch[field[i]].indexOf(str)>=0)
				return RESULF_FIND_CONTANT;
		}
		return RESULF_FIND_NONE;
	}
	
	@Override
	public String toString() {
		return maso+"#"+ten+"#"+nhacsi+"#"+casi+"#"+loi;
	}
}