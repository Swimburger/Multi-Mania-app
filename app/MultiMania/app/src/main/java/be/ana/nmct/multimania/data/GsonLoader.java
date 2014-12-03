package be.ana.nmct.multimania.data;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import be.ana.nmct.multimania.utils.Utility;

/**
 * Created by Axel on 22/10/2014.
 */

/**
 * Abstract class to create models from JSON files
 * @param <T> The model to create from a JSON file
 */
public class GsonLoader<T> extends AsyncTaskLoader<List<T>> {

    private static final String BASE_URL = be.ana.nmct.multimania.BuildConfig.API_URL;

    private final TypeToken mToken;
    private String mApiPath;

    private Object lock = new Object();

    public GsonLoader(Context context, String apiPath, TypeToken token) {
        super(context);
        this.mApiPath = apiPath;
        this.mToken = token;
    }

    /**
     * Loads the parseJson() method in a background thread
     * @return A list of the requested model
     */
    @Override
    public List<T> loadInBackground() {
        return parseJson();
    }

    /**
     * Forces loading just in case it won't start automagically
     */
    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * Parses the JSON file from an online source
     * @return A List of the requested model
     */
    public List<T> parseJson() {
        synchronized (lock) {
            List<T> result = new ArrayList<T>();

            InputStream source = null;
            try {
                source = new URL(BASE_URL + mApiPath).openStream();
                Gson gson = new GsonBuilder()
                        .setDateFormat(Utility.getDateFormat())
                        .create();

                Reader reader = new InputStreamReader(source);

                result = gson.fromJson(reader, mToken.getType());  //collectionType);
                reader.close();
                source.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }
    }




}
