package jp.sourceforge.stigmata.filter;

/*
 * $Id$
 */

import jp.sourceforge.stigmata.ComparisonPairFilter;
import jp.sourceforge.stigmata.spi.ComparisonPairFilterSpi;

/**
 * 
 * @author Haruaki TAMADA
 */
public abstract class AbstractComparisonPairFilter implements ComparisonPairFilter{
    private ComparisonPairFilterSpi service;
    private Criterion criterion = Criterion.EQUALS_AS;

    public AbstractComparisonPairFilter(ComparisonPairFilterSpi service){
        this.service = service;
    }

    @Override
    public ComparisonPairFilterSpi getService(){
        return service;
    }

    @Override
    public Criterion getCriterion(){
        return criterion;
    }

    @Override
    public void setCriterion(Criterion criterion){
        if(!isAcceptable(criterion)){
            throw new IllegalArgumentException("illegal criterion: "
                    + criterion + ": accepts only " + getAcceptableCriteria());
        }
        this.criterion = criterion;
    }

    @Override
    public boolean isAcceptable(Criterion criterion){
        Criterion[] criteria = getAcceptableCriteria();
        for(int i = 0; i < criteria.length; i++){
            if(criteria[i] == criterion){
                return true;
            }
        }
        return false;
    }
}
