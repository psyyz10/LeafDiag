package com.project.leafdiag;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class ImageDetails {

	private File picture = null;
	private static ImageDetails dtlsObject;
	XMLparser parser = new XMLparser();
	
	public void fillInInfo(File pic) throws ParserConfigurationException, SAXException, IOException {
		dtlsObject = parser.parse(pic);
		picture = pic;
	}
	
	public static synchronized ImageDetails getSingletonObject() {
		if (dtlsObject == null) {
			dtlsObject = new ImageDetails();
		}
		return dtlsObject;
	}

	public File getPicture() {
		return picture;
	}
	

}
