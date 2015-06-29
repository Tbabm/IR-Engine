package test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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
		
		byte temp = (byte) 0x81;
		int result = 0;
		//<<的优先级较低
		result = (result<<7) +(temp&0x7F);
		
		System.out.println(result);
	}
}
