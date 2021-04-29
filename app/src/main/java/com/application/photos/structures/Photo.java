package com.application.photos.structures;
import java.io.Serializable;
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

    /**
     * Filepath of this photo.
     */
    private String path;

    /**
     * TreeMap for the tags for this photo.
     */
    private TreeMap<String, ArrayList<String>> tags;

    /**
     * Constructor for creating a new photo given its path on the filesystem..
     * 
     * @param path    The path of this photo on the filesystem.
     */
    public Photo(String path){
        this.path = path;

        this.tags = new TreeMap<>(
                (Comparator<String> & Serializable) (o1, o2) -> o1.compareToIgnoreCase(o2)
        );
    }

    /**
     * Return the filesystem path of this photo.
     * 
     * @return    The path on the filesystem of this photo.
     */
    public String getPath(){
        return this.path;
    }
    
    /**
     * Set the filesystem path of this photo.
     * 
     * @param newPath   new filePath to assign to the current Photo
     */
    public void setPath(String newPath){
        this.path = newPath;
    }

    /**
     * Get the tags list of this photo.
     * 
     * @return    TreeMap of tags for this photo.
     */
    public TreeMap<String, ArrayList<String>> getTags(){
        return this.tags;
    }

    /**
     * Get the tags for this photo as a formatted string.
     * 
     * @return    ArrayList of type String, each formatted string is one tag.
     */
    public ArrayList<String> getTagStrings() {
        ArrayList<String> tagStrings = new ArrayList<String>();
        for(String name : this.tags.keySet()) {
            for(String val : this.tags.get(name)) {
                tagStrings.add("(" + name + ", " + val + ")");
            }
        }
        return tagStrings;
    }

    /**
     * Get all the tag names for this photo.
     * 
     * @return    Set of type String, the tag names for this photo.
     */
    public Set<String> getTagNames() {
        return this.tags.keySet();
    }

    /**
     * Add a tag to this photo.
     * 
     * @param name                         Name of the tag to be added.
     * @param val                          Value of the tag to be added.
     * @throws IllegalArgumentException    Thrown if tag already exists for this photo.
     */
    public void addTag(String name, String val) throws IllegalArgumentException {
        name = name.toLowerCase();
        val = val.toLowerCase();
        ArrayList<String> vals = new ArrayList<String>();
        if(!this.getTagNames().contains(name)) {
            this.tags.put(name, vals);
        }
        vals = this.getTagValsByName(name);
        for(String s : vals) {
            if(s.equals(val)) throw new IllegalArgumentException("Tag Value already exists for this photo!");
        }
        this.tags.get(name).add(val);
    }

    /**
     * Get the tag values for the provided tag name for this photo.
     * 
     * @param name    The tag name to get the values for.
     * @return        The tag values for this tag name.
     */
    public ArrayList<String> getTagValsByName(String name) {
        name = name.toLowerCase();
        ArrayList<String> vals = this.tags.get(name);
        if(vals == null) vals = new ArrayList<String>();
        return vals;
    }

    /**
     * Remove a tag from this photo.
     * 
     * @param name                         Name of tag to be added.
     * @param val                          Value of tag to be added.
     * @throws IllegalArgumentException    Thrown if tag does not exist for this photo.
     */
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

    /**
     * Return <code>true</code> if this photo has the given tag.
     * 
     * @param name    Tag name to be checked.
     * @param val     Tag value to be checked.
     * @return        <code>true</code> if this photo has the given tag.
     *                <code>false</code> otherwise.
     */
    public boolean hasTag(String name, String val) {
        name = name.toLowerCase();
        val = val.toLowerCase();
        ArrayList<String> vals = this.getTagValsByName(name);
        if(vals.isEmpty()) return false;
        for(String s : vals) {
            if(s.equalsIgnoreCase(val)) return true;
        }
        return false;
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
            return this.getPath().equals(otherPhoto.getPath());
        } else {
            String otherString = (String) o;
            return this.getPath().equals(otherString);
        }
    }
}
