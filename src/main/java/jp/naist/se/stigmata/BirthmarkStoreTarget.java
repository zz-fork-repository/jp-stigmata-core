package jp.naist.se.stigmata;

/*
 * $Id$
 */

/**
 * This class represents how to store extracted birthmarks.
 * memory?, databases?, or files?
 * 
 * @author Haruaki Tamada
 * @version $Revision$ $Date$
 */
public enum BirthmarkStoreTarget{
    MEMORY,
    XMLFILE,
    MEMORY_SINGLE,
    RDB,
//    DERBY,
//    FILE,
//    MYSQL, 
//    POSTGRESQL,
}
