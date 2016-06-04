package com.antilo0p.porkskin.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by rigre on 07/05/2016.
 */
public class DietIngredient implements Parcelable {
    private int id;
    private String name;
    private int carbs;
    private int img;
    private int allowed;
    private String fase;


    public DietIngredient(String name, int carbs, int img, int allow, String phase) {
        this.name = name;
        this.carbs = carbs;
        this.img = img;
        this.allowed = allow;
        this.fase = phase;

    }


    public DietIngredient() {
        super();

    }

    public int getCarbs() {
        return carbs;
    }

    public void setCarbs(int carbs) {
        this.carbs = carbs;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public int getAllowed() {
        return allowed;
    }

    public void setAllowed(int allowed) {
        this.allowed = allowed;
    }

    public String getFase() {
        return fase;
    }

    public void setFase(String fase) {
        this.fase = fase;
    }

    public static Creator<DietIngredient> getCREATOR() {
        return CREATOR;
    }


    private DietIngredient(Parcel in) {
        super();
        this.id = in.readInt();
        this.name = in.readString();
        this.carbs = in.readInt();
        this.img = in.readInt();
        this.allowed = in.readInt();

        this.fase = in.readString();
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

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "Ingredient [id=" + id + ", name=" + name + ", carbs="
                + carbs + ", fase=" + fase + "]";
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
        DietIngredient other = (DietIngredient) obj;
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
        dest.writeInt(getCarbs());
        dest.writeInt(getAllowed());
        dest.writeString(getFase());


    }

    public static final Creator<DietIngredient> CREATOR = new Creator<DietIngredient>() {
        public DietIngredient createFromParcel(Parcel in) {
            return new DietIngredient(in);
        }

        public DietIngredient[] newArray(int size) {
            return new DietIngredient[size];
        }
    };
}
