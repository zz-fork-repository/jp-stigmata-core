package jp.naist.se.stigmata;

/*
 * $Id$
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents comparison pair.
 * @author  Haruaki TAMADA
 * @version  $Revision$ $Date$
 */
public class ComparisonPair implements Iterable<ComparisonPairElement>{
    private BirthmarkSet target1;
    private BirthmarkSet target2;
    private BirthmarkEnvironment environment;

    /**
     * constructor.
     */
    public ComparisonPair(BirthmarkSet target1, BirthmarkSet target2, BirthmarkEnvironment environment){
        this.target1 = target1;
        this.target2 = target2;
        this.environment = environment;

        if(target1.getBirthmarksCount() != target2.getBirthmarksCount()){
            throw new IllegalArgumentException("birthmark count is not matched");
        }
    }

    /**
     * return a target.
     * @see  #getTarget2()
     */
    public BirthmarkSet getTarget1(){
        return target1;
    }

    /**
     * return other target
     * @see  #getTarget1()
     */
    public BirthmarkSet getTarget2(){
        return target2;
    }

    /**
     * calculate similarity between target1 and target2.
     */
    public double calculateSimilarity(){
        double similarity = 0d;
        int count = 0;
        for(ComparisonPairElement elem: this){
            double sim = elem.getSimilarity();
            if(!Double.isNaN(sim) && !Double.isInfinite(sim)){
                similarity += sim;
                count++;
            }
        }
        return similarity / count;
    }

    /**
     * Return the number of birthmarks contained a target.
     * Note that other target must have same birthmarks.
     */
    public int getBirthmarksCount(){
        return target1.getBirthmarksCount();
    }

    /**
     * return the iterator for comparing each birthmarks.
     */
    public synchronized Iterator<ComparisonPairElement> iterator(){
        List<ComparisonPairElement> list = new ArrayList<ComparisonPairElement>();
        for(Iterator<String> i = target1.birthmarkTypes(); i.hasNext(); ){
            String type = i.next();

            Birthmark b1 = target1.getBirthmark(type);
            Birthmark b2 = target2.getBirthmark(type);
            if(b1 != null && b2 != null){
                list.add(new ComparisonPairElement(b1, b2, environment.getService(type).getComparator()));
            }
        }
        return list.iterator();
    }
}
