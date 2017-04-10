package com.android.myapplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Sai Krishna on 4/10/2017.
 */

/* this class helps to get data from server*/
public class ClientServerInterface {

    static InputStream is = null;
    static JSONArray jobj = null;
    static String json = "";

    public ClientServerInterface(){

    }
    //this method returns json object.
    public JSONArray makeHttpRequest(String url){
//http client helps to send and receive data
        DefaultHttpClient httpclient = new DefaultHttpClient();
//our request method is post
        HttpPost httppost = new HttpPost(url);
        try {
//get the response
            HttpResponse httpresponse = httpclient.execute(httppost);
            HttpEntity httpentity = httpresponse.getEntity();
// get the content and store it into inputstream object.
            is = httpentity.getContent();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
//convert byte-stream to character-stream.
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            try {
                while((line = reader.readLine())!=null){
                    sb.append(line+"\n");

                }
//close the input stream
                is.close();
                json = sb.toString();
                try {
                    jobj = new JSONArray(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return jobj;
    }
}