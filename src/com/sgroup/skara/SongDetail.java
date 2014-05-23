package com.sgroup.skara;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class SongDetail extends Activity {

	public TextView maso;
	public TextView ten;
	public TextView nhacsi;
	public TextView casi;
	public TextView loi;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_song_detail);
		Log.w("My trace", ">>>>>>>>>>>>>show>>>>>>>>>>>>>>");
		maso= (TextView)findViewById(R.id.textView1);
		ten= (TextView)findViewById(R.id.textView2);
		nhacsi= (TextView)findViewById(R.id.textView3);
		casi= (TextView)findViewById(R.id.textView4);
		loi= (TextView)findViewById(R.id.textView5);
		
		Intent intent = getIntent();
	    String message = intent.getStringExtra(SongFragment.DATA_MESSAGE);
	    Log.w("My trace", ">>>>>>>>>>>>>show>>>>>>>>>>>>>>"+message);
	    String[] info= message.split("#");
	    maso.setText(info[0]);
	    ten.setText(info[1]);
	    nhacsi.setText(info[2]);
	    casi.setText(info[3]);
	    loi.setText(info[4]);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.song_detail, menu);
		return true;
	}

}
