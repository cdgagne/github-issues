package com.navcanada.github;


public class Issue {
	public Integer number;
	public String title;

	public String toString() {
		return this.number + ": " + this.title;
	}
}