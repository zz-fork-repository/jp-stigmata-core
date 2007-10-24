package jp.naist.se.stigmata.result.history;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import jp.naist.se.stigmata.ExtractionResultSet;
import jp.naist.se.stigmata.result.MemoryExtractionResultSet;

public class MemoryExtractedBirthmarkHistory implements ExtractedBirthmarkHistory{
    private Map<String, MemoryExtractionResultSet> map = new HashMap<String, MemoryExtractionResultSet>();

    public MemoryExtractedBirthmarkHistory(){
    }

    public MemoryExtractedBirthmarkHistory(MemoryExtractionResultSet[] mersArray){
        for(MemoryExtractionResultSet mers: mersArray){
            map.put(mers.getId(), mers);
        }
    }

    public void addResultSet(MemoryExtractionResultSet mers){
        map.put(mers.getId(), mers);
    }

    public void delete(String id){
        map.remove(id);
    }

    public void deleteAll(){
        map.clear();
    }

    public ExtractionResultSet getExtractionResultSet(String id){
        return map.get(id);
    }

    public synchronized String[] getIds(){
        return map.keySet().toArray(new String[map.size()]);
    }

    public Iterator<String> iterator(){
        return Collections.unmodifiableSet(map.keySet()).iterator();
    }

    public void refresh(){
    }
}
