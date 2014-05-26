package com.sgroup.skara;

import com.sgroup.skara.database.SharedPreferencesDB;
import com.sgroup.skara.model.Song;
import com.sgroup.skara.model.UserOption;
import com.sgroup.skara.util.DBUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SongDetail extends Activity implements OnClickListener{

	private TextView tvCode;
	private TextView tvName;
	private TextView tvAuthor;
	private TextView tvSinger;
	private TextView tvLyric;
	private TextView tvStyle;
	private String message;
	private int device;
	private Button btFavorite;
	private Song song;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_song_detail);
		tvCode= (TextView)findViewById(R.id.tv_code);
		tvName= (TextView)findViewById(R.id.tv_name);
		tvAuthor= (TextView)findViewById(R.id.tv_author);
		tvSinger= (TextView)findViewById(R.id.tv_singer);
		tvLyric= (TextView)findViewById(R.id.tv_lyric);
		tvStyle = (TextView)findViewById(R.id.tv_type);
		btFavorite = (Button)findViewById(R.id.btFavorite);
		btFavorite.setOnClickListener(this);
		Intent intent = getIntent();
	    message = intent.getStringExtra(SongFragment.DATA_MESSAGE);
	    
	    
	}
	public void onResume(){
		super.onResume();
		SharedPreferencesDB dbShare  = new SharedPreferencesDB(this);
		device  = dbShare.getDevice();
		song  = createSong(message);
		if(song != null) inputDataToView(song);
		
	}
	private Song createSong(String message){
		Song  song = null;
		if(message != null && message.length() > 0 && message.contains("#")){
			String content[] = message.split("#");
			song  = new Song();
			    song.setCode(content[0]);
			    song.setName(content[1]);
			    song.setAuthor(content[2]);
			    song.setSinger(content[3]);
			    song.setType(content[4]);
			    song.setLyric(content[5]);
			return song;
		}
		return song;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.song_detail, menu);
		return true;
	}
	private void inputDataToView(Song a){
		
			 tvCode.setText(song.getCode());
			 tvName.setText(song.getName());
			 String format  = this.getResources().getString(R.string.detail_author);
			 format  = String.format(format,song.getAuthor());
			 tvAuthor.setText(format);
			  format  = this.getResources().getString(R.string.detail_singer);
			 format  = String.format(format,song.getSinger());
			 tvSinger.setText(format);
			 format  = this.getResources().getString(R.string.detail_style);
			 format  = String.format(format,song.getType());
			 tvStyle.setText(format);
			 format  = song.getLyric();
			 tvLyric.setText(format);
			 if(a.getFavorite().equalsIgnoreCase("1")){
				 btFavorite.setText("Liked");
			 }else{
				 btFavorite.setText("UnLike");
			 }
		

	}
  private void pressedLike(Song song){
	  if(song != null){
		  String favorite  = "1";
		  if(song.getFavorite().equalsIgnoreCase("1")) favorite = "0" ;
		  boolean isupdated = DBUtil.updateFavorite(song.getCode(),favorite, UserOption.LIST_DEVICE[device]);
		  btFavorite.setText("UnLike");
		  if(isupdated && favorite.equalsIgnoreCase("1")){
			  btFavorite.setText("Liked");
			  song.setFavorite(favorite);
		  }
	  }
	  
	  
  }
@Override
public void onClick(View v) {
	if(v.getId() == btFavorite.getId()){
		pressedLike(song);
	}
	
}
 @Override
 public void onBackPressed(){
	  Intent t  = new Intent();
	  t.putExtra("favorite", song.getFavorite());
	  this.setResult(SongFragment.PICK_UP_ITEM, t);
	  this.finish();
 }

}
