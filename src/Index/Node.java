package Index;

/**
 * B+树的节点
 * Created by Henry on 2015/6/10.
 */
public class Node<T extends Comparable<T>, V> {
    private Node<T,V> next;
    private Node<T,Node> parent;
    protected KeyValue<T,V>[] key;
    private int M;
    private int elements;

    public static final int DEFAULT_M = 8;

    public Node(){
        this(DEFAULT_M);
    }

    public Node(int M){
        this.M = M;
        elements = 0;
        key = new KeyValue[M];
        next = null;
    }

    /**
     * 到下一个
     * @return
     */
    public Node toNext(){
        return next;
    }

    /**
     * 返回父亲节点
     * @return
     */
    public Node Parent(){
        return parent;
    }

    /**
     * 有效节点数
     * @return
     */
    public int size(){ return elements; }

    /**
     * 得到主关键词
     * @return
     */
    public T getkey(){
        return this.key[0].getKey();
    }

    /**
     * 下标访问键值
     * @param i
     * @return
     */
    public V getby(int i){
        return this.key[i].getValue();
    }

    /**
     * 得到键值
     * @param key
     * @return
     */
    public V get(T key){
        int i = 0;
        for(i = 0; i < elements && ((key).compareTo(this.key[i].getKey())) > 0; ++i);
        if(i < elements && this.key[i].getKey().equals(key))
            return this.key[i].getLeafValue();
        else if(i > 0){
            return (this.key[i-1].getValue()).getClass().equals(Node.class)?(V)((Node)(this.key[i-1].getValue())).get(key):null;
        }
        else if(i == 0){
            return (this.key[0].getValue()).getClass().equals(Node.class)?(V)((Node)(this.key[0].getValue())).get(key):null;
        }
        return null;
    }

    /**
     * 到达key可能所在的叶子节点
     * @param key
     * @return
     */
    public Node<T,V> toLeaf(T key){
        int i = 0;
        for(; i < elements && key.compareTo(this.key[i].getKey())>0; ++i);
        if(i < elements && this.key[i].getKey().equals(key))
            return this.key[i].toleaf()==null ? this:this.key[i].toleaf();
        else if(i>0)
            return this.key[i-1].getValue().getClass().equals(Node.class)?((Node)this.key[i-1].getValue()).toLeaf(key):this;
        else
            return this.key[0].toleaf();
    }

    /**
     * 找到指向节点左边的节点，从父亲中查询
     * @return ptr
     */
    private Node findPrev(){
        Node ptr = parent;
        T key = this.key[0].getKey();
        int k = 1;
        while( ptr != null && ptr.indexOf(key) == 0) {
            k++;
            ptr = ptr.parent;
        }
        if(ptr == null || ptr.indexOf(key) == -1)
            return null;
        ptr = (Node)ptr.key[ptr.indexOf(key)-1].getValue();
        while(--k > 0)
            ptr = (Node)ptr.key[ptr.elements-1].getValue();
        return ptr;
    }

    /**
     * 找到key所在的叶子节点
     * @param key
     * @return
     */
    public Node getNode(T key){
        return toLeaf(key).indexOf(key)==-1?null:toLeaf(key);
    }

    /**
     * 找在key在块内的位置
     * @param key
     * @return
     */
    public int indexOf(T key){
        int i = 0;
        for(i = 0; i < elements; ++i)
            if(this.key[i].getKey().equals(key))
                break;
        return i == elements ? -1 : i;
    }

    /**
     * 更新节点信息
     * @param key
     */
    private void update(KeyValue<T,V> key, int pos, T fsign){
        if(pos == -1)
            return;
        if(pos == 0 && parent != null){
            Node ptr = parent;
            int k;
            do{
                k = ptr.indexOf(fsign);
                if(k == -1)
                    break;
                ptr.key[k].setKey(key.getKey());
                ptr = ptr.parent;
            }while(k == 0 && ptr != null);
        }
        if(!this.key[pos].equals(key))
            this.key[pos] = key;
    }

    /**
     * 更新键值
     * @param key
     * @param val
     */
    public void update(T key, V val){
        int k = indexOf(key);
        if(k != -1)
            this.key[k].setValue(val);
    }

    /**
     * 删除
     * @param key
     */
    public void remove(T key){
        int k = indexOf(key);
        if(k != -1){
            T fsign = this.key[0].getKey();
            for(int i=indexOf(key); i<elements-1; i++)
                this.key[i] = this.key[i+1];
            elements--;
            if(k == 0)
                update(this.key[0], 0, fsign);
            if(elements < M/2){
                if(next != null)
                    next.average(this);
            }
        }
    }

    /**
     * 插入
     * @param key
     */
    public void insert(KeyValue<T,V> key) {
        if (indexOf(key.getKey()) != -1) {
            update(key, indexOf(key.getKey()), this.key[0].getKey());
        }
        else {
            if (elements < M) {
                int i = 0;
                for (; i < elements && (this.key[i].getKey()).compareTo(key.getKey()) < 0; i++) ;
                if (i == elements) {
                    this.key[elements] = key;
                }
                else {
                    T fsign = null;
                    if(elements != 0)
                        fsign = this.key[0].getKey();
                    for (int j = elements; j >= i+1; --j) {
                        this.key[j] = this.key[j - 1];
                    }
                    update(key, i, fsign);
                }
                elements++;
            } else {
                devide(key);
            }
        }
    }

    /**
     * 分裂
     * @param key
     */
    private void devide(KeyValue<T,V> key){
        int i = 0;
        //判断分裂后的插入位置
        for(; i<elements && key.getKey().compareTo(this.key[i].getKey())>0; ++i);
        elements = M/2+1;
        Node<T,V> child = new Node<T, V>(M);
        //选择留在原的节点中还是插入新的节点
        if(i >= elements) {
            child.elements = M-elements;
            System.arraycopy(this.key, elements, child.key, 0, M - elements);
            child.insert(key);
        }
        else {
            System.arraycopy(this.key, elements-1, child.key, 0, M - elements+1);
            child.elements = M-elements+1;
            elements--;
            insert(key);
        }
        //更新新的在树中的链接关系
        child.parent = parent;
        if(child.key[0].getValue().getClass().equals(Node.class)){
            for(i = 0; i<child.elements; i++)
                ((Node)child.key[i].getValue()).parent = child;
        }
        //插入父亲节点中
        if(parent != null) {
            if(parent.elements == M && next != null && next.merge(child))  //在父节点满的情况下尝试合并
                return;
            else {
                parent.insert(new KeyValue(child.key[0].getKey(), child));
                child.next = this.next;
                this.next = child;
            }
        }
        else{  //根节点分裂
            Node<T, Node> root = new Node<T, Node>(M);
            this.parent = root;
            child.parent = root;
            this.next = child;
            root.insert(new KeyValue(this.key[0].getKey(), this));
            root.insert(new KeyValue(child.key[0].getKey(), child));
        }
    }

    /**
     * 合并
     * @param node
     */
    private boolean merge(Node<T,V> node){
        if(elements+node.elements <= M) {
            T fsign = this.key[0].getKey();
            elements += node.elements;
            for (int i = elements-1; i >= node.elements; --i)
                key[i] = key[i - node.elements];
            System.arraycopy(node.key, 0, this.key, 0, node.elements);
            if(key[0].getValue().getClass().equals(Node.class)) {
                for (int i = 0; i < node.elements; ++i)
                    ((Node)key[i].getValue()).parent = this;
            }
            update(key[0], 0, fsign);
            return true;
        }
        return false;
    }

    /**
     * 平均两个节点
     * @param node
     */
    private void average(Node node){
        if(!merge(node)){
            T fsign = this.key[0].getKey();
            int k = (node.elements+elements)/2;
            System.arraycopy(this.key, 0, node.key, node.elements, k-node.elements);
            for(int i = 0; i < elements+node.elements-k; ++i)
                this.key[i] = this.key[i+1];
            elements = elements + node.elements-k;
            node.elements = k;
            update(this.key[0], 0, fsign);
        }
        else {
            if(node.findPrev() != null)
                node.findPrev().next = this;
        }
    }

    /**
     * 遍历输出 for test
     */
    public void printall(){
        for(int i = 0; i < elements; ++i)
            System.out.println(key[i].getKey().toString());
    }
}
