package jp.naist.se.stigmata;

import java.util.Iterator;

public interface ExtractionResultSet extends Iterable<BirthmarkSet>{
    public boolean isTableType();

    public void setTableType(boolean flag);

    public BirthmarkEnvironment getEnvironment();

    public BirthmarkContext getContext();

    public String[] getBirthmarkTypes();

    public int getBirthmarkSetSize();

    public Iterator<BirthmarkSet> iterator();

    public BirthmarkSet getBirthmarkSet(int index);

    public BirthmarkSet getBirthmarkSet(String name);

    public BirthmarkSet[] getBirthmarkSets();

    public int getBirthmarkSetSize(ExtractionTarget target);

    public void removeBirthmarkSet(BirthmarkSet bs);

    public void removeAllBirthmarkSets();

    public Iterator<BirthmarkSet> birthmarkSets(ExtractionTarget target);

    public BirthmarkSet getBirthmarkSet(ExtractionTarget target, int index);

    public BirthmarkSet getBirthmarkSet(ExtractionTarget target, String setname);

    public BirthmarkSet[] getBirthmarkSets(ExtractionTarget target);

    public void addBirthmarkSet(ExtractionTarget target, BirthmarkSet set);

    public void setBirthmarkSets(ExtractionTarget target, BirthmarkSet[] sets);

    public void removeBirthmarkSet(ExtractionTarget target, BirthmarkSet set);

    public void removeAllBirthmarkSets(ExtractionTarget target);
}
