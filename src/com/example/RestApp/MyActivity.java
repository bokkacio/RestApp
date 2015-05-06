package com.example.RestApp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MyActivity extends Activity {
    private String RES_LOG = "json_";

    private String Rest1 = "https://bugzilla.mozilla.org/rest/bug/35";
    private String Rest2 = "https://bugzilla.mozilla.org/rest/bug/707428/comment";
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void mainOnClick(View view){
        Log.d(RES_LOG, "mainOnClick");
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    Log.d(RES_LOG, "Thread run()");
                    String result = readBugzilla(Rest1);
                    Log.d(RES_LOG, result);

                    //A modifiable set of name/value mappings. Names are unique, non-null strings. Values may be any mix of JSONObjects, JSONArrays, Strings, Booleans, Integers, Longs, Doubles or NULL. Values may not be null, NaNs, infinities, or of any type not listed here.
                    //Creates a new JSONObject with name/value mappings from the JSON string.
                    JsonParser parser = new JsonParser(new JSONObject(result));
                    Log.d(RES_LOG, parser.GetJsonArrayElement("bugs", "assigned_to"));
                } catch (Exception ex) {
                    Log.d(RES_LOG, ex.getMessage());
                }
            }
        };

        thread.start();
    }

    private String readBugzilla(String url) {
        StringBuilder builder = new StringBuilder();

        //Interface for an HTTP client. HTTP clients encapsulate a smorgasbord of objects required to execute HTTP requests while handling cookies, authentication, connection management, and other features. Thread safety of HTTP clients depends on the implementation and configuration of the specific client.
        HttpClient client = new DefaultHttpClient();

        //The GET method means retrieve whatever information (in the form of an entity) is identified by the Request-URI. If the Request-URI refers to a data-producing process, it is the produced data which shall be returned as the entity in the response and not the source text of the process, unless that text happens to be the output of the process.
        //GetMethods will follow redirect requests from the http server by default. This behavour can be disabled by calling setFollowRedirects(false).
        HttpGet httpGet = new HttpGet(url);

        try {
            //the response to the request. This is always a final response, never an intermediate response with an 1xx status code. Whether redirects or authentication challenges will be returned or handled automatically depends on the implementation and configuration of this client.
            HttpResponse response = client.execute(httpGet);

            //Obtains the status line of this response. The status line can be set using one of the setStatusLine methods, or it can be initialized in a constructor.
            StatusLine statusLine = response.getStatusLine();

            //Represents a status line as returned from a HTTP server. See RFC2616, section 6.1. Implementations are expected to be thread safe.
            int statusCode = statusLine.getStatusCode();

            if (statusCode == 200) {
                //Obtains the message entity of this response, if any. The entity is provided by calling setEntity. (the response entity, or null if there is none)
                HttpEntity entity = response.getEntity();

                //Creates a new InputStream object of the entity. It is a programming error to return the same InputStream object more than once. Entities that are not repeatable will throw an exception if this method is called multiple times.
                //IOException	if the stream could not be created
                InputStream content = entity.getContent();

                //Wraps an existing Reader and buffers the input. Expensive interaction with the underlying reader is minimized, since most (smaller) requests can be satisfied by accessing the buffer alone. The drawback is that some extra space is required to hold the buffer and that copying takes place when filling that buffer, but this is usually outweighed by the performance benefits.
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            } else {
                Log.d(RES_LOG, "Failed to download file");
            }
        }
        catch (IOException e) {
            Log.d(RES_LOG, e.getMessage());
        }
        catch (Exception e) {
            Log.d(RES_LOG, e.getMessage());
        }
        return builder.toString();
    }
}