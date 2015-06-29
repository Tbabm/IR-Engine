package index;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

//this version save idf & logtf in files
public class ReverseIndex {
	
	public static int docNum;
	private static BPlusTree<String, PostingList> root;	
	
	public static PostingList getPostingLinks(String item)	{
		return root.get(item);
	}
	
	public static void read() throws Exception{
		if(root==null){
			root = new BPlusTree<String, PostingList>();
		}
		Scanner input = new Scanner(new File("ri.index"));
		docNum = input.nextInt();//docNum:int
		while(input.hasNextLine())//foreach line
		{
			PostingList postlst = new PostingList(input.next());//itemName:String
			int cnt = input.nextInt();//cnt:int
			postlst.setDf(cnt);
			while(cnt-- != 0)
			{
				postlst.records.add(new Record(input.nextInt(), input.nextInt()));//docID, tf : int
			}
			root.insert(postlst.getItem(),postlst);
		}
		input.close();
	}
	

	
}