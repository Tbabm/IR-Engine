package Index;

/**
 * 文档的处理类
 * Created by Henry on 2015/6/12.
 */
public class DocNode implements Comparable{
    private long doc_id;
    private double value;
    protected int tf;


    public DocNode(long id){
        setID(id);
        value = 0;
        tf = 1;
    }

    public void setID(long id){ doc_id = id; }

    public double getValue(){ return value; }

    public long getDoc_id(){ return doc_id; }

    public void plusTf(){ tf++; }

    public void plusTf(int k){ tf += k; }

    public void updateValue(long docs){ value = (1+Math.log10(tf))*Math.log10(Documents.total/docs); }

    @Override
    public int compareTo(Object o){
        if(doc_id>((DocNode)o).doc_id)
            return 1;
        else if(doc_id==((DocNode)o).doc_id)
            return 0;
        else
            return -1;
    }

    @Override
    public boolean equals(Object o){
        return doc_id == ((DocNode)o).getDoc_id();
    }
}
