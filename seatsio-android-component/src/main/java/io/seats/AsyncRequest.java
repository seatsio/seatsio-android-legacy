package io.seats;

import io.seats.function.Consumer;

public class AsyncRequest {

    public Consumer<String> successCallback;
    public Runnable errorCallback;

    public AsyncRequest(Consumer<String> successCallback, Runnable errorCallback) {
        this.successCallback = successCallback;
        this.errorCallback = errorCallback;
    }

    public AsyncRequest(Consumer<String> successCallback) {
        this.successCallback = successCallback;
    }
}
