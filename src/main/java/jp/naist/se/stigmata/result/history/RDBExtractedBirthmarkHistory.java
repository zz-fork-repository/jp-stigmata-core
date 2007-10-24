package jp.naist.se.stigmata.result.history;

/*
 * $Id$
 */

import java.sql.SQLException;
import java.util.Iterator;

import javax.sql.DataSource;

import jp.naist.se.stigmata.ExtractionResultSet;
import jp.naist.se.stigmata.result.RDBExtractionResultSet;
import jp.naist.se.stigmata.result.RDBExtractionResultSet.StringHandler;
import jp.naist.se.stigmata.utils.ArrayIterator;

import org.apache.commons.dbutils.QueryRunner;

/**
 * 
 * @author Haruaki Tamada
 * @version $Revision$ $Date$
 */
public class RDBExtractedBirthmarkHistory implements ExtractedBirthmarkHistory{
    private DataSource source;

    public RDBExtractedBirthmarkHistory(DataSource source){
        this.source = source;
    }

    public void delete(String id){
        QueryRunner runner = new QueryRunner(source);

        try{
            runner.update("DELETE FROM EXTRACTED_BIRTHMARK_TYPES WHERE EXTRACTED_ID=?", id);
            runner.update("DELETE FROM EXTRACTED_BIRTHMARK WHERE EXTRACTED_ID=?", id);
            runner.update("DELETE FROM EXTRACTED_BIRTHMARKS WHERE EXTRACTED_ID=?", id);
        } catch(SQLException e){
        }
    }

    public void deleteAll(){
        QueryRunner runner = new QueryRunner(source);
        try{
            runner.update("DELETE FROM EXTRACTED_BIRTHMARK_TYPES");
            runner.update("DELETE FROM EXTRACTED_BIRTHMARK");
            runner.update("DELETE FROM EXTRACTED_BIRTHMARKS");
        } catch(SQLException e){
        }
    }

    public ExtractionResultSet getExtractionResultSet(String id){
        return new RDBExtractionResultSet(source, id);
    }

    public String[] getIds(){
        QueryRunner runner = new QueryRunner(source);
        try{
            String[] ids = (String[])runner.query(
                "SELECT EXTRACTED_ID FROM EXTRACTED_BIRTHMARKS", new StringHandler()
            );
            return ids;
        } catch(SQLException e){
        }
        return new String[0];
    }

    public Iterator<String> iterator(){
        return new ArrayIterator<String>(getIds());
    }

    public void refresh(){
        // do nothing.
    }
}
