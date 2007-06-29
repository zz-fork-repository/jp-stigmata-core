package jp.naist.se.stigmata.birthmarks.comparators;

/*
 * $Id$
 */

import java.util.HashMap;
import java.util.Map;

import jp.naist.se.stigmata.Birthmark;
import jp.naist.se.stigmata.BirthmarkElement;
import jp.naist.se.stigmata.birthmarks.ValueCountable;
import jp.naist.se.stigmata.spi.BirthmarkSpi;

/**
 * Comparing birthmarks by cosine similarity algorithm. This class compares
 * birthmarks which must be implemented
 * {@link ValueCountable <code>ValueCountable</code>} interface.
 * 
 * @author Haruaki Tamada
 * @version $Revision$ $Date$
 */
public class CosineSimilarityBirthmarkComparator extends
        AbstractBirthmarkComparator{

    public CosineSimilarityBirthmarkComparator(BirthmarkSpi spi){
        super(spi);
    }

    public double compare(Birthmark b1, Birthmark b2){
        if(!b1.getType().equals(b2.getType())){
            return Double.NaN;
        }
        if(b1.getElementCount() == 0 && b2.getElementCount() == 0){
            return 1d;
        }
        else if(b1.getElementCount() == 0 || b2.getElementCount() == 0){
            return 0d;
        }

        Map<String, CountPair> pairs = new HashMap<String, CountPair>();
        addCount(pairs, b1, true);
        addCount(pairs, b2, false);

        double norm1 = norm(pairs, true);
        double norm2 = norm(pairs, false);
        double product = innerproduct(pairs);
        double similarity = product / (norm1 * norm2);
        // System.out.printf("%g / (%g * %g) = %g%n", product, norm1, norm2, similarity);

        // double radian = Math.acos(product / (norm1 * norm2));
        // double angle = 90 - (180 * radian / Math.PI);
        // double sim = angle / 90;
        // System.out.printf("angle: %g (%g“x, %g)%n", radian, angle, sim);

        return similarity;
    }

    private double innerproduct(Map<String, CountPair> pairs){
        double sum = 0;
        for(CountPair pair: pairs.values()){
            sum += pair.get(true) * pair.get(false);
        }
        return sum;
    }

    private double norm(Map<String, CountPair> pairs, boolean first){
        double sum = 0;
        for(CountPair pair: pairs.values()){
            sum += pair.get(first) * pair.get(first);
        }
        return Math.sqrt(sum);
    }

    private void addCount(Map<String, CountPair> pairs, Birthmark birthmark, boolean first){
        for(BirthmarkElement element: birthmark){
            ValueCountable vc = (ValueCountable)element;
            CountPair cp = pairs.get(vc.getValueName());
            if(cp == null){
                cp = new CountPair();
                pairs.put(vc.getValueName(), cp);
            }
            cp.set(first, vc.getValueCount());
        }
    }

    private class CountPair{
        private int c1 = 0;
        private int c2 = 0;

        public int get(boolean first){
            if(first){
                return c1;
            }
            else{
                return c2;
            }
        }

        public void set(boolean first, int count){
            if(first){
                c1 = count;
            }
            else{
                c2 = count;
            }
        }
    }

    /**
     * This method is used for debugging.
     */
    @SuppressWarnings("unused")
    private void printAll(Map<String, CountPair> pairs){
        System.out.println("----------");
        for(Map.Entry<String, CountPair> entry: pairs.entrySet()){
            CountPair pair = entry.getValue();
            System.out.printf("%40s: %5d, %5d%n", entry.getKey(), pair.get(true), pair.get(false));
        }
    }
}
