package jp.naist.se.stigmata;

/*
 * $Id$
 */

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Concrete class for ComparisonResultSet. This instance compare class
 * files by certain pair. The pair is guessed by system with class
 * name, or specified by user.
 *
 * @author  Haruaki TAMADA
 * @version  $Revision$ $Date$
 */
public class CertainPairComparisonResultSet implements ComparisonResultSet{
    private BirthmarkContext context;
    private List<ComparisonPair> pairList = new ArrayList<ComparisonPair>();
    private Map<URL, BirthmarkSet> sources = new HashMap<URL, BirthmarkSet>();

    /**
     * This constructor is the comparison pair list is specified.
     */
    public CertainPairComparisonResultSet(ComparisonPair[] pairs, BirthmarkContext context){
        this.context = context;
        for(int i = 0; i < pairs.length; i++){
            pairList.add(pairs[i]);
            sources.put(pairs[i].getTarget1().getLocation(), pairs[i].getTarget1());
            sources.put(pairs[i].getTarget2().getLocation(), pairs[i].getTarget2());
        }
    }

    /**
     * This constructor is the comparison pair was guessed with class name.
     */
    public CertainPairComparisonResultSet(BirthmarkSet[] targetX, BirthmarkSet[] targetY, BirthmarkContext context){
        this.context = context;
        for(int i = 0; i < targetX.length; i++){
            BirthmarkSet target2 = findTarget(targetX[i].getClassName(), targetY);

            if(target2 != null){
                pairList.add(new ComparisonPair(targetX[i], target2, context));
            }
        }
        addSources(targetX);
        addSources(targetY);
    }

    /**
     * This constructor is the comparison pair was specified as mapping.
     */
    public CertainPairComparisonResultSet(BirthmarkSet[] targetX, BirthmarkSet[] targetY, Map<String, String> mapping, BirthmarkContext context){
        this.context = context;
        for(Map.Entry<String, String> entry: mapping.entrySet()){
            BirthmarkSet target1 = findTarget(entry.getKey(), targetX);
            BirthmarkSet target2 = findTarget(entry.getValue(), targetY);
            if(target1 == null && target2 == null){
                target1 = findTarget(entry.getKey(), targetY);
                target2 = findTarget(entry.getValue(), targetX);
                if(target1 != null && target2 != null){
                    // mapping table is swapped.
                    BirthmarkSet[] tmp = targetX;
                    targetX = targetY;
                    targetY = tmp;
                }
            }
            if(target1 != null && target2 != null){
                pairList.add(new ComparisonPair(target1, target2, context));
            }
        }
        addSources(targetX);
        addSources(targetY);
    }

    /**
     * return the context.
     */
    public BirthmarkContext getContext(){
        return context;
    }

    /**
     * return comparison count.
     */
    public int getComparisonCount(){
        return pairList.size();
    }

    /**
     * return the iterator of each pair.
     */
    public Iterator<ComparisonPair> iterator(){
        return pairList.iterator();
    }

    public BirthmarkSet[] getComparisonSources(){
        BirthmarkSet[] entries = new BirthmarkSet[sources.size()];
        int index = 0;
        for(Map.Entry<URL, BirthmarkSet> entry: sources.entrySet()){
            entries[index] = entry.getValue();
            index++;
        }
        return entries;
    }

    /**
     * find BirthmarkSet from given array by given class name.
     */
    private BirthmarkSet findTarget(String className, BirthmarkSet[] target){
        for(int i = 0; i < target.length; i++){
            if(className.equals(target[i].getClassName())){
                return target[i];
            }
        }
        return null;
    }

    private void addSources(BirthmarkSet[] bs){
        for(BirthmarkSet s: bs){
            sources.put(s.getLocation(), s);
        }
    }
}
