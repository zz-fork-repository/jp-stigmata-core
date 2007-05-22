package jp.naist.se.stigmata.filter;

/*
 * $Id$
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jp.naist.se.stigmata.BirthmarkContext;
import jp.naist.se.stigmata.BirthmarkSet;
import jp.naist.se.stigmata.ComparisonPair;
import jp.naist.se.stigmata.ComparisonPairFilterSet;
import jp.naist.se.stigmata.ComparisonResultSet;

/**
 * Filtering {@link ComparisonResultSet <code>ComparisonResultSet</code>}.
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class FilteredComparisonResultSet implements ComparisonResultSet{
    private ComparisonResultSet resultset;
    private List<ComparisonPairFilterSet> filters = new ArrayList<ComparisonPairFilterSet>(); 

    /**
     * constructor.
     * 
     * @param resultset filtering target
     */
    public FilteredComparisonResultSet(ComparisonResultSet resultset){
        this.resultset = resultset;
    }

    /**
     * constructor.
     * @param resultset filtering target
     * @param filters filtering rule
     */
    public FilteredComparisonResultSet(ComparisonResultSet resultset, ComparisonPairFilterSet[] filters){
        this.resultset = resultset;
        for(int i = 0; i < filters.length; i++){
            addFilterSet(filters[i]);
        }
    }

    public void addFilterSet(ComparisonPairFilterSet filter){
        filters.add(filter);
    }

    public void removeFilterSet(ComparisonPairFilterSet filter){
        filters.remove(filter);
    }

    public int getComparisonCount(){
        return resultset.getComparisonCount();
    }

    public BirthmarkContext getContext(){
        return resultset.getContext();
    }

    public Iterator<ComparisonPair> iterator(){
        return new FilteredIterator(resultset.iterator());
    }

    public BirthmarkSet[] getComparisonSources(){
        return resultset.getComparisonSources();
    }

    private class FilteredIterator implements Iterator<ComparisonPair>{
        private Iterator<ComparisonPair> iterator;
        private ComparisonPair next;

        public FilteredIterator(Iterator<ComparisonPair> iterator){
            this.iterator = iterator;
            
            next = findNext();
        }

        public boolean hasNext(){
            return next != null;
        }

        public ComparisonPair next(){
            ComparisonPair returnValue = next;
            next = findNext();
            return returnValue;
        }

        public void remove(){
            throw new InternalError("not implemented");
        }

        private ComparisonPair findNext(){
            boolean nowFinding = true;
            while(nowFinding && iterator.hasNext()){
                ComparisonPair nextPair = iterator.next();
                // return the pair which the all filters is passed
                if(isAllFilterPassed(nextPair)){
                    nowFinding = false; // found next value!
                    next = nextPair;
                }
            }
            if(nowFinding && !iterator.hasNext()){
                next = null;
            }
            return next;
        }

        private boolean isAllFilterPassed(ComparisonPair pair){
            boolean flag = true;
            for(Iterator<ComparisonPairFilterSet> i = filters.iterator(); i.hasNext(); ){
                ComparisonPairFilterSet filter = i.next();
                if(!filter.isFiltered(pair)){
                    flag = false;
                    break;
                }
            }
            return flag;
        }
    };
}
