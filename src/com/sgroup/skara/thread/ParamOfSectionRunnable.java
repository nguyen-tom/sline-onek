package com.sgroup.skara.thread;


import com.sgroup.skara.SongFragment;

public class ParamOfSectionRunnable {
	private String device;
	private String charFillter;
	private SongFragment songFragment;
	
	
	public String getDevice() {
		return device;
	}
	public void setDevice(String device) {
		this.device = device;
	}
	public String getCharFillter() {
		return charFillter;
	}
	public void setCharFillter(String charFillter) {
		this.charFillter = charFillter;
	}
	public void setSongFragment(SongFragment listView){
		this.songFragment  = listView;
	}
	public SongFragment getSongFragment(){
		return this.songFragment;
	};
	
	
	

}
