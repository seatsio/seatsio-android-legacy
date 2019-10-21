package test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import io.seats.seatingChart.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import io.seats.function.*;

import static io.seats.seatingChart.SelectionValidator.consecutiveSeats;
import static io.seats.seatingChart.SelectionValidator.noOrphanSeats;

public class ShowSeatingChartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Map<String, String> messages = new LinkedHashMap<>();
        messages.put("A", "lolzor");
        Map<String, String> objectCategories = new LinkedHashMap<>();
        messages.put("A-1", "R");
        messages.put("A-2", "R");
        Map<String, String> extraConfig = new LinkedHashMap<>();
        extraConfig.put("color", "blue");
        AtomicBoolean changed = new AtomicBoolean(false);
        SeatingChartConfig config = new SeatingChartConfig()
                .setPublicKey("publicDemoKey")
                .setEvent("smallTheatreEvent1")
                .setOnObjectSelected(new BiConsumer<SeatsioObject, TicketType>() {
                    @Override
                    public void accept(SeatsioObject object, TicketType ticketType) {
                        Log.i(ShowSeatingChartActivity.class.toString(), "Selected " + object.id + " TT " + ticketType);
                    }
                })
                .setOnObjectDeselected(new BiConsumer<SeatsioObject, TicketType>() {
                    @Override
                    public void accept(SeatsioObject object, TicketType ticketType) {
                        Log.i(ShowSeatingChartActivity.class.toString(), "Deselected " + object.id + " TT " + ticketType);
                    }
                })
                .setOnObjectClicked(new Consumer<SeatsioObject>() {
                    @Override
                    public void accept(SeatsioObject object) {
                        Log.i(ShowSeatingChartActivity.class.toString(), "Clicked " + object.id);
                    }
                })
                .setOnBestAvailableSelected(new BiConsumer<List<SeatsioObject>, Boolean>() {
                    @Override
                    public void accept(List<SeatsioObject> seatsioObjects, Boolean nextToEachOther) {
                        Log.i(ShowSeatingChartActivity.class.toString(), "Best available selected " + nextToEachOther);
                    }
                })
                .setOnBestAvailableSelectionFailed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(ShowSeatingChartActivity.class.toString(), "Best available failed");
                    }
                })
                .setOnSelectionValid(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(ShowSeatingChartActivity.class.toString(), "Selection valid");
                    }
                })
                .setOnSelectionInvalid(new Consumer<List<SelectionValidatorType>>() {
                    @Override
                    public void accept(List<SelectionValidatorType> violations) {
                        Log.i(ShowSeatingChartActivity.class.toString(), "Selection invalid " + violations);
                    }
                })
                .setOnSelectedObjectBooked(new Consumer<SeatsioObject>() {
                    @Override
                    public void accept(SeatsioObject object) {
                        Log.i(ShowSeatingChartActivity.class.toString(), "Booked " + object.id);
                    }
                })
                .setOnChartRendered(this.onChartRendered(changed, extraConfig))
                .setOnChartRenderingFailed(new Consumer<SeatingChartView>() {
                    @Override
                    public void accept(SeatingChartView chart) {
                        Log.i(ShowSeatingChartActivity.class.toString(), "nonono");
                    }
                })
                .setPricing(new PricingForCategory("2", new SimplePricing(34)))
                .setPriceFormatter(new Function<Float, String>() {
                    @Override
                    public String apply(Float price) {
                        return price + "€";
                    }
                })
                .setMessages(messages)
                .setShowLegend(true)
                .setShowActiveSectionTooltip(false)
                .setShowViewFromYourSeat(false)
                .setSelectionValidators(noOrphanSeats(), consecutiveSeats())
                .setHoldOnSelect(true)
                .setHoldOnSelectForGAs(true)
                .setOnHoldSucceeded(new BiConsumer<List<SeatsioObject>, List<TicketType>>() {
                    @Override
                    public void accept(List<SeatsioObject> objects, List<TicketType> ticketTypes) {
                        Log.i(ShowSeatingChartActivity.class.toString(), "Hold succeeded " + objects);
                    }
                })
                .setOnHoldFailed(new BiConsumer<List<SeatsioObject>, List<TicketType>>() {
                    @Override
                    public void accept(List<SeatsioObject> objects, List<TicketType> ticketTypes) {
                        Log.i(ShowSeatingChartActivity.class.toString(), "Hold failed " + objects);
                    }
                })
                .setOnReleaseHoldSucceeded(new BiConsumer<List<SeatsioObject>, List<TicketType>>() {
                    @Override
                    public void accept(List<SeatsioObject> objects, List<TicketType> ticketTypes) {
                        Log.i(ShowSeatingChartActivity.class.toString(), "Release hold succeeded " + objects);
                    }
                })
                .setOnReleaseHoldFailed(new BiConsumer<List<SeatsioObject>, List<TicketType>>() {
                    @Override
                    public void accept(List<SeatsioObject> objects, List<TicketType> ticketTypes) {
                        Log.i(ShowSeatingChartActivity.class.toString(), "Release hold failed " + objects);
                    }
                })
                .setObjectLabel("object => object.labels.own");
        setContentView(new SeatingChartView(config, getApplicationContext()));
    }

    private Consumer<SeatingChartView> onChartRendered(final AtomicBoolean changed, final Map<String, String> extraConfig) {
        return new Consumer<SeatingChartView>() {
            @Override
            public void accept(SeatingChartView chart) {
                {
                    chart.listSelectedObjects(new Consumer<List<SeatsioObject>>() {
                        @Override
                        public void accept(List<SeatsioObject> objects) {
                            Log.i(ShowSeatingChartActivity.class.toString(), objects.toString());
                        }
                    });
                    chart.findObject("Adfsqs\"'fd-1",
                            new Consumer<SeatsioObject>() {
                                @Override
                                public void accept(SeatsioObject object) {
                                    Log.i(ShowSeatingChartActivity.class.toString(), object.toString());
                                }
                            },
                            new Runnable() {
                                @Override
                                public void run() {
                                    Log.i(ShowSeatingChartActivity.class.toString(), "not found");
                                }
                            });
                    chart.listCategories(new Consumer<List<Category>>() {
                        @Override
                        public void accept(List<Category> categories) {
                            Log.i(ShowSeatingChartActivity.class.toString(), categories.toString());
                        }
                    });
                    chart.getHoldToken(new Consumer<String>() {
                        @Override
                        public void accept(String holdToken) {
                            Log.i(ShowSeatingChartActivity.class.toString(), holdToken);
                        }
                    });
                    if (!changed.get()) {
                        changed.set(true);
                        chart.changeConfig(new ConfigChange().setExtraConfig(extraConfig).setObjectColor("(object, dflt, extraConfig) => extraConfig.color"));
                    }
                }
            }
        };
    }

}
