package jp.naist.se.stigmata.filter;

/*
 * $Id$
 */

import java.util.ArrayList;
import java.util.List;

import jp.naist.se.stigmata.ComparisonPair;
import jp.naist.se.stigmata.spi.ComparisonPairFilterSpi;

/**
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class BirthmarkElementCountComparisonPairFilter extends AbstractComparisonPairFilter{
    private static final List<Criterion> CRITERIA = new ArrayList<Criterion>();

    private int threshold = 0;
    private Target target;
    private String birthmarkType;

    static{
        CRITERIA.add(Criterion.GREATER_EQUALS);
        CRITERIA.add(Criterion.GREATER_THAN);
        CRITERIA.add(Criterion.LESS_EQUALS);
        CRITERIA.add(Criterion.LESS_THAN);
        CRITERIA.add(Criterion.EQUALS_AS);
        CRITERIA.add(Criterion.NOT_EQUALS_AS);
    }

    public BirthmarkElementCountComparisonPairFilter(ComparisonPairFilterSpi service){
        super(service);
    }

    public String getBirthmarkType(){
        return birthmarkType;
    }

    public void setBirthmarkType(String birthmarkType){
        this.birthmarkType = birthmarkType;
    }

    public static Criterion[] getValidCriteria(){
        return CRITERIA.toArray(new Criterion[CRITERIA.size()]);
    }

    public Criterion[] getAcceptableCriteria(){
        return getValidCriteria();
    }

    private boolean isFilteredTwo(ComparisonPair pair){
        boolean flag = false;

        String type = getBirthmarkType();
        if(pair.getTarget1().hasBirthmark(type) && pair.getTarget2().hasBirthmark(type)){
            int elem1 = pair.getTarget1().getBirthmark(type).getElementCount();
            int elem2 = pair.getTarget2().getBirthmark(type).getElementCount();

            switch(getCriterion()){
            case GREATER_EQUALS:
                flag = (target == Target.BOTH_TARGET && elem1 >= threshold && elem2 >= threshold) ||
                (target == Target.ONE_OF_TARGET && (elem1 >= threshold || elem2 >= threshold));
                break;
            case GREATER_THAN:
                flag = (target == Target.BOTH_TARGET && elem1 > threshold && elem2 > threshold) ||
                    (target == Target.ONE_OF_TARGET && (elem1 > threshold || elem2 > threshold));
                break;
            case LESS_EQUALS:
                flag = (target == Target.BOTH_TARGET && elem1 <= threshold && elem2 <= threshold) ||
                (target == Target.ONE_OF_TARGET && (elem1 <= threshold || elem2 <= threshold));
                break;
            case LESS_THAN:
                flag = (target == Target.BOTH_TARGET && elem1 < threshold && elem2 < threshold) ||
                    (target == Target.ONE_OF_TARGET && (elem1 < threshold || elem2 < threshold));
                break;
            case EQUALS_AS:
                flag = (target == Target.BOTH_TARGET && elem1 == threshold && elem2 == threshold) ||
                (target == Target.ONE_OF_TARGET && (elem1 == threshold || elem2 == threshold));
                break;
            case NOT_EQUALS_AS:
                flag = (target == Target.BOTH_TARGET && elem1 != threshold && elem2 != threshold) ||
                    (target == Target.ONE_OF_TARGET && (elem1 != threshold || elem2 != threshold));
                break;
            default:
                flag = false;
                break;
            }
        }
        return flag;
    }

    public boolean isFiltered(ComparisonPair pair){
        if(target == Target.BOTH_TARGET || target == Target.ONE_OF_TARGET){
            return isFilteredTwo(pair);
        }
        boolean flag = false;
        String type = getBirthmarkType();
        if(pair.getTarget1().hasBirthmark(type) && pair.getTarget2().hasBirthmark(type)){
            int total = 0;
            int threshold = getThreshold();
            if(target == Target.TARGET_1){
                total = pair.getTarget1().getBirthmark(type).getElementCount();
            }
            if(target == Target.TARGET_2){
                total = pair.getTarget2().getBirthmark(type).getElementCount();
            }
            switch(getCriterion()){
            case GREATER_EQUALS:
                flag = total >= threshold;
                break;
            case GREATER_THAN:
                flag = total > threshold;
                break;
            case LESS_EQUALS:
                flag = total <= threshold;
                break;
            case LESS_THAN:
                flag = total < threshold;
                break;
            case EQUALS_AS:
                flag = total == threshold;
                break;
            case NOT_EQUALS_AS:
                flag = total != threshold;
                break;
            default:
                flag = false;
                break;
            }
        }
        return flag;
    }

    public int getThreshold(){
        return threshold;
    }

    public void setThreshold(int threshold){
        if(threshold < 0){
            throw new IllegalArgumentException("threshold must be positive value: " + threshold);
        }
        this.threshold = threshold;
    }

    public Target getTarget(){
        return target;
    }

    public void setTarget(Target target){
        this.target = target;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        switch(getTarget()){
        case TARGET_1:      sb.append("target1"); break;
        case TARGET_2:      sb.append("target2"); break;
        case BOTH_TARGET:   sb.append("(target1&target2)");    break;
        case ONE_OF_TARGET: sb.append("(target1|target2)");
        }
        sb.append(".").append(birthmarkType);
        sb.append(".size");
        switch(getCriterion()){
        case GREATER_EQUALS: sb.append(" >= "); break;
        case GREATER_THAN:   sb.append(" >  "); break;
        case LESS_EQUALS:    sb.append(" <= "); break;
        case LESS_THAN:      sb.append(" <  "); break;
        case EQUALS_AS:      sb.append(" == "); break;
        case NOT_EQUALS_AS:  sb.append(" != "); break;
        }
        sb.append(Integer.toString(getThreshold()));

        return new String(sb);
    }
}
