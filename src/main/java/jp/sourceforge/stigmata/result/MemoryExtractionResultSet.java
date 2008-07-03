package jp.sourceforge.stigmata.result;

/*
 * $Id$
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jp.sourceforge.stigmata.BirthmarkContext;
import jp.sourceforge.stigmata.BirthmarkSet;
import jp.sourceforge.stigmata.BirthmarkStoreTarget;
import jp.sourceforge.stigmata.ExtractionTarget;
import jp.sourceforge.stigmata.utils.MultipleIterator;

/**
 * 
 * @author Haruaki Tamada
 * @version $Revision$ $Date$
 */
public class MemoryExtractionResultSet extends AbstractExtractionResultSet{
    private List<BirthmarkSet> targetX = new ArrayList<BirthmarkSet>();
    private List<BirthmarkSet> targetY = new ArrayList<BirthmarkSet>();

    MemoryExtractionResultSet(BirthmarkContext context){
        this(context, true);
    }

    MemoryExtractionResultSet(BirthmarkContext context, boolean tableType){
        super(context, tableType);
    }

    public BirthmarkStoreTarget getStoreTarget(){
        return BirthmarkStoreTarget.MEMORY;
    }

    public void removeAllBirthmarkSets(ExtractionTarget target){
        switch(target){
        case TARGET_X:
        case TARGET_XY:
            targetX.clear();
            break;
        case TARGET_Y:
            targetY.clear();
            break;
        case TARGET_BOTH:
        default:
            throw new IllegalArgumentException("unknown target: " + target);
        }
    }

    public void removeBirthmarkSet(ExtractionTarget target, BirthmarkSet set){
        switch(target){
        case TARGET_X:
        case TARGET_XY:
            targetX.remove(set);
            break;
        case TARGET_Y:
            targetY.remove(set);
            break;
        case TARGET_BOTH:
        default:
            throw new IllegalArgumentException("unknown target: " + target);
        }
    }

    public void addBirthmarkSet(ExtractionTarget target, BirthmarkSet set){
        switch(target){
        case TARGET_X:
        case TARGET_XY:
            targetX.add(set);
            break;
        case TARGET_Y:
            targetY.add(set);
            break;
        case TARGET_BOTH:
        default:
            throw new IllegalArgumentException("unknown target: " + target);
        }
    }

    public Iterator<BirthmarkSet> birthmarkSets(ExtractionTarget target){
        Iterator<BirthmarkSet> iterator;
        switch(target){
        case TARGET_X:
        case TARGET_XY:
            iterator = targetX.iterator();
            break;
        case TARGET_Y:
            iterator = targetY.iterator();
            break;
        case TARGET_BOTH:
        default:
            MultipleIterator<BirthmarkSet> i = new MultipleIterator<BirthmarkSet>();
            i.add(targetX.iterator());
            i.add(targetY.iterator());
            iterator = i;
            break;
        }
        return iterator;
    }
    public int getBirthmarkSetSize(ExtractionTarget target){
        int count = 0;
        switch(target){
        case TARGET_X:
        case TARGET_XY:
            count = targetX.size();
            break;
        case TARGET_Y:
            count = targetY.size();
            break;
        case TARGET_BOTH:
        default:
            count = targetX.size() + targetY.size();
            break;
        }
        return count;
    }

    public BirthmarkSet getBirthmarkSet(ExtractionTarget target, int index){
        BirthmarkSet set;
        switch(target){
        case TARGET_X:
            set = targetX.get(index);
            break;
        case TARGET_Y:
            set = targetY.get(index);
            break;
        case TARGET_XY:
        case TARGET_BOTH:
        default:
            if(index < targetX.size()){
                set = targetX.get(index);
            }
            else{
                set = targetY.get(index - targetX.size());
            }
            break;
        }
        return set;
    }

    public BirthmarkSet[] getBirthmarkSets(ExtractionTarget target){
        BirthmarkSet[] sets;
        switch(target){
        case TARGET_X:
        case TARGET_XY:
            sets = targetX.toArray(new BirthmarkSet[targetX.size()]);
            break;
        case TARGET_Y:
            sets = targetY.toArray(new BirthmarkSet[targetX.size()]);
            break;
        case TARGET_BOTH:
        default:
            sets = new BirthmarkSet[targetX.size() + targetY.size()];
            System.arraycopy(targetX.toArray(new BirthmarkSet[targetX.size()]), 0, sets, 0, targetX.size());
            System.arraycopy(targetY.toArray(new BirthmarkSet[targetY.size()]), 0, sets, targetX.size(), targetY.size());
            break;
        }
        return sets;
    }
}
