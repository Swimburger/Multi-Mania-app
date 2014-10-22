package be.ana.nmct.multimania.model;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by Axel on 22/10/2014.
 */
public class User {

    public String id;
    @Expose
    public List<Talk> favorites;
}
