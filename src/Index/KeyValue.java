package index;

/**
 * B+树的键值
 * Created by Henry on 2015/6/10.
 */
class KeyValue<T extends Comparable<T>, V>{
    private T key;
    private V value;

    public KeyValue(){
        key = null;
        value = null;
    }

    public KeyValue(T key, V value){
        setKey(key);
        setValue(value);
    }

    public V getValue(){
        return value;
    }

    public V getLeafValue(){
        if(value.getClass().equals(Node.class))
            return (V)(((Node)value).getby(0));
        else
            return value;
    }

    public Node toleaf(){
        if(value.getClass().equals(Node.class) && !((Node)value).getby(0).getClass().equals(Node.class))
            return (Node)value;
        else if(value.getClass().equals(Node.class))
            return ((Node)value).key[0].toleaf();
        return null;
    }

    public T getKey(){ return key; }

    public void setKey(T key){ this.key = key; }

    public void setValue(V value){ this.value = value; }

    @Override
    public boolean equals(Object o){
        return key.equals(((KeyValue)o).key) && value.equals(((KeyValue)o).value);
    }

//    @Override
//    public int compareTo(Object o){
//        return key.compareTo(((KeyValue<T,V>)o).getKey());
//    }
}
