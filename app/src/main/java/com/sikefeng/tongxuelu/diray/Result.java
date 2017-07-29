package com.sikefeng.tongxuelu.diray;

import java.util.List;

public class Result {
	private int result;
	private List<Book> booksData;

	public Result(int result, List<Book> booksData) {
		this.result = result;
		this.booksData = booksData;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public List<Book> getBooksData() {
		return booksData;
	}

	public void setBooksData(List<Book> booksData) {
		this.booksData = booksData;
	}
}
