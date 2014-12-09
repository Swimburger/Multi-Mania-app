package be.ana.nmct.multimania.vm;

/**
 * Created by Astrid on 3/12/2014.
 */

/**
 * A class for the navigationdrawer, there is an image and a name for the category
 */
public class NavigationItem {

    public int icon;
    public String name;

    // Constructor.
    public NavigationItem(int icon, String name) {

        this.icon = icon;
        this.name = name;
    }
}
