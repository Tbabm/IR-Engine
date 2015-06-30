package index;

import index.PostingList;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;

public class VBCode {
	
	public final static String indexFileName = "material/vb_ri.index";

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

