package com.sgroup.skara.model;

import com.sgroup.skara.util.StrUtil;
import com.sgroup.skara.util.StringMatcher;


public class Song {
	
	public static int RESULF_FIND_NONE = 0;
	public static int RESULF_FIND_EXACT = 1;
	public static int RESULF_FIND_CONTANT = 2;
	
	public static final int NUM_SEARCH_FIELD = 4;
	public static final int ID_FIELD_TEN = 0;
	public static final int ID_FIELD_LOI = 1;
	public static final int ID_FIELD_NHACSI = 2;
	public static final int ID_FIELD_CASI = 3;
	
	private String code;
	private String name;
	private String author;
	private String type;
	private String lyric;
	private String singer;
	private String vol;
	private String favorite = "0";
	
	public String[] fieldForSearch;
	public Song(){
		
	}
	public Song(String data)
	{
	  if(!StrUtil.isNullString(data)){
		  String[] arr= data.split("#");
			if (arr.length>=4)
			{
				code=arr[0];
				name=arr[1];
				lyric=arr[2];
				author=arr[3];
				
				fieldForSearch= new String[3];
				fieldForSearch[ID_FIELD_TEN] = SimplifiedString(name);
				fieldForSearch[ID_FIELD_LOI]    = SimplifiedString(lyric);
				fieldForSearch[ID_FIELD_NHACSI] = SimplifiedString(author);
				//fieldForSearch[ID_FIELD_CASI] = SimplifiedString(casi);
			}
			else
			{
				code="";
				android.util.Log.w("My trace", ">>>"+data);	
			}
	  }
		
	}
	public Song(String maso,String ten,String loi,String nhacsi){
		this.code  = maso;
		this.name   = ten;
		this.lyric   = loi;
		this.author = nhacsi;
	}
	
	public String SimplifiedString(String str)
	{
		
		for (int i=0; i<7; i++)
		{
			char[] arr= StringMatcher.unicode[i];
			for (int j=1; j<arr.length; j++)
			{
				str = str.replace(arr[j], arr[0]);
			}
		}
		return str;
	}
	@Override
	public String toString() {
		return code+"#"
	           +name+"#"
			   +author+"#"
	           +singer+"#"
	           +type+"#"
			   +lyric;
	}
	public String getFavorite() {
		return favorite;
	}
	public void setFavorite(String favorite) {
		this.favorite = favorite;
	}
	
	
	public String getVol() {
		return vol;
	}
	public void setVol(String vol) {
		this.vol = vol;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getLyric() {
		return lyric;
	}
	public void setLyric(String lyric) {
		this.lyric = lyric;
	}
	public String getSinger() {
		return singer;
	}
	public void setSinger(String singer) {
		this.singer = singer;
	}
	 
}