/**
 *
 * Copyright (c) 2014, the Railo Company Ltd. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * 
 **/
package org.lucee.extension.chart;

import java.util.Locale;

import org.lucee.extension.chart.util.CurrencyUtil;
import org.lucee.extension.chart.util.DateUtil;

import lucee.loader.engine.CFMLEngine;
import lucee.loader.engine.CFMLEngineFactory;
import lucee.runtime.PageContext;
import lucee.runtime.exp.PageException;
import lucee.runtime.ext.function.BIF;
import lucee.runtime.type.dt.DateTime;
import lucee.runtime.util.Cast;

public class LabelFormatUtil {


	public static final int LABEL_FORMAT_NUMBER = 0;
	public static final int LABEL_FORMAT_CURRENCY = 1;
	public static final int LABEL_FORMAT_PERCENT = 2;
	public static final int LABEL_FORMAT_DATE = 3;

	public static String formatDate(PageContext pc,double value) {
		Cast caster = CFMLEngineFactory.getInstance().getCastUtil();
		DateTime d = caster.toDate(caster.toDouble(value),true,null,null);
		
		try {
			return DateUtil.lsDateFormat(pc,d);
			
		} 
		catch (PageException pe) {}
		return caster.toString(d,null);
	}

	public static String formatNumber(double value) {
		return CFMLEngineFactory.getInstance().getCastUtil().toString(value);
	}

	public static String formatPercent(double value) {
		return CFMLEngineFactory.getInstance().getCastUtil().toIntValue(value*100)+" %";
	}

	public static String formatCurrency(PageContext pc,double value) {
		//PageContext pc = Thread LocalPageContext.get();
		Locale locale=pc==null?Locale.US:pc.getLocale();
		try {
			return CurrencyUtil.format(pc,value,"local",locale);
		}
		catch (Exception e) {
			CFMLEngine engine=CFMLEngineFactory.getInstance();
			throw engine.getExceptionUtil().createPageRuntimeException(engine.getCastUtil().toPageException(e));
		}
	}

	public static String format(int labelFormat, double value) {
		
		switch(labelFormat) {
		case LABEL_FORMAT_CURRENCY:	return formatCurrency(CFMLEngineFactory.getInstance().getThreadPageContext(),value);
		case LABEL_FORMAT_DATE:		return formatDate(CFMLEngineFactory.getInstance().getThreadPageContext(),value);
		case LABEL_FORMAT_NUMBER:	return formatNumber(value);
		case LABEL_FORMAT_PERCENT:	return formatPercent(value);
		}
		return CFMLEngineFactory.getInstance().getCastUtil().toString(value);
	}
}