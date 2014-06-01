package com.sgroup.skara.thread;

import java.util.List;

import com.sgroup.skara.model.Section;
import com.sgroup.skara.model.Song;
import com.sgroup.skara.model.UserOption;
import com.sgroup.skara.util.DBUtil;

public class SectionRunnable  implements Runnable{
   
	static final int STATE_FAILED     = -1;
    static final int STATE_STARTED    =  0;
    static final int STATE_COMPLETED  =  1;
    
	interface SectionRunableMethod {
	   public Section getSection();
	   public void setSection(Section section);
	   public void setCurrentThread(Thread currentThread);
	   public Thread getCurrentThread();
	   public ParamOfSectionRunnable getKey();
	   public void handlerStateLoading(int state);
	   
	}
	final SectionRunableMethod sectionTask ;
	public SectionRunnable (SectionRunableMethod sectionTask){
		this.sectionTask = sectionTask;
	}
	
	
	@Override
	public void run() {
		sectionTask.setCurrentThread(Thread.currentThread());
		
		// Dau current thread vao background
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        
        Section section = sectionTask.getSection();
        
        	//co gang lay record tu database
        	try{
        		
        		//truoc khi bat dau thi chung ta phai kiem tra xem
        		// Thread co kha dung hay khong
        		 if (Thread.interrupted()) {
                     
                     throw new InterruptedException();
                 }
        		 if (null == section) {
        			 sectionTask.handlerStateLoading(STATE_STARTED);
        			 //Lam cac viec can thiet o day.
        			 section  = doWork();
        		 }
        		 sectionTask.setSection(section);
        		 sectionTask.handlerStateLoading(STATE_COMPLETED);
        		
        	}catch(InterruptedException e){
        		
        	}finally{
        		//kiem tra data vua duoc lay len co bi null hay khong
        		if(null == section ) sectionTask.handlerStateLoading(STATE_FAILED);
        		//update Current Thead  = null de giai phong bo nho.
        		sectionTask.setCurrentThread(null);
        		// xoa co interrup cua currentThread
        		Thread.interrupted();
        	}
        
	}
	private Section doWork(){
		Section section  = null;
		try {
			//String mSections = "123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
			ParamOfSectionRunnable param  = sectionTask.getKey();
				List<Song> arrStr  = DBUtil.getSectionAnphabe(param.getDevice(),param.getCharFillter());
				section = new Section();
				section.setLsSong(arrStr);
			//section = fileUtil.getSection(context, userOption,share.getEndChar(),);
			//share.setEndchar(section.getEndChar());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return section;
	}
	

}
