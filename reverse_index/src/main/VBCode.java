package main;

import index.PostingList;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Scanner;

public class VBCode {
	
	public static String indexFileName = "vb_ri.index";
	public static String docFileName = "vb_doc.txt";
	//压缩之后直接以字符流输出到文件中
	public static void EncodeAndOutput(LinkedList<PostingItem> riVector) throws IOException{		
		DataOutputStream indexOutput = new DataOutputStream(new BufferedOutputStream(
													new FileOutputStream(indexFileName)));
		
		File docFile = new File(docFileName);
		if(docFile.exists()){
			if(docFile.delete())
				System.out.println("Create a new file for vb doc");
			else{
				System.out.println("Doc file already exists");
			}
		}
		PrintWriter docOutput = new PrintWriter(docFile);
		
		File debugFile = new File("debugFile");
		if(debugFile.exists()){
			if(debugFile.delete())
				System.out.println("Create a new file for debug");
			else{
				System.out.println("Debug file already exists");
			}
		}
		PrintWriter debugOutput = new PrintWriter(debugFile);
		
		//每次重新使用需要clear
		//for debug
//		debugOutput.println(ReverseIndex.docNum);
//		debugOutput.println(riVector.size());
		
		indexOutput.writeInt(ReverseIndex.docNum);
		indexOutput.writeInt(riVector.size());
		for(PostingItem p:riVector){
//			indexOutput.writeUTF(p.getItem());
			docOutput.println(p.getItem());
			indexOutput.writeInt(p.getDf());
//			debugOutput.print(p.getDf());
//			debugOutput.print(" ");
			
			int i = 0;
			int tempDocID = 0;
			//每一条记录
			for(Record r:p.records){
				if(i++ == 0){
					tempDocID = r.getDocID();
					indexOutput.writeInt(tempDocID);
					//将tf压缩并写入文件中
					EncodeNumAndWrite(r.getTf(),indexOutput);
					
				}
				else{
					EncodeNumAndWrite(r.getDocID() - tempDocID,indexOutput);
					tempDocID = r.getDocID();
					EncodeNumAndWrite(r.getTf(),indexOutput);		
				}
			}
		}
		
		debugOutput.close();
		docOutput.close();
		indexOutput.close();
	}
	
	public static void EncodeNumAndWrite(int i,DataOutputStream indexOutput) throws IOException{
		LinkedList<Byte> byteStream = new LinkedList<Byte>();
		
		while(true){
			//先入的会被移到后面
			//7F，而非FF
			byteStream.addFirst((byte)(i&0x7F));
			if(i < 128)
				break;
			i = i>>7;
			
		}
		byte temp = (byte) (byteStream.getLast()|0x80);
		byteStream.removeLast();
		byteStream.addLast(temp);
		
		for(Byte b:byteStream){
			indexOutput.writeByte(b);
		}
	}
	
	//读入文件，解析称相应的数据结构
	public static LinkedList<PostingList> InputAndDecode() throws IOException{
		LinkedList<PostingList> tempList = new LinkedList<PostingList>();
		
		Scanner docInput = new Scanner(new File(docFileName));
		
		DataInputStream indexInput = new DataInputStream(new BufferedInputStream(
				new FileInputStream(indexFileName)));
		
		index.ReverseIndex.docNum = indexInput.readInt();
		int listNum = indexInput.readInt();
		
		for(int i=0;i<listNum;i++){	
			// PostingList tempPosting = new PostingList(indexInput.readUTF());
			PostingList tempPosting = new PostingList(docInput.next());
			int df = indexInput.readInt();
			tempPosting.setDf(df);
			
			int docID = indexInput.readInt();
			int tf = DecodeNum(indexInput);
			index.Record tempRecord = new index.Record(docID,tf);
			tempPosting.records.add(tempRecord);
			
			for(int j=1;j<df;j++){
				//存入的是docID的间隔
				docID = docID+DecodeNum(indexInput);
				tf = DecodeNum(indexInput);
				tempRecord = new index.Record(docID,tf);
				tempPosting.records.add(tempRecord);
			}
			tempList.add(tempPosting);
		}
		
		docInput.close();	
		indexInput.close();
			
		return tempList;
	}
	
	public static int DecodeNum(DataInputStream indexInput) throws IOException{
		int result = 0;
		
		while(true){
			byte temp = indexInput.readByte();
			//不能使用temp<128,因为temp如果最高位为1，那么temp就是-127……
			if((temp&0x80) == 0){
				result = (result<<7) + temp;
			}
			else{
				result = (result<<7) + (temp&0x7F);
				break;
			}
		}
		
		return result;
	}
	
}

