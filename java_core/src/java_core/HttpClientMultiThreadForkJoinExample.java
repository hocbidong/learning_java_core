package java_core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

public class HttpClientMultiThreadForkJoinExample {
	public static void main(String... args) {
		long startTime = System.currentTimeMillis();
		List<Map<Integer, Object>> listResponse = createListHttpResponse();
		long endTime = System.currentTimeMillis();
		long collapsedTime = endTime - startTime;
		System.out.print("Collapsed time is: " + collapsedTime);
	}

	public static List<Map<Integer, Object>> createListHttpResponse() {
		ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
		Worker worker = new Worker(new Integer(100));
		return forkJoinPool.invoke(worker);
	}

	public static class Worker extends RecursiveTask<List<Map<Integer, Object>>> {

		private static final long serialVersionUID = 1L;

		List<Map<Integer, Object>> listHttpResponse;

		int index;
		
		Integer numberRequest;

		public Worker(Integer numberRequest) {
			this.numberRequest = numberRequest;
		}

		public Worker(int index) {
			this.index = index;
		}
		
		public Map<Integer, Object> process() {
			// TODO Auto-generated method stub
			Map<Integer, Object> map = new HashMap<>();
			String url = "http://www.google.com/search?q=httpClient";

			HttpClient client = HttpClientBuilder.create().build();
			HttpGet request = new HttpGet(url);
			HttpResponse response = null;
			try {
				response = client.execute(request);
				Thread.sleep(1000);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			map.put(index, response);
			return map;
		}
		
		private List<Worker> createListWorker(Integer length) {
			List<Worker> listWorker = new ArrayList<>();
			for(int i = 1; i <= length; i++) {
				listWorker.add(new Worker(i));
			}
			return listWorker;
		}

		@Override
		protected List<Map<Integer, Object>> compute() {
			List<Map<Integer, Object>> listObject = new ArrayList<Map<Integer, Object>>();
			if (this.numberRequest == 0) {
				listObject.add(process());
				return listObject;
			} else {
				List<Worker> listWorker = createListWorker(this.numberRequest);
				listWorker = (List<Worker>) ForkJoinTask.invokeAll(listWorker);
				for (Worker worker : listWorker) {
					listObject.addAll(worker.join());
				}
				return listObject;
			}
		}
	}
}
