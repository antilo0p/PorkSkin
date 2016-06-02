package com.antilo0p.porkskin.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by rigre on 07/05/2016.
 */
public class DietMeal implements Parcelable {
    private int id;
    private String name;
    private String meal;
    private String mealType;
    private Date mealDaytime;

    private DietWeek dietWeek;

    public DietMeal(String name, String meal, String mealType, Date mealDaytime, DietWeek dietWeek) {
        this.name = name;
        this.mealDaytime = mealDaytime;
        this.meal = meal;
        this.mealType = mealType;
        this.dietWeek = dietWeek;
    }


    public DietWeek getDietWeek() {
        return dietWeek;
    }

    public void setDietWeek(DietWeek dietWeek) {
        this.dietWeek = dietWeek;
    }

    public DietMeal(){
        super();

    }

    private DietMeal(Parcel in){
        super();
        this.id = in.readInt();
        this.name = in.readString();
        this.mealDaytime = new Date(in.readLong());
        this.meal = in.readString();
        this.mealType = in.readString();

        this.dietWeek = in.readParcelable(DietWeek.class.getClassLoader());
    }


    public int  getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getMealDaytime() {
        return mealDaytime;
    }

    public void setMealDaytime(Date mealDaytime) {
        this.mealDaytime = mealDaytime;
    }

    public String getMeal() {
        return meal;
    }

    public void setMeal(String meal) {
        this.meal = meal;
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    @Override
    public String toString() {
        return "DietMeal [id=" + id + ", name=" + name + ", mealDaytime="
                + mealDaytime + ", mealType=" + mealType + "]";
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
        DietMeal other = (DietMeal) obj;
        if (id != other.id)
            return false;
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getId());
        dest.writeString(getName());
        dest.writeLong(getMealDaytime().getTime());
        dest.writeString(getMeal());
        dest.writeString(getMealType());
        dest.writeParcelable(getDietWeek(), flags);
    }

    public static final Parcelable.Creator<DietMeal> CREATOR = new Parcelable.Creator<DietMeal>(){
        public DietMeal createFromParcel(Parcel in){
            return new DietMeal(in);
        }

        public DietMeal[] newArray(int size) {
            return new DietMeal[size];
        }
    };
}
