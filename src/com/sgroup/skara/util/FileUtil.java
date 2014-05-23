package com.sgroup.skara.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.sgroup.skara.SKaraActivity;
import com.sgroup.skara.model.LinkSongs;
import com.sgroup.skara.model.Section;
import com.sgroup.skara.model.Song;
import com.sgroup.skara.model.UserOption;

@SuppressLint("NewApi")
public class FileUtil {
   private static final String TAG  = "FileUtil";
   /**
    *  convert inputStream from Asset to File SDCard.
    *  
    * @param input
    * @param path
    * @throws Exception
    */
   public void saveInputStreamToFile(InputStream input,String path) throws Exception{
	   if(input == null || input.available() <= 0) throw new Exception();
	   if(StrUtil.isNullString(path)) throw new Exception();
	   OutputStream outputStream  = null;
	 try{
		 outputStream  = new FileOutputStream (new File(path));
		   int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = input.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
	 }catch(Exception e){
		 throw e;
	 }finally{
		 if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (outputStream != null) {
				try {
					outputStream.flush();
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
	 
			}
	 }
	   
	   
   }
   public boolean checkFileExist(String path){
	   File f  = new File( path);
	   if(f.exists() && f.canRead() && f.isFile()){
		   return true;
	   }
	   return false;
   }
   
   
   public Section  getSection(Context context,
		                                      UserOption userOption,
		                                      int startChar,
		                                      int numberLine) throws Exception{
	   
	   Section section  = new Section();
	   if(userOption == null ) throw new Exception();
	   if(StrUtil.isNullString(String.valueOf(userOption.getDevice()))) throw new Exception();
	   if(StrUtil.isNullString(String.valueOf(userOption.getLanguage()))) throw new Exception();
	   String path  = StrUtil.buildPath(userOption);
	   if(numberLine <  0 ||  startChar < 0  ) throw new Exception();
		try {
			   InputStream input = context.getAssets().open(path);
			   Reader reader = new InputStreamReader(input, "UTF-8");
			   LineNumberReader buff = new LineNumberReader(reader); 
		       Log.d(TAG,"Line start:" + startChar);
		       buff.skip(startChar);
			   int  totalCharReaded = startChar;
		       int line  = 0; 
		        while(line < numberLine){
		           String lineStr = buff.readLine(); 
		           line ++;
		           Song song  = new Song(lineStr);
		           totalCharReaded += lineStr.length() + 1; // 1byte for code 13
                   section.addSong(song);
		       
		        }
		        int numberLineEnd = buff.getLineNumber();
		        Log.d(TAG,"Line End:" + numberLineEnd);
		       // Log.d(TAG,"First item in list :" + section.getLsSong().get(0));
		        section.setEndChar(totalCharReaded);
		        section.setNumberLine(numberLine);
	   }catch(Exception ioe){
		   throw ioe;
	   }finally{
		   
	   }
	   return section;
   }
   public Section  getSectionPart(Context context,UserOption userOption,int start,int numberLines ) throws Exception {
	   Section section  = new Section();
     	if(userOption == null ) throw new Exception();
	   if(StrUtil.isNullString(String.valueOf(userOption.getDevice()))) throw new Exception();
	   if(StrUtil.isNullString(String.valueOf(userOption.getLanguage()))) throw new Exception();
	   String path  = StrUtil.buildPath(userOption);
	   BufferedInputStream is = null;
	    try {
	    	InputStream input = context.getAssets().open(path);
			   is = new BufferedInputStream(input);
	    	int totalCharReaded  = start;
	        byte[] c = new byte[1024];
	        int count = 0;
	        int readChars = 0;
	        boolean empty = true;
	        byte byteArr[] = null ;
	        is.skip(start);
	        while ((readChars = is.read(c)) != -1 && count < numberLines) {
	        	
	            empty = false;
	            int i  =0;
	            for (; i < readChars; ++i) {
	                if (c[i] == '\n') {
	                    ++count;
	                    byteArr  = new byte[i];
	                    break;
	                }
	            }
	            byteArr  = Arrays.copyOf(byteArr, byteArr.length);
	            String line  = new String(byteArr); 
	            Log.d(TAG,"Line :[" + count +"] [" + line);
	            Song song  = new Song(line);
	            section.addSong(song);
	            totalCharReaded += byteArr.length; 
	        }
	        section.setEndChar(totalCharReaded);
	        section.setNumberLine(count);
	        
	    } finally {
	       if(is != null)  is.close();
	    }
	    return section;
	}
   

   public List<String> getListSongFvr(Context context)
	{
   	String data;
   	String[] ds={};
		try{
			String filePath = Constant.DATA_FAVORITE_FILE;
			InputStream inputStream = context.openFileInput(filePath);
			
			int sizeOfFile = inputStream.available();
	        Log.w("My trace", ">>>buffer size>>>" + Integer.toString(sizeOfFile));
	        byte[] bytes = new byte[sizeOfFile];
	        inputStream.read(bytes);
	        
	        data= new String(bytes);
	        ds = data.split("\r\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<String> rs= new ArrayList<String>();
		if (ds.length>0)
			rs = new ArrayList<String>(Arrays.asList(ds));
       return rs;
	}
   
   
   
   public UserOption loadOptions(Context context){
		    UserOption userOption  = new UserOption();
			int device    = userOption.getDevice();
			int language  = userOption.getLanguage();
			try{
				String filePath = Constant.USER_OPTION_FILE;
				InputStream inputStream = context.openFileInput(filePath);
				int sizeOfFile = inputStream.available();
		        
		        byte[] bytes = new byte[sizeOfFile];
		        inputStream.read(bytes);
		        String data= new String(bytes);
		        String[] ds = data.split("#");
		        
		        if (ds.length > 1){
		        	String log  = String.format("device[%s] language[%s]",ds[0],ds[1]);
		        	Log.i(TAG, "read UserOption : " + log);
			        device    = Integer.parseInt(ds[0]);
			        language = Integer.parseInt(ds[1]);
		        }
			} catch (IOException e) {
				e.printStackTrace();
			}
			userOption.setDevice(device);
			userOption.setLanguage(language);
			
			return userOption;
			
		}
  
   public boolean clearAllFavoriteSong(Context context){
		FileOutputStream outputStream = null;
		try {
			outputStream = context.openFileOutput(Constant.DATA_FAVORITE_FILE, Context.MODE_PRIVATE);
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
   public void addFavorite(Context context,LinkSongs linkSongs,Song bh)
	{
		if (linkSongs.getLkFavorite().contains(bh.ten)){
			Toast.makeText(context, "B??i h??t "+bh.ten+" ???? c?? trong danh s??ch y??u th??ch", 100).show();
			return;
		}
		linkSongs.getLkFavorite().add(bh);
		linkSongs.getListFavoriteName().add(bh.ten);
		
		//main.fragmentYeuThich.reLoadListSong(listFavoriteSong);
		
		FileOutputStream outputStream=null;
		try {
			outputStream = context.openFileOutput(Constant.DATA_FAVORITE_FILE, Context.MODE_PRIVATE);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String temp="";
		for (int i=0; i<linkSongs.getListFavoriteName().size(); i++)
			temp=temp+linkSongs.getListFavoriteName().get(i)+"\r\n";
		try {
			outputStream.write(temp.getBytes());
			outputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		Toast.makeText(context, "???? th??m b??i h??t "+bh.ten+" v??o danh s??ch y??u th??ch", 100).show();
	}
   public void RemoveFavorite(SKaraActivity context,LinkSongs linkSongs,Song bh){
	   
	    linkSongs.getLkFavorite().remove(bh);
	    linkSongs.getListFavoriteName().remove(bh.ten);
		
		context.getFrFavorite().reLoadListSong(linkSongs.getLkFavorite());
		
		FileOutputStream outputStream=null;
		try {
			outputStream = context.openFileOutput(Constant.DATA_FAVORITE_FILE, Context.MODE_PRIVATE);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		String temp="";
		for (int i=0; i<linkSongs.getListFavoriteName().size(); i++)
			temp=temp+linkSongs.getListFavoriteName().get(i)+"\r\n";
		try {
			outputStream.write(temp.getBytes());
			outputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		Toast.makeText(context, "???? x??a b??i h??t "+bh.ten+" kh???i danh s??ch y??u th??ch", 100).show();
	}
   
    public static int countLines(UserOption userOption ) throws Exception {
    	if(userOption == null ) throw new Exception();
 	   if(StrUtil.isNullString(String.valueOf(userOption.getDevice()))) throw new Exception();
 	   if(StrUtil.isNullString(String.valueOf(userOption.getLanguage()))) throw new Exception();
 	   String path  = StrUtil.buildPath(userOption);
	    InputStream is = new BufferedInputStream(new FileInputStream(path));
	    try {
	        byte[] c = new byte[1024];
	        int count = 0;
	        int readChars = 0;
	        boolean empty = true;
	        while ((readChars = is.read(c)) != -1) {
	            empty = false;
	            for (int i = 0; i < readChars; ++i) {
	                if (c[i] == '\n') {
	                    ++count;
	                }
	            }
	        }
	        return (count == 0 && !empty) ? 1 : count;
	    } finally {
	        is.close();
	    }
	}
	
}
