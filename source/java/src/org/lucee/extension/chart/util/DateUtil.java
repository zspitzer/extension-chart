package org.lucee.extension.chart.util;

import java.util.TimeZone;

import lucee.loader.engine.CFMLEngine;
import lucee.loader.engine.CFMLEngineFactory;
import lucee.runtime.PageContext;
import lucee.runtime.exp.PageException;
import lucee.runtime.ext.function.BIF;
import lucee.runtime.type.dt.DateTime;

public class DateUtil {
	
	// TODO make this internally
	public static String format(PageContext pc, DateTime date, TimeZone tz) throws PageException {
		CFMLEngine engine = CFMLEngineFactory.getInstance();
		
		try {
			BIF dateFormat = engine.getClassUtil().loadBIF(pc, "lucee.runtime.functions.displayFormatting.DateFormat");
			BIF timeFormat = engine.getClassUtil().loadBIF(pc, "lucee.runtime.functions.displayFormatting.TimeFormat");

			return dateFormat.invoke(pc, new Object[]{date,"short",tz})
					+" "+
					timeFormat.invoke(pc, new Object[]{date,"short",tz});
		}
		catch (Exception e) {
			throw engine.getCastUtil().toPageException(e);
		}
	}

	// TODO make this internally
	public static void dateAdd(PageContext pc, String datepart, double number, DateTime date) throws PageException {
		CFMLEngine engine = CFMLEngineFactory.getInstance();
		
		try {
			BIF bif = engine.getClassUtil().loadBIF(pc, "lucee.runtime.functions.dateTime.DateAdd");
			engine.getCastUtil().toDoubleValue(bif.invoke(pc, new Object[]{datepart, number, date}));
		}
		catch (Exception e) {
			throw engine.getCastUtil().toPageException(e);
		}
	}

	// TODO make this internally
	public static String lsDateFormat(PageContext pc, DateTime d) throws PageException {
		CFMLEngine engine = CFMLEngineFactory.getInstance();
		
		try {
			BIF bif = CFMLEngineFactory.getInstance().getClassUtil()
					.loadBIF(pc, "lucee.runtime.functions.international.LSDateFormat");
				return (String) bif.invoke(pc, new Object[]{d});
		}
		catch (Exception e) {
			throw engine.getCastUtil().toPageException(e);
		}
		
		
		
	}

}
