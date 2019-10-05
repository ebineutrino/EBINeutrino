package org.modules.utils;

import org.modules.EBIModule;
import org.sdk.EBISystem;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimerTask;

public class EBITimerTaskFixRate extends TimerTask {

	private EBIModule ebiModule = null;
	
	public EBITimerTaskFixRate(final EBIModule module){
		ebiModule = module;
	}
	
	@Override
	public void run(){
		ResultSet set = null;
		final Calendar date1 = Calendar.getInstance();
		date1.setTime(new Date());
		date1.set(Calendar.HOUR_OF_DAY, 0);
		
		final Calendar date2 = Calendar.getInstance();
		date2.setTime(new Date());
		date2.set(Calendar.HOUR_OF_DAY, 23);
		
		try{
			final PreparedStatement ps = EBISystem.getInstance().
                    iDB().initPreparedStatement("SELECT * FROM COMPANYACTIVITIES " +
																	" WHERE DUEDATE BETWEEN ? AND ? AND TIMERDISABLED=? AND CREATEDFROM=?");
	
			ps.setTimestamp(1, new java.sql.Timestamp(date1.getTimeInMillis()));
			ps.setTimestamp(2, new java.sql.Timestamp(date2.getTimeInMillis()));
			ps.setInt(3, 0);
			ps.setString(4, EBISystem.ebiUser);
			
			set = EBISystem.getInstance().iDB().executePreparedQuery(ps);
			
			set.last();
			if(set.getRow() != ebiModule.allertTimer.getCountTask()){
				ebiModule.allertTimer.setUpAvailableTimer();
			}
			set.close();
		}catch(final SQLException ex){
			ex.printStackTrace();
		}
			
	}
}
