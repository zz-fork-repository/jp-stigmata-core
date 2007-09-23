package jp.naist.se.stigmata.result;

/*
 * $Id$
 */

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jp.naist.se.stigmata.BirthmarkContext;
import jp.naist.se.stigmata.BirthmarkEnvironment;
import jp.naist.se.stigmata.BirthmarkSet;
import jp.naist.se.stigmata.ComparisonPair;
import jp.naist.se.stigmata.ComparisonResultSet;
import jp.naist.se.stigmata.ExtractionResultSet;

/**
 * Abstract class for ComparisonResultSet.
 *
 * @author Haruaki Tamada
 * @version $Revision$ $Date$
 */
public abstract class AbstractComparisonResultSet implements ComparisonResultSet{
    protected ExtractionResultSet extraction;
    private int count = -1;

    public AbstractComparisonResultSet(ExtractionResultSet extraction){
        this.extraction = extraction;
    }

    public abstract Iterator<ComparisonPair> iterator();

    public abstract Iterator<BirthmarkSet> pairSources();

    public synchronized BirthmarkSet[] getPairSources(){
        return AbstractComparisonResultSet.<BirthmarkSet>getArrays(pairSources());
    }

    public int getPairCount(){
        if(count < 0){
            int calculateCount = 0;
            for(Iterator<ComparisonPair> i = iterator(); i.hasNext(); ){
                calculateCount++;
                i.next();
            }
            this.count = calculateCount;
        }
        return count;
    }

    public synchronized ComparisonPair[] getPairs(){
        return AbstractComparisonResultSet.<ComparisonPair>getArrays(iterator());
    }

    public ComparisonPair getPairAt(int index){
        int currentIndex = 0;
        for(Iterator<ComparisonPair> i = iterator(); i.hasNext(); ){
            ComparisonPair pair = i.next();
            if(currentIndex == index){
                return pair;
            }
            currentIndex++;
        }
        return null;
    }

    public BirthmarkContext getContext(){
        return extraction.getContext();
    }

    public BirthmarkEnvironment getEnvironment(){
        return extraction.getEnvironment();
    }

    @SuppressWarnings("unchecked")
    static synchronized <T> T[] getArrays(Iterator<T> i){
        List<Object> list = new ArrayList<Object>();
        Object o = null;
        while(i.hasNext()){
            o = i.next();
            list.add(i.next());
        }
        int size = 0;
        if(o != null) size = list.size();
        T[] array = (T[])Array.newInstance(o.getClass(), size);
        for(int index = 0; index < list.size(); index++){
            array[index] = (T)list.get(index);
        }
        return array;
    }
}
