package jp.naist.se.stigmata.birthmarks;

/*
 * $Id$
 */

import jp.naist.se.stigmata.AbstractBirthmark;

/**
 *
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class PlainBirthmark extends AbstractBirthmark{
    private static final long serialVersionUID = 2370435723234463456L;

    private String type;

    public PlainBirthmark(String type){
        this.type = type;
    }

    public void setType(String type){
        this.type = type;
    }

    @Override
    public String getType(){
        return type;
    }
}
