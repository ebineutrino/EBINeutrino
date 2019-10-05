package org.sdk.utils;

import org.sdk.EBISystem;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: ebicrm
 * Date: 08.06.2010
 * Time: 20:47:39
 * To change this template use File | Settings | File Templates.
 */

public class EBIDBLocking {

    public EBIDBLocking(){}


    public boolean lockRecord(final int id, final String table){
       boolean success = true;
        try {
            final PreparedStatement ps = EBISystem.db().initPreparedStatement("INSERT INTO EBIPESSIMISTIC (RECORDID,MODULENAME,LOCKDATE,STATUS,USER) values (?,?,?,?,?) ");
            ps.setInt(1,id);
            ps.setString(2,table);
            ps.setTimestamp(3, new Timestamp(new Date().getTime()));
            ps.setInt(4,1);
            ps.setString(5,EBISystem.ebiUser);
            EBISystem.db().executePreparedStmt(ps);

        } catch (final SQLException e) {
            success = false;
            e.printStackTrace();
        }

        return success;
    }

    public boolean unlockRecord(final int id, final String table){
       boolean success;
        try {

            PreparedStatement ps = EBISystem.db().initPreparedStatement(" SELECT RECORDIS,MODULENAME,USER FROM EBIPESSIMISTIC WHERE RECORDID=? AND MODULENAME=? AND USER=?");
            ps.setInt(1,id);
            ps.setString(2,table);
            ps.setString(3,EBISystem.ebiUser);
            final ResultSet set =  EBISystem.db().executePreparedQuery(ps);

            set.last();
            if(set.getRow() > 0){
                ps = EBISystem.db().initPreparedStatement(" DELETE FROM EBIPESSIMISTIC WHERE RECORDID=? AND MODULENAME=? ");
                ps.setInt(1,id);
                ps.setString(2,table);
                success = EBISystem.db().executePreparedStmt(ps);

            }else{
                success = false;
            }
        } catch (final SQLException e) {
            success = false;
            e.printStackTrace();
        }

        return success;
    }

    public boolean forceUnlock(final int id, final String table){
        boolean success;

        try {

            final PreparedStatement ps = EBISystem.db().initPreparedStatement(" DELETE FROM EBIPESSIMISTIC WHERE RECORDID=? AND MODULENAME=? ");
            ps.setInt(1,id);
            ps.setString(2,table);
            success = EBISystem.db().executePreparedStmt(ps);

        } catch (final SQLException e) {
            success = false;
            e.printStackTrace();
        }

        return success;
    }

    public boolean isRecordLocked(final int id, final String table){
        boolean success = false;
        try {

            final PreparedStatement ps = EBISystem.db().initPreparedStatement(" SELECT RECORDIS,MODULENAME,USER FROM EBIPESSIMISTIC WHERE RECORDID=? AND MODULENAME=? ");
            ps.setInt(1,id);
            ps.setString(2,table);
            final ResultSet set =  EBISystem.db().executePreparedQuery(ps);

            set.last();
            if(set.getRow() > 0){
                success = true;
            }

        } catch (final SQLException e) {
            success = false;
            e.printStackTrace();
        }

       return success;
    }

}
