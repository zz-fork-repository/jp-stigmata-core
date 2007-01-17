/* jbirth: A Tool for Comparing Birthmarks Extracted from Java Class Files
 * Copyright (C) 2003-2004 Haruaki TAMADA
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA 
 */
package jp.naist.se.stigmata;

/*
 * $Id$
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Abstract class for concrete [@link Birthmark <code>Birthmark</code>}
 * @author  Haruaki TAMADA
 * @version  $Revision$ $Date$
 */
public abstract class AbstractBirthmark implements Birthmark, Serializable{
    /**
     * collections for elements.
     */
    private List<BirthmarkElement> elements = new ArrayList<BirthmarkElement>();

    public void addElement(BirthmarkElement element){
        elements.add(element);
    }

    public int getElementCount(){
        int numberOfElement = 0;
        BirthmarkElement[] elements = getElements();
        if(elements != null){
            numberOfElement = elements.length;
        }
        return numberOfElement;
    }

    /**
     * @return  elements
     * @uml.property  name="elements"
     */
    public BirthmarkElement[] getElements(){
        return elements.toArray(new BirthmarkElement[elements.size()]);
    }

    public Iterator<BirthmarkElement> iterator(){
        return elements.iterator();
    }

    public abstract String getType();

    public boolean isSameType(Birthmark b){
        return getType().equals(b.getType());
    }
}
