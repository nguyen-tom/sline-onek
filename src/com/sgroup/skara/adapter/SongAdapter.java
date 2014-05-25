package com.sgroup.skara.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.sgroup.skara.R;
import com.sgroup.skara.model.Song;
import com.sgroup.skara.util.StringMatcher;

public class SongAdapter extends ArrayAdapter<Song>  implements SectionIndexer{
	
	Context context         = null;
	List<Song> myArray       = null;
	int layoutId;
	private LayoutInflater mInflater;
	HashMap<String, Integer> alphaIndexer;
	private String mSections = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";

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

	@Override
	public int getPositionForSection(int section) {
		for (int i = section; i >= 0; i--) {
			for (int j = 0; j < getCount(); j++) {
				if (i == 0) {
					// For numeric section
					for (int k = 0; k <= 9; k++) {
						if (StringMatcher.match(String.valueOf(getItem(j).ten.charAt(0)), String.valueOf(k)))
							return j;
					}
				} else {
					if (StringMatcher.match(String.valueOf(getItem(j).ten.charAt(0)), String.valueOf(mSections.charAt(i))))
						return j;
				}
			}
		}
		return 0;
	}

	@Override
	public int getSectionForPosition(int arg0) {
		 return 0;
	}

	@Override
	public Object[] getSections() {
		// TODO Auto-generated method stub
		String[] sections = new String[mSections.length()];
		for (int i = 0; i < mSections.length(); i++)
			sections[i] = String.valueOf(mSections.charAt(i));
		return sections;
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
		
		
		TextView txtdisplay;
		TextView txtten;
		TextView txtloi;
//		ImageView img1;
//		ImageView img2;
		
	}


