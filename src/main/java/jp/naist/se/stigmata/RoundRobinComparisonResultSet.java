package jp.naist.se.stigmata;

/*
 * $Id$
 */

import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Concrete class for ComparisonResultSet. This instance compare class files by round robin.
 *
 * @author  Haruaki TAMADA
 * @version  $Revision$ $Date$
 */
public class RoundRobinComparisonResultSet implements ComparisonResultSet{
    private List<BirthmarkSet> holders1;
    private List<BirthmarkSet> holders2;
    private BirthmarkContext context;

    private int compareCount;
    private boolean tablePair = true;
    private boolean samePair = false;

    /**
     * constructor.  if user gives { a, b, c, } as holders1, then
     * the instance (created by this constructor) compares { a<->b, a<->c,
     * b<->c, }.
     */
    public RoundRobinComparisonResultSet(BirthmarkSet[] holders1, BirthmarkContext context){
        this(holders1, context, false);
    }

    /**
     * constructor.  if user gives { a, b, c, } as holders1, then the
     * instance (created by this constructor when samePair is true)
     * compares { a<->a, b<->a, b<->b, c<->a, c<->b, c<->c, }.
     * Otherwise, the instance compares { a<->b, a<->c, b<->c, } when
     * samePair is false.
     */
    public RoundRobinComparisonResultSet(BirthmarkSet[] holders1, BirthmarkContext context,
                                          boolean samePair){
        this.holders1 = Arrays.asList(holders1);
        this.holders2 = Arrays.asList(holders1);
        this.context = context;

        tablePair = false;
        setCompareSamePair(samePair);
    }

    /**
     * constructor.  if user gives { a, b, c, } as holders1 and { x,
     * y, z, } as holders2, then the instance compares { a<->x, a<->y,
     * a<->z, b<->x, b<->y, b<->z, c<->x, c<->y, c<->z, }.
     */
    public RoundRobinComparisonResultSet(final BirthmarkSet[] holders1, final BirthmarkSet[] holders2,
                                          BirthmarkContext context){
        this.holders1 = Arrays.asList(holders1);
        this.holders2 = Arrays.asList(holders2);
        this.context = context;
        tablePair = true;

        this.compareCount = holders1.length * holders2.length;
    }

    /**
     * @return  context
     */
    public BirthmarkContext getContext(){
        return context;
    }

    /**
     * update same pair comparing flag unless two birthmark array is setted.
     */
    public void setCompareSamePair(boolean flag){
        samePair = flag;
        if(samePair){
            compareCount = holders1.size() * (holders1.size() + 1) / 2;
        }
        else{
            compareCount = holders1.size() * (holders1.size() - 1) / 2;
        }
    }

    public boolean isCompareSamePair(){
        return samePair;
    }

    /**
     * returns the compare count of birthmark sets.
     */
    public int getComparisonCount(){
        return compareCount;
    }

    /**
     * return a iterator of whole comparison.
     */
    public Iterator<ComparisonPair> iterator(){
        return new ComparisonIterator();
    }

    public BirthmarkSet[] getComparisonSources(){
        Map<URL, BirthmarkSet> map = new HashMap<URL, BirthmarkSet>();
        for(BirthmarkSet set: holders1){
            map.put(set.getLocation(), set);
        }
        for(BirthmarkSet set: holders2){
            map.put(set.getLocation(), set);
        }

        BirthmarkSet[] entries = new BirthmarkSet[map.size()];
        int index = 0;
        for(Map.Entry<URL, BirthmarkSet> entry: map.entrySet()){
            entries[index] = entry.getValue();
            index++;
        }

        return entries;
    }

    /**
     * iterator class.
     */
    private class ComparisonIterator implements Iterator<ComparisonPair>{
        private int i = 0;
        private int j = 0;
        private int count = 0;

        public boolean hasNext(){
            return count < getComparisonCount();
        }

        public ComparisonPair next(){
            if((tablePair  && i == holders1.size()) || 
               (!tablePair && !samePair && i == j) ||
               (!tablePair && samePair && i > j)){
                i = 0;
                j++;
            }
            ComparisonPair pair = new ComparisonPair(holders1.get(i), holders2.get(j), context);
            count++;
            i++;
            return pair;
        }

        public void remove(){
        }
    }
}
