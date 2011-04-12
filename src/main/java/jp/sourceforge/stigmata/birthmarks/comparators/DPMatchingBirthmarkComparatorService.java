package jp.sourceforge.stigmata.birthmarks.comparators;

import jp.sourceforge.stigmata.BirthmarkComparator;
import jp.sourceforge.stigmata.spi.BirthmarkService;

/**
 * SPI of {@link BirthmarkComparator <code>BirthmarkComparator</code>}.
 * Comparing birthmarks by DP matching algorithm.
 *
 * @author Haruaki TAMADA
 */
public class DPMatchingBirthmarkComparatorService extends AbstractBirthmarkComparatorService{
    /**
     * returns a type of the birthmark this service provides.
     */
    @Override
    public String getType(){
        return "dpmatching";
    }

    @Override
    public String getComparatorClassName(){
        return DPMatchingBirthmarkComparator.class.getName();
    }

    /**
     * returns a extractor for the birthmark of this service.
     */
    @Override
    public BirthmarkComparator getComparator(BirthmarkService service){
        return new DPMatchingBirthmarkComparator(service);
    }
}

