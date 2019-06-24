package pl.sda.shop.weather.service;


import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sda.shop.UserContextService;
import pl.sda.shop.users.Customer;
import pl.sda.shop.users.UsersRepository;
import pl.sda.shop.weather.OpenWeatherMapJ8;
import pl.sda.shop.weather.WeatherResult;
import retrofit2.Retrofit;
import retrofit2.adapter.java8.Java8CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.CompletableFuture;

@Service
public class WeatherService {
    public static final String API_KEY = "ea900b66f547fd7b23625544873a4200";

    private static Gson gson = new Gson();
    @Autowired
    private UserContextService userContextService;
    @Autowired
    private UsersRepository<Customer> usersRepository;


    public String getWeather() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(Java8CallAdapterFactory.create())
                .build();

        OpenWeatherMapJ8 openWeatherMapJ8 =
                retrofit.create(OpenWeatherMapJ8.class);

        CompletableFuture<WeatherResult> forecast1 = openWeatherMapJ8.currentByCity(
                usersRepository.findByUsername(userContextService.getLoggedUserEmail()).map(e -> e.getUserAddress().getCity() + "," + e.getUserAddress().getCountry().toUpperCase()).orElse(""),
                API_KEY,
                "metric",
                "pl"
        );

        return downloadWeather(forecast1);
    }

    private String downloadWeather(CompletableFuture<WeatherResult> forecast) {

        return forecast
                .thenApplyAsync(pogoda -> {
                    System.out.println("Przetwarzam");
                    return gson.toJson(pogoda);
                })
                .thenApplyAsync(result -> {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        System.err.println(e.getMessage());
                    }
                    System.out.println(result);
                    return result;
                })
                .exceptionally(throwable -> {
                    throw new RuntimeException("Błąd");
                }).join();

    }
}