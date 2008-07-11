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
 * @version $Revision$ 
 */
public class SequentialMethodCallBirthmarkService extends AbstractBirthmarkService implements BirthmarkSpi{
    private BirthmarkComparator comparator = new PlainBirthmarkComparator(this);
    private BirthmarkExtractor extractor = new SequentialMethodCallBirthmarkExtractor(this);

    public String getType(){
        return "smc";
    }

    public String getDefaultDescription(){
        return "Sequence of method call which order is appeared in method definition.";
    }

    public BirthmarkExtractor getExtractor(){
        return extractor;
    }

    public BirthmarkComparator getComparator(){
        return comparator;
    }

    public boolean isExpert(){
        return false;
    }

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
