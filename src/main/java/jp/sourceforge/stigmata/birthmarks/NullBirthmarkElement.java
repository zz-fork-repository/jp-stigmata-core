package jp.sourceforge.stigmata.birthmarks;

/*
 * $Id$
 */

import jp.sourceforge.stigmata.BirthmarkElement;

/**
 * Null birthmark element.
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$ 
 */
public class NullBirthmarkElement extends BirthmarkElement{
    private static final long serialVersionUID = -92345638932523L;

    private static final NullBirthmarkElement ELEMENT = new NullBirthmarkElement();

    private NullBirthmarkElement(){
        super(null);
    }

    public static BirthmarkElement getInstance(){
        return ELEMENT;
    }

    public int hashCode(){
        return 0;
    }

    public boolean equals(Object o){
        return o instanceof NullBirthmarkElement;
    }

    public String toString(){
        return "<null>";
    }
}
