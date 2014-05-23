package com.sgroup.skara.adapter;

import java.util.List;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sgroup.skara.R;
import com.sgroup.skara.model.Song;

public class SongAdapter extends ArrayAdapter<Song> {
	
	Context context         = null;
	List<Song> myArray       = null;
	int layoutId;
	private LayoutInflater mInflater;

	public SongAdapter(Fragment listSongFragment,List<Song>arr){
		super(listSongFragment.getActivity(),
                R.layout.item_song,
                android.R.id.text1,
                arr);
		   this.myArray  = arr;
		   layoutId  = R.layout.item_song;
		   mInflater  = listSongFragment.getActivity().getLayoutInflater();
           
			this.context  = listSongFragment.getActivity();
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		
		SongHolder holder = null; 
		 View v = convertView;
		 if (v == null) {
			 v = mInflater.inflate(layoutId, null);
			 holder  = new SongHolder(v);
			 holder.txtdisplay  =  (TextView) v.findViewById(R.id.txtmaso);
			 holder.txtloi      =  (TextView) v.findViewById(R.id.txtloi);
			 holder.txtten      =  (TextView) v.findViewById(R.id.txtten);
			 holder.imgitem     =  (ImageView) v.findViewById(R.id.imgitem);
			 holder.imgitem.setImageResource(R.drawable.mic);
			 v.setTag(holder);
		 }else{
			 holder  = (SongHolder)v.getTag();
		 }
		 final Song song =myArray.get(position);
		 if(song != null){
			 holder.txtdisplay.setText(song.maso);
				holder.txtten.setText(song.ten);
				holder.txtloi.setText(song.loi);
		 }
		 holder.imgitem.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//Toast.makeText(context, ">>ADD FAVORITE SONG>>"+emp.ten, 100).show();
					//addFavoriteSong(emp);
				}
			});
		return v;
	}
	

	@Override
	public int getCount() {		
		return myArray.size() ;
	}

	@Override
	public Song getItem(int position) {		
		return myArray.get(position);
	}

	@Override
	public long getItemId(int position) {		
		return myArray.get(position).hashCode();
	}


	
//	public void addFavoriteSong(Song bh)
//	{
//		parent.AddFavoriteSong(bh);
//	}
}
	class SongHolder{
		View v;

		public SongHolder(View v) {
			this.v = v;
		}
        public View getView(){
        	return v;
        }
		
		
		ImageView imgitem;
		TextView txtdisplay;
		TextView txtten;
		TextView txtloi;
//		ImageView img1;
//		ImageView img2;
		
	}


