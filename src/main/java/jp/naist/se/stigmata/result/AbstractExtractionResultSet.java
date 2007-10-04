package jp.naist.se.stigmata.result;

/*
 * $Id$
 */

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;

import jp.naist.se.stigmata.BirthmarkContext;
import jp.naist.se.stigmata.BirthmarkEnvironment;
import jp.naist.se.stigmata.BirthmarkSet;
import jp.naist.se.stigmata.BirthmarkStoreException;
import jp.naist.se.stigmata.ExtractionResultSet;
import jp.naist.se.stigmata.ExtractionTarget;

/**
 * Abstract class for ExtractionResultSet.
 * 
 * @author Haruaki Tamada
 * @version $Revision$ $Date$
 */
public abstract class AbstractExtractionResultSet implements ExtractionResultSet{
    private BirthmarkContext context;
    private boolean tableType = true;

    /**
     * constructor.
     */
    public AbstractExtractionResultSet(BirthmarkContext context){
        this(context, true);
    }

    /**
     * constructor.
     */
    public AbstractExtractionResultSet(BirthmarkContext context, boolean tableType){
        this.context = context;
    }

    public BirthmarkEnvironment getEnvironment(){
        return context.getEnvironment();
    }

    public BirthmarkContext getContext(){
        return context;
    }

    public abstract void addBirthmarkSet(ExtractionTarget target, BirthmarkSet set) throws BirthmarkStoreException;

    public abstract void removeBirthmarkSet(ExtractionTarget target, BirthmarkSet set);

    public abstract void removeAllBirthmarkSets(ExtractionTarget target);

    public abstract int getBirthmarkSetSize(ExtractionTarget target);

    public abstract Iterator<BirthmarkSet> birthmarkSets(ExtractionTarget target);

    public int getBirthmarkSetSize(){
        return getBirthmarkSetSize(ExtractionTarget.TARGET_BOTH);
    }

    public Iterator<BirthmarkSet> iterator(){
        return birthmarkSets(ExtractionTarget.TARGET_BOTH);
    }

    public BirthmarkSet getBirthmarkSet(int index){
        return getBirthmarkSet(ExtractionTarget.TARGET_BOTH, index);
    }

    public BirthmarkSet getBirthmarkSet(String name){
        return getBirthmarkSet(ExtractionTarget.TARGET_BOTH, name);
    }

    public BirthmarkSet[] getBirthmarkSets(){
        return getBirthmarkSets(ExtractionTarget.TARGET_BOTH);
    }

    public void removeBirthmarkSet(BirthmarkSet bs){
        removeBirthmarkSet(ExtractionTarget.TARGET_BOTH, bs);
    }

    public void removeAllBirthmarkSets(){
        removeAllBirthmarkSets(ExtractionTarget.TARGET_BOTH);
    }

    public String[] getBirthmarkTypes(){
        return context.getBirthmarkTypes();
    }

    public BirthmarkSet getBirthmarkSet(ExtractionTarget target, int index){
        int currentIndex = 0;
        for(Iterator<BirthmarkSet> i = birthmarkSets(target); i.hasNext(); ){
            if(currentIndex == index){
                return i.next();
            }
            i.next();
            currentIndex++;
        }
        return null;
    }

    public BirthmarkSet getBirthmarkSet(ExtractionTarget target, String setname){
        for(Iterator<BirthmarkSet> i = birthmarkSets(target); i.hasNext(); ){
            BirthmarkSet bs = i.next();
            if(bs.getName().equals(setname)){
                return bs;
            }
        }
        return null;
    }

    /**
     * @return all of BirthmarkSet this instance have. elements is obtained from birthmarkSet.
     */
    public synchronized BirthmarkSet[] getBirthmarkSets(ExtractionTarget target){
        return AbstractComparisonResultSet.<BirthmarkSet>getArrays(birthmarkSets(target), new BirthmarkSet[0]);
    }

    public void setBirthmarkSets(ExtractionTarget target, BirthmarkSet[] sets) throws BirthmarkStoreException{
        removeAllBirthmarkSets(target);
        for(int i = 0; i < sets.length; i++){
            addBirthmarkSet(target, sets[i]);
        }
    }

    public boolean isTableType(){
        return tableType;
    }

    public void setTableType(boolean flag){
        this.tableType = flag;
    }

    protected String generateId(){
        SimpleDateFormat cdf = new SimpleDateFormat("yyyyMMdd-HH:mm:ss.SSS");
        return cdf.format(Calendar.getInstance().getTime());
    }
}
