package com.example.moodifyer.pojo;

import java.util.Comparator;

import android.graphics.Bitmap;

public class MusicDetails{
	String artist, title, genre, mood, url, cover_url,valence,songid;
	Bitmap image;

	public String getSongid() {
		return songid;
	}

	public void setSongid(String songid) {
		this.songid = songid;
	}

	public String getValence() {
		return valence;
	}

	public void setValence(String valence) {
		this.valence = valence;
	}

	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getMood() {
		return mood;
	}

	public void setMood(String mood) {
		this.mood = mood;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCover_url() {
		return cover_url;
	}

	public void setCover_url(String cover_url) {
		this.cover_url = cover_url;
	}

//	public MusicDetails(String artist, String title, String genre, String mood,
//			String url, String cover_url, Bitmap image) {
//		super();
//		this.artist = artist;
//		this.title = title;
//		this.genre = genre;
//		this.mood = mood;
//		this.url = url;
//		this.cover_url = cover_url;
//		this.image = image;
//	}

	public MusicDetails(String artist, String title, String genre, String mood,
			String url, Bitmap image) {
		super();
		this.artist = artist;
		this.title = title;
		this.genre = genre;
		this.mood = mood;
		this.url = url;
		this.image = image;
	}

	public MusicDetails(String artist, String title, String genre, String mood,
			String url, String valence, String songid) {
		super();
		this.artist = artist;
		this.title = title;
		this.genre = genre;
		this.mood = mood;
		this.url = url;
		this.valence = valence;
		this.songid = songid;
		//this.image = image;
	}
	
	public MusicDetails(String artist, String title, String genre, String mood,
			String url) {
		super();
		this.artist = artist;
		this.title = title;
		this.genre = genre;
		this.mood = mood;
		this.url = url;
		
	}

	public MusicDetails(String artist, String title, String genre, String mood,
			String url, String valence) {
		super();
		this.artist = artist;
		this.title = title;
		this.genre = genre;
		this.mood = mood;
		this.url = url;
		this.valence = valence;
	}
	
	public MusicDetails(String artist, String title, String genre, String mood,
			String url, String valence, Bitmap image) {
		super();
		this.artist = artist;
		this.title = title;
		this.genre = genre;
		this.mood = mood;
		this.url = url;
		this.valence = valence;
		this.image = image;
	}
	
	public static Comparator<MusicDetails> MuCompare = new Comparator<MusicDetails>(){

		@Override
		public int compare(MusicDetails m1, MusicDetails m2) {
			String h = m1.getValence();
			String h2 = m2.getValence();
			return h.compareTo(h2);
		}};

}
