package com.application.photos.structures;
import java.util.*;
import java.io.Serializable;

/**
 * Album is a class that models a photo-album in the application
 * 
 * @author Robert Kulesa
 * @author Aaron Kan 
 */
public class Album implements Serializable {
    /**
     * Serial version of this class.
     */
    private static final long serialVersionUID = 2L;
    
    /**
     * Name of the album
     */
    private String name;
    
    /**
     * Data structure that holds all the photos associated with the album
     */
    private ArrayList<Photo> photos;


    /**
     * Create a new Album given its name.
     * 
     * @param name  String that the Album will have its name assigned to
     */
    public Album(String name){
        this.name = name;
        this.photos = new ArrayList<Photo>();
    }

    /**
     * Get the name of this album.
     * 
     * @return String   the name of the Album
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set the name of this album.
     * 
     * @param newName   New name for the album.
     */
    public void setName(String newName) {
        this.name = newName;
    }

    /**
     * Add a photo to this album and update the album's date properties.
     * 
     * @param p    The photo to be added.
     */
    public void addPhoto(Photo p) {
        this.photos.add(p);
    }

    /**
     * Remove a photo from this album and update the album's date properties.
     * 
     * @param p    The photo to be removed from this album.
     */
    public void deletePhoto(Photo p) {
        this.photos.remove(p);
    }

    /**
     * Get the ArrayList of photos in this album.
     * 
     * @return    ArrayList of type Photo with the photos in this album.
     */
    public ArrayList<Photo> getPhotos() {
        if(this.photos == null) this.photos = new ArrayList<Photo>();
        return this.photos;
    }

    public Photo getPhoto(int index) {
        if(this.photos == null) this.photos = new ArrayList<Photo>();
        return this.photos.get(index);
    }

    public void removePhoto(Photo p) {
        if(this.photos == null) this.photos = new ArrayList<Photo>();
        this.photos.remove(p);
    }

    public void removePhoto(int index) {
        if(this.photos == null) this.photos = new ArrayList<Photo>();
        this.photos.remove(index);
    }

    /**
     * Get the number of photos in this album.
     * 
     * @return    The number of photos in this album.
     */
    public int getNumPhotos() {
        return this.photos.size();
    }

    /**
     * Return <code>true</code> if this album contains the passed photo.
     * 
     * @param p    The photo to be checked if located in this album.
     * @return     <code>true</code> if this album contains the passed photo.
     *             <code>false</code> otherwise.
     */
    public boolean containsPhoto(Photo p) {
        return this.photos.contains(p);
    }

    /**
     * Returns <code>true</code> if the passed string or album's name is equal to this album's name.
     * 
     * @return    <code>true</code> if the passed object is equal to this album.
     *            <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if(o==null || !(o instanceof Album || o instanceof String)) {
            return false;
        }
        if(o instanceof Album) {
            Album otherUser = (Album) o;
            return this.getName().equalsIgnoreCase(otherUser.getName());
        } else {
            String otherString = (String) o;
            return this.getName().equalsIgnoreCase(otherString);
        }
    }
}
