package index;

import java.io.File;
import java.util.Collections;
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
		LinkedList<PostingList> lsts = VBCode.InputAndDecode();
		for(PostingList postlst: lsts){
			root.insert(postlst.getItem(), postlst);
		}
		
//		Scanner input = new Scanner(new File("ri.index"));
//		docNum = input.nextInt();//docNum:int
//		int cntLine = input.nextInt();
//		while(cntLine-- != 0)//foreach line
//		{
//			PostingList postlst = new PostingList(input.next());//itemName:String
//			int cnt = input.nextInt();//cnt:int
//			postlst.setDf(cnt);
//			while(cnt-- != 0)
//			{
//				Record tmp = new Record(input.nextInt(), input.nextInt());
//				postlst.records.add(tmp);//docID, tf : int
//				postlst.bool_records.add(tmp.getDocID());
//			}
//			Collections.sort(postlst.bool_records);
//			root.insert(postlst.getItem(),postlst);
//		}
//		input.close();
		root.printAll();
	}
}