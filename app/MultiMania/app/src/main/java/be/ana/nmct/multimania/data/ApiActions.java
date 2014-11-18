package be.ana.nmct.multimania.data;

import android.content.Context;

import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.future.ResponseFuture;

import be.ana.nmct.multimania.BuildConfig;

/**
 * Created by Niels on 18/11/2014.
 */
public final class ApiActions {
    private static final String POST = "POST";
    private static final String DELETE = "DELETE";
    private static final String GET = "GET";
    private ApiActions(){};
    public static ResponseFuture<String> postUser(Context context,String userId) {
        return Ion.with(context).load(POST,BuildConfig.API_URL+"users/"+userId)
                .asString();
    }

    public static ResponseFuture<String> postFavoriteTalk(Context context,String userId,long talkId) {
        return Ion.with(context).load(POST, BuildConfig.API_URL+"users/"+userId+"/talks/"+talkId)
                .asString();
    }

    public static ResponseFuture<String> deleteFavoriteTalk(Context context,String userId,long talkId) {
        return Ion.with(context).load(DELETE,BuildConfig.API_URL+"users/"+userId+"/talks/"+talkId)
                .asString();
    }

    public static ResponseFuture<String> getLastUpdated(Context context,String userId) {
        return Ion.with(context).load(GET, BuildConfig.API_URL + "users/" + userId + "/lastupdated")
                .asString();
    }
}
