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

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.text.AttributedString;
import java.util.Iterator;
import java.util.List;

import lucee.loader.engine.CFMLEngine;
import lucee.loader.engine.CFMLEngineFactory;

import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.data.general.PieDataset;
import org.lucee.extension.chart.util.StringUtil;

public class PieSectionLegendLabelGeneratorImpl implements
		PieSectionLabelGenerator {


	private FontMetrics metrics;
	private int with;

	public PieSectionLegendLabelGeneratorImpl(Font font,int with) {
		Graphics2D graphics = new BufferedImage(1, 1,BufferedImage.TYPE_INT_ARGB).createGraphics();
		this.metrics=graphics.getFontMetrics(font);
		this.with=with-20;
	}

	@Override
	public AttributedString generateAttributedSectionLabel(PieDataset dataset, Comparable key) {
		return null;
	}

	@Override
	public String generateSectionLabel(PieDataset pd, Comparable c) {
		CFMLEngine engine = CFMLEngineFactory.getInstance();
		String value=engine.getCastUtil().toString(pd.getKey(pd.getIndex(c)),"");
		Iterator<String> it = engine.getListUtil().toListRemoveEmpty(value, '\n').iterator();
		
		StringBuilder sb=new StringBuilder();
		String line;
		int lineLen;
		while(it.hasNext()) {
			line=it.next();
			lineLen=metrics.stringWidth(line);
			if(lineLen>with) {
				reorganize(sb,it,new StringBuilder(line));
				break;
			}
			if(sb.length()>0)sb.append('\n');
			sb.append(line);
		}
		
		
		
		//int strLen = metrics.stringWidth(value);
		return sb.toString();//metrics.stringWidth(value)+"-"+with+":"+value;
		//return "StringUtil.reverse()";
	}

	private void reorganize(StringBuilder sb, Iterator<String> it, StringBuilder rest) {
		// fill rest
		String item;
		while(it.hasNext()) {
			item=it.next();
			rest.append('\n');
			rest.append(item);
		}
		
		Iterator<String> words = StringUtil.toWordList(rest.toString()).iterator();
		StringBuffer line=new StringBuffer();
		
		while(words.hasNext()) {
			item=words.next();
			
			if(line.length()>0 && metrics.stringWidth(item.concat(" ").concat(line.toString()))>with) {
				if(sb.length()>0) sb.append('\n');
				sb.append(line);
				//print.out("line:"+line);
				line=new StringBuffer(item);
			}
			else {
				//item=words.next();
				if(line.length()>0)line.append(' ');
				line.append(item);
			}
		}
		if(line.length()>0){
			if(sb.length()>0) sb.append('\n');
			sb.append(line);
		}
	}
}