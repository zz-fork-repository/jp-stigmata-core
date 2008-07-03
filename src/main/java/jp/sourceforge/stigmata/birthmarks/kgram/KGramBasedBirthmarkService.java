package jp.sourceforge.stigmata.birthmarks.kgram;

/*
 * $Id$
 */

import jp.sourceforge.stigmata.BirthmarkComparator;
import jp.sourceforge.stigmata.BirthmarkElement;
import jp.sourceforge.stigmata.BirthmarkExtractor;
import jp.sourceforge.stigmata.birthmarks.AbstractBirthmarkService;
import jp.sourceforge.stigmata.birthmarks.comparators.LogicalAndBirthmarkComparator;
import jp.sourceforge.stigmata.spi.BirthmarkSpi;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class KGramBasedBirthmarkService extends AbstractBirthmarkService implements BirthmarkSpi{
    private BirthmarkComparator comparator = new LogicalAndBirthmarkComparator(this);
    private BirthmarkExtractor extractor = new KGramBasedBirthmarkExtractor(this);

    public String getType(){
        return "kgram";
    }

    public String getDefaultDescription(){
        return "k-gram based birthmark.";
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
		value = value.trim();
		if(value.startsWith("{") && value.endsWith("}")){
			String[] param = value.substring(1, value.length() - 1).split(", *");
			KGram<Integer> kgram = new KGram<Integer>(param.length);
			for(int i = 0; i < param.length; i++){
				kgram.set(i, new Integer(param[i].trim()));
			}
			return new KGramBasedBirthmarkElement<Integer>(kgram);
		}
		return null;
	}
}
