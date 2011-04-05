package jp.sourceforge.stigmata.birthmarks.smc;

/*
 * $Id$
 */

import jp.sourceforge.stigmata.BirthmarkComparator;
import jp.sourceforge.stigmata.BirthmarkElement;
import jp.sourceforge.stigmata.BirthmarkExtractor;
import jp.sourceforge.stigmata.birthmarks.AbstractBirthmarkService;
import jp.sourceforge.stigmata.birthmarks.comparators.PlainBirthmarkComparator;
import jp.sourceforge.stigmata.spi.BirthmarkSpi;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 */
public class SequentialMethodCallBirthmarkService extends AbstractBirthmarkService implements BirthmarkSpi{
    private BirthmarkComparator comparator = new PlainBirthmarkComparator(this);
    private BirthmarkExtractor extractor = new SequentialMethodCallBirthmarkExtractor(this);

    @Override
    public String getType(){
        return "smc";
    }

    @Override
    public String getDefaultDescription(){
        return "Sequence of method call which order is appeared in method definition.";
    }

    @Override
    public BirthmarkExtractor getExtractor(){
        return extractor;
    }

    @Override
    public BirthmarkComparator getComparator(){
        return comparator;
    }

    @Override
    public boolean isExperimental(){
        return false;
    }

    @Override
    public boolean isUserDefined(){
        return false;
    }

	@Override
	public BirthmarkElement buildBirthmarkElement(String value) {
		String className = value.substring(0, value.indexOf('#'));
		String methodName = value.substring(value.indexOf('#') + 1, value.lastIndexOf('!'));
		String signature = value.substring(value.lastIndexOf('!') + 1);

		return new MethodCallBirthmarkElement(className, methodName, signature);
	}
}
