package com.sgroup.skara.util;

import com.sgroup.skara.model.Section;

public class CacheSection  extends android.support.v4.util.LruCache<String, Section>{
    public static final int MAX_SIZE  = 4 * 1024 * 1204 ; //4Mb
	public CacheSection(int maxSize) {
		super(maxSize);
		
	}
	
	

}
