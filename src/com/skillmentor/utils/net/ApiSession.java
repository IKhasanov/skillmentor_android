package com.skillmentor.utils.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.skillmentor.models.Advert;

import android.graphics.drawable.Drawable;
import android.util.Log;

public class ApiSession {

	private static final String serverURI = "http://skillmentor.cloudapp.net";
	
	public List<Advert> advertList()
	{
		final String methodURI = "/listings/offer";
		final String uri = formUriString(serverURI,methodURI);
		JSONArray jsn = null;
		
		List<Advert> result = new ArrayList<Advert>();
		
		try {
			try {
				jsn = sendGetRequest(uri.toString());
				JSONArray arr = jsn;
				for (int i = 0; i < arr.length(); i++) {
					Advert advert = new Advert();
					advert.map(arr.getJSONObject(i));
					result.add(advert);
				}
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
		
	}

	public List<Advert> requestList()
	{
		final String methodURI = "/listings/request";
		final String uri = formUriString(serverURI,methodURI);
		JSONArray jsn = null;
		
		List<Advert> result = new ArrayList<Advert>();
		
		try {
			try {
				jsn = sendGetRequest(uri.toString());
				JSONArray arr = jsn;
				for (int i = 0; i < arr.length(); i++) {
					Advert advert = new Advert();
					advert.map(arr.getJSONObject(i));
					result.add(advert);
				}
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public void createAdvert(String title, String description, String location, String type) {
		final String methodURI = "/listings/create";
		final String uri = formUriString(serverURI,methodURI);
		JSONArray jsn = null;
		
		List<Advert> result = new ArrayList<Advert>();
		
		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
	        nameValuePairs.add(new BasicNameValuePair("title", title));
	        nameValuePairs.add(new BasicNameValuePair("description", description));
	        nameValuePairs.add(new BasicNameValuePair("location", location));
	        nameValuePairs.add(new BasicNameValuePair("type", type));

			try {
				jsn = sendPostRequest(uri.toString(), new UrlEncodedFormEntity(nameValuePairs,"utf-8"));
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private JSONArray sendAPIRequest(final HttpUriRequest request) throws ClientProtocolException, IOException, JSONException {
		BufferedReader in = null;

		try {
			final HttpClient client = new DefaultHttpClient();
			final HttpResponse response = client.execute(request);

			in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			final StringBuilder jsnStr = new StringBuilder("");
			String line = "";

			while ((line = in.readLine()) != null) {
				jsnStr.append(line);
			}

			System.out.println("Respond = " + jsnStr.toString());

			final JSONArray jsnObj = new JSONArray(jsnStr.toString());

			return jsnObj;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (final IOException e) {
					final StackTraceElement[] stack = e.getStackTrace();
					final StackTraceElement lastElem = stack[stack.length - 1];
					Log.e(lastElem.getClassName() + '.' + lastElem.getMethodName() + ": Error while closing input stream", e.getMessage());
				}
			}
		}
	}

	private JSONArray sendGetRequest(final String uri) throws ClientProtocolException, IOException, JSONException, URISyntaxException {
		final HttpGet request = new HttpGet();
		request.setURI(new URI(uri));

		return sendAPIRequest(request);
	}

	private JSONArray sendPostRequest(final String uri, final HttpEntity entity) throws ClientProtocolException, IOException, JSONException, URISyntaxException {
		final HttpPost request = new HttpPost();
		request.setURI(new URI(uri));
		if (entity != null) {
			request.setEntity(entity);
		}

		return sendAPIRequest(request);
	}

	public static Drawable loadImageFromAPI(final String url) {
		try {
			final InputStream is = (InputStream) new URL(serverURI + url).getContent();
			final Drawable d = Drawable.createFromStream(is, "Skillmentor API");
			System.out.println("Loaded image from" + serverURI + url);
			return d;
		} catch (final Exception e) {
			final StackTraceElement[] stack = e.getStackTrace();
			final StackTraceElement lastElem = stack[stack.length - 1];
			Log.e(lastElem.getClassName() + '.' + lastElem.getMethodName() + ": Error in load image", "Can't load image from url " + url);
			return null;
		}
	}

	private String formUriString(final Object... args) {
		final StringBuilder uri = new StringBuilder("");

		for (final Object arg : args) {
			uri.append(arg);
		}

		return uri.toString();
	}
}
