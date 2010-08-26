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
 * @version $Revision$ 
 */
public class KGramBasedBirthmarkService extends AbstractBirthmarkService implements BirthmarkSpi{
    private BirthmarkComparator comparator = new LogicalAndBirthmarkComparator(this);
    private BirthmarkExtractor extractor = new KGramBasedBirthmarkExtractor(this);

    @Override
    public String getType(){
        return "kgram";
    }

    @Override
    public String getDefaultDescription(){
        return "k-gram based birthmark.";
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
