package index;

public class Record {
	private int docID;
	private int tf;
	private double logTf;
	//private PostingItem postItem;
	
	public Record(int docID, int tf){//, PostingItem postinglist){
		this.docID = docID;
		this.tf = tf;
		this.calLogTf();
		//this.postItem = postinglist;
	}

	public Record(int tf){//, PostingItem postinglist){
		this.tf = tf;
		this.docID = -1; //haven't been set
		this.calLogTf();
		//this.postItem = postinglist;
	}
	
	public int getDocID() {
		return docID;
	}
	public void setDocID(int docID) {
		this.docID = docID;
	}

	public int getTf() {
		return tf;
	}
	public double getLogTf() {
		return logTf;
	}
	public void setTf(int tf) {
		this.tf = tf;
		this.calLogTf();
	}	
	private void calLogTf() {
		this.logTf = Math.log10(this.tf)+1;
	}
		
}