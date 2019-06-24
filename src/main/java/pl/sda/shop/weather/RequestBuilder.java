package pl.sda.shop.weather;

import pl.sda.shop.weather.model.Units;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RequestBuilder {
    private final String apiKey;
    private final OpenWeatherMap openWeatherMap;
    private String cityName;
    private Double latitude;
    private Double longitude;
    private Units units = Units.METRIC;
    private String language = "pl";

    public RequestBuilder(String apiKey) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        openWeatherMap =
                retrofit.create(OpenWeatherMap.class);

        this.apiKey = apiKey;
    }

    public RequestBuilder byCity(String cityName) {
        this.cityName = cityName;
        return this;
    }

    public RequestBuilder byCoords(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        return this;
    }

    public RequestBuilder withUnits(Units units) {
        this.units = units;
        return this;
    }

    public RequestBuilder withLanguage(String language) {
        this.language = language;
        return this;
    }

    public Call<WeatherResult> build() {
        return openWeatherMap.current(cityName, latitude, longitude, apiKey, units, language);
    }
}


