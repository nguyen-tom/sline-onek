package com.sgroup.skara.thread;

import java.lang.ref.WeakReference;

import com.sgroup.skara.model.Section;
import com.sgroup.skara.thread.SectionRunnable.SectionRunableMethod;

public class SectionTask implements SectionRunableMethod {
	
	
	private Runnable mRunnable;
	
	private WeakReference<ParamOfSectionRunnable> mSectionWeakRef;
	private Section section;
	
	private ParamOfSectionRunnable param;
	private boolean mCacheEnabled;
	Thread mThreadThis;
	
    private Thread mCurrentThread;
    
    /*
     * An object that contains the ThreadPool singleton.
     */
    private static ManagerSectionTask sPhotoManager;
    public SectionTask() {
        // Create the runnables
    	mRunnable = new SectionRunnable(this);
        sPhotoManager = ManagerSectionTask.getInstance();
    }
    void initializeDownloaderTask(
    		ManagerSectionTask photoManager,
    		ParamOfSectionRunnable param,
            boolean cacheFlag)
    {
        // Sets this object's ThreadPool field to be the input argument
        sPhotoManager = photoManager;
        
        // Gets the URL for the View
        this.param = param;

        // Instantiates the weak reference to the incoming view
        mSectionWeakRef = new WeakReference<ParamOfSectionRunnable>(param);

        // Sets the cache flag to the input argument
        mCacheEnabled = cacheFlag;
        
    }

	@Override
	public Section getSection() {
		return section;
	}
    void recycle() {
        
        if ( null != mSectionWeakRef ) {
        	mSectionWeakRef.clear();
        	mSectionWeakRef = null;
        }
        
        section = null;
    }

	@Override
	public void setSection(Section section) {
		this.section  = section;
		
	}
	void handleState(int state) {
        sPhotoManager.handleState(this, state);
    }
	public Runnable getSectionRunnable(){
		return mRunnable;
	}
	boolean isCacheEnabled() {
        return mCacheEnabled;
    }
   
	 public Thread getCurrentThread() {
	        synchronized(sPhotoManager) {
	            return mCurrentThread;
	        }
	    }

	@Override
	public ParamOfSectionRunnable getKey() {
	        if ( null != mSectionWeakRef ) {
	            return mSectionWeakRef.get();
	        }
	        return null;
	    
	}
	public void setCurrentThread(Thread thread) {
        synchronized(sPhotoManager) {
            mCurrentThread = thread;
        }
    }



	@Override
	public void handlerStateLoading(int state) {
		 int outState;
	        
	        // Converts the download state to the overall state
	        switch(state) {
	            case SectionRunnable.STATE_COMPLETED:
	                outState = ManagerSectionTask.LOADING_COMPLETE;
	                break;
	            case SectionRunnable.STATE_FAILED:
	                outState = ManagerSectionTask.LOADING_FAILED;
	                break;
	            default:
	                outState = ManagerSectionTask.LOADING_STARTED;
	                break;
	        }
	        // Passes the state to the ThreadPool object.
	        handleState(outState);
		
	}

}
