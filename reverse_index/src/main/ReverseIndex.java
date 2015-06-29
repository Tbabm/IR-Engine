package main;

import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

public class ReverseIndex {
	private static int docNum;
	private static int elementNum;
	
	public static void main(String[] args) throws Exception{
		Scanner input = new Scanner(new File("item_docs.txt"));
		
		File sortedFile = new File("sorted_pairs.txt");
		if(sortedFile.exists()){
			if(sortedFile.delete())
				System.out.println("Create a new file for sorted result");
			else{
				System.out.println("Sorted file already exists");
				System.exit(0);
			}
		}
		PrintWriter sortedOutput = new PrintWriter(sortedFile);
		
		File riFile = new File("ri.index");
		if(riFile.exists()){
			if(riFile.delete())
				System.out.println("Create a new file for reverse index");
			else{
				System.out.println("RI file already exists");
			}
		}
		PrintWriter riOutput = new PrintWriter(riFile);
		
		docNum = input.nextInt();
		elementNum = input.nextInt();
		
		Pair[] itemDocPairs = new Pair[elementNum];
		for(int i=0;i<elementNum;i++){
			itemDocPairs[i] = new Pair(input.next(),input.nextInt());
		}
		Arrays.sort(itemDocPairs);
		
		//输出排序结果
		for(int i=0;i<elementNum;i++){
			sortedOutput.println(itemDocPairs[i]);
		}

		//构造倒排记录表
		LinkedList<PostingItem> riVector = getRiVector(itemDocPairs);
		
		//输出倒排记录表
		//use foreach to save time!
		for(PostingItem p : riVector){
			//传入docNum来计算idf
			p.calIdf(docNum);
			riOutput.println(p);
		}
		
		input.close();
		sortedOutput.close();	
		riOutput.close();
	}
	
	public static LinkedList<PostingItem> getRiVector(Pair[] itemDocPairs){
		LinkedList<PostingItem> riVector = new LinkedList<PostingItem>();
		
		//插入第一条记录
		PostingItem tempPosting = new PostingItem(itemDocPairs[0].getItem());
		riVector.add(tempPosting);
		Record tempRecord = new Record(itemDocPairs[0]);
		tempPosting.records.add(tempRecord);
		
		for(int i=1;i<elementNum;i++){
			if(tempPosting.getItem().equals(itemDocPairs[i].getItem())){
				//item相同
				
				//更新cf
				tempPosting.increaseCf();
				
				//更新postingItem的records
				if(tempRecord.getDocID() == itemDocPairs[i].getDocID()){
					//docID相同
					tempPosting.records.getLast().increaseTf();
				}
				else{ 
					tempRecord = new Record(itemDocPairs[i]);
					tempPosting.records.add(tempRecord);
				}
			}
			else{
				//item不同
				tempPosting = new PostingItem(itemDocPairs[i].getItem());
				riVector.add(tempPosting);
				tempRecord = new Record(itemDocPairs[i]);
				tempPosting.records.add(tempRecord);
			}
		}

		return riVector;
	}
	
}

class Pair implements Comparable<Object>{
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

class PostingItem{
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
	
	public void calIdf(int docNum){
		this.idf = Math.log10(docNum/getDf());;
	}
	
	public double getIdf() {
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
		temp.append(idf);
		temp.append(" ");
		temp.append(records.size());
		for(Record r:records){
			temp.append(" ");
			temp.append(r.getDocID());
			temp.append(" ");
			temp.append(r.getLogTf());
		}
		return temp.toString();
	}
	
}

class Record {
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