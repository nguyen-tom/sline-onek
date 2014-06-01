package com.sgroup.skara.thread;

import java.net.URL;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.sgroup.skara.R;
import com.sgroup.skara.model.Section;

public class ManagerSectionTask {

	private static final String TAG  = "ManagerSectionTask";
	
	    static final int LOADING_FAILED   = -1;
	    static final int LOADING_STARTED  = 1;
	    static final int LOADING_COMPLETE = 2;
	 
	 // Sets the size of the storage that's used to cache images
	    private static final int IMAGE_CACHE_SIZE = 1024 * 1024 * 4;

	    // Sets the amount of time an idle thread will wait for a task before terminating
	    private static final int KEEP_ALIVE_TIME = 1;

	    // Sets the Time Unit to seconds
	    private static final TimeUnit KEEP_ALIVE_TIME_UNIT;

	    // Sets the initial threadpool size to 8
	    private static final int CORE_POOL_SIZE = 8;

	    // Sets the maximum threadpool size to 8
	    private static final int MAXIMUM_POOL_SIZE = 8;

	    /**
	     * NOTE: This is the number of total available cores. On current versions of
	     * Android, with devices that use plug-and-play cores, this will return less
	     * than the total number of cores. The total number of cores is not
	     * available in current Android implementations.
	     */
	    private static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

	    /*
	     * Creates a cache of byte arrays indexed by image URLs. As new items are added to the
	     * cache, the oldest items are ejected and subject to garbage collection.
	     */
	    private final LruCache<ParamOfSectionRunnable, Section> mPhotoCache;

	    // A queue of Runnables for the image download pool
	    private final BlockingQueue<Runnable> mLoadingSectionWorkQueue;


	    // A queue of PhotoManager tasks. Tasks are handed to a ThreadPool.
	    private final Queue<SectionTask> mPhotoTaskWorkQueue;

	    // A managed pool of background download threads
	    private final ThreadPoolExecutor mDownloadThreadPool;


	    // An object that manages Messages in a Thread
	    private Handler mHandler;

	    // A single instance of PhotoManager, used to implement the singleton pattern
	    private static ManagerSectionTask sInstance = null;

	    // A static block that sets class fields
	    static {
	        
	        // The time unit for "keep alive" is in seconds
	        KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
	        
	        // Creates a single static instance of PhotoManager
	        sInstance = new ManagerSectionTask();
	    }
	    /**
	     * Constructs the work queues and thread pools used to download and decode images.
	     */
	    private ManagerSectionTask() {

	        /*
	         * Creates a work queue for the pool of Thread objects used for downloading, using a linked
	         * list queue that blocks when the queue is empty.
	         */
	        mLoadingSectionWorkQueue = new LinkedBlockingQueue<Runnable>();


	        /*
	         * Creates a work queue for the set of of task objects that control downloading and
	         * decoding, using a linked list queue that blocks when the queue is empty.
	         */
	        mPhotoTaskWorkQueue = new LinkedBlockingQueue<SectionTask>();

	        /*
	         * Creates a new pool of Thread objects for the download work queue
	         */
	        mDownloadThreadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE,
	                KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT, mLoadingSectionWorkQueue);

	        

	        // Instantiates a new cache based on the cache size estimate
	        mPhotoCache = new LruCache<ParamOfSectionRunnable, Section>(IMAGE_CACHE_SIZE) {

	            /*
	             * This overrides the default sizeOf() implementation to return the
	             * correct size of each cache entry.
	             */

//	            @Override
//	            protected int sizeOf(ParamOfSectionRunnable paramURL, Section paramArrayOfByte) {
//	                return paramArrayOfByte.;
//	            }
	        };
	        /*
	         * Instantiates a new anonymous Handler object and defines its
	         * handleMessage() method. The Handler *must* run on the UI thread, because it moves photo
	         * Bitmaps from the PhotoTask object to the View object.
	         * To force the Handler to run on the UI thread, it's defined as part of the PhotoManager
	         * constructor. The constructor is invoked when the class is first referenced, and that
	         * happens when the View invokes startDownload. Since the View runs on the UI Thread, so
	         * does the constructor and the Handler.
	         */
	        mHandler = new Handler(Looper.getMainLooper()) {

	            /*
	             * handleMessage() defines the operations to perform when the
	             * Handler receives a new Message to process.
	             */
	            @Override
	            public void handleMessage(Message inputMessage) {

	                // Gets the image task from the incoming Message object.
	                SectionTask photoTask = (SectionTask) inputMessage.obj;

	                // Sets an PhotoView that's a weak reference to the
	                // input ImageView
	                ParamOfSectionRunnable localView = photoTask.getKey();

	                // If this input view isn't null
	                if (localView != null) {

	                    /*
	                     * Gets the URL of the *weak reference* to the input
	                     * ImageView. The weak reference won't have changed, even if
	                     * the input ImageView has.
	                     */
//	                    ParamOfSectionRunnable localURL = localView.getLocation();

	                    /*
	                     * Compares the URL of the input ImageView to the URL of the
	                     * weak reference. Only updates the bitmap in the ImageView
	                     * if this particular Thread is supposed to be serving the
	                     * ImageView.
	                     */
//	                    if (photoTask.getImageURL() == localURL)

	                        /*
	                         * Chooses the action to take, based on the incoming message
	                         */
	                        switch (inputMessage.what) {

	                            // If the download has started, sets background color to dark green
	                            case LOADING_STARTED:
	                               // localView.setStatusResource(R.drawable.imagedownloading);
	                                break;

	                            /*
	                             * If the download is complete, but the decode is waiting, sets the
	                             * background color to golden yellow
	                             */
	                            case LOADING_COMPLETE:
	                                // Sets background color to golden yellow
	                            	localView.getSongFragment().callBack(photoTask.getSection());
	                            	Log.d(TAG,"loading Completed");
	                            	 recycleTask(photoTask);
		                                break;
	                            // If the decode has started, sets background color to orange
	                           
	                            
	                            case LOADING_FAILED:
	                                //localView.setStatusResource(R.drawable.imagedownloadfailed);
	                                
	                                // Attempts to re-use the Task object
	                                recycleTask(photoTask);
	                                break;
	                            default:
	                                // Otherwise, calls the super method
	                                super.handleMessage(inputMessage);
	                        }
	                }
	            }
	        };
	    }

	    /**
	     * Returns the PhotoManager object
	     * @return The global PhotoManager object
	     */
	    public static ManagerSectionTask getInstance() {

	        return sInstance;
	    }
	    
	    
	    
	    
	    
	
	public void handleState(SectionTask photoTask, int state) {
        switch (state) {
            
            case LOADING_COMPLETE:
            	 if (photoTask.isCacheEnabled()) {
                     // If the task is set to cache the results, put the buffer
                     // that was
                     // successfully decoded into the cache
                     mPhotoCache.put(photoTask.getKey(), photoTask.getSection());
                 }
                 
                 // Gets a Message object, stores the state in it, and sends it to the Handler
                 Message completeMessage = mHandler.obtainMessage(state, photoTask);
                 completeMessage.sendToTarget();
                 break;
            // In all other cases, pass along the message without any other action.
            default:
                mHandler.obtainMessage(state, photoTask).sendToTarget();
                break;
        }

    }

    /**
     * Cancels all Threads in the ThreadPool
     */
    public static void cancelAll() {

        /*
         * Creates an array of tasks that's the same size as the task work queue
         */
        SectionTask[] taskArray = new SectionTask[sInstance.mPhotoTaskWorkQueue.size()];

        // Populates the array with the task objects in the queue
        sInstance.mPhotoTaskWorkQueue.toArray(taskArray);

        // Stores the array length in order to iterate over the array
        int taskArraylen = taskArray.length;

        /*
         * Locks on the singleton to ensure that other processes aren't mutating Threads, then
         * iterates over the array of tasks and interrupts the task's current Thread.
         */
        synchronized (sInstance) {
            
            // Iterates over the array of tasks
            for (int taskArrayIndex = 0; taskArrayIndex < taskArraylen; taskArrayIndex++) {
                
                // Gets the task's current thread
                Thread thread = taskArray[taskArrayIndex].mThreadThis;
                
                // if the Thread exists, post an interrupt to it
                if (null != thread) {
                    thread.interrupt();
                }
            }
        }
    }

    /**
     * Stops a download Thread and removes it from the threadpool
     *
     * @param downloaderTask The download task associated with the Thread
     * @param pictureURL The URL being downloaded
     */
    static public void removeDownload(SectionTask downloaderTask, ParamOfSectionRunnable pictureURL) {

        // If the Thread object still exists and the download matches the specified URL
        if (downloaderTask != null && downloaderTask.getKey().equals(pictureURL)) {

            /*
             * Locks on this class to ensure that other processes aren't mutating Threads.
             */
            synchronized (sInstance) {
                
                // Gets the Thread that the downloader task is running on
                Thread thread = downloaderTask.getCurrentThread();

                // If the Thread exists, posts an interrupt to it
                if (null != thread)
                    thread.interrupt();
            }
            /*
             * Removes the download Runnable from the ThreadPool. This opens a Thread in the
             * ThreadPool's work queue, allowing a task in the queue to start.
             */
            sInstance.mDownloadThreadPool.remove(downloaderTask.getSectionRunnable());
        }
    }

    /**
     * Starts an image download and decode
     *
     * @param imageView The ImageView that will get the resulting Bitmap
     * @param cacheFlag Determines if caching should be used
     * @return The task instance that will handle the work
     */
    static public SectionTask startDownload(
            ParamOfSectionRunnable imageView,
            boolean cacheFlag) {

        /*
         * Gets a task from the pool of tasks, returning null if the pool is empty
         */
    	SectionTask downloadTask = sInstance.mPhotoTaskWorkQueue.poll();

        // If the queue was empty, create a new task instead.
        if (null == downloadTask) {
            downloadTask = new SectionTask();
        }

        // Initializes the task
        downloadTask.initializeDownloaderTask(ManagerSectionTask.sInstance, imageView, cacheFlag);
        
        /*
         * Provides the download task with the cache buffer corresponding to the URL to be
         * downloaded.
         */
        downloadTask.setSection(sInstance.mPhotoCache.get(downloadTask.getKey()));

        // If the byte buffer was empty, the image wasn't cached
        if (null == downloadTask.getSection()) {
            
            /*
             * "Executes" the tasks' download Runnable in order to download the image. If no
             * Threads are available in the thread pool, the Runnable waits in the queue.
             */
            sInstance.mDownloadThreadPool.execute(downloadTask.getSectionRunnable());

            // Sets the display to show that the image is queued for downloading and decoding.
// imageView.setStatusResource(R.drawable.imagequeued);
        
        // The image was cached, so no download is required.
        } else {
            
            /*
             * Signals that the download is "complete", because the byte array already contains the
             * undecoded image. The decoding starts.
             */
            
            sInstance.handleState(downloadTask, LOADING_COMPLETE);
        }

        // Returns a task object, either newly-created or one from the task pool
        return downloadTask;
    }

    /**
     * Recycles tasks by calling their internal recycle() method and then putting them back into
     * the task queue.
     * @param downloadTask The task to recycle
     */
    void recycleTask(SectionTask downloadTask) {
        
        // Frees up memory in the task
        downloadTask.recycle();
        
        // Puts the task object back into the queue for re-use.
        mPhotoTaskWorkQueue.offer(downloadTask);
    }
	

}
