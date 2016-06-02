package com.antilo0p.porkskin.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by rigre on 07/05/2016.
 */
public class DietWeek implements Parcelable{
    private int id;
    private String name;
    private String phase;
    private Date startDay;
    private Date createdAt;

    public DietWeek(){
        super();
    }
    public DietWeek(int id, String name, String phase, Date startDay) {
        this.id = id;
        this.name = name;
        this.phase = phase;
        this.startDay = startDay;
    }

    public DietWeek(String name, String phase, Date startDay) {
        this.name = name;
        this.phase = phase;
        this.startDay = startDay;
    }

    private DietWeek(Parcel in){
        super();
        this.id = in.readInt();
        this.name =  in.readString();
        this.phase = in.readString();
        this.startDay = new Date(in.readLong());
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "id:" + id + ", name:" + name + ", date:" + startDay;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public Date getStartDay() {
        return startDay;
    }

    public void setStartDay(Date startDay) {
        this.startDay = startDay;
    }


    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DietWeek other = (DietWeek) obj;
        if (id != other.id)
            return false;
        return true;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getId());
        dest.writeString(getName());
        dest.writeString(getPhase());
        dest.writeLong(getStartDay().getTime());

    }

    public static final Parcelable.Creator<DietWeek> CREATOR = new Parcelable.Creator<DietWeek>(){
        public DietWeek createFromParcel(Parcel in) {
            return new DietWeek(in);
        }

        public DietWeek[] newArray(int size) {
            return new DietWeek[size];
        }
    };
}
