package com.doublechen.androidtest.database.entity;

import com.doublechen.androidtest.database.Entity;
import com.doublechen.androidtest.database.annotation.Unique;

public class Staff extends Entity {
	@Unique
	public int id;
	public String name;
	public byte sex; // 0:male; 1:female
	public String address;

}