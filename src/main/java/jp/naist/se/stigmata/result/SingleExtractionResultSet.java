package jp.naist.se.stigmata.result;

/*
 * $Id$
 */

import java.util.Iterator;
import java.util.NoSuchElementException;

import jp.naist.se.stigmata.BirthmarkContext;
import jp.naist.se.stigmata.BirthmarkSet;
import jp.naist.se.stigmata.ExtractionTarget;
import jp.naist.se.stigmata.utils.NullIterator;

/**
 *
 * @author Haruaki Tamada
 * @version $Revision$ $Date$
 */
public class SingleExtractionResultSet extends AbstractExtractionResultSet{
    private BirthmarkSet bs;

    public SingleExtractionResultSet(BirthmarkContext context, BirthmarkSet bs){
        super(context, false);
        this.bs = bs;
    }

    public SingleExtractionResultSet(BirthmarkContext context){
        super(context, false);
    }

    public void addBirthmarkSet(ExtractionTarget target, BirthmarkSet set){
        this.bs = set;
    }

    public void setBirthmarkSets(ExtractionTarget target, BirthmarkSet[] sets){
        this.bs = sets[0];
    }

    public Iterator<BirthmarkSet> birthmarkSets(ExtractionTarget target){
        if(bs != null){
            return new SingleIterator(bs);
        }
        return new NullIterator<BirthmarkSet>();
    }

    public void removeBirthmarkSet(ExtractionTarget target, BirthmarkSet set){
        bs = null;
    }

    public void removeAllBirthmarkSets(ExtractionTarget target){
        bs = null;
    }

    public BirthmarkSet getBirthmarkSet(ExtractionTarget target, int index){
        if(index != 0){
            throw new IndexOutOfBoundsException("illegal index: " + index);
        }
        return bs;
    }

    public BirthmarkSet getBirthmarkSet(ExtractionTarget target, String name){
        if(bs.getName().equals(name)){
            return bs;
        }
        return null;
    }

    public int getBirthmarkSetSize(ExtractionTarget target){
        return 1;
    }

    private static class SingleIterator implements Iterator<BirthmarkSet>{
        private boolean first = true;
        private BirthmarkSet bs;

        public SingleIterator(BirthmarkSet bs){
            this.bs = bs;
        }

        public BirthmarkSet next(){
            if(first){
                return bs;
            }
            throw new NoSuchElementException();
        }

        public boolean hasNext(){
            boolean flag = first;
            first = false;
            return flag;
        }

        public void remove(){
        }
    }
}
