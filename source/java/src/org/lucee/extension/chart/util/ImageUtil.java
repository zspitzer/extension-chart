package org.lucee.extension.chart.util;

import java.awt.Color;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.awt.image.renderable.ParameterBlock;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageOutputStream;
import javax.media.jai.BorderExtenderConstant;
import javax.media.jai.JAI;
import javax.media.jai.LookupTableJAI;

import lucee.runtime.exp.PageException;

public class ImageUtil {
	
	private static final int QUALITY=1;

	
	/**
	 * add a border to image
	 * @param thickness
	 * @param color 
	 * @param color
	 * @param borderType 
	 * @return 
	 */
	public static BufferedImage addBorder(BufferedImage img,int thickness, Color color)  throws PageException{
		ColorModel cm = img.getColorModel();
		if (((cm instanceof IndexColorModel)) && (cm.hasAlpha()) && (!cm.isAlphaPremultiplied())) {
			img=paletteToARGB(img);
			cm = img.getColorModel();
		}

		BufferedImage alpha = null;
		if ((cm.getNumComponents() > 3) && (cm.hasAlpha())) {
			alpha = getAlpha(img);
			img=removeAlpha(img);
		}
		if (alpha != null) {
			ParameterBlock params1 = new ParameterBlock();
			params1.addSource(alpha);
			
			params1.add(thickness); // left
			params1.add(thickness); // right
			params1.add(thickness); // top
			params1.add(thickness); // bottom
			params1.add(new BorderExtenderConstant(new double[] { 255D }));
			
			RenderingHints hints = new RenderingHints(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			hints.add(new RenderingHints(JAI.KEY_REPLACE_INDEX_COLOR_MODEL, Boolean.TRUE));
			alpha = JAI.create("border", params1, hints).getAsBufferedImage();
		}

	    ParameterBlock params = new ParameterBlock();
	    params.addSource(img);
	    params.add(thickness); // left
		params.add(thickness); // right
		params.add(thickness); // top
		params.add(thickness); // bottom
	    params.add(toBorderExtender(color));

	    img=JAI.create("border", params).getAsBufferedImage();

	    if (alpha != null) {
	      img=addAlpha(img, alpha, thickness, thickness);
	    }
	    return img;
	}
	

	private static Object toBorderExtender(Color color) {
		double[] colorArray = { color.getRed(), color.getGreen(), color.getBlue() };
		return new BorderExtenderConstant(colorArray);
	}
	
	private static BufferedImage addAlpha(BufferedImage src, BufferedImage alpha, int x, int y) {
    	int w = src.getWidth();
    	int h = src.getHeight();
    	BufferedImage bi = new BufferedImage(w, h, 2);
    	WritableRaster wr = bi.getWritableTile(0, 0);
    	WritableRaster wr3 = wr.createWritableChild(0, 0, w, h, 0, 0, new int[] { 0, 1, 2 });
    	WritableRaster wr1 = wr.createWritableChild(0, 0, w, h, 0, 0, new int[] { 3 });
    	wr3.setRect(src.getData());
    	wr1.setRect(alpha.getData());
    	bi.releaseWritableTile(0, 0);
    	return bi;
    }
	
    private static BufferedImage getAlpha(BufferedImage src) {
    	return JAI.create("bandselect", src, new int[] { 3 }).getAsBufferedImage();
    }
    
    private static BufferedImage removeAlpha(BufferedImage src) {
    	return JAI.create("bandselect", src, new int[] { 0, 1, 2 }).getAsBufferedImage();
    }
    
    private static BufferedImage paletteToARGB(BufferedImage src) {
    	IndexColorModel icm = (IndexColorModel) src.getColorModel();
    	int bands = icm.hasAlpha()?4:3;
    	
    	byte[][] data = new byte[bands][icm.getMapSize()];
    	if (icm.hasAlpha()) icm.getAlphas(data[3]);
    	icm.getReds(data[0]);
    	icm.getGreens(data[1]);
    	icm.getBlues(data[2]);
    	LookupTableJAI rtable = new LookupTableJAI(data);
    	return JAI.create("lookup", src, rtable).getAsBufferedImage();
    }


	public static void writeOut(BufferedImage bi,OutputStream os) throws IOException, PageException {
		ImageOutputStream ios = ImageIO.createImageOutputStream(os);
		try{
			_writeOut(bi,ios);
		}
		finally{
			try{ios.close();}catch (Exception t){}
		}
	}
	private static void _writeOut(BufferedImage im, ImageOutputStream ios) throws IOException, PageException {
		IIOMetadata meta = null;
		
		ImageWriter writer = null;
    	ImageTypeSpecifier type =ImageTypeSpecifier.createFromRenderedImage(im);
    	Iterator<ImageWriter> iter = ImageIO.getImageWriters(type, "png");
    	
    	
    	if (iter.hasNext()) {
    		writer = iter.next();
    	}
    	if (writer == null) throw new IOException("no writer for format [png] found!");
    	
    	
		ImageWriteParam iwp = writer.getDefaultWriteParam();
    	
		try {iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);}catch (Exception t) {}
		try {iwp.setCompressionQuality(QUALITY);}catch (Exception t) {}
		writer.setOutput(ios);
    	try {
    		writer.write(meta, new IIOImage(im, null, meta), iwp);
    		
    	} 
    	finally {
    		writer.dispose();
    		ios.flush();
    	}
	}
}
