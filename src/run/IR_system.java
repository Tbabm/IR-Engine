package run;
import Search.BoolSearch;
import Search.SpellCorrect;
import Search.VectorSearch;
import index.BPlusTree;
import index.Node;
import index.PostingList;
import index.ReverseIndex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Henry on 2015/6/6.
 */
public class IR_system {

	private static String search_words = null;
	public static BPlusTree<String, String> rawStemTree;
	private static Scanner in = new Scanner(System.in);

    /**
     * 主函数
     * @param args 命令行参数
     * @throws Exception 
     */
    public static void main(String []args) throws Exception{
    	//建立词干还原树
    	rawStemTree = cstTree("Raw_Stemmed_Dic");
        //读取命令，进行相应
        IR_system getcommand = new IR_system();
        String cmd;
        while(!(cmd=in.nextLine()).equalsIgnoreCase("quit"))
            getcommand.do_operation(getcommand.parse(cmd));

		in.close();
    }

    /**
     * 命令解析
     * @param cmd 命令
     * @return 解析结果
     * op words
     */
    public int parse(String cmd){
    	if(cmd.length()==0) return -1;
        int k = cmd.indexOf(' ');
        String op = cmd.substring(0, k==-1?cmd.length():k);
        if(op.equalsIgnoreCase("search")) {
            int begin = cmd.indexOf('\"');
            int end = cmd.lastIndexOf('\"');
            if(begin == -1 || begin == end)
                System.out.println("You might loss \" in your search!");
            else {
                search_words = cmd.substring(cmd.indexOf('\"') + 1, cmd.lastIndexOf('\"'));
                //System.out.println(search_words);
            }
            return 2;
        }
        else if(op.equalsIgnoreCase("search_bool")){
            int begin = cmd.indexOf('\"');
            int end = cmd.lastIndexOf('\"');
            if(begin == -1 || begin == end)
                System.out.println("You might loss \" in your search!");
            else {
                search_words = cmd.substring(cmd.indexOf('\"') + 1, cmd.lastIndexOf('\"'));
                System.out.println(search_words);
            }
            return 1;
        }
        else if(op.equalsIgnoreCase("make_vector")){
            return 0;
        }
        else {
            System.out.println("Syntactic error!");
        }
        return -1;
    }

    /**
     * 根据命令做相应的操作
     * @param cmd 解析后的命令
     */
    public void do_operation(int cmd) throws Exception{
    	try    	{
	        switch(cmd){
	            case 0:
	                ReverseIndex.read();
	                break;
	            case 1:
	                System.out.println(search_words);
	                List<Integer> re_bool = new BoolSearch(search_words).getResult();
	                System.out.println("Result (docID): --bool");
	                if(re_bool == null) {
	                    System.out.println("未找到结果 or 布尔查询格式错误");
	                    return;
	                }
	                for(int i: re_bool)
	                    System.out.println(i);
	                break;
	            case 2:
	                System.out.println(search_words);
	                List<Integer> re = VectorSearch.getResult(toStemArray(search_words));
	                System.out.println("Result (docID):");
	                for(int i: re)
	                    System.out.println(i);
	                break;
	        }
    	}
    	catch(Exception ex)	{
    		System.out.println("error: " + ex.getMessage());
    	}
    	finally{
    		
    	}
    }

    /**
     * 将查询的search_words字符串词干还原，并以字符串数组形式输出
     * @param str
     * @return
     * @throws Exception
     */
    public static String[] toStemArray(String str) throws Exception{
        StringBuffer tmp = new StringBuffer(str);
        for(int i = 0; i < tmp.length()-1; i++){
            if(tmp.charAt(i) == ' ' && tmp.charAt(i+1) == ' ')
                tmp.deleteCharAt(i);
        }
        if(tmp.charAt(tmp.length()-1) == ' ')
            tmp.deleteCharAt(tmp.length()-1);
        String sstr = tmp.toString();
    	String[] ret = sstr.toLowerCase().split(" ");
    	for(int i = 0;i < ret.length;++i){
    		String spcorr = SpellCorrect.spellCorrect(ret[i]);
    		if(!spcorr.equals(ret[i])){
    			System.out.println("Do you want to type '" + spcorr + "' instead of '" + ret[i] +  "'(y/n)?");
    			String resp = in.next();
    			if(resp.toLowerCase().charAt(0)=='y'){
    				ret[i] = spcorr;
    			}
    		}
    		String stem = rawStemTree.get(ret[i]);
    		if(stem!=null){
    			ret[i] = stem;    			
    		}
    	}
    	return ret;
    }
    
    //建立词干还原树
	public static BPlusTree<String,String> cstTree(String filename){
		BPlusTree<String,String> RawStemTree = new BPlusTree<String, String>(); 
		
		File file = new File(filename);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
            	String[] result = tempString.split("\t");
            	RawStemTree.insert(result[0], result[1]);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        
        return RawStemTree;
	}
}
