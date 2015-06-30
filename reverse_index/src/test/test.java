package test;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

public class test {
	public static void main(String[] args) throws IOException{
//		String indexFileName = "test.txt";
//		
//		DataOutputStream out = new DataOutputStream(new BufferedOutputStream(
//													new FileOutputStream(indexFileName)));
//		out.writeUTF("hehehe");
//		out.writeInt(12);
//		out.writeDouble(3.2);
//		
//		out.close();
//		
//		DataInputStream in = new DataInputStream(new BufferedInputStream(
//												 new FileInputStream(indexFileName)));
//		System.out.println(in.readUTF());
//		System.out.println(in.readInt());
//		System.out.println(in.readDouble());
//		in.close();
		
//		byte temp = (byte) 0x81;
//		int result = 0;
//		//<<的优先级较低
//		result = (result<<7) +(temp&0x7F);
//		
//		System.out.println(result);
		
			Scanner in = new Scanner(System.in);
		
			LinkedList<Byte> byteStream = new LinkedList<Byte>();
			
			int i= in.nextInt();
			
			while(true){
				//先入的会被移到后面
				//这里应该是7F，因为我们只需要7位
				byteStream.addFirst((byte)(i&0x7F));
				if(i < 128)
					break;
				i = i>>7;
				
			}
			byte temp = (byte) (byteStream.getLast()|0x80);
			byteStream.removeLast();
			byteStream.addLast(temp);
			
			int result = 0;
			while(true){
				byte temp1 = byteStream.removeFirst();
				//不能使用temp<128,因为temp如果最高位为1，那么temp就是-127……
				if((temp1&0x80) == 0){
					result = (result<<7) + temp1;
				}
				else{
					result = (result<<7) + (temp1&0x7F);
					break;
				}
			}
			
			System.out.println(result);
	}
}
