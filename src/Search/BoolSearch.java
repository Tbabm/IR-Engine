package Search;

import run.IR_system;
import index.PostingList;
import index.Record;
import index.ReverseIndex;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * Created by Henry Huang on 2015/6/29.
 */
public class BoolSearch {
    public static final int OR = 0;
    public static final int AND = 1;
    public static final int NOT = 2;
    public static final int LP = 3;
    //public static final int TOTAL_DOC_NUM = ;
    public static final int RP = 4;

    private String sentence;
    //private List<booloperation> re;
    private Stack<boolitem> wordset;
    private Stack<Integer> op;

    public BoolSearch(String sentence){
        this.sentence = sentence;
        //re = new LinkedList<booloperation>();
        wordset = new Stack<boolitem>();
        op = new Stack<Integer>();
    }

    public static List<Integer> _do_and_not(List<Integer> list0, List<Integer> list1){
        List<Integer> result = new ArrayList<Integer>();
        int ptr0 = 0, ptr1 = 0;
        while(ptr1 < list1.size() && ptr0 < list0.size()){
            int tmp0 = list0.get(ptr0), tmp1 = list1.get(ptr1);
            if (tmp0 < tmp1)
                ptr0++;
            else if (tmp0 > tmp1) {
                if(tmp1 > 0)
                    result.add(tmp1);
                ptr1++;
            }
            else {
                ptr1++;
                ptr0++;
            }
        }
        return result.size()>0?result:null;
    }

    public static List<Integer> _do_and_both(List<Integer> list0, List<Integer> list1){
        if(list0 == null && list1 == null)
            return null;
        else{
            if(list0 == null)
                return list1;
            if(list1 == null)
                return list0;
        }
        List<Integer> result = new ArrayList<Integer>();
        int ptr0 = 0, ptr1 = 0;
        while (ptr0 < list0.size() && ptr1 < list1.size()) {
            int tmp0 = list0.get(ptr0), tmp1 = list1.get(ptr1);
            if (tmp0 < tmp1)
                ptr0++;
            else if (tmp0 > tmp1)
                ptr1++;
            else {
                if(tmp0 >= 0)
                    result.add(tmp0);
                ptr1++;
                ptr0++;
            }
        }
        return result.size()>0?result:null;
    }

    public static List<Integer> _do_or_both(List<Integer> list0, List<Integer> list1){
        if(list0 == null || list1 == null)
            return null;
        List<Integer> result = new ArrayList<Integer>();
        int ptr0 = 0, ptr1 = 0;
        while (ptr0 < list0.size() && ptr1 < list1.size()) {
            int tmp0 = list0.get(ptr0), tmp1 = list1.get(ptr1);
            if (tmp0 < tmp1) {
                if(tmp0 >= 0)
                    result.add(tmp0);
                ptr0++;
            }
            else if (tmp0 > tmp1) {
                if(tmp1 >= 0)
                    result.add(tmp1);
                ptr1++;
            }
            else {
                if(tmp0 >= 0)
                    result.add(tmp0);
                ptr1++;
                ptr0++;
            }
        }
        while(ptr0 < list0.size()) {
            result.add(list0.get(ptr0));
            ptr0++;
        }
        while(ptr1 < list1.size()) {
            result.add(list1.get(ptr1));
            ptr1++;
        }
        return result.size()>0?result:null;
    }

    class booloperation {
        boolitem op1, op2;
        int op;

        public booloperation(boolitem op1, boolitem op2, int op){
            this.op1 = op1;
            this.op2 = op2;
            this.op  = op;
        }

        boolitem get_result(){
            switch(op) {
                case OR: return do_or();
                case AND: return do_and();
                case NOT: return do_not();
                default: return null;
            }
        }

        boolitem do_not(){
            List<Integer> list = new LinkedList<Integer>(op1.boolrecord);
            return new boolitem(!op1.is_not, list);
        }

        boolitem do_or(){
            if(!op1.is_not && !op2.is_not)
                return new boolitem(false, _do_or_both(op1.boolrecord, op2.boolrecord));
            else {
                if(op1.is_not && op2.is_not)
                    return new boolitem(true, _do_and_both(op1.boolrecord, op2.boolrecord));
                else if(op1.is_not && !op2.is_not)
                    return new boolitem(true, _do_and_not(op2.boolrecord, op1.boolrecord));
                else if(!op1.is_not && op2.is_not)
                    return new boolitem(true, _do_and_not(op1.boolrecord, op2.boolrecord));
            }
            return null;
        }

        boolitem do_and(){
            //List<Integer> list0 = op1.boolrecord, list1 = op2.boolrecord;
            if(!op1.is_not && !op2.is_not)
                return new boolitem(false, _do_and_both(op1.boolrecord, op2.boolrecord));
            else {
                if(op1.is_not && op2.is_not)
                    return new boolitem(true, _do_or_both(op1.boolrecord, op2.boolrecord));
                else if(op1.is_not && !op2.is_not)
                    return new boolitem(false, _do_and_not(op1.boolrecord, op2.boolrecord));
                else if(!op1.is_not && op2.is_not)
                    return new boolitem(false, _do_and_not(op2.boolrecord, op1.boolrecord));
            }
            return null;
        }

    }

    class boolitem{
        boolean is_not;
        List<Integer> boolrecord;

        public boolitem(boolean is_not, List<Integer> boolrecord){
            this.is_not = is_not;
            this.boolrecord = boolrecord;
        }
    }

    boolean parse(){
        StringBuilder mys = new StringBuilder();
        boolean is_taken = false;
        String last = "";
        //String[] words = sentence.split(" ");
        for(int i = 0; i < sentence.length(); i++){
            char ch = sentence.charAt(i);
            if(ch == ' ') {
                String tmp = mys.toString();
                if(tmp.equals("AND")) {
                    if(last.equals("AND") || last.equals("OR") || last.equals("NOT"))
                        return false;
                    if(op.size() > 0 && op.peek() > AND && op.peek() != LP)
                        make_op();
                    op.push(AND);
                }
                else if(tmp.equals("OR")){
                    if(last.equals("AND") || last.equals("OR") || last.equals("NOT"))
                        return false;
                    if(op.size() > 0 && op.peek() > OR && op.peek() != LP)
                        make_op();
                    op.push(OR);
                }
                else if(tmp.equals("NOT"))
                    op.push(NOT);
                else if(tmp.equals("")){
                    continue;
                }
                else{
                    if(!last.equals("AND") && !last.equals("OR") && !last.equals("NOT") && !last.equals(""))
                        return false;
                    System.out.println(tmp + "   ssss");
                    tmp = tmp.toLowerCase();
                    String opw = SpellCorrect.spellCorrect(tmp);
                    if(tmp.equals(opw))
                        wordset.push(new boolitem(false, ReverseIndex.getPostingLinks(IR_system.rawStemTree.get(opw.toLowerCase())).bool_records));
                    else{
//                        if(op.size() > 0 && op.peek() == NOT)
//                            op.pop();
                        List<Integer> list = new LinkedList<Integer>();
                        list.add(-1);
                        wordset.push(new boolitem(false, list));
                    }
                }
                last = mys.toString();
                mys.delete(0, mys.length());
            }
            else if(ch == '('){
                op.push(LP);
            }
            else if(ch == ')'){
                make_op_rp();
            }
            else {
                mys.append(ch);
            }
        }
        String tmp = mys.toString();
        if(tmp.equals("") && (last.equals("NOT") || last.equals("AND") || last.equals("OR")))
            op.pop();
        if(!tmp.equals("")){
            if(!tmp.equals("NOT") && !tmp.equals("AND") && !tmp.equals("OR")) {
                System.out.println(tmp + "   ssss");
                tmp = tmp.toLowerCase();
                String opw = SpellCorrect.spellCorrect(tmp);
                if(tmp.equals(opw))
                    wordset.push(new boolitem(false, ReverseIndex.getPostingLinks(IR_system.rawStemTree.get(opw.toLowerCase())).bool_records));
                else{
//                    if(op.size() > 0 && op.peek() == NOT)
//                        op.pop();
                    List<Integer> list = new LinkedList<Integer>();
                    list.add(-1);
                    wordset.push(new boolitem(false, list));
                }
            }
        }
        return true;
    }

    void make_op(){
        int operation = op.pop();
        if(operation != NOT) {
            if(wordset.size() < 2)
                return;
            boolitem op1 = wordset.pop();
            boolitem op2 = wordset.pop();
            wordset.push(new booloperation(op1, op2, operation).get_result());
        }
        else{
            boolitem op1 = wordset.pop();
            wordset.push(new booloperation(op1, null, operation).get_result());
        }
    }

    void make_op_rp(){
        while(op.peek()!= LP)
            make_op();
        op.pop();
    }

    void make_op_all(){
        while(op.size() > 0)
            make_op();
    }

    public List<Integer> getResult(){
        if(!parse())
            return null;
        make_op_all();
        if(wordset.isEmpty())
            return null;
        if(wordset.peek().is_not)
            wordset.peek().boolrecord.add(0, -1);
        return wordset.pop().boolrecord;
    }
}
