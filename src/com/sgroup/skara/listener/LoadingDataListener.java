package com.sgroup.skara.listener;

import com.sgroup.skara.model.Section;

public interface LoadingDataListener {
	
	public void callBack(Section lkSong);
	public void error(String message);
	public void loading(boolean show);


}
