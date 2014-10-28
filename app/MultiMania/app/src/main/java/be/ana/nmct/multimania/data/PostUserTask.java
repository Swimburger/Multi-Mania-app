package be.ana.nmct.multimania.data;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

import be.ana.nmct.multimania.BuildConfig;

/**
 * Created by Niels on 28/10/2014.
 */
public class PostUserTask extends AsyncTask<Void,Void,String> {
    public static final String API_URL = BuildConfig.API_URL;
    private final String mUserId;
    private IOException mException;
    private PostUserCallbacks mCallback;

    public PostUserTask(String userId){
        mUserId=userId;
    }

    public PostUserTask(String userId,PostUserCallbacks callback){
        mUserId=userId;
        mCallback=callback;
    }

    /**
     * For testing purposes
     * @return String UserId
     */
    public String executeSynchronously(){
        return doInBackground();
    }
    @Override
    protected String doInBackground(Void... params) {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(API_URL+"users/"+mUserId);
        String userId = null;
        try {
           HttpResponse response = client.execute(post);
           userId = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            mException = e;
            e.printStackTrace();
        }
        return userId;
    }

    @Override
    protected void onPostExecute(String userId) {
        super.onPostExecute(userId);
        if(mCallback!=null)
            mCallback.OnUserInserted(userId);
    }

    public interface PostUserCallbacks{
        public void OnUserInserted(String userId);
    }
}
