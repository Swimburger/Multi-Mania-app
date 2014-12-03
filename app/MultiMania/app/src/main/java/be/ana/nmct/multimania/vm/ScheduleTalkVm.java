package be.ana.nmct.multimania.vm;

import java.util.Date;

import be.ana.nmct.multimania.model.Talk;

/**
 * Created by Niels on 19/11/2014.
 */

/**
 * The viewmodel for a Talk
 */
public class ScheduleTalkVm extends Talk {
    public String tags;
    public String room;
    public String fromString;
    public String untilString;
    public boolean isDoubleBooked;

    public ScheduleTalkVm() {
    }

    public ScheduleTalkVm(int id, String title, Date from, Date to, String description, int roomId, boolean isKeynote) {
        this.id = id;
        this. title = title;
        this.from = from;
        this.to = to;
        this.description = description;
        this.roomId = roomId;
        this.isKeynote = isKeynote;
    }
}
