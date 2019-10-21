package io.seats;

import io.seats.function.Consumer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AsyncRequests {

    private final SeatsioWebView seatsioWebView;
    private Map<String, AsyncRequest> asyncRequests = new HashMap<>();

    AsyncRequests(SeatsioWebView seatsioWebView) {
        this.seatsioWebView = seatsioWebView;
    }

    public void doRequest(String function, Consumer<String> successCallback) {
        String requestId = UUID.randomUUID().toString();
        asyncRequests.put(requestId, new AsyncRequest(successCallback));
        String js = "chart." + function + "(" +
                "asyncCallSuccess('" + requestId + "')" +
                ")";
        seatsioWebView.evaluateJavascript(js, null);
    }

    public void doRequest(String function, String param, Consumer<String> successCallback, Runnable errorCallback) {
        String requestId = UUID.randomUUID().toString();
        asyncRequests.put(requestId, new AsyncRequest(successCallback, errorCallback));
        String js = "chart." + function + "(" +
                "'" + escapeSingleQuotes(param) + "'" +
                ", asyncCallSuccess('" + requestId + "')" +
                ", asyncCallError('" + requestId + "')" +
                ")";
        seatsioWebView.evaluateJavascript(js, null);
    }

    public void doRequest(String function, Consumer<String> successCallback, Runnable errorCallback) {
        String requestId = UUID.randomUUID().toString();
        asyncRequests.put(requestId, new AsyncRequest(successCallback, errorCallback));
        String js = "chart." + function + "(" +
                "asyncCallSuccess('" + requestId + "')" +
                ", asyncCallError('" + requestId + "')" +
                ")";
        seatsioWebView.evaluateJavascript(js, null);
    }

    private static String escapeSingleQuotes(String s) {
        return s.replaceAll("'", "\\\\'");
    }

    public void onSuccess(String result, String requestId) {
        AsyncRequest asyncRequest = asyncRequests.remove(requestId);
        asyncRequest.successCallback.accept(result);
    }

    public void onError(String requestId) {
        AsyncRequest asyncRequest = asyncRequests.remove(requestId);
        asyncRequest.errorCallback.run();
    }
}
