package jp.sourceforge.stigmata;

/*
 * $Id$
 */

/**
 * This enum type represents birthmark extraction unit.
 * 
 * The name of
 * {@link jp.sourceforge.stigmata.BirthmarkSet <code>BirthmarkSet</code>} will be
 * class name, method name, package name, or product name.
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public enum ExtractionUnit{
    CLASS,
    PACKAGE,
    ARCHIVE,
    @Deprecated
    METHOD,
}