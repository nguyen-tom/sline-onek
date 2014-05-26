package com.sgroup.skara;

import java.io.UnsupportedEncodingException;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sgroup.skara.listener.LoadingDataListener;
import com.sgroup.skara.model.LinkSongs;
import com.sgroup.skara.model.Section;
import com.sgroup.skara.util.DBUtil;
import com.viewpagerindicator.TabPageIndicator;

public class SKaraActivity extends FragmentActivity implements LoadingDataListener{

	private static final String[] CONTENT = new String[] { "Danh sách", "Yêu thích"};
	public static final int ID_TAB_LIST = 0;
	public static final int ID_TAB_FAVORITE = 1;
	ViewPager pager;
	TabPageIndicator indicator;
	FragmentPagerAdapter adapter;
	LinearLayout loading;
	private SongFragment fragmentDanhSach;
	private SongFragment fragmentYeuThich;
    private LinkSongs linkSongs = new LinkSongs();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        try {
			DBUtil.initialize(this);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
        adapter = new MyPagerAdapterAdapter(getSupportFragmentManager());
        loading = (LinearLayout)findViewById(R.id.loading);
        pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(adapter);
       
        indicator = (TabPageIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(pager);
       
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        
        //loading.setVisibility(View.GONE);
	}
	@Override
	public void onResume(){
		super.onResume();
		
		 
	}
	public void showLoading(Boolean show){
		if (show){
			indicator.setVisibility(View.INVISIBLE);
			pager.setVisibility(View.INVISIBLE);
			loading.setVisibility(View.VISIBLE);
		}else{
			indicator.setVisibility(View.VISIBLE);
			pager.setVisibility(View.VISIBLE);
			loading.setVisibility(View.INVISIBLE);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	class MyPagerAdapterAdapter extends FragmentPagerAdapter {
	    public MyPagerAdapterAdapter(FragmentManager fm) {
	        super(fm);
	    }

	    @Override
	    public Fragment getItem(int position) {
	    	if(position == ID_TAB_LIST){
	    		SongFragment listSongFragment = new SongFragment();
	    		return listSongFragment;
	    	}
	    	FavoriteFragment listSongFragment = new FavoriteFragment();
    		return listSongFragment;
//	    	listSongFragment.setData(linkSongs);
//	    	listSongFragment.main = pThis;
//	    	listSongFragment.setTabId(position);
        	
	    }

	    @Override
	    public CharSequence getPageTitle(int position) {
	        return CONTENT[position % CONTENT.length].toUpperCase();
	    }

	    @Override
	    public int getCount() {
	      return CONTENT.length;
	    }
	    
	}
	public SongFragment getFrFavorite(){
		return fragmentYeuThich;
	}
	public SongFragment getFrSong(){
		return fragmentDanhSach;
	}
	public void setFrFavorite(SongFragment frg){
		this.fragmentYeuThich = frg;
	}
	public void setFrSong(SongFragment frg){
		this.fragmentDanhSach = frg;
	}
	@Override
	public void callBack(Section lkSong) {
		Toast.makeText(this, "UpdateDatabase Completed", Toast.LENGTH_SHORT).show();
		
	}
	@Override
	public void error(String message) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void loading(boolean show) {
		// TODO Auto-generated method stub
		
	}
	
}
