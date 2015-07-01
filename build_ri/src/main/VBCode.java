package main;

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
	//public static String docFileName = "vb_doc.txt";
	//压缩之后直接以二进制流输出到文件中
	public static void EncodeAndOutput(LinkedList<PostingItem> riVector) throws IOException{		
		DataOutputStream indexOutput = new DataOutputStream(new BufferedOutputStream(
													new FileOutputStream(indexFileName)));
		//方案2
//		File docFile = new File(docFileName);
//		if(docFile.exists()){
//			if(docFile.delete())
//				System.out.println("Create a new file for vb doc");
//			else{
//				System.out.println("Doc file already exists");
//			}
//		}
//		PrintWriter docOutput = new PrintWriter(docFile);
		
		indexOutput.writeInt(ReverseIndex.docNum);
		indexOutput.writeInt(riVector.size());
		for(PostingItem p:riVector){
			indexOutput.writeUTF(p.getItem());
//			docOutput.println(p.getItem());
			indexOutput.writeInt(p.getDf());
			
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
		
//		docOutput.close();
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
	
}

