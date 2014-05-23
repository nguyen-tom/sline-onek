package com.sgroup.skara.util;
//package com.sgroup.akara.util;
//
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.Arrays;
//import java.util.LinkedList;
//
//import android.content.Context;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.sgroup.akara.AKaraActivity;
//import com.sgroup.akara.model.LinkSongs;
//import com.sgroup.akara.model.Song;
//
//public class DataProvider {
//   
//	private Context context;
//	private  AKaraActivity main;
//	
//	private LinkedList<Song>   listSong;
//	private LinkedList<String> listFavoriteName;
//	private LinkedList<Song>   listFavoriteSong;
//	
//	public int device = -1;
//	public int language = -1;
//	
//	public DataProvider(Context ctx)
//	{
//		context=ctx;
//	}
//	public void readDataFirst() {
//		
//		readUserOption();
//		int dv=device;
//		int lg=language;
//		device=-1;
//		language=-1;
//		readData(dv, lg);
//	}
//	
//	public void ReadData(int device, int language)
//	{
//		if (device==this.device && language== this.language) return;
//		listFavoriteSong=new LinkedList<Song>();
//		if (listFavoriteName == null) listFavoriteName = GetListSongFvr();
//		listSong= getListSong(device, language);
//		this.device=device;
//		this.language=language;
//		StoreUserOption();
//	}
//	
//	public void changeLanguage(int language)
//	{
//		if (language== this.language) return;
//		listFavoriteSong = new LinkedList<Song>();
//		if (listFavoriteName == null) listFavoriteName = GetListSongFvr();
//		listSong= getListSong(device, language);
//		this.language=language;
//		StoreUserOption();
//	}
//	public void ChangeDevice(int device)
//	{
//		if (device== this.device)
//			return;
//		listFavoriteSong=new LinkedList<Song>();
//		if (listFavoriteName == null)
//			listFavoriteName = GetListSongFvr();
//		listSong= getListSong(device, language);
//		this.device=device;
//		StoreUserOption();
//	}
//	
//	
//	
//
//	
//	
//	public void AddFavorite(Song bh)
//	{
//		if (listFavoriteName.contains(bh.ten))
//		{
//			Toast.makeText(main, "B??i h??t "+bh.ten+" ???? c?? trong danh s??ch y??u th??ch", 100).show();
//			return;
//		}
//		listFavoriteName.add(bh.ten);
//		listFavoriteSong.add(bh);
//		//main.fragmentYeuThich.reLoadListSong(listFavoriteSong);
//		
//		FileOutputStream outputStream=null;
//		try {
//			outputStream = context.openFileOutput(Constant.DATA_FAVORITE_FILE, Context.MODE_PRIVATE);
//		} catch (FileNotFoundException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		
//		String temp="";
//		for (int i=0; i<listFavoriteName.size(); i++)
//			temp=temp+listFavoriteName.get(i)+"\r\n";
//		try {
//			outputStream.write(temp.getBytes());
//			outputStream.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}	
//		
//		Toast.makeText(main, "???? th??m b??i h??t "+bh.ten+" v??o danh s??ch y??u th??ch", 100).show();
//	}
//	
//	public void RemoveFavorite(Song bh)
//	{
//		listFavoriteName.remove(bh.ten);
//		listFavoriteSong.remove(bh);
//		main.fragmentYeuThich.reLoadListSong(listFavoriteSong);
//		
//		FileOutputStream outputStream=null;
//		try {
//			outputStream = context.openFileOutput(DATA_FAVORITE_FILE, Context.MODE_PRIVATE);
//		} catch (FileNotFoundException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		
//		String temp="";
//		for (int i=0; i<listFavoriteName.size(); i++)
//			temp=temp+listFavoriteName.get(i)+"\r\n";
//		try {
//			outputStream.write(temp.getBytes());
//			outputStream.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}	
//		
//		Toast.makeText(main, "???? x??a b??i h??t "+bh.ten+" kh???i danh s??ch y??u th??ch", 100).show();
//	}
//
//	
//	
//	
//	
//	
//	
//	
//	
//}
