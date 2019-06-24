package pl.sda.shop.weather;

import pl.sda.shop.weather.model.Units;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherMap {
    @GET("data/2.5/weather")
    Call<WeatherResult> currentByCity(
            @Query("q") String cityName,
            @Query("appid") String apiKey,
            @Query("units") String units,
            @Query("lang") String language
    );

    @GET("data/2.5/weather")
    Call<WeatherResult> currentByCoords(
            @Query("lat") Double latitude,
            @Query("lon") Double longitude,
            @Query("appid") String apiKey,
            @Query("units") String units,
            @Query("lang") String language
    );

    @GET("data/2.5/weather")
    Call<WeatherResult> current(
            @Query("q") String cityName,
            @Query("lat") Double latitude,
            @Query("lon") Double longitude,
            @Query("appid") String apiKey,
            @Query("units") Units units,
            @Query("lang") String language
    );
}
