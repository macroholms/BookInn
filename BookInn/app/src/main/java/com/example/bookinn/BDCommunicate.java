package com.example.bookinn;

import android.os.Handler;
import android.util.Log;

import com.example.bookinn.ApiService;
import com.example.bookinn.Hotels;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BDCommunicate {

    OkHttpClient client = new OkHttpClient().newBuilder()
            .build();

    public void postUserToDb(String data) throws IOException{
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, data);
        Request request = new Request.Builder()
                .url("http://10.0.2.2:5000/api/Users")
                .header("Host", "localhost")
                .method("POST", body)
                .build();
        client.newCall(request).execute();
    }

    public void getAllHotels(Callback callBack) throws IOException {
        Request request = new Request.Builder()
                .url("http://10.0.2.2:5000/api/v_Hotels_with_Photos")
                .header("Host", "localhost")
                .method("GET", null)
                .build();
        client.newCall(request).enqueue(callBack);
    }

    public void getAHotels(String id, Callback callback) throws IOException{
        Request request = new Request.Builder()
                .url("http://10.0.2.2:5000/api/Hotels?id=" + id)
                .header("Host", "localhost")
                .method("GET", null)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public void getUserWithEmail(String email, Callback callback) throws IOException{
        Request request = new Request.Builder()
                .url("http://10.0.2.2:5000/api/Users?Email=" + email)
                .header("Host", "localhost")
                .method("GET", null)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public void updateUserData(String id, String data) throws IOException{
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, data);
        Request request = new Request.Builder()
                .url("http://10.0.2.2:5000/api/Users?id=" + id)
                .header("Host", "localhost")
                .method("PUT", body)
                .build();
        client.newCall(request).execute();
    }

    public void getRoomWithHotel(String id, Callback callback) throws IOException{
        Request request = new Request.Builder()
                .url("http://10.0.2.2:5000/api/v_Rooms_Details?hotelId=" + id)
                .header("Host", "localhost")
                .method("GET", null)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public void getReviews(String id, Callback callback) throws IOException{
        Request request = new Request.Builder()
                .url("http://10.0.2.2:5000/api/v_Hotel_Reviews?hotel_id=" + id)
                .header("Host", "localhost")
                .method("GET", null)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public void postReview(String data) throws IOException{
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, data);
        Request request = new Request.Builder()
                .url("http://10.0.2.2:5000/api/Reviews")
                .header("Host", "localhost")
                .method("POST", body)
                .build();
        client.newCall(request).execute();
    }

    public void postBooking(String data) throws IOException{
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, data);
        Request request = new Request.Builder()
                .url("http://10.0.2.2:5000/api/Bookings")
                .header("Host", "localhost")
                .method("POST", body)
                .build();
        Response response = client.newCall(request).execute();
        int code = response.code();
        Log.e("!", String.valueOf(code));
    }

    public void getBookingsWithUser(String id, Callback callback) throws IOException{
        Request request = new Request.Builder()
                .url("http://10.0.2.2:5000/api/Bookings?UserId=" + id)
                .header("Host", "localhost")
                .method("GET", null)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public void getRoomWithRoom(String id, Callback callback) throws IOException{
        Request request = new Request.Builder()
                .url("http://10.0.2.2:5000/api/v_Rooms_Details?roomId=" + id)
                .header("Host", "localhost")
                .method("GET", null)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public void putUser(String id, String data) throws IOException{
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, data);
        Request request = new Request.Builder()
                .url("http://10.0.2.2:5000/api/Users?id=" + id)
                .header("Host", "localhost")
                .method("PUT", body)
                .build();
        Response response = client.newCall(request).execute();
        String code = String.valueOf(response.code());
        Log.e("!", code);
    }
}

