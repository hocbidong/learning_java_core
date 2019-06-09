package java_core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

public class HttpClientMultiThreadCallableExample {
	public static void main(String... args) {
		long startTime = System.currentTimeMillis();
		List<Map<Integer, Object>> listResponse = createListHttpResponse();
		long endTime = System.currentTimeMillis();
		long collapsedTime = endTime - startTime;
		System.out.print("Collapsed time is: " + collapsedTime);
	}

	public static List<Map<Integer, Object>> createListHttpResponse() {
		ExecutorService executor = Executors.newCachedThreadPool();
		List<Map<Integer, Object>> listHttpResponse = new ArrayList<>();
		List<Future<Map<Integer, Object>>> listFutureHttpResponse = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			Worker worker = new Worker(i);
			Future<Map<Integer, Object>> ret = executor.submit(worker);
			listFutureHttpResponse.add(ret);
			
		}
		executor.shutdown();
		for (Future<Map<Integer, Object>> future : listFutureHttpResponse) {
			try {
				if(future.get() != null) {
					listHttpResponse.add(future.get());
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return listHttpResponse;
	}

	public static class Worker implements Callable<Map<Integer, Object>> {

		List<Map<Integer, Object>> listHttpResponse;

		Integer index;

		public Worker(Integer index) {
			this.index = index;
		}

		@Override
		public Map<Integer, Object> call() {
			// TODO Auto-generated method stub
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
			return map;
		}
	}
}
