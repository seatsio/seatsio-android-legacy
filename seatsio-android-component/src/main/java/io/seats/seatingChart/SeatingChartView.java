package io.seats.seatingChart;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.webkit.ValueCallback;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.seats.SeatsioWebView;
import io.seats.function.Consumer;

import java.lang.reflect.Type;
import java.util.List;

public class SeatingChartView extends SeatsioWebView {

    private final SeatingChartConfig config;

    public SeatingChartView(SeatingChartConfig config, Context context) {
        super(config.toJson(), new SeatingChartJavascriptInterface(config), context);
        this.config = config;
    }

    public SeatingChartView(SeatingChartConfig config, Context context, @Nullable AttributeSet attrs) {
        super(config.toJson(), new SeatingChartJavascriptInterface(config), context, attrs);
        this.config = config;
    }

    public SeatingChartView(SeatingChartConfig config, Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(config.toJson(), new SeatingChartJavascriptInterface(config), context, attrs, defStyleAttr);
        this.config = config;
    }

    public SeatingChartView(SeatingChartConfig config, Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(config.toJson(), new SeatingChartJavascriptInterface(config), context, attrs, defStyleAttr, defStyleRes);
        this.config = config;
    }

    public void getHoldToken(final Consumer<String> callback) {
        evaluateJavascript("chart.holdToken", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                callback.accept(value);
            }
        });
    }

    public void zoomToSelectedObjects() {
        evaluateJavascript("chart.zoomToSelectedObjects()", null);
    }

    public void selectBestAvailable(BestAvailable bestAvailable) {
        evaluateJavascript("chart.selectBestAvailable(" + new Gson().toJson(bestAvailable) + ")", null);
    }

    public void selectObjects(SelectedObject... objects) {
        evaluateJavascript("chart.selectObjects(" + new Gson().toJson(objects) + ")", null);
    }

    public void deselectObjects(SelectedObject... objects) {
        evaluateJavascript("chart.deselectObjects(" + new Gson().toJson(objects) + ")", null);
    }

    public void listSelectedObjects(final Consumer<List<SeatsioObject>> callback) {
        asyncRequests.doRequest(
                "listSelectedObjects",
                new Consumer<String>() {
                    @Override
                    public void accept(String objects) {
                            Type listType = new TypeToken<List<SeatsioObject>>() {}.getType();
                            callback.accept(new Gson().<List<SeatsioObject>>fromJson(objects, listType));
                    }
                }
        );
    }

    public void listCategories(final Consumer<List<Category>> callback) {
        asyncRequests.doRequest(
                "listCategories",
                new Consumer<String>() {
                    @Override
                    public void accept(String categories) {
                        Type listType = new TypeToken<List<Category>>() {}.getType();
                        callback.accept(new Gson().<List<Category>>fromJson(categories, listType));
                    }
                }
        );
    }

    public void findObject(String label, final Consumer<SeatsioObject> successCallback, Runnable errorCallback) {
        asyncRequests.doRequest(
                "findObject",
                label,
                new Consumer<String>() {
                    @Override
                    public void accept(String object) {
                        successCallback.accept(new Gson().fromJson(object, SeatsioObject.class));
                    }
                },
                errorCallback
        );
    }

    public void clearSelection(final Runnable successCallback, Runnable errorCallback) {
        asyncRequests.doRequest(
                "clearSelection",
                new Consumer<String>() {
                    @Override
                    public void accept(String s) {
                        successCallback.run();
                    }
                },
                errorCallback
        );
    }

    public void changeConfig(ConfigChange configChange) {
        evaluateJavascript("chart.changeConfig(" + new Gson().toJson(configChange) + ")", null);
    }
}
