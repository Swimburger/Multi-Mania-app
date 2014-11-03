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
public class GsonLoader<T> extends AsyncTaskLoader<List<T>> {

    private static final String BASE_URL = be.ana.nmct.multimania.BuildConfig.API_URL;
    private final TypeToken token;
    private String apiPath;
    private Object lock = new Object();

    public GsonLoader(Context context, String apiPath,TypeToken token) {
        super(context);
        this.apiPath = apiPath;
        this.token=token;
    }

    @Override
    public List<T> loadInBackground() {
        return parseJson();
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    public List<T> parseJson() {
        synchronized (lock) {
            List<T> result = new ArrayList<T>();

            InputStream source = null;
            try {
                source = new URL(BASE_URL + apiPath).openStream();
                Gson gson = new GsonBuilder()
                        .setDateFormat(Utility.getDateFormat())
                        .create();

                Reader reader = new InputStreamReader(source);
                //Type collectionType = new TypeToken<List<T>>(){}.getRawType();

                result = gson.fromJson(reader,token.getType());  //collectionType);
                reader.close();
                source.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }
    }




}
