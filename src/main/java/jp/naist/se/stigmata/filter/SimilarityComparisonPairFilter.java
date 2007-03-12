package jp.naist.se.stigmata.filter;

/*
 * $Id$
 */

import jp.naist.se.stigmata.ComparisonPair;
import jp.naist.se.stigmata.spi.ComparisonPairFilterSpi;

/**
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class SimilarityComparisonPairFilter extends AbstractComparisonPairFilter{
    private static final double EQUALS_THRESHOLD = 5E-5;

    public static Criterion[] CRITERIA = new Criterion[] {
        Criterion.GREATER_EQUALS, Criterion.GREATER_THAN,
        Criterion.LESS_EQUALS, Criterion.LESS_THAN,
        Criterion.EQUALS_AS, Criterion.NOT_EQUALS_AS, 
    };


    private double threshold;

    public SimilarityComparisonPairFilter(ComparisonPairFilterSpi service){
        super(service);
        setThreshold(0.8d);
    }
    
    public boolean isFiltered(ComparisonPair pair){
        double similarity = pair.calculateSimilarity();
        boolean flag;
        switch(getCriterion()){
        case GREATER_EQUALS:
            flag = similarity >= getThreshold();
            break;
        case GREATER_THAN:
            flag = similarity > getThreshold();
            break;
        case LESS_EQUALS:
            flag = similarity <= getThreshold();
            break;
        case LESS_THAN:
            flag = similarity < getThreshold();
            break;
        case EQUALS_AS:
            flag = (similarity - getThreshold()) <= EQUALS_THRESHOLD;
            break;
        case NOT_EQUALS_AS:
            flag = (similarity - getThreshold()) > EQUALS_THRESHOLD;
            break;
        default:
            flag = false;
            break;
        }
        return flag;
    }

    public Criterion[] getAcceptableCriteria(){
        return CRITERIA;
    }

    public double getThreshold(){
        return threshold;
    }

    public void setThreshold(double threshold){
        if(threshold < 0d || threshold >= 1.0d){
            throw new IllegalArgumentException("threshold must be 0.0-1.0");
        }
        this.threshold = threshold;
    }

    public String toString(){
        StringBuffer sb = new StringBuffer();
        sb.append("similarity");
        switch(getCriterion()){
        case GREATER_EQUALS: sb.append(" >= "); break;
        case GREATER_THAN:   sb.append(" >  "); break;
        case LESS_EQUALS:    sb.append(" <= "); break;
        case LESS_THAN:      sb.append(" <  "); break;
        case EQUALS_AS:      sb.append(" == "); break;
        case NOT_EQUALS_AS:  sb.append(" != "); break;
        }
        sb.append(getThreshold());
        return new String(sb);
    }
}
