package jp.naist.se.stigmata.filter;

/*
 * $Id$
 */

import jp.naist.se.stigmata.ComparisonPair;
import jp.naist.se.stigmata.spi.ComparisonPairFilterSpi;

/**
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class TargetNameComparisonPairFilter extends AbstractComparisonPairFilter{
    public static final Criterion[] CRITERIA = new Criterion[] {
        Criterion.STARTS_WITH, Criterion.NOT_STARTS_WITH, 
        Criterion.ENDS_WITH, Criterion.NOT_ENDS_WITH,
        Criterion.EQUALS_AS, Criterion.NOT_EQUALS_AS,
        Criterion.MATCH, Criterion.NOT_MATCH,
    };
    private Target target = Target.BOTH_TARGET;
    private String value;

    public TargetNameComparisonPairFilter(ComparisonPairFilterSpi service){
        super(service);
    }

    public Criterion[] getAcceptableCriteria(){
        return CRITERIA;
    }

    public boolean isFiltered(ComparisonPair pair){
        String v = value;
        if(v == null) v = "";
        boolean flag;
        if(getTarget() == Target.TARGET_1){
            flag = checkMatch(pair.getTarget1().getClassName(), v);
        }
        else if(getTarget() == Target.TARGET_2){
            flag = checkMatch(pair.getTarget2().getClassName(), v);
        }
        else{
            flag = checkMatch(pair.getTarget1().getClassName(), pair.getTarget2().getClassName(), v);
        }
        return flag;
    }

    public String getValue(){
        return value;
    }

    public void setValue(String value){
        this.value = value;
    }

    public Target getTarget(){
        return target;
    }

    public void setTarget(Target target){
        this.target = target;
    }

    private boolean checkMatch(String name1, String name2, String value){
        boolean flag1;
        boolean flag2;
        switch(getCriterion()){
        case STARTS_WITH:
            flag1 = name1.startsWith(value);
            flag2 = name2.startsWith(value);
            break;
        case ENDS_WITH:
            flag1 = name1.endsWith(value);
            flag2 = name2.endsWith(value);
            break;
        case EQUALS_AS:
            flag1 = name1.equals(value);
            flag2 = name2.equals(value);
            break;
        case NOT_EQUALS_AS:
            flag1 = !name1.equals(value);
            flag2 = !name2.equals(value);
            break;
        case MATCH:
            flag1 = name1.equals(name2);
            flag2 = flag1;
            break;
        case NOT_MATCH:
            flag1 = !name1.equals(name2);
            flag2 = flag1;
        default:
            flag1 = false;
            flag2 = false;
            break;
        }
        return (getTarget() == Target.BOTH_TARGET && (flag1 && flag2)) ||
            (getTarget() == Target.ONE_OF_TARGET && (flag1 || flag2));
    }

    private boolean checkMatch(String name, String value){
        boolean flag;
        switch(getCriterion()){
        case STARTS_WITH:
            flag = name.startsWith(value);
            break;
        case ENDS_WITH:
            flag = name.endsWith(value);
            break;
        case EQUALS_AS:
            flag = name.equals(value);
            break;
        case NOT_EQUALS_AS:
            flag = !name.equals(value);
            break;
        default:
            flag = false;
            break;
        }
        return flag;
    }

    public String toString(){
        if(getCriterion() == Criterion.MATCH || getCriterion() == Criterion.NOT_MATCH){
            String value = " match ";
            if(getCriterion() == Criterion.NOT_MATCH) value = " not match ";
            return "target1.name" + value + "target2.name";
        }
        StringBuilder sb = new StringBuilder();
        switch(getTarget()){
        case TARGET_1:      sb.append("target1.name");           break;
        case TARGET_2:      sb.append("target2.name");           break;
        case BOTH_TARGET:   sb.append("(target1&target2).name"); break;
        case ONE_OF_TARGET: sb.append("(target1|target2).name"); break; 
        }
        switch(getCriterion()){
        case STARTS_WITH:     sb.append(" starts with ");     break;
        case NOT_STARTS_WITH: sb.append(" not starts with "); break;
        case ENDS_WITH:       sb.append(" ends with ");       break;
        case NOT_ENDS_WITH:   sb.append(" not ends with ");   break;
        case EQUALS_AS:       sb.append(" equals as ");       break;
        case NOT_EQUALS_AS:   sb.append(" not equals as ");   break;
        }
        sb.append(getValue());

        return new String(sb);
    }
}
