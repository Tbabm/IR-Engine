package index;

/**
 * B+树的建立
 * Created by Henry on 2015/6/10.
 */
public class BPlusTree<T extends Comparable<T>, V>{
    private Node root;
    private Node<T,V> min;
    private int M;

    public BPlusTree(){
        this(Node.DEFAULT_M);
    }

    public BPlusTree(int M){
        this.M = M;
        root = null;
    }

    /**
     * 类的形式插入
     * @param key
     */
    public void insert(KeyValue key){
        if(root == null){
            root = new Node<T, V>(M);
            min = root;
            root.insert(key);
        }
        else {
            if(key.getKey().compareTo(min.getkey())<0)
                min.insert(key);
            else
                root.toLeaf((T)key.getKey()).insert(key);
        }
        root = root.Parent()==null?root:root.Parent();
    }

    /**
     * 插入
     * @param key
     * @param value
     */
    public void insert(T key, V value){
        insert(new KeyValue(key, value));
    }

    /**
     * 删除
     * @param key
     */
    public void remove(T key){
        root.toLeaf(key).remove(key);
    }

    /**
     * 更新
     * @param key
     * @param val
     */
    public void update(T key, V val){
        root.toLeaf(key).update(key, val);
    }

    /**
     * 获得key所对应的键值
     * @param key
     * @return
     */
    public V get(T key){
        return (V)root.get(key);
    }

    /**
     * for test
     */
    public void printAll(){
        Node ptr = root.toLeaf(root.getkey());
        while(ptr != null){
            ptr.printall();
            ptr = ptr.toNext();
        }
    }

//    /**
//     * for test
//     * @param key
//     */
//    public void testPrev(T key){
//        System.out.println(root.toLeaf(key)==root.toLeaf(key).findPrev().toNext());
//    }

//    /**
//     * for test
//     * @param key1
//     * @param key2
//     */
//    public void testAver(T key1, T key2){
//        System.out.println("before:");
//        Node p1 = root.toLeaf(key1);
//        Node p2 = root.toLeaf(key2);
//        for(int i = 0; i < p1.size(); ++i)
//            System.out.print(p1.key[i].getKey()+" ");
//        System.out.print("\n");
//        for(int i = 0; i < p2.size(); ++i)
//            System.out.print(p2.key[i].getKey()+" ");
//        System.out.print("\n");
//        if(p1 != p2) {
//            p2.average(p1);
//            System.out.println("after:");
//            p1 = root.toLeaf(key1);
//            p2 = root.toLeaf(key2);
//            for(int i = 0; i < p1.size(); ++i)
//                System.out.print(p1.key[i].getKey()+" ");
//            System.out.print("\n");
//            for(int i = 0; i < p2.size(); ++i)
//                System.out.print(p2.key[i].getKey()+" ");
//        }
//    }
}
