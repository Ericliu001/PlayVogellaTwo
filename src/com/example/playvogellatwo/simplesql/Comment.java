package com.example.playvogellatwo.simplesql;

public class Comment {
	private long id;
	private String comment;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return comment;
	}
}
