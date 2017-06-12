package org.lucee.extension.chart.util;

import lucee.commons.io.res.Resource;
import lucee.commons.io.res.filter.ResourceFilter;

public class FileUtil {
	
	/**
	 * return the size of the Resource, other than method length of Resource this mthod return the size of all files in a directory
	 * @param collectionDir
	 * @return
	 */
	public static long getRealSize(Resource res) {
		return getRealSize(res,null);
	}
	
	/**
	 * return the size of the Resource, other than method length of Resource this mthod return the size of all files in a directory
	 * @param collectionDir
	 * @return
	 */
	public static long getRealSize(Resource res, ResourceFilter filter) {
		if(res.isFile()) {
			return res.length();
		}
		else if(res.isDirectory()) {
			long size=0;
			Resource[] children = filter==null?res.listResources():res.listResources(filter);
			for(int i=0;i<children.length;i++) {
				size+=getRealSize(children[i]);
			}
			return size;
		}
		
		return 0;
	}
}
