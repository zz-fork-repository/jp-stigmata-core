package jp.sourceforge.stigmata.hook;

/*
 * $Id$
 */

/**
 * 
 * @author Haruaki Tamada
 * @version $Revision$ 
 */
public enum Phase{
    SETUP,
    TEAR_DOWN,
    BEFORE_EXTRACTION,
    AFTER_EXTRACTION,
    BEFORE_COMPARISON,
    AFTER_COMPARISON,
    BEFORE_FILTERING,
    AFTER_FILTERING,
}