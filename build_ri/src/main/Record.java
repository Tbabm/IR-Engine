package main;

public class Record {
	private int docID;
	private int tf;
	private double logTf;
	
	public Record(Pair pair){
		this.docID = pair.getDocID();
		this.tf = 1;
		this.logTf = 0;
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

	public void increaseTf() {
		this.tf+=1;
	}

	public double getLogTf() {
		//在需要的时候计算
		this.calLogTf();
		return logTf;
	}

	public void calLogTf() {
		this.logTf = Math.log10(this.tf)+1;
	}
}
