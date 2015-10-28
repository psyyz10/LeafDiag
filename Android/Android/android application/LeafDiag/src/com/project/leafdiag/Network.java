package com.project.leafdiag;
import java.io.File;
import java.io.IOException;

import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;


@SuppressWarnings("deprecation")
public class Network {
	public String getXML(File image) throws ClientProtocolException, IOException {

		//Connect to the server
		HttpClient client = new DefaultHttpClient();
		client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		 
		//Create a POST request
		HttpPost        post   = new HttpPost( "http://10.154.135.169:8080/WebApplication1/CallRecog");
		MultipartEntity entity = new MultipartEntity( HttpMultipartMode.BROWSER_COMPATIBLE );
		 
		//Upload image
		FileBody img = new FileBody(image);
		entity.addPart( "img", img);
		post.setEntity( entity );
		
		//Execute post and get the response
		String response = "";
		try {
			response = EntityUtils.toString( client.execute( post ).getEntity(), "UTF-8" );
		} catch (Exception e) {
			System.out.println("Failed to get the response");
		}
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;	
	}
}