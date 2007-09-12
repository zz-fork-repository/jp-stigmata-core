package jp.naist.se.stigmata;

/*
 * $Id$
 */

import jp.naist.se.stigmata.BirthmarkContext.ExtractionTarget;

/**
 * 
 * @author Haruaki Tamada
 * @version $Revision$ $Date$
 */
public class ExtractionResult{
    private BirthmarkSet[] targetX;
    private BirthmarkSet[] targetY;
    private ExtractionTarget target = ExtractionTarget.TARGET_X;

    public BirthmarkSet[] getBirthmarkSetXY(){
        if(target != ExtractionTarget.TARGET_XY){
            throw new IllegalStateException("extraction mode is not XY");
        }
        return targetX;
    }

    public void setBirthmarkSetXY(BirthmarkSet[] targetXY){
        target = ExtractionTarget.TARGET_XY;
        this.targetX = targetXY;
    }

    public BirthmarkSet[] getBirthmarkSetX(){
        if(target == ExtractionTarget.TARGET_XY){
            throw new IllegalStateException("extraction mode is XY");
        }
        return targetX;
    }

    public void setBirthmarkSetX(BirthmarkSet[] targetX){
        target = ExtractionTarget.TARGET_X;
        this.targetX = targetX;
    }

    public BirthmarkSet[] getBirthmarkSetY(){
        if(target == ExtractionTarget.TARGET_XY){
            throw new IllegalStateException("extraction mode is XY");
        }
        return targetY;
    }

    public void setBirthmarkSetY(BirthmarkSet[] targetY){
        target = ExtractionTarget.TARGET_Y;
        this.targetY = targetY;
    }

    public ExtractionTarget getExtractionTarget(){
        return target;
    }

    public void setExtractionTarget(ExtractionTarget target){
        this.target = target;
    }
}
