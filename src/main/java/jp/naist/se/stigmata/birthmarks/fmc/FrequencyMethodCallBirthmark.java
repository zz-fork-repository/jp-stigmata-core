package jp.naist.se.stigmata.birthmarks.fmc;

/*
 * $Id$
 */

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import jp.naist.se.stigmata.AbstractBirthmark;
import jp.naist.se.stigmata.BirthmarkElement;
import jp.naist.se.stigmata.utils.ArrayIterator;

/**
 * 
 * @author Haruaki Tamada
 * @version $Revision$ $Date$
 */
public class FrequencyMethodCallBirthmark extends AbstractBirthmark{
    private static final long serialVersionUID = 1905526895627693908L;

    private Map<String, FrequencyOfMethodBirthmarkElement> counts = new HashMap<String, FrequencyOfMethodBirthmarkElement>();
    private String type;

    public FrequencyMethodCallBirthmark(String type){
        this.type = type;
    }

    @Override
    public int getElementCount(){
        return counts.size();
    }

    @Override
    public synchronized BirthmarkElement[] getElements(){
        FrequencyOfMethodBirthmarkElement[] elements = new FrequencyOfMethodBirthmarkElement[counts.size()];
        int index = 0;
        for(Map.Entry<String, FrequencyOfMethodBirthmarkElement> entry: counts.entrySet()){
            elements[index] = entry.getValue();
            index++;
        }
        Arrays.sort(elements, new Comparator<FrequencyOfMethodBirthmarkElement>(){
            public int compare(FrequencyOfMethodBirthmarkElement o1, FrequencyOfMethodBirthmarkElement o2){
                return o1.getValueName().compareTo(o2.getValueName());
            }
        });
        
        return elements;
    }

    @Override
    public Iterator<BirthmarkElement> iterator(){
        return new ArrayIterator<BirthmarkElement>(getElements());
    }

    public void addElement(BirthmarkElement element){
        if(element instanceof FrequencyOfMethodBirthmarkElement){
            FrequencyOfMethodBirthmarkElement e = (FrequencyOfMethodBirthmarkElement)element;
            FrequencyOfMethodBirthmarkElement foundElement = counts.get(e.getValueName());
            if(foundElement != null){
                foundElement.incrementValueCount();
            }
            else{
                foundElement = e;
            }
            counts.put(e.getValueName(), foundElement);
        }        
    }

    @Override
    public String getType(){
        return type;
    }
}
