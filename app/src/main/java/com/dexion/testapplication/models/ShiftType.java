package com.dexion.testapplication.models;

import android.provider.BaseColumns;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

@Table(name = "shift_types", id = BaseColumns._ID)
public class ShiftType extends Model
{
    @Column(name = "name", index = true)
    public String name;

    @Column(name = "color")
    public int color;

    @Column(name = "weight")
    public int weight;

    public ShiftType()
    {
        super();
    }

    public ShiftType(String sName)
    {
        super();

        name = sName;
        color = 0xffff0000;
        weight = ShiftType.allShiftTypes().size();

    }

    public ShiftType(String sName, int mColor)
    {
        super();

        name = sName;
        color = mColor;
        weight = ShiftType.allShiftTypes().size();

    }

    public static ArrayList<ShiftType> allShiftTypes()
    {
        List<ShiftType> typesList = new Select().from(ShiftType.class).orderBy("weight ASC").execute();
        return new ArrayList<>(typesList);
    }

    public static ShiftType getByPosition(int position)
    {
        return new Select().from(ShiftType.class).orderBy("weight ASC").limit(1).offset(position).executeSingle();
    }

    public static ShiftType getById(long _id)
    {
        return new Select().from(ShiftType.class).where("_id = ?", _id).limit(1).executeSingle();
    }

    public static void reorderBottom(int fromPosition, int ignorePosition)
    {
        int weight = ((ShiftType) new Select().from(ShiftType.class).orderBy("weight ASC").limit(1).offset(fromPosition).executeSingle()).weight;
        weight += 1;

        List<ShiftType> typesList = new Select()
                .from(ShiftType.class)
                .orderBy("weight ASC")
                .where("weight >= ?", fromPosition).and("weight <> ?", ignorePosition)
                .execute();

        ActiveAndroid.beginTransaction();
        try {
            for(ShiftType i : typesList){
                i.weight = weight;
                weight += 1;
                i.save();
            }
            ActiveAndroid.setTransactionSuccessful();
        }
        finally {
            ActiveAndroid.endTransaction();
        }
    }

    public static void reorderTop(int fromPosition, int ignorePosition)
    {
        int weight = 0;
        List<ShiftType> typesList = new Select()
                .from(ShiftType.class)
                .orderBy("weight ASC")
                .where("weight <= ?", fromPosition).and("weight <> ?", ignorePosition)
                .execute();

        ActiveAndroid.beginTransaction();
        try {
            for(ShiftType i : typesList){
                i.weight = weight;
                i.save();
                weight++;
            }
            ActiveAndroid.setTransactionSuccessful();
        }
        finally {
            ActiveAndroid.endTransaction();
        }
    }

    public static int reorderAfterDeletion(int position) {
        List<ShiftType> typesList = new Select()
                .from(ShiftType.class)
                .orderBy("weight ASC")
                .where("weight >= ?", position).and("weight <> ?", position)
                .execute();

        ActiveAndroid.beginTransaction();
        try {
            for(ShiftType i : typesList){
                i.weight -= 1;
                i.save();
            }
            ActiveAndroid.setTransactionSuccessful();
        }
        finally {
            ActiveAndroid.endTransaction();
        }
        return typesList.size();
    }
    public boolean canDelete(int position){
        return true;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getText() {
        return name;
    }

    public int getColor() {
        return color;
    }
}
