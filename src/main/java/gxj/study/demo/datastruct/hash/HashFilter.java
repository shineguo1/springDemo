package gxj.study.demo.datastruct.hash;

/**
 * 非常糟糕，不能用
 *
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2022/9/5 17:44
 */
public class HashFilter<V> {

    private int threshold = 0;
    private int size = 0;
    private Integer[] hashArray;
    private float loadFactor;

    static final int MAXIMUM_CAPACITY = 1 << 30;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16


    /* =========================== 构造器 =============================*/

    public HashFilter() {
        this(8, DEFAULT_LOAD_FACTOR);
    }

    public HashFilter(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public HashFilter(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal initial capacity: " +
                    initialCapacity);
        if (initialCapacity > MAXIMUM_CAPACITY)
            initialCapacity = MAXIMUM_CAPACITY;
        if (loadFactor <= 0 || Float.isNaN(loadFactor))
            throw new IllegalArgumentException("Illegal load factor: " +
                    loadFactor);
        this.loadFactor = loadFactor;
        this.threshold = tableSizeFor(initialCapacity);
    }

    /* ==================================== 公开接口 =============================== */

    public Integer put(V value) {
        return putHash(Hash.hashInt(value));
    }

    public int size() {
        return size;
    }


    /* ==================================== 私有方法 =============================== */

    /**
     * @return previous hash, or null if none
     */
    private final Integer putHash(int hash) {
        Integer[] tab;
        Integer oldValue;
        int n, i;
        if ((tab = hashArray) == null || (n = tab.length) == 0)
            n = (tab = resize()).length;
        if ((oldValue = tab[i = (n - 1) & hash]) == null) {
            size++;
        }
        tab[i] = hash;
        if (size > threshold)
            resize();
        return oldValue;
    }


    private Integer[] resize() {
        Integer[] oldTab = hashArray;
        int oldCap = (oldTab == null) ? 0 : oldTab.length;
        int oldThr = threshold;
        int newCap, newThr = 0;
        if (oldCap > 0) {
            if (oldCap >= MAXIMUM_CAPACITY) {
                threshold = Integer.MAX_VALUE;
                return oldTab;
            } else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                    oldCap >= DEFAULT_INITIAL_CAPACITY) {
                // double threshold
                newThr = oldThr << 1;
            }
        }
        // initial capacity was placed in threshold
        else if (oldThr > 0) {
            newCap = oldThr;
        }
        // zero initial threshold signifies using defaults
        else {
            newCap = DEFAULT_INITIAL_CAPACITY;
            newThr = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        }
        if (newThr == 0) {
            float ft = (float) newCap * loadFactor;
            newThr = (newCap < MAXIMUM_CAPACITY && ft < (float) MAXIMUM_CAPACITY ?
                    (int) ft : Integer.MAX_VALUE);
        }
        threshold = newThr;
        System.out.println(size + " " + threshold);
        Integer[] newTab = new Integer[newCap];
        hashArray = newTab;
        if (oldTab != null) {
            for (Integer hash : oldTab) {
                size = 0;
                if (hash != null) {
                    putHash(hash);
                }
            }
        }
        return newTab;
    }

    /**
     * Returns a power of two size for the given target capacity.
     */
    private static final int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }

}
