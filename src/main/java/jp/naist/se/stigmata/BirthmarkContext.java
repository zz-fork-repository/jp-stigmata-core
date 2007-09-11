package jp.naist.se.stigmata;

/*
 * $Id$
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jp.naist.se.stigmata.event.OperationType;

/**
 * Runtime properties.
 * 
 * @author Haruaki Tamada
 * @version $Revision$ $Date$
 */
class BirthmarkContext{
    public enum ExtractionTarget{
        TARGET_X, TARGET_Y, TARGET_XY;
    };
    private List<String> birthmarkTypes = new ArrayList<String>();
    private ComparisonMethod method = ComparisonMethod.ROUND_ROBIN;
    private ExtractionTarget et = ExtractionTarget.TARGET_X;
    private List<String> filterTypes = new ArrayList<String>();
    private Map<String, String> nameMappings = new HashMap<String, String>();
    private ExtractionUnit unit = ExtractionUnit.CLASS;
    private OperationType operation;

    public BirthmarkContext(OperationType type){
        setOperation(type);
    }

    public OperationType getOperation(){
        return operation;
    }

    public void setOperation(OperationType operation){
        this.operation = operation;
    }

    public void addNameMapping(String name1, String name2){
        nameMappings.put(name1, name2);
    }

    public void removeNameMapping(String name1){
        nameMappings.remove(name1);
    }

    public Map<String, String> getNameMappings(){
        return Collections.unmodifiableMap(nameMappings);
    }

    public void setExtractionTarget(ExtractionTarget target){
        this.et = target;
    }

    public ExtractionTarget getExtractionTarget(){
        return et;
    }

    public void addExtractionType(String type){
        birthmarkTypes.add(type);
    }

    public void removeExtractionType(String type){
        birthmarkTypes.remove(type);
    }

    public synchronized String[] getExtractionTypes(){
        return birthmarkTypes.toArray(new String[getExtractionTypeCount()]);
    }

    public int getExtractionTypeCount(){
        return birthmarkTypes.size();
    }

    public ComparisonMethod getComparisonMethod(){
        return method;
    }

    public void setComparisonMethod(ComparisonMethod method){
        this.method = method;
    }

    public ExtractionUnit getExtractionUnit(){
        return unit;
    }

    public void setExtractionUnit(ExtractionUnit unit){
        this.unit = unit;
    }

    public void addFilterType(String filterType){
        filterTypes.add(filterType);
    }

    public void removeFilterType(String filterType){
        filterTypes.remove(filterType);
    }

    public synchronized String[] getFilterTypes(){
        return filterTypes.toArray(new String[getFilterTypesCount()]);
    }

    public Iterator<String> filterTypes(){
        return filterTypes.iterator();
    }

    public int getFilterTypesCount(){
        return filterTypes.size();
    }
}
