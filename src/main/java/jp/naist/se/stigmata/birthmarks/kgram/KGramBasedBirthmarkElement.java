package jp.naist.se.stigmata.birthmarks.kgram;

/*
 * $Id$
 */

import jp.naist.se.stigmata.BirthmarkElement;

/**
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class KGramBasedBirthmarkElement extends BirthmarkElement{
    private static final long serialVersionUID = 28546543857543634L;

    private KGram kgram;

    public KGramBasedBirthmarkElement(KGram kgram){
        super(kgram.toString());
        this.kgram = kgram;
    }

    public boolean equals(Object o){
        return o instanceof KGramBasedBirthmarkElement &&
            kgram.equals(((KGramBasedBirthmarkElement)o).kgram);
    }

    public int hashCode(){
        int v = kgram.hashCode();

        return (v & 0xff << 24) | (v >> 8); 
    }
}
