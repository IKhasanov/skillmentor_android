package com.skillmentor.utils.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.drawable.Drawable;
import android.util.Log;

public class ApiSession {

	private static final String serverURI = "http://skillmentor.cloudapp.net/";

	private JSONObject sendAPIRequest(final HttpUriRequest request) throws ClientProtocolException, IOException, JSONException {
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

			final JSONObject jsnObj = new JSONObject(jsnStr.toString());

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

	private JSONObject sendGetRequest(final String uri) throws ClientProtocolException, IOException, JSONException, URISyntaxException {
		final HttpGet request = new HttpGet();
		request.setURI(new URI(uri));

		return sendAPIRequest(request);
	}

	private JSONObject sendPostRequest(final String uri, final HttpEntity entity) throws ClientProtocolException, IOException, JSONException, URISyntaxException {
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
