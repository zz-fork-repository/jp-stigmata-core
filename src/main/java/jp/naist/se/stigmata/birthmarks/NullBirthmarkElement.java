package jp.naist.se.stigmata.birthmarks;

/*
 * $Id$
 */

import jp.naist.se.stigmata.BirthmarkElement;

/**
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

    public boolean equals(Object o){
        return o instanceof NullBirthmarkElement;
    }

    public String toString(){
        return "<null>";
    }
}
