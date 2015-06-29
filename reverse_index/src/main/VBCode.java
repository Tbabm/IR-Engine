package main;

import index.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;

public class VBCode {
	
	public static String indexFileName = "vb_ri.index";
	//压缩之后直接以字符流输出到文件中
	public static void EncodeAndOutput(LinkedList<PostingItem> riVector) throws IOException{		
		DataOutputStream indexOutput = new DataOutputStream(new BufferedOutputStream(
													new FileOutputStream(indexFileName)));
		
		//每次重新使用需要clear		
		indexOutput.writeInt(ReverseIndex.docNum);
		indexOutput.writeInt(riVector.size());
		for(PostingItem p:riVector){
			indexOutput.writeUTF(p.getItem());
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
		
		indexOutput.close();
	}
	
	public static void EncodeNumAndWrite(int i,DataOutputStream indexOutput) throws IOException{
		LinkedList<Byte> byteStream = new LinkedList<Byte>();
		
		while(true){
			//先入的会被移到后面
			byteStream.addFirst((byte)(i&0xFF));
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
		
		DataInputStream indexInput = new DataInputStream(new BufferedInputStream(
				new FileInputStream(indexFileName)));
		
		index.ReverseIndex.docNum = indexInput.readInt();
		int listNum = indexInput.readInt();
		
		for(int i=0;i<listNum;i++){
			PostingList tempPosting = new PostingList(indexInput.readUTF());
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

