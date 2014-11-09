package be.ana.nmct.multimania.vm;

import android.os.Parcel;
import android.os.Parcelable;

import com.felipecsl.asymmetricgridview.library.model.AsymmetricItem;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by Niels on 5/11/2014.
 */
public class ScheduleGridHeader implements AsymmetricItem {
    private Date dateUntil;
    private Date dateFrom;
    private int columnSpan;
    private int rowSpan = 1;
    private int position;

    public ScheduleGridHeader() {
        this(1, 0);
    }

    public ScheduleGridHeader(final int columnSpan, int position) {
        this.columnSpan = columnSpan;
        this.position = position;
    }

    public ScheduleGridHeader(final Parcel in) {
        readFromParcel(in);
    }

    public ScheduleGridHeader(final int columnSpan, int position,Date dateFrom,Date dateUntil) throws ParseException {
        this.columnSpan = columnSpan;
        this.position = position;
        this.dateFrom = dateFrom;
        this.dateUntil = dateUntil;
    }

    @Override
    public int getColumnSpan() {
        return columnSpan;
    }

    @Override
    public int getRowSpan() {
        return rowSpan;
    }

    public int getPosition() {
        return position;
    }

    public void setDateFrom(Date dateFrom){
        this.dateFrom=dateFrom;
    }

    public Date getDateFrom(){
        return dateFrom;
    }

    public void setDateUntil(Date dateUntil){
        this.dateUntil=dateUntil;
    }

    public Date getDateUntil(){
        return dateUntil;
    }

    @Override
    public String toString() {
        return String.format("%s: %sx%s", position, rowSpan, columnSpan);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private void readFromParcel(final Parcel in) {
        columnSpan = in.readInt();
        rowSpan = in.readInt();
        position = in.readInt();
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeInt(columnSpan);
        dest.writeInt(rowSpan);
        dest.writeInt(position);
    }

    /* Parcelable interface implementation */
    public static final Parcelable.Creator<ScheduleGridHeader> CREATOR = new Parcelable.Creator<ScheduleGridHeader>() {

        @Override
        public ScheduleGridHeader createFromParcel(final Parcel in) {
            return new ScheduleGridHeader(in);
        }

        @Override
        public ScheduleGridHeader[] newArray(final int size) {
            return new ScheduleGridHeader[size];
        }
    };
}
