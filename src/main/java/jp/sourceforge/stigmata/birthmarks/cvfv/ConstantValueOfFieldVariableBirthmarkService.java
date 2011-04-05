package jp.sourceforge.stigmata.birthmarks.cvfv;

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
public class ConstantValueOfFieldVariableBirthmarkService extends AbstractBirthmarkService implements BirthmarkSpi{
	private BirthmarkComparator comparator = new PlainBirthmarkComparator(this);
    private BirthmarkExtractor extractor = new ConstantValueOfFieldVariableBirthmarkExtractor(this);

    @Override
    public String getType(){
        return "cvfv";
    }

    @Override
    public String getDefaultDescription(){
        return "Field type and its initial value.";
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
    	String signature = value.substring(0, value.indexOf('='));
    	String subValue = value.substring(value.indexOf('=') + 1);
    	Object elementValue = subValue;

        if(subValue.equals("null")){
            elementValue = null;
        }
        else{
            switch(signature.charAt(0)){
            case 'Z':{
                if(value.equals("true")) elementValue = Boolean.TRUE;
                else                     elementValue = Boolean.FALSE;
                break;
            }
            case 'C': elementValue = new Character(subValue.charAt(0)); break;
            case 'D': elementValue = new Double(subValue);  break;
            case 'F': elementValue = new Float(subValue);   break;
            case 'S': elementValue = new Short(subValue);   break;
            case 'B': elementValue = new Byte(subValue);    break;
            case 'I': elementValue = new Integer(subValue); break;
            default:  elementValue = value; break;
            }
    	}
    	return new TypeAndValueBirthmarkElement(signature, elementValue);
	}
}
