package com.inhatc.listapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Weather extends AppCompatActivity {
    double lon;
    double lat;
    String weatherinfo;
    TextView weather, menu1, menu2, menu3;
    ImageView imageview;
    String arr_random_menu[] = new String[10]; // 랜덤
    int a[] = new int[10];
    List<Integer> list = new ArrayList<>();

    private GoogleMap mMap;
    private Marker currentMarker = null;

    private static final String TAG = "googlemap_example";
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int UPDATE_INTERVAL_MS = 1000;  // 1초
    private static final int FASTEST_UPDATE_INTERVAL_MS = 500; // 0.5초

    List<Marker> previous_marker = null;

    // onRequestPermissionsResult에서 수신된 결과에서 ActivityCompat.requestPermissions를 사용한 퍼미션 요청을 구별하기 위해 사용됩니다.
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    boolean needRequest = false;


    // 앱을 실행하기 위해 필요한 퍼미션을 정의합니다.
    String[] REQUIRED_PERMISSIONS  = {android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};  // 외부 저장소


    Location mCurrentLocatiion;
    LatLng currentPosition;


    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private Location location;


    private View mLayout;  // Snackbar 사용하기 위해서는 View가 필요합니다.
    // (참고로 Toast에서는 Context가 필요했습니다.)


    ArrayList<String> day = new ArrayList<String>(Arrays.asList("초밥","냉면", "마라탕", "돈까스", "치킨", "피자", "떡볶이", "회",
            "곱창", "빙수", "돈부리", "족발", "샌드위치", "토스트", "샐러드", "핫도그", "비빔국수", "월남쌈", "김치볶음밥"));
    ArrayList<String> clode = new ArrayList<String>(Arrays.asList("햄버거", "생선구이", "보쌈", "삼겹살", "라면", "쌀국수", "양꼬치",
            "닭도리탕", "김치찌개", "카레", "된장찌개", "마파두부", "팟타이", "잔치국수", "제육덮밥", "비빔밥", "오므라이스", "순두부찌개", "동태찌개"));
    ArrayList<String> rain = new ArrayList<String>(Arrays.asList("파전", "삼계탕", "오뎅탕", "짬뽕", "부대찌개", "아구찜", "해물탕", "칼국수",
            "수제비", "두부김치", "짜장면", "닭발", "홍합탕", "선지해장국", "우거지국", "뼈해장국", "순대국", "갈비탕", "찜닭", "골뱅이무침", "순대", "잔치국수"));
    ArrayList<String> snow = new ArrayList<String>(Arrays.asList("우동", "밀푀유나베", "샤브샤브", "파스타", "부대찌개", "호떡", "팥죽", "설렁탕", "군고구마", "간장게장",
            "북엇국", "육개장", "닭개장", "떡국", "추어탕", "소고기무국", "수육", "매운탕", "잔치국수"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weathers);
//        ArrayList<Double> loc = getLoc();

        getWeatherData(lat, lon);

        imageview = (ImageView) findViewById(R.id.weather_icon);
        weather = (TextView) findViewById(R.id.weather);
        menu1 = (TextView) findViewById(R.id.menu1);
        menu2 = (TextView) findViewById(R.id.menu2);
        menu3 = (TextView) findViewById(R.id.menu3);

    }
    private void getWeatherData ( double lat, double lon ){

        String key = "f1d16f2871f111695a26372e35f11f62";
        try {
            String url = "https://api.openweathermap.org/data/2.5/weather?" +
                    "lat=" + lat +
                    "&lon=" + lon +
                    "&units=metric&appid=" + key;

            ReceiveWeatherTask receiveUseTask = new ReceiveWeatherTask();
            receiveUseTask.execute(url);

            Log.d("url:", url + "");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Double> getLoc() {
        ArrayList<Double> curLoc = new ArrayList<>();
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //마지막 위치 받아오기
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
        }
        Location loc_Current = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (loc_Current == null) loc_Current = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        double cur_lat;
        cur_lat = loc_Current.getLatitude(); //위도
//        cur_lat = 37.450718;
        curLoc.add(cur_lat);

        double cur_lon;
        cur_lon = loc_Current.getLongitude(); //경도
//        cur_lon = 126.663233;
        curLoc.add(cur_lon);

        return curLoc;
    }

    public class ReceiveWeatherTask extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected void onPreExecute() { super.onPreExecute(); }
        @Override
        protected JSONObject doInBackground(String... datas) {
            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(datas[0]).openConnection();
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);
                conn.connect();

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream is = conn.getInputStream();
                    InputStreamReader reader = new InputStreamReader(is);
                    BufferedReader in = new BufferedReader(reader);

                    String readed;
                    while ((readed = in.readLine()) != null) {
                        JSONObject jObject = new JSONObject(readed);
                        String result = jObject.getJSONArray("weather").getJSONObject(0).getString("icon");
                        return jObject;
                    }
                } else {
                    return null;
                }
                return null;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject result){
            Log.i("result:", result.toString());
            if( result != null ){
                String iconName = "";
                String nowTemp = "";
                String maxTemp = "";
                String minTemp = "";
                String humidity = "";
                String speed = "";
                String main = "";
                String description = "";
                try {
                    iconName = result.getJSONArray("weather").getJSONObject(0).getString("icon");
                    nowTemp = result.getJSONObject("main").getString("temp");
                    humidity = result.getJSONObject("main").getString("humidity");
                    minTemp = result.getJSONObject("main").getString("temp_min");
                    maxTemp = result.getJSONObject("main").getString("temp_max");
                    speed = result.getJSONObject("wind").getString("speed");
                    main = result.getJSONArray("weather").getJSONObject(0).getString("main");
                    description = result.getJSONArray("weather").getJSONObject(0).getString("description");
                    Log.i("icon:", iconName);
                    if(iconName.equals("01d") || iconName.equals("01n")) {
                        weatherinfo = "맑음";
                        Random random = new Random();

                        for(int i = 0; i < 3; i++){
                            a[i] = random.nextInt(day.size());
                            for(int j=0;j<i;j++) {
                                if(a[i]==a[j]) {
                                    i--;
                                }
                            }
                            for (int b=0;b<3;b++) {
                                arr_random_menu[b] = day.get(a[b]);
                            }

                        }
                        imageview.setImageResource(R.drawable.sun);
                        weather.setText(weatherinfo);
                        menu1.setText(arr_random_menu[0]);
                        menu2.setText(arr_random_menu[1]);
                        menu3.setText(arr_random_menu[2]);
                    }
                    else if (iconName.equals("02d") || iconName.equals("02n") || iconName.equals("03d") || iconName.equals("03n") || iconName.equals("04d") || iconName.equals("04n") || iconName.equals("50d") || iconName.equals("50n")) {
                        weatherinfo = "흐림";
                        Random random = new Random();

                        for(int i = 0; i < 3; i++){
                            a[i] = random.nextInt(clode.size());
                            for(int j=0;j<i;j++) {
                                if(a[i]==a[j]) {
                                    i--;
                                }
                            }
                            for (int b=0;b<3;b++) {
                                arr_random_menu[b] = clode.get(a[b]);
                            }

                        }
                        imageview.setImageResource(R.drawable.cloude);
                        weather.setText(weatherinfo);
                        menu1.setText(arr_random_menu[0]);
                        menu2.setText(arr_random_menu[1]);
                        menu3.setText(arr_random_menu[2]);
                    }
                    else if (iconName.equals("09d") || iconName.equals("09n") || iconName.equals("10d") || iconName.equals("10n") || iconName.equals("11d") || iconName.equals("11n")) {
                        weatherinfo = "비";
                        Random random = new Random();

                        for(int i = 0; i < 3; i++){
                            a[i] = random.nextInt(rain.size());
                            for(int j=0;j<i;j++) {
                                if(a[i]==a[j]) {
                                    i--;
                                }
                            }
                            for (int b=0;b<3;b++) {
                                arr_random_menu[b] = rain.get(a[b]);
                            }

                        }
                        imageview.setImageResource(R.drawable.rain);
                        weather.setText(weatherinfo);
                        menu1.setText(arr_random_menu[0]);
                        menu2.setText(arr_random_menu[1]);
                        menu3.setText(arr_random_menu[2]);
                    }
                    else if (iconName.equals("13d") || iconName.equals("13n")) {
                        weatherinfo = "눈";
                        Random random = new Random();

                        for(int i = 0; i < 3; i++){
                            a[i] = random.nextInt(snow.size());
                            for(int j=0;j<i;j++) {
                                if(a[i]==a[j]) {
                                    i--;
                                }
                            }
                            for (int b=0;b<3;b++) {
                                arr_random_menu[b] = snow.get(a[b]);
                            }

                        }
                        weather.setText(weather.getText() + weatherinfo);
                        menu1.setText(menu1.getText() + arr_random_menu[0]);
                        menu2.setText(menu2.getText() + arr_random_menu[1]);
                        menu3.setText(menu3.getText() + arr_random_menu[2]);
                    }


                }
                catch (JSONException e ){
                    e.printStackTrace();
                }
                description = transferWeather( description );
                final String msg = description + " 습도 " + humidity +"%, 풍속 " + speed +"m/s" + " 온도 현재:"+nowTemp+" / 최저:"+ minTemp + " / 최고:" + maxTemp;

            }


        }

        private String transferWeather(String weather) {
            weather = weather.toLowerCase();

            if( weather.equals("haze") ){
                return "안개";
            }
            else if( weather.equals("fog") ){
                return "안개";
            }
            else if( weather.equals("clouds") ){
                return "구름";
            }
            else if( weather.equals("few clouds") ){
                return "구름 조금";
            }
            else if( weather.equals("scattered clouds") ){
                return "구름 낌";
            }
            else if( weather.equals("broken clouds") ){
                return "구름 많음";
            }
            else if( weather.equals("overcast clouds") ){
                return "구름 많음";
            }
            else if( weather.equals("clear sky") ){
                return "맑음";
            }

            return "";
        }
    }

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);

            List<Location> locationList = locationResult.getLocations();

            if (locationList.size() > 0) {
                location = locationList.get(locationList.size() - 1);
                //location = locationList.get(0);

                currentPosition
                        = new LatLng(location.getLatitude(), location.getLongitude());


                String markerTitle = getCurrentAddress(currentPosition);
                String markerSnippet = "위도:" + String.valueOf(location.getLatitude())
                        + " 경도:" + String.valueOf(location.getLongitude());
                lat = (double) location.getLatitude();
                lon = (double) location.getLongitude();

                Log.d(TAG, "onLocationResult : " + markerSnippet);

                mCurrentLocatiion = location;
            }
        }

    };

    public String getCurrentAddress(LatLng latlng) {

        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(
                    latlng.latitude,
                    latlng.longitude,
                    1);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }

        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";

        } else {
            Address address = addresses.get(0);
            return address.getAddressLine(0).toString();
        }

    }



}



