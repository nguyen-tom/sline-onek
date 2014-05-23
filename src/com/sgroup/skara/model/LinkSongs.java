package com.sgroup.skara.model;

import java.util.LinkedList;
import java.util.List;

public class LinkSongs {
  private List<Song> lkSong;
  private LinkedList<Song> lkFavorite;
  private List<String> listFavoriteName;
  
  public LinkSongs(){
	  lkFavorite  = new LinkedList<Song> ();
	  lkSong  = new LinkedList<Song> ();
	  listFavoriteName = null;
  }
  public LinkSongs(LinkedList<Song> listSongs){
	  this.lkSong = listSongs;
  }
public List<Song> getLkSong() {
	return lkSong;
}

public void setLkSong(LinkedList<Song> lkSong) {
	this.lkSong = lkSong;
}
public LinkedList<Song> getLkFavorite() {
	return lkFavorite;
}
public void setLkFavorite(LinkedList<Song> lkFavorite) {
	this.lkFavorite = lkFavorite;
}
public List<String> getListFavoriteName() {
	return listFavoriteName;
}
public void setListFavoriteName(List<String> listFavoriteName) {
	this.listFavoriteName = listFavoriteName;
}

  
  
  
}
