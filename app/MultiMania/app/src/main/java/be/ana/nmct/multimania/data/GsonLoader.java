package be.ana.nmct.multimania.data;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Axel on 22/10/2014.
 */
public class GsonLoader<T> extends AsyncTaskLoader<List<T>> {

    private static final String BASE_URL = "http://student.howest.be/niels.swimberghe/multimania/";
    private String apiPath;
    private Object lock = new Object();

    public GsonLoader(Context context, String apiPath) {
        super(context);
        this.apiPath = apiPath;
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
                Gson gson = new Gson();
                Reader reader = new InputStreamReader(source);
                Type collectionType = new TypeToken<List<T>>(){}.getType();
                result = gson.fromJson(reader, collectionType);
                reader.close();
                source.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }
    }




}
