package org.lucee.extension.chart.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

import lucee.loader.engine.CFMLEngineFactory;
import lucee.loader.util.Util;
import lucee.runtime.exp.PageException;

public class JavaUtil {
	
	public static String serialize(Serializable o) throws IOException, PageException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        serialize(o, baos);
        return CFMLEngineFactory.getInstance().getCastUtil().toBase64(baos.toByteArray());
    }
	
	public static void serialize(Serializable o, OutputStream os) throws IOException {
        ObjectOutputStream oos=null;
        try {
	        oos = new ObjectOutputStream(os);
	        oos.writeObject(o);
        }
        finally {
           Util.closeEL(oos);
           Util.closeEL(os);
        }
    }
}
