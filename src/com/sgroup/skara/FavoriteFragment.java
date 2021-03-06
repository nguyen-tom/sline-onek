package com.sgroup.skara;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.sgroup.skara.adapter.SongAdapter;
import com.sgroup.skara.component.IndexableListView;
import com.sgroup.skara.database.SharedPreferencesDB;
import com.sgroup.skara.listener.DBListener;
import com.sgroup.skara.listener.LoadingDataListener;
import com.sgroup.skara.model.Section;
import com.sgroup.skara.model.Song;
import com.sgroup.skara.model.UserOption;
import com.sgroup.skara.util.Constant;
import com.sgroup.skara.util.DBUtil;
import com.sgroup.skara.util.DBWorking;
import com.sgroup.skara.util.DataLoading;
import com.sgroup.skara.util.MultiSpinner;

public class FavoriteFragment extends Fragment  implements LoadingDataListener,DBListener, OnClickListener{
    private static final String TAG  = "SongFragment";
    public static final int PICK_UP_ITEM = 122;
    public static final  String POST_ITEM   = "item_post";
	private int device   =  Constant.DEVICE_ARIRANG;
	private int language =  Constant.VIETNAMESE;
	private int tabId;
	private HashMap<String,Integer> param  = new HashMap<String,Integer>();
	private int searchField = Song.ID_FIELD_TEN;
	public Handler handler;
	private DataLoading dataloading;
	public Runnable r;
	private SongAdapter songAdapter;
	String data;
	String[] ds;
	SharedPreferencesDB db ;
	private UserOption  userOption;
	List<Song> danhSachBaiHat;
	private int positionClicked;
	
	
	private Button bt_Arirang;
	private Button bt_California;
	private Button bt_MisicCore;
	private IndexableListView lvDanhSach;
	private Button bt_searchOpt;
	private EditText searchText;
	private Button bt_Filter;
	
	private ProgressBar prgBar;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
       super.onCreateView(inflater, container, savedInstanceState);
          View rootView = inflater.inflate(R.layout.layout_danhsach,container, false);
         initData();
		 initView(rootView);
		 loadTabDanhSach(rootView);
		  //loadGroupFilter(rootView);
		 Log.w("time", ">>LoadListView>>"+System.currentTimeMillis());
		return rootView;
	}
	private void initView(View rootView){
		
		lvDanhSach = (IndexableListView)rootView.findViewById(R.id.lvDanhSach);
		lvDanhSach.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				showDetail(songAdapter.getItem(arg2));
				positionClicked = arg2;
				Log.d("SONG FRAGMENT :", "POSITION :" + positionClicked );
				
			}
		});
		
		prgBar        = (ProgressBar) rootView.findViewById(R.id.progressBar1);
		searchText    =  (EditText) rootView.findViewById(R.id.editTextSearch);
		bt_searchOpt  = (Button) rootView.findViewById(R.id.button1);
		bt_California = (Button) rootView.findViewById(R.id.bt_california);
		bt_MisicCore  = (Button) rootView.findViewById(R.id.bt_music_core);
		bt_Arirang    = (Button) rootView.findViewById(R.id.bt_ariang);
		
		bt_California.setOnClickListener(this);
		bt_MisicCore.setOnClickListener(this);
		bt_Arirang.setOnClickListener(this);
		bt_Arirang.setSelected(true);
		searchText.clearFocus();
	}
	@Override
	public void onClick(View v) {
		selectButton(v.getId());
		if(v.getId()  == bt_Arirang.getId()){
			if(this.device != Constant.DEVICE_ARIRANG){
				this.device  = Constant.DEVICE_ARIRANG;
				 loadDataListView();
			}
			
		}else if(v.getId()  == bt_MisicCore.getId()){
			if(this.device != Constant.DEVICE_MUSICCORE){
				this.device  = Constant.DEVICE_MUSICCORE;
				 loadDataListView();
			}
			
		}else if(v.getId()  == bt_California.getId()){
			if(this.device != Constant.DEVICE_CALIFORNIA){
				this.device  = Constant.DEVICE_CALIFORNIA;
				loadDataListView();
			}
		}else{
			
		}
		
	}
	private void selectButton(int id){
		if(id  == bt_Arirang.getId()){
			bt_Arirang.setSelected(true);
			bt_California.setSelected(false);
			bt_MisicCore.setSelected(false);
		}else if(id  == bt_MisicCore.getId()){
			bt_Arirang.setSelected(false);
			bt_California.setSelected(false);
			bt_MisicCore.setSelected(true);
		}else if(id == bt_California.getId()){
			bt_Arirang.setSelected(false);
			bt_California.setSelected(true);
			bt_MisicCore.setSelected(false);
		}else{
			
		}
	}
	private void initData(){
		    danhSachBaiHat  = new ArrayList<Song>();
			dataloading     = new DataLoading(getActivity(), this);
	}
	
	@Override
	public void onActivityCreated(Bundle save){
		//DBWorking dbWorking  = new DBWorking(this.getActivity(), this);
		//dbWorking.execute();
		showLoading();
		super.onActivityCreated(save);
		db = new SharedPreferencesDB(this.getActivity());
		db.setEndchar(0);
		lvDanhSach.setFastScrollEnabled(true);
		((SKaraActivity)this.getActivity()).showLoading(false);
	}

	public void loadData(){
		showLoading();
		SharedPreferencesDB db = new SharedPreferencesDB(this.getActivity());
		db.setDevice(device);
		db.setLanguage(language);
		userOption  = new UserOption();
		userOption.setDevice(device);
		userOption.setLanguage(language);
		//dataloading  = new DataLoading(getActivity(), this);
		//dataloading.execute(userOption);
		//lvDanhSach.setAdapter(songAdapter);
	}
	private void showLoading(){
		prgBar.setVisibility(View.VISIBLE);
		lvDanhSach.setVisibility(View.GONE);
	}
	private void hideLoading(){
		prgBar.setVisibility(View.GONE);
		lvDanhSach.setVisibility(View.VISIBLE);
	}
	@Override
	public void onResume() {
		super.onResume();
	}
	
	
	
	String arrSearchOpt[]={"T??n b??i h??t", "L???i", "Nh???c s??", "Ca s??"};
	public void loadTabDanhSach(View rootView)
	{
		
		searchText.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
					handler.removeCallbacks(r);
					search(searchText.getText().toString());
                    return true;
				}
				return false;
			}
		});
		searchText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (handler!=null)
					handler.removeCallbacks(r);
				handler= new Handler();
				r = new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Log.w("search", ">>Timecal draw loading>>"+System.currentTimeMillis());
						search(searchText.getText().toString());
					}
				};
				handler.postDelayed(r, 1500);
			}
		});
		
		

		bt_searchOpt= (Button) rootView.findViewById(R.id.button1);
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrSearchOpt);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		bt_searchOpt.setAdapter(adapter);
//		bt_searchOpt.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//			@Override
//			public void onItemSelected(AdapterView<?> parent, View view,
//					int position, long id) {
//				// TODO Auto-generated method stub
//				//((TextView)view).setText("");
//				searchField=(int) id;
//				String hint="Nh???p "+arrSearchOpt[(int) id];
//				searchText.setHint(hint);
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> parent) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
		
//		bt_Filter= (Button) rootView.findViewById(R.id.buttonFilter);
//		bt_Filter.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				showFilter();
//			}
//		});
	}	
	
	public int visibleFilter=View.GONE;
	private LinearLayout groupFilter;
	public void showFilter()
	{
		if (visibleFilter==View.VISIBLE)
			visibleFilter=View.GONE;
		else
			visibleFilter=View.VISIBLE;
		groupFilter.setVisibility(visibleFilter);
	}

	
	private Spinner languageOption;
	private MultiSpinner volOption;
	private MultiSpinner kindOption;
	List<String> listKind = new ArrayList<String>(Arrays.asList("T??n nh???c","C??? nh???c","Thi???u nhi","Remix"));
	List<String> listVol = new ArrayList<String>(Arrays.asList("Vol 1", "Vol 2", "Vol 3", "Vol 4", "Vol 5", "Vol 6", "Vol 7", "Vol 8", "Vol 9", "Vol 10",
				"Vol 11", "Vol 12", "Vol 13", "Vol 14", "Vol 15", "Vol 16", "Vol 17", "Vol 18", "Vol 19", "Vol 20"));
	public void loadDataListView() {
		 ((SKaraActivity)this.getActivity()).showLoading(false);
		   danhSachBaiHat.clear();
		   songAdapter = new SongAdapter(this, danhSachBaiHat);
		   lvDanhSach.setAdapter(songAdapter);
		
		 new SharedPreferencesDB(getActivity()).setEndchar(0);
		 loadData();
	}
	
	public void reLoadListSong(LinkedList<Song> ds){
		danhSachBaiHat = ds;
		songAdapter = new SongAdapter(this,danhSachBaiHat);
		lvDanhSach.setAdapter(songAdapter);
	}
	public static String DATA_MESSAGE= "Songdata";
	public void showDetail(Object bh){
		Song song = (Song)bh;
		Intent intent = new Intent(getView().getContext(), SongDetail.class);
		intent.putExtra(DATA_MESSAGE, song.toString());
		startActivityForResult(intent, PICK_UP_ITEM);
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    // Check which request it is that we're responding to
	    if (requestCode == PICK_UP_ITEM && data != null) {
	    	   String favorite = data.getStringExtra("favorite");
	    	   if(danhSachBaiHat != null && danhSachBaiHat.size() > 0){
	    		   Log.d("SONG FRAGMENT :", "Result :" + positionClicked  + " favorite :" + favorite);
	    		    danhSachBaiHat.get(positionClicked).setFavorite(favorite);
		    		songAdapter.notifyDataSetChanged();
	    	   }
	    }

	 }
	public Boolean Correct(String str, Song bh)
	{
		str=str.toLowerCase();
		bh.setName(bh.getName().toLowerCase());
		bh.setLyric(bh.getLyric().toLowerCase());
		bh.setAuthor(bh.getAuthor().toLowerCase());
		return (str.indexOf(bh.getName())>=0 || 
				bh.getName().indexOf(str)>=0 ||
				str.indexOf(bh.getAuthor())>=0 ||
				bh.getAuthor().indexOf(str)>=0 ||
				str.indexOf(bh.getLyric())>=0 ||
				bh.getLyric().indexOf(str)>=0
				);
	}
	public void search(String key) {
		SharedPreferencesDB db  = new SharedPreferencesDB(this.getActivity());
		List<Song> lsSong  = DBUtil.searchAriangList(UserOption.LIST_DEVICE[db.getDevice()],key);
		if(lsSong != null){
			danhSachBaiHat.clear();
			danhSachBaiHat.addAll(lsSong);
			if(songAdapter!= null )songAdapter.notifyDataSetChanged();
		}else{
			Toast.makeText(getActivity(), "FIND NO RECORD" , Toast.LENGTH_SHORT).show();
		}
	}

	public int getTabId() {
		return tabId;
	}

@Override
public void callBack(Section lkSong) {
    hideLoading();
    if(lkSong != null && lkSong.getLsSong().size() > 0){
    	Log.d(TAG,"Count List Result :" + lkSong.getLsSong().size());
    	Log.d(TAG,"ITEM FIRST :" + lkSong.getLsSong().get(0));
    	danhSachBaiHat.addAll(lkSong.getLsSong());
    	songAdapter     = new SongAdapter(this, danhSachBaiHat);
    	lvDanhSach.setAdapter(songAdapter);
    }else{
    	Toast.makeText(getActivity(), "No Data", Toast.LENGTH_SHORT).show();
    }
	
}

@Override
public void error(String message) {
	// TODO Auto-generated method stub
	
}

@Override
public void loading(boolean show) {
	// TODO Auto-generated method stub
	
}

@Override
public void loadedDB() {
	loadData();
	Toast.makeText(this.getActivity(), "LOADED DB", Toast.LENGTH_SHORT).show();
	
}


	
	
	
}
