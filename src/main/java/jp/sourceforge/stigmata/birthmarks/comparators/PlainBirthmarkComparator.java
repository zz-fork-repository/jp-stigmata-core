package jp.sourceforge.stigmata.birthmarks.comparators;

/*
 * $Id$
 */

import jp.sourceforge.stigmata.Birthmark;
import jp.sourceforge.stigmata.BirthmarkComparator;
import jp.sourceforge.stigmata.BirthmarkElement;
import jp.sourceforge.stigmata.spi.BirthmarkSpi;

/**
 * An implementation of {@link BirthmarkComparator
 * <code>BirthmarkComparator</code>}.  Let <i>p</i> and <i>q</i> be
 * programs, <i>f(p)</i> and <i>f(q)</i> be extracted birthmarks from
 * <i>p</i> and <i>q</i>.  Let <i>|f(p)|</i> be a element count of
 * <i>f(p)</i>.  Then, expression of comparing birthmarks algorithm of
 * this class is defined as <i>|f(p) and f(q)|/(|f(p)| + |f(q)|)</i>.
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ 
 */
public class PlainBirthmarkComparator extends AbstractBirthmarkComparator{
    public PlainBirthmarkComparator(BirthmarkSpi spi){
        super(spi);
    }

    public double compare(Birthmark b1, Birthmark b2) {
        if(!b1.getType().equals(b2.getType())){
            return Double.NaN;
        }

        BirthmarkElement[] element1 = b1.getElements();
        BirthmarkElement[] element2 = b2.getElements();
        int len = element1.length + element2.length;
        int frac = 0;
        for(int i = 0; i < element1.length && i < element2.length; i++){
            if(element1[i].equals(element2[i])){
                frac += 2;
            }
        }

        double similarity = (double)frac / (double)len;
        if(len == 0 && frac == 0){
            similarity = 1d;
        }
        return similarity;
    }

    public int getCompareCount(Birthmark b1, Birthmark b2){
        return b1.getElementCount() + b2.getElementCount();
    }
}
