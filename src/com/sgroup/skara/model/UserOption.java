package com.sgroup.skara.model;

public class UserOption {
	
	
	
	public static final int DEVICE_ARIRANG     = 0;
	public static final int DEVICE_CALIFORNIA  = 1;
	public static final int DEVICE_MUSICCORE   = 2;
	
	public static final int VIETNAMESE        = 0;
	public static final int ENGLISH           = 1;
	
	 public static final String[]LIST_DEVICE = {"arirang","california","musicCore"};
	 public static final String[]LIST_LANGUAGE = {"vi","en"};
     private int device;
     private int language;
     
     public UserOption(){
    	 device   = DEVICE_ARIRANG;
    	 language = VIETNAMESE;
     }
     public UserOption(int lang,int device){
    	 this.language  = lang;
    	 this.device    = device;
     }
     
	public int getDevice() {
		return device;
	}
	public void setDevice(int device) {
		this.device = device;
	}
	public int getLanguage() {
		return language;
	}
	public void setLanguage(int language) {
		this.language = language;
	}
     
     
     
}
