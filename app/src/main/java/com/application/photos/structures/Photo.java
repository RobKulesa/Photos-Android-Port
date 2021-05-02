package com.application.photos.structures;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.OpenableColumns;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Predicate;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.io.IOException;

/**
 * Photo is a class that models a photo in the application.
 * 
 * @author Robert Kulesa
 * @author Aaron Kan 
 */
public class Photo implements Serializable {

    /**
     * Serial version of this class.
     */
    private static final long serialVersionUID = 3L;

    public static final int isLocation = 0;

    public static final int isPerson = 1;



    /**
     * path of this photo.
     */
    private String path;

    private Bitmap bitmap;

    /**
     * TreeMap for the tags for this photo.
     */
    private ArrayList<String> tags;

    public Photo(String path) {
        this.path = path;
        this.tags = new ArrayList<String>();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Get the tags list of this photo.
     * 
     * @return    TreeMap of tags for this photo.
     */
    public ArrayList<String> getTags(){
        return this.tags;
    }


    /**
     * Get all the tag names for this photo.
     * 
     * @return    Set of type String, the tag names for this photo.
     */
    //public ArrayList<String> getTagNames() { }


    public void addTag(int tagType, String val) throws IllegalArgumentException {
        String s = "";
        if(tagType == isLocation)
            s += "location: ";
        else if (tagType == isPerson)
            s += "person: ";
        else
            s+= "error: ";

        val = val.toLowerCase();
        ArrayList<String> vals = getTagValsByType(tagType);
        for(String str : vals) {
            if(str.equals(val)) throw new IllegalArgumentException("Tag Value already exists for this photo!");
        }
        s += val;
        this.tags.add(s);
        System.out.println("Tag added = " + s);
    }


    public ArrayList<String> getTagValsByType(int tagType) {
        ArrayList<String> valList = new ArrayList<String>();
        for(String s : tags){
            if(isTagType(tagType, s))
                valList.add(getTagValue(s));
        }
        return valList;
    }

    /**
     * Remove a tag from this photo.
     * 
     * @param name                         Name of tag to be added.
     * @param val                          Value of tag to be added.
     * @throws IllegalArgumentException    Thrown if tag does not exist for this photo.
     */
    /*
    public void removeTag(String name, String val) throws IllegalArgumentException {
        name = name.toLowerCase();
        if(!this.getTagNames().contains(name)) throw new IllegalArgumentException("Tag Name does not exist!");

        final String value = val.toLowerCase();
        ArrayList<String> vals = this.getTagValsByName(name);
        if(!vals.contains(val)) throw new IllegalArgumentException("Tag Value does not exist!");
        vals.removeIf(new Predicate<String>() {
            @Override
            public boolean test(String arg0) {
                return arg0.equalsIgnoreCase(value);
            }
        });
    }
    */


    /**
     * Return <code>true</code> if this photo has the given tag.
     *
     * @param tagType    Tag type to be checked.
     * @param val     Tag value to be checked.
     * @return        <code>true</code> if this photo has the given tag.
     *                <code>false</code> otherwise.
     */
    public boolean hasTag(int tagType, String val) {
        val = val.toLowerCase();
        ArrayList<String> vals = this.getTagValsByType(tagType);
        if(vals.isEmpty()) return false;
        for(String s : vals) {
            System.out.println("Comparing: " + s + " with: " + val);
            if(s.equalsIgnoreCase(val)) return true;
        }
        return false;
    }




    public boolean isTagType(int tagNameType, String fullTagString){
        String tagName = fullTagString.substring(0, fullTagString.indexOf(':'));
        if(tagNameType == isLocation)
            return tagName.equals("location");
        else if(tagNameType == isPerson)
            return tagName.equals("person");
        else
            return false;
    }

    public String getTagName(String fullTagString){
        return fullTagString.substring(0, fullTagString.indexOf(':'));
    }

    public String getTagValue(String fullTagString){
        return fullTagString.substring(fullTagString.indexOf(':')+2);
    }



    public String getFileName(Context context) {
        Uri uri = this.getUri();
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public Uri getUri() {
        return Uri.parse(this.path);
    }

    /**
     * Return <code>true</code> if photo's path is the same as the passed string or passed photo's path.
     * 
     * @param o    The string or photo to be compared to this photo's path.
     * @return        <code>true</code> if photo's path is the same as the passed string or passed photo's path.
     *                <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if(o==null || !(o instanceof Photo || o instanceof String)) {
            return false;
        }
        if(o instanceof Photo) {
            Photo otherPhoto = (Photo) o;
            return this.getUri().equals(otherPhoto.getUri());
        } else {
            String otherString = (String) o;
            return this.getUri().toString().equals(otherString);
        }
    }

    public static Bitmap getBitmap(Context context, Photo p) {
        InputStream input = null;
        try {
            input = context.getContentResolver().openInputStream(p.getUri());
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            input.close();
            return bitmap;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap getThumbnail(Context context, Photo p) {
        InputStream input = null;
        try {
            input = context.getContentResolver().openInputStream(p.getUri());
            BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
            onlyBoundsOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
            input.close();

            if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1)) {
                return null;
            }

            int originalSize = Math.max(onlyBoundsOptions.outHeight, onlyBoundsOptions.outWidth);

            double ratio = (originalSize > 100) ? (originalSize / 100) : 1.0;

            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
            input = context.getContentResolver().openInputStream(p.getUri());
            Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
            input.close();
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static int getPowerOfTwoForSampleRatio(double ratio){
        int k = Integer.highestOneBit((int)Math.floor(ratio));
        if(k==0) return 1;
        else return k;
    }

}
