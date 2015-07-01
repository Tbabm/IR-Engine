package main;

import java.util.LinkedList;

public class PostingItem{
	private String item;
	private int cf;
	private double idf;
	public LinkedList<Record> records;
	
	public PostingItem(String item){
		this.item = item;
		this.cf = 0;
		this.idf = 0;
		this.records = new LinkedList<Record>();
	}
	
	public String getItem() {
		return item;
	}
	
	public void setItem(String item) {
		this.item = item;
	}
	
	public int getDf() {
		return records.size();
	}
	
	public void calIdf(){
		this.idf = Math.log10(ReverseIndex.docNum / records.size());;
	}
	
	public double getIdf() {
		//需要时计算
		this.calIdf();
		return idf; 
	}

	public int getCf() {
		return cf;
	}
	
	//每次扫描时调用该方法计算cf
	public void increaseCf() {
		this.cf += 1;
	}
	
	public String toString(){
		StringBuffer temp = new StringBuffer();
		temp.append(item);
		temp.append(" ");
		//调用get函数，保证idf被计算
		//temp.append(getIdf());
		//temp.append(" ");
		temp.append(records.size());
		for(Record r:records){
			temp.append(" ");
			temp.append(r.getDocID());
			temp.append(" ");
			//temp.append(r.getLogTf());
			temp.append(r.getTf());
		}
		return temp.toString();
	}
	
}