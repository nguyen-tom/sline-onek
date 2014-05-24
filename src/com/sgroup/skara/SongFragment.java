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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.sgroup.skara.adapter.SongAdapter;
import com.sgroup.skara.database.SharedPreferencesDB;
import com.sgroup.skara.listener.DBListener;
import com.sgroup.skara.listener.LoadingDataListener;
import com.sgroup.skara.model.Section;
import com.sgroup.skara.model.Song;
import com.sgroup.skara.model.UserOption;
import com.sgroup.skara.util.Constant;
import com.sgroup.skara.util.DBWorking;
import com.sgroup.skara.util.DataLoading;
import com.sgroup.skara.util.MultiSpinner;
import com.sgroup.skara.util.MultiSpinner.MultiSpinnerListener;

public class SongFragment extends Fragment  implements LoadingDataListener,DBListener{
    private static final String TAG  = "SongFragment";
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
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.layout_danhsach,container, false);
	    danhSachBaiHat  = new ArrayList<Song>();
		songAdapter     = new SongAdapter(this, danhSachBaiHat);
		dataloading     = new DataLoading(getActivity(), this);
		
		loadTabDanhSach(rootView);
		loadGroupFilter(rootView);
		Log.w("time", ">>LoadListView>>"+System.currentTimeMillis());
		
		//loadDataListView();
//		handler= new Handler();
//		r = new Runnable() {
//			
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				LoadDataListView();
//			}
//		};
//		handler.postDelayed(r, 500);
//		Log.w("time", ">>LoadListView Complete>>"+System.currentTimeMillis());
		
		return rootView;
	}
	
	@Override
	public void onActivityCreated(Bundle save){
		DBWorking dbWorking  = new DBWorking(this.getActivity(), this);
		 dbWorking.execute();
		super.onActivityCreated(save);
		db = new SharedPreferencesDB(this.getActivity());
		db.setEndchar(0);
		loadData();
		lvDanhSach.setAdapter(songAdapter);
		lvDanhSach.setFastScrollEnabled(true);
		((SKaraActivity)this.getActivity()).showLoading(false);
	}
	public void loadData(){
		SharedPreferencesDB db = new SharedPreferencesDB(this.getActivity());
		db.setDevice(device);
		db.setLanguage(language);
		userOption  = new UserOption();
		userOption.setDevice(device);
		userOption.setLanguage(language);
		dataloading  = new DataLoading(getActivity(), this);
		dataloading.execute(userOption);
		//lvDanhSach.setAdapter(songAdapter);
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//Toast.makeText(getActivity(), ">>>>>>>"+tabId, 100).show();
	}
	
	//@Override
	//public void onActivityCreated(Bundle savedInstanceState) {
	//	loadTabDanhSach();
	//};
	
	private Button bt_Arirang;
	private Button bt_California;
	private Button bt_MisicCore;
	private ListView lvDanhSach;
	private Spinner bt_searchOpt;
	private EditText searchText;
	private Button bt_Filter;
	
	
	String arrSearchOpt[]={"T??n b??i h??t", "L???i", "Nh???c s??", "Ca s??"};
	public void loadTabDanhSach(View rootView)
	{
		searchText =(EditText) rootView.findViewById(R.id.editTextSearch);
		searchText.clearFocus();
		searchText.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
					handler.removeCallbacks(r);
					Search(searchText.getText().toString());
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
						Search(searchText.getText().toString());
					}
				};
				handler.postDelayed(r, 1500);
			}
		});
		
		lvDanhSach = (ListView)rootView.findViewById(R.id.lvDanhSach);
		lvDanhSach.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				showDetail(songAdapter.getItem(arg2));
				
			}
		});

		bt_searchOpt= (Spinner) rootView.findViewById(R.id.button1);
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrSearchOpt);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		bt_searchOpt.setAdapter(adapter);
		bt_searchOpt.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				//((TextView)view).setText("");
				searchField=(int) id;
				String hint="Nh???p "+arrSearchOpt[(int) id];
				searchText.setHint(hint);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		
//		bt_Filter= (Button) rootView.findViewById(R.id.buttonFilter);
//		bt_Filter.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				showFilter();
//			}
//		});
		
		bt_Arirang= (Button) rootView.findViewById(R.id.bt_ariang);
		bt_Arirang.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				device = Constant.DEVICE_ARIRANG;
				 loadDataListView();
				//main.getFrFavorite().loadDataListView();
			}
		});
		bt_California= (Button) rootView.findViewById(R.id.bt_california);
		bt_California.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				device  = Constant.DEVICE_CALIFORNIA;
				loadDataListView();
			}
		});
		bt_MisicCore= (Button) rootView.findViewById(R.id.bt_music_core);
		bt_MisicCore.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v) {
				device  = Constant.DEVICE_MUSICCORE;
				loadDataListView();
				//main.getFrFavorite().loadDataListView();
			}
		});
		
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
	public void loadGroupFilter(View rootView)
	{
		groupFilter= (LinearLayout) rootView.findViewById(R.id.groupoption);
		groupFilter.setVisibility(visibleFilter);
		
		String arrLang[]={
				"Ti???ng Vi???t",
				"Ti???ng Anh"};
		
		languageOption= (Spinner) rootView.findViewById(R.id.spinner1);
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrLang);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		languageOption.setAdapter(adapter);
		languageOption.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
				device  = position;
				
				loadDataListView();
				//main.getFrSong().loadDataListView();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		
		volOption= (MultiSpinner) rootView.findViewById(R.id.spinner2);
		volOption.setItems(listVol, "T???t c??? Vol",new MultiSpinnerListener() {
			
			@Override
			public void onItemsSelected(boolean[] selected) {
				// TODO Auto-generated method stub
			}
		});
		
		kindOption= (MultiSpinner) rootView.findViewById(R.id.spinner3);
		kindOption.setItems(listKind, "T???t c??? th??? lo???i",new MultiSpinnerListener() {
			
			@Override
			public void onItemsSelected(boolean[] selected) {
				// TODO Auto-generated method stub
			}
		});
	}
	
	
	
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
	public void showDetail(Object bh)
	{
		Song song = (Song)bh;
		Intent intent = new Intent(getView().getContext(), SongDetail.class);
		intent.putExtra(DATA_MESSAGE, song.toString());
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
	}

	
	public Boolean Correct(String str, Song bh)
	{
		str=str.toLowerCase();
		bh.ten=bh.ten.toLowerCase();
		bh.loi=bh.loi.toLowerCase();
		bh.nhacsi=bh.nhacsi.toLowerCase();
		return (str.indexOf(bh.ten)>=0 || 
				bh.ten.indexOf(str)>=0 ||
				str.indexOf(bh.nhacsi)>=0 ||
				bh.nhacsi.indexOf(str)>=0 ||
				str.indexOf(bh.loi)>=0 ||
				bh.loi.indexOf(str)>=0
				);
	}
	
	public void Search1(String txt){
		LinkedList<Song> rs= new LinkedList<Song>();
		for (int i=0; i<danhSachBaiHat.size(); i++)
			if (Correct(txt, danhSachBaiHat.get(i)))
			{
				rs.add(danhSachBaiHat.get(i));
			}
		
		songAdapter = new SongAdapter(this, rs);
		lvDanhSach.setAdapter(songAdapter);
		
	}
	
	public void Search(String key) {
		LinkedList<Song> rsEx= new LinkedList<Song>();
		LinkedList<Song> rsCt= new LinkedList<Song>();
		int l=danhSachBaiHat.size();
		for (int i=0; i<l; i++)
		{
			Song bh = danhSachBaiHat.get(i);
			int rs = bh.Compare(key, new int[]{searchField});
			if (rs==Song.RESULF_FIND_EXACT)
			{
				rsEx.add(bh);
			}
			if (rs==Song.RESULF_FIND_CONTANT)
			{
				rsCt.add(bh);
			}
		}
		rsEx.addAll(rsCt);
		
		songAdapter = new SongAdapter(this, rsEx);
		lvDanhSach.setAdapter(songAdapter);
	}
	
//	public void addFavoriteSong(Song bh)
//	{
//		if (tabId== ID_TAB_DANHSACH)
//		  //fileUtil.
//		else
//			main.data.RemoveFavorite(bh);
//	}

	public int getTabId() {
		return tabId;
	}

@Override
public void callBack(Section lkSong) {
	Log.d(TAG,"Count List Result :" + lkSong.getLsSong().size());
	Log.d(TAG,"ITEM FIRST :" + lkSong.getLsSong().get(0));
	danhSachBaiHat.addAll(lkSong.getLsSong());
	songAdapter.notifyDataSetChanged();
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
	Toast.makeText(this.getActivity(), "LOADED DB", Toast.LENGTH_SHORT).show();
	
}

	
	
	
}
