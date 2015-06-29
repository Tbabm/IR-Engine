package main;

import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

//this version save idf & logtf in files
public class ReverseIndex {
	public static int docNum;
	public static int elementNum;
	
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