package jp.naist.se.stigmata.utils;

/*
 * $Id$
 */

/**
 * A rule for checking wellknown class or not.
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class WellknownClassJudgeRule{
    public static final int PREFIX_TYPE = 1;

    public static final int SUFFIX_TYPE = 2;

    public static final int MATCH_TYPE = 4;

    public static final int FULLY_TYPE = 0x10;

    public static final int PACKAGE_TYPE = 0x20;

    public static final int CLASS_NAME_TYPE = 0x40;

    public static final int EXCLUDE_TYPE = 0x80;

    private int type;

    private String name;

    public WellknownClassJudgeRule(String name, int type){
        this.name = name;
        this.type = type;
    }

    public boolean isExcludeType(){
        return getMatchPartType() == EXCLUDE_TYPE;
    }

    public boolean isFullyType(){
        return getMatchPartType() == FULLY_TYPE;
    }

    public boolean isPackageType(){
        return getMatchPartType() == PACKAGE_TYPE;
    }

    public boolean isClassNameType(){
        return getMatchPartType() == CLASS_NAME_TYPE;
    }

    public int getMatchPartType(){
        return getType() & 0xf0;
    }

    public int getMatchType(){
        return getType() & 0xf;
    }

    public int getType(){
        return type;
    }

    public String getName(){
        return name;
    }

    public String toString(){
        switch(getMatchType()){
        case PREFIX_TYPE:
            return "<prefix>" + getName() + "</prefix>";
        case SUFFIX_TYPE:
            return "<suffix>" + getName() + "</suffix>";
        case MATCH_TYPE:
            return "<match>" + getName() + "</match>";
        }
        return null;
    }
}
