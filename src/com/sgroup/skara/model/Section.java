package com.sgroup.skara.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author VanTho
 *  Section like as Part of file.
 *  We will using that for read a part from file.
 *  Section have start position and end porsition.
 *  String data is all song in part.
 *  
 */
public class Section {
   private String path;  // path of file.
   private List<Song> lsSong; // data of section
   private int startChar; //start line in section
   private int endChar;
   private int numberLine; // end line in section
   
   /**
    * Constructor
    *  We create empty data when we create Instance Object.
    */
   public Section(){
	  this.startChar  = 0;
	  this.numberLine    = 0;
	  this.lsSong  = new ArrayList<Song>();
   }
   /**
    *  setup start/end into Constructor. Empty data.
    * @param start position of byte start
    * @param end   position of byte end
    */
   public Section(int startChar,int lines){
	   super();
		  this.startChar  = startChar;
		  this.numberLine    = lines;
   }
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public List<Song> getLsSong() {
		return lsSong;
	}
	public void setLsSong(List<Song> lsSong) {
		this.lsSong = lsSong;
	}
	public long getStartChar() {
		return startChar;
	}
	public void setStartChar(int startChar) {
		this.startChar = startChar;
	}
	public int getNumberLine() {
		return numberLine;
	}
	public void setNumberLine(int numberLine) {
		this.numberLine = numberLine;
	}
	public void addSong(Song song){
		 lsSong.add(song);
	}
	public int getEndChar(){
		return endChar;
	
	}
	public void setEndChar(int endchar){
		 this.endChar  = endchar;
	}
   
   
   

   
   
}
