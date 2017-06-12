package org.lucee.extension.chart.util;

import java.util.Locale;

import lucee.loader.engine.CFMLEngine;
import lucee.loader.engine.CFMLEngineFactory;
import lucee.runtime.PageContext;
import lucee.runtime.exp.PageException;
import lucee.runtime.ext.function.BIF;

public class CurrencyUtil {
	
	public static String format(PageContext pc,double number, String type, Locale locale) throws PageException {
		CFMLEngine engine = CFMLEngineFactory.getInstance();
		
		try {
			BIF bif = CFMLEngineFactory.getInstance().getClassUtil().loadBIF(pc, "lucee.runtime.functions.international.LSCurrencyFormat");
			return (String)bif.invoke(pc, new Object[]{number,type,locale});
		}
		catch (Exception e) {
			throw engine.getCastUtil().toPageException(e);
		}
		
		
	}

}
