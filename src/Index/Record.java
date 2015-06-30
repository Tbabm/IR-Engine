package Index;

public class Record {
	private int docID;
	private int tf;
	private double logTf;
	
	public Record(int docID, int tf){
		this.docID = docID;
		this.tf = tf;
		this.calLogTf();
	}

	public Record(int tf){
		this.tf = tf;
		this.docID = -1; //haven't been set
		this.calLogTf();
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