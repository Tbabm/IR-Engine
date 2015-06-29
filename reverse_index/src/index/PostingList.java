package index;

import java.util.LinkedList;

public class PostingList{
	private String item;
	private double idf;
	public LinkedList<Record> records;
	
	public PostingList(String item){
		this.item = item;
		this.idf = 0;
		this.records = new LinkedList<Record>();
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
		this.idf = Math.log10(ReverseIndex.docNum / df);
	}
	
	public String toString(){
		StringBuffer temp = new StringBuffer();
		temp.append(item);
		temp.append(" ");
		temp.append(idf);
		temp.append(" ");
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