﻿package whitepaper.smcall.remote;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
public class Jax {

	/******************************************
	Copyright (c) 2013
	Author: Samsung Software MemberShip in HAYAN Soft.
	
	
	Licensed under the Apache License, Version 2.0 (the "License"); 
	you may not use this file except in compliance with the License. 
	You may obtain a copy of the License at
	http://www.apache.org/licenses/LICENSE-2.0
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
	 ******************************************/
	
	public static String resultString;
	public JSONObject jsonObj = new JSONObject();
	
	
	public String sendJson(final String server , final String args[]) {
		Log.i("JAX","////////////////////JAX ! Top Champion////////////////////");
		resultString =new String();
		
		//횽은 파라미터 짝수 개만 받는다.
		if(args.length%2!=0){
			Log.i("JSON","parameter : Odd");
			return null;
		}
		//너님들은 메인에 UI 쓰레드가 Lock up 되는것을 막기위해 네트워크 액티비티에 한해 새로운 쓰레드를 만들어야만 한다. 
        Thread t = new Thread() {
            public void run() {
            	//자식쓰레드의 메시지 풀을 준비하자.
                //Looper.prepare(); 
                HttpClient client = new DefaultHttpClient();
                //연결에 대한 시간제한을 걸자. - ms
                HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); 
                HttpResponse response;
               JSONObject json = new JSONObject();
                try {
                   
                    int j=0;
                    for(int i=0; i<args.length/2; i++){
                    	json.put(args[j], args[j+1]);
                    	j+=2;
                    }
                	
                	
                	Log.i("JAX"," 보내는 데이터 : "+json.toString());
                    StringEntity se = new StringEntity(json.toString());  
                   se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                 
                   HttpPost post = new HttpPost(server);
                   post.setEntity(se);
                    response = client.execute(post);
                    
                    //결과물을 체크하자. 
                    if(response!=null){
                    	//결과물을 Entity에 넣고 또 getContent..
                    	
                    	///////////////////////////////////////////////////////////////////////////////////////////////////////////
                    	HttpEntity resultentity = response.getEntity();
                    	InputStream inputstream = resultentity.getContent();
                    	//gzip 인코딩일때 GZIP 인풋스트림에 넣어서 처리하자
                    	Header contentencoding = response.getFirstHeader("Content-Encoding");
                    	if(contentencoding != null && contentencoding.getValue().equalsIgnoreCase("gzip")) {
                    	    inputstream = new GZIPInputStream(inputstream);
                    	}
                    	//스트림을 스트링으로 바꿔주는 매우 고마운 함수를 쓰도록 한다. 물론 구현은 밑에서 다시 해야겠지.
                    	//convertStreamToJSON도 만들어야 할듯.
                    	if(resultString.equals("")){
                    	resultString = convertStreamToString(inputstream);
                    	//이젠 스트림 너한테 볼일 없어. 껒혀
                    	inputstream.close();
                    	// 스트링 마지막에는 -1이 있을테니까 그거 제외하자
                    	//Jax.resultString = resultString.substring(1,resultString.length()-1);
                    	}
                    	
                    	//Log.i("JAX","result : "+resultString);
                    	//jsonObj=new JSONObject(resultString);
                    	
                    	/*
                    	Log.i("JAX",resultString); //+ httppostreq.toString().getBytes());
                    	//recvdref.setText(resultstring + "\n\n" + httppostreq.toString().getBytes());
                    	//JSONObject recvdjson = new JSONObject(resultstring);
                    	//recvdref.setText(recvdjson.toString(2));
                    	JSONObject jax=new JSONObject(resultString);
                    	
                    	result=jax.getString(resultString);
                    	Log.i("JAX", result);
                    	
                    	//InputStream in = response.getEntity().getContent(); 
                    	//Log.i("JAX","Response : "+in.toString());
                        */
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                    	Log.i("JAX", "Cannot Estabilish Connection");
                } 
              //메시지 루프.
                //Looper.loop(); 
            }
        };

        t.start();
        try {
			t.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Log.i("JAX" , "sendJson()의  리턴: "+ Jax.resultString);
    	return Jax.resultString;
    }
	private String convertStreamToString(InputStream is) {
	    String line = "";
	    StringBuilder total = new StringBuilder();
	    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	    try {
	        while ((line = rd.readLine()) != null) {
	            total.append(line);
	        }
	    } catch (Exception e) {
	    	Log.i("JAX : Stream Exception" , e.toString());
	    }
	return total.toString();
	}
	public String getValue(String json , String key){
		String value=new String();
		try {
			JSONObject jsonObj = new JSONObject(json);
			//jsonObj = (JSONObject) JSONValue.parse(value);
			value=jsonObj.getString(key);
			Log.i("JAX","getValue의 리턴 : "+value);
			//return jsonObj.get(key).toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.i("JAX","getValue 에러. 아마 키 값을 잘못 입력했거나 json값이 없음. : "+e.toString());
			e.printStackTrace();
		}
		return value ;
	}
	
	
}
