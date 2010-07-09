package jp.sourceforge.stigmata.result;

/*
 * $Id$
 */

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;

import jp.sourceforge.stigmata.BirthmarkContext;
import jp.sourceforge.stigmata.BirthmarkEnvironment;
import jp.sourceforge.stigmata.BirthmarkSet;
import jp.sourceforge.stigmata.BirthmarkStoreException;
import jp.sourceforge.stigmata.ExtractionResultSet;
import jp.sourceforge.stigmata.ExtractionTarget;
import jp.sourceforge.stigmata.ExtractionUnit;

/**
 * Abstract class for ExtractionResultSet.
 * 
 * @author Haruaki Tamada
 * @version $Revision$ 
 */
public abstract class AbstractExtractionResultSet implements ExtractionResultSet{
    private BirthmarkContext context;
    private boolean tableType = true;
    private String id;

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
        id = generateId();
    }

    void setId(String id){
        this.id = id;
    }

    public String getId(){
        return id;
    }

    /**
     * returns a birthmark environment.
     */
    public BirthmarkEnvironment getEnvironment(){
        return context.getEnvironment();
    }

    /**
     * returns a birthmark context.
     */
    public BirthmarkContext getContext(){
        return context;
    }

    public abstract void addBirthmarkSet(ExtractionTarget target, BirthmarkSet set) throws BirthmarkStoreException;

    public abstract void removeBirthmarkSet(ExtractionTarget target, BirthmarkSet set);

    public abstract void removeAllBirthmarkSets(ExtractionTarget target);

    public abstract int getBirthmarkSetSize(ExtractionTarget target);

    public abstract Iterator<BirthmarkSet> birthmarkSets(ExtractionTarget target);

    /**
     * returns the sum of birthmark set size this instance has.
     * <code>getBirthmarkSetSize(ExtractionTarget.TARGET_BOTH)</code>
     */
    public int getBirthmarkSetSize(){
        return getBirthmarkSetSize(ExtractionTarget.TARGET_BOTH);
    }

    /**
     * returns an iterator.
     * <code>birthmarkSets(ExtractionTarget.TARGET_BOTH)</code>
     */
    public Iterator<BirthmarkSet> iterator(){
        return birthmarkSets(ExtractionTarget.TARGET_BOTH);
    }

    /**
     * returns a birthmark set related of given index.
     * <code>getBirthmarkSet(ExtractionTarget.TARGET_BOTH, index)</code>
     */
    public BirthmarkSet getBirthmarkSet(int index){
        return getBirthmarkSet(ExtractionTarget.TARGET_BOTH, index);
    }

    /**
     * returns a birthmark set related with given name.
     * <code>getBirthmarkSet(ExtractionTarget.TARGET_BOTH, name)</code>
     */
    public BirthmarkSet getBirthmarkSet(String name){
        return getBirthmarkSet(ExtractionTarget.TARGET_BOTH, name);
    }

    /**
     * returns all of birthmark sets.
     * <code>getBirthmarkSets(ExtractionTarget.TARGET_BOTH)</code>
     */
    public BirthmarkSet[] getBirthmarkSets(){
        return getBirthmarkSets(ExtractionTarget.TARGET_BOTH);
    }

    /**
     * remove specified birthmark set from this instance.
     * <code>removeBirthmarkSet(ExtractionTarget.TARGET_BOTH, bs)</code>
     */
    public void removeBirthmarkSet(BirthmarkSet bs){
        removeBirthmarkSet(ExtractionTarget.TARGET_BOTH, bs);
    }

    /**
     * remove all of birthmark sets.
     * <code>removeBirthmarkSet(ExtractionTarget.TARGET_BOTH)</code>
     */
    public void removeAllBirthmarkSets(){
        removeAllBirthmarkSets(ExtractionTarget.TARGET_BOTH);
    }

    /**
     * returns an array of extracted birthmark types.
     */
    public String[] getBirthmarkTypes(){
        return context.getBirthmarkTypes();
    }

    /**
     * returns an unit of extraction from.
     */
    public ExtractionUnit getExtractionUnit(){
        return context.getExtractionUnit();
    }

    /**
     * returns the birthmark set at the specified position in the specified target. 
     */
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

    /**
     * returns the birthmark set related with the specified name in the specified target.
     */
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

    protected static String generateId(){
        SimpleDateFormat cdf = new SimpleDateFormat("yyyyMMdd-HHmmss.SSS");
        return cdf.format(Calendar.getInstance().getTime());
    }
}
