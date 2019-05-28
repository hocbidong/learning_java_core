package java_core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

public class HttpClientMultiThreadExample {
	public static void main(String... args) {
		long startTime = System.currentTimeMillis();
		List<Map<Integer, Object>> listResponse = createListHttpResponse();
		long endTime = System.currentTimeMillis();
		long collapsedTime = endTime - startTime;
		System.out.print("Collapsed time is: " + collapsedTime);
	}

	public static List<Map<Integer, Object>> createListHttpResponse() {
		ExecutorService executor = Executors.newFixedThreadPool(5);
		List<Map<Integer, Object>> listHttpResponse = new ArrayList<>();
		synchronized (listHttpResponse) {
			for (int i = 0; i < 50; i++) {
				Worker worker = new Worker(listHttpResponse, i);
				executor.execute(worker);
			}
			executor.shutdown();
			while (!executor.isTerminated()) {

			}
		}
		return listHttpResponse;
	}

	public static class Worker implements Runnable {

		List<Map<Integer, Object>> listHttpResponse;

		Integer index;

		public Worker(List<Map<Integer, Object>> listHttpResponse, Integer index) {
			this.listHttpResponse = listHttpResponse;
			this.index = index;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			addHttpResponse(this.listHttpResponse, this.index);
		}

		private List<Map<Integer, Object>> addHttpResponse(List<Map<Integer, Object>> listHttpResponse, Integer index) {
			Map<Integer, Object> map = new HashMap<>();
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
			map.put(index, response);
			listHttpResponse.add(map);
			return listHttpResponse;
		}

	}
}
