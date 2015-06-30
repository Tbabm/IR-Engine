package Search;

import index.PostingList;
import index.Record;
import index.ReverseIndex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Henry on 2015/6/6.
 */
public class VectorSearch {
	public static int K = 10;
	public static int getK() { return K; }
	public static void setK(int newK) { K = newK; }
    /**
     * 获得查询结果
     * @param search 查询词
     * @return re 文档的ID列表
     */
    public static List<Integer> getResult(String[] searchWords){
    	//还原成词干的过程
        List< Pair<String, Integer> > queryRecords = convertQuery(searchWords);
    	//处理查询
        HashMap<Integer, Double> length_doc = new HashMap<Integer, Double>();//记录文档向量的模的大小(docID->norm)，由于查询向量在所有的值中都出现，这里面就不用计算了
        HashMap<Integer, Double> hashmap = new HashMap<Integer, Double>();	//记录文档docID->Double的映射，储存其计算与查询的余弦值的分子部分
        for (Pair<String, Integer> item : queryRecords){
            String word = item.first;
            Record rQuery = new Record(item.second);
            PostingList lst_word = ReverseIndex.getPostingLinks(word);
            if(lst_word==null){ //this word was not found in the dictionary
            	continue;
            }
            double idfWord = lst_word.getIdf();
            double idfSquare = idfWord*idfWord;
            
        	for (Record reDoc : lst_word.records) {
        		int docID = reDoc.getDocID();
        		
	        	Double w_curr = hashmap.get(docID);
	        	if(w_curr==null)  w_curr = new Double(0);
	        	
	        	Double length_curr = length_doc.get(docID);
	        	if(length_curr==null) 
	        		length_curr = new Double(0);
	        	
	        	double logTf = reDoc.getLogTf();
	        	w_curr += logTf*rQuery.getLogTf()*idfSquare;
	        	length_curr += logTf*logTf*idfSquare;
	        	if(length_curr==0) 
	        		length_curr = 1e-8;
                
	        	hashmap.put(docID, w_curr);
                length_doc.put(docID, length_curr);
        	}   	
        }
        //遍历每一个docID对应的权值，并且除以其对应向量的模值
        ArrayList<Pair<Double, Integer>> ret_tmp= new ArrayList<Pair<Double, Integer>>();//score->docID, sort order by score
        Iterator<Map.Entry<Integer, Double>> iter = hashmap.entrySet().iterator();
    	while(iter.hasNext()) {
    		Map.Entry entry = (Map.Entry)iter.next();
    		Integer docID = (Integer) entry.getKey();
    		Double w = (Double) entry.getValue() / Math.sqrt(length_doc.get(docID));
    		ret_tmp.add(new Pair<Double,Integer>(w,docID));
    	}
    	//取前K个并返回,大的K个（Pair中以第一个元素比较，从大到小排序）
    	int k = K;
    	Collections.sort(ret_tmp);
    	List<Integer> ret = new ArrayList<Integer>();
    	for(Pair<Double, Integer> p : ret_tmp)	{
    		ret.add(p.second);
    		System.out.println(p.first);
    		if(--k==0) break;
    	}
        return ret;
    }
    
    private static List< Pair<String, Integer> > convertQuery(String[] words)
    {
    	List< Pair<String, Integer> > ret = new ArrayList<Pair<String, Integer> >();
    	Arrays.sort(words);
    	String prev = null;
    	boolean first = true;
    	int cnt = 0;
    	for (String word: words) {
    		if(first) {
    			prev = word;
    			cnt = 0;
    			first = false;
    		}
    		else{
    			if(!word.equalsIgnoreCase(prev)) {
    				ret.add(new Pair<String, Integer>(prev, cnt));
    				cnt = 0;
    				prev = word;
    			}
    		}
			++cnt;
    	}
		ret.add(new Pair<String, Integer>(prev, cnt));
    	return ret;
    }

}

