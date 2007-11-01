package jp.naist.se.stigmata.hook;

/*
 * $Id$
 */

/**
 * 
 * @author Haruaki Tamada
 * @version $Revision$ $Date$
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