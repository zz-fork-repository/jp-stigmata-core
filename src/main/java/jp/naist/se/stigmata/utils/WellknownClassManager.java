package jp.naist.se.stigmata.utils;

/* 
 * $Id$
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.objectweb.asm.Opcodes;

/**
 * Managing wellknown class checking rule.
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class WellknownClassManager{
    public static final int FULLY_PREFIX_TYPE       = WellknownClassJudgeRule.FULLY_TYPE | WellknownClassJudgeRule.PREFIX_TYPE;
    public static final int FULLY_SUFFIX_TYPE       = WellknownClassJudgeRule.FULLY_TYPE | WellknownClassJudgeRule.SUFFIX_TYPE;
    public static final int FULLY_MATCH_TYPE        = WellknownClassJudgeRule.FULLY_TYPE | WellknownClassJudgeRule.MATCH_TYPE;
    public static final int PACKAGE_PREFIX_TYPE     = WellknownClassJudgeRule.PACKAGE_TYPE | WellknownClassJudgeRule.PREFIX_TYPE;
    public static final int PACKAGE_SUFFIX_TYPE     = WellknownClassJudgeRule.PACKAGE_TYPE | WellknownClassJudgeRule.SUFFIX_TYPE;
    public static final int PACKAGE_MATCH_TYPE      = WellknownClassJudgeRule.PACKAGE_TYPE | WellknownClassJudgeRule.MATCH_TYPE;
    public static final int CLASS_NAME_PREFIX_TYPE  = WellknownClassJudgeRule.CLASS_NAME_TYPE | WellknownClassJudgeRule.PREFIX_TYPE;
    public static final int CLASS_NAME_SUFFIX_TYPE  = WellknownClassJudgeRule.CLASS_NAME_TYPE | WellknownClassJudgeRule.SUFFIX_TYPE;
    public static final int CLASS_NAME_MATCH_TYPE   = WellknownClassJudgeRule.CLASS_NAME_TYPE | WellknownClassJudgeRule.MATCH_TYPE;

    private List<WellknownClassJudgeRule> systemClassesList = new ArrayList<WellknownClassJudgeRule>();
    private List<WellknownClassJudgeRule> excludes = new ArrayList<WellknownClassJudgeRule>();

    public WellknownClassManager(){
    }

    public WellknownClassManager(WellknownClassManager manager){
        systemClassesList = new ArrayList<WellknownClassJudgeRule>(manager.systemClassesList);
        excludes = new ArrayList<WellknownClassJudgeRule>(manager.excludes);
    }

    public void remove(String value, int type){
        int index = -1;
        for(int i = 0; i < systemClassesList.size(); i++){ 
            WellknownClassJudgeRule section = (WellknownClassJudgeRule)systemClassesList.get(i);
            if(section.getName().equals(value) && section.getType() == type){
                index = i;
                break;
            }
        }
        systemClassesList.remove(index);
    }

    public void clear(){
        systemClassesList.clear();
        excludes.clear();
    }

    public WellknownClassJudgeRule[] getSections(){
        List<WellknownClassJudgeRule> sections = new ArrayList<WellknownClassJudgeRule>();
        sections.addAll(excludes);
        sections.addAll(systemClassesList);
        return sections.toArray(new WellknownClassJudgeRule[sections.size()]);
    }

    public void add(WellknownClassJudgeRule section){
        if(section.isExcludeType()){
            excludes.add(section);
        }
        else{
            systemClassesList.add(section);
        }
    }

    private boolean checkSystemClass(String className){
        String fully = className.replace('/', '.');
        int index = className.lastIndexOf('.');
        String cn = className.substring(index + 1);
        String pn = "";
        if(index > 0){
            pn = fully.substring(0, index - 1);
        }

        if(isExcludes(fully)){
            return false;
        }

        for(Iterator i = systemClassesList.iterator(); i.hasNext(); ){
            WellknownClassJudgeRule section = (WellknownClassJudgeRule)i.next();
            String target = fully;
            if(section.isClassNameType()){
                target = cn;
            }
            else if(section.isPackageType()){
                target = pn;
            }
            switch(section.getMatchType()){
            case WellknownClassJudgeRule.PREFIX_TYPE:
                if(target.startsWith(section.getName())){
                    return true;
                }
                break;
            case WellknownClassJudgeRule.SUFFIX_TYPE:
                if(target.endsWith(section.getName())){
                    return true;
                }
                break;
            case WellknownClassJudgeRule.MATCH_TYPE:
                if(target.equals(section.getName())){
                    return true;
                }
                break;
            }
        }
        return false;
    }

    private boolean isExcludes(String fully){
        for(Iterator i = excludes.iterator(); i.hasNext(); ){
            WellknownClassJudgeRule s = (WellknownClassJudgeRule)i.next();
            switch(s.getMatchType()){
            case WellknownClassJudgeRule.PREFIX_TYPE:
                if(fully.startsWith(s.getName())){
                    return true;
                }
                break;
            case WellknownClassJudgeRule.SUFFIX_TYPE:
                if(fully.endsWith(s.getName())){
                    return true;
                }
                break;
            case WellknownClassJudgeRule.MATCH_TYPE:
                if(fully.equals(s.getName())){
                    return true;
                }
                break;
            }
        }
        return false;
    }

    /**
     * 以下のどれかのメソッドであれば true を返す．
     * <ul>
     *   <li><code>public static void main(String[])</code></li>
     *   <li><code>static void &lt;clinit&gt;(void)</code>(static イニシャライザ)</li>
     *   <li><code>void &lt;init&gt;</code>(コンストラクタ)</li>
     * </ul>
     */
    private boolean checkSystemMethod(int access, String methodName, String signature){
        if(methodName.equals("main")){
            return signature.equals("([Ljava/lang/String;)V") && 
                checkAccess(access, Opcodes.ACC_PUBLIC);
        }
        else if(methodName.equals("<clinit>")){
            return signature.equals("()V") &&
                checkAccess(access, Opcodes.ACC_STATIC);
        }
        else if(methodName.equals("<init>")){
            return !checkAccess(access, Opcodes.ACC_STATIC);
        }
        return false;
    }

    /**
     * <code>static final long serialVersionUID</code> かどうかを判定する．
     */
    private boolean checkSystemField(int access, String fieldName, String signature){
        if(fieldName.equals("serialVersionUID")){
            return checkAccess(access, Opcodes.ACC_STATIC) &&
                checkAccess(access, Opcodes.ACC_FINAL) &&
                signature.equals("J");
        }

        return false;
    }

    public boolean isWellKnownClass(String className){
        return checkSystemClass(className);
    }

    public boolean isSystemMethod(int access, String methodName, String signature){
        return checkSystemMethod(access, methodName, signature);
    }

    /**
     * <code>static final long serialVersionUID</code> かどうかを判定する．
     */
    public boolean isSystemField(int access, String fieldName, String signature){
        return checkSystemField(access, fieldName, signature);
    }

    private boolean checkAccess(int access, int code){
        return (access & code) == code;
    }
}
