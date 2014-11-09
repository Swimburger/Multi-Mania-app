package be.ana.nmct.multimania.vm;

import android.os.Parcel;
import android.os.Parcelable;

import com.felipecsl.asymmetricgridview.library.model.AsymmetricItem;

import be.ana.nmct.multimania.model.Talk;

/**
 * Created by Niels on 5/11/2014.
 */
public class ScheduleGridItem extends Talk implements AsymmetricItem {
    private int columnSpan;
    private int rowSpan;
    private int position;
    public String room;
    public String tags;

    public ScheduleGridItem() {
        this(1,1, 0);
    }

    public ScheduleGridItem(final int columnSpan,final int rowSpan, int position) {
        this.columnSpan = columnSpan;
        this.rowSpan = rowSpan;
        this.position = position;
    }

    public ScheduleGridItem(final Parcel in) {
        readFromParcel(in);
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
    public static final Parcelable.Creator<ScheduleGridItem> CREATOR = new Parcelable.Creator<ScheduleGridItem>() {

        @Override
        public ScheduleGridItem createFromParcel(final Parcel in) {
            return new ScheduleGridItem(in);
        }

        @Override
        public ScheduleGridItem[] newArray(final int size) {
            return new ScheduleGridItem[size];
        }
    };
}
