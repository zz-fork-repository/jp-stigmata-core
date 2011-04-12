package jp.sourceforge.stigmata.birthmarks.comparators;

import jp.sourceforge.stigmata.BirthmarkComparator;
import jp.sourceforge.stigmata.spi.BirthmarkService;

/**
 * SPI of {@link BirthmarkComparator <code>BirthmarkComparator</code>}.
 * Comparing birthmarks by logical AND algorithm.
 *
 * @author Haruaki TAMADA
 */
public class LogicalAndBirthmarkComparatorService extends AbstractBirthmarkComparatorService{
    /**
     * returns a type of the birthmark this service provides.
     */
    @Override
    public String getType(){
        return "and";
    }

    @Override
    public String getComparatorClassName(){
        return LogicalAndBirthmarkComparator.class.getName();
    }

    /**
     * returns a extractor for the birthmark of this service.
     */
    @Override
    public BirthmarkComparator getComparator(BirthmarkService service){
        return new LogicalAndBirthmarkComparator(service);
    }
}

