package com.project.leafdiag;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.app.Activity;
import android.os.StrictMode;
import android.widget.Toast;

public class XMLparser extends Activity {

	@SuppressWarnings("finally")
	public ImageDetails parse(File imgFile) throws ParserConfigurationException,
			SAXException, IOException {
		final ImageDetails imgDetails = ImageDetails.getSingletonObject();
		Network connection = new Network();
		File image = imgFile;

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		try {
			connection.getXML(image);
		} catch (Exception e) {
			System.out.println("Failed to receive XML");
			Toast.makeText(this, "Error- Failed to contact server", Toast.LENGTH_LONG).show();
		}
		finally {
			return imgDetails;
		}
	}
}
