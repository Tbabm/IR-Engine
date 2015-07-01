package main;

public class Pair implements Comparable<Object>{
	private String item;
	private int docID;
	
	public Pair(String item,int docID){
		this.item = item;
		this.docID = docID;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public int getDocID() {
		return docID;
	}

	public void setDocID(int docID) {
		this.docID = docID;
	}	
	
	//for debug
	public String toString(){
		String temp = this.item+" "+this.docID;
		return temp;
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		Pair temp = (Pair)o;
		int itemResult = this.item.compareTo(temp.getItem());
		if(itemResult > 0){
			return 1;
		}
		else if(itemResult == 0){
			return this.docID-temp.docID;
		}
		else{
			return -1;
		}
	}
}