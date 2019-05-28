package java_core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.*;
import org.apache.http.client.*		;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

public class HttpClientExample {
	public static void main(String... args) {
		long startTime = System.currentTimeMillis();
		List<Map<Integer, Object>> listResponse = createListHttpResponse();
		long endTime = System.currentTimeMillis();
		System.out.print("Collapsed time is: " + (endTime - startTime));
	}

	public static List<Map<Integer, Object>> createListHttpResponse(){
		List<Map<Integer, Object>> listHttpResponse = new ArrayList<>();
		for(int i = 0; i < 50; i++) {
			Map<Integer, Object> map = new HashMap<>();
			HttpResponse response = executeHttpClient();
			map.put(i, response);
			listHttpResponse.add(map);
		}
		return listHttpResponse;
	}
	
	public static HttpResponse executeHttpClient() {
		String url = "http://www.google.com/search?q=httpClient";
		
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url);
		HttpResponse response = null;
		try {
			response = client.execute(request);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}
}
