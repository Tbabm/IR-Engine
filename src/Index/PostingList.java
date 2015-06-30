package index;

import java.util.LinkedList;

public class PostingList{
	private String item;
	private double idf;
	public LinkedList<Record> records;
	public LinkedList<Integer> bool_records;
	
	public PostingList(String item){
		this.item = item;
		this.idf = 0;
		this.records = new LinkedList<Record>();
		this.bool_records = new LinkedList<Integer>();
	}
	
	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}
	
	public double getIdf() {
		return idf; 
	}
	public void setDf(int df) {
		this.idf = Math.log10(ReverseIndex.docNum / (double) df);
	}
	
}