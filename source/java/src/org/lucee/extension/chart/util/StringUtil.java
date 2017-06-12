package org.lucee.extension.chart.util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class StringUtil {
	public static Set<String> toWordList(String list) {
        if(list.length()==0) return new HashSet<String>();
        int len=list.length();
        int last=0;
        Set<String> rtn = new HashSet<String>();
        for(int i=0;i<len;i++) {
            if(Character.isWhitespace(list.charAt(i))) {
                rtn.add(list.substring(last,i));
                last=i+1;
            }
        }
        if(last<=len)rtn.add(list.substring(last));
        return rtn;
    }
}
