package com.application.photos.structures;

import android.content.Context;
import android.widget.Toast;

import java.io.*;
import java.util.*;

/**
 * AlbumList is a serializable class that holds an ArrayList of {@link Album}s. An instance
 * of this class can be saved and loaded between runs of the application.
 *
 * @author Robert Kulesa
 * @author Aaron Kan
 */
public class AlbumList implements Serializable {

    /**
     * Serial version of this class.
     */
    private static final long serialVersionUID = 2L;

    /**
     * ArrayList of albums.
     */
    private ArrayList<Album> albums;

    /**
     * File where serialized AlbumList objects will be stored.
     */
    public static final String storeFile = "albums.dat";

    /**
     * Create a AlbumList object with an empty ArrayList of {@link Album}s.
     */
    public AlbumList(){
        this.albums = new ArrayList<Album>();
    }

    /**
     * Create a AlbumList object with a passed ArrayList of {@link Album}s.
     *
     * @param albums    ArrayList of {@link Album}s.
     */
    public AlbumList(ArrayList<Album> albums) {
        if(albums == null) albums = new ArrayList<Album>();
        this.albums = albums;
    }

    /**
     * Add a album to the AlbumList.
     *
     * @param u     The Album to be added to the AlbumList
     */
    public void addAlbum(Album u){
        albums.add(u);
    }

    /**
     * Add a album to the AlbumList at a specific index.
     *
     * @param u         The Album to be added to the AlbumList
     * @param index     The index at which the Album will be inserted
     */
    public void addAlbum(Album u, int index) {
        albums.add(index, u);
    }

    /**
     * Remove a album to the AlbumList.
     *
     * @param u    {@link Album} to be removed.
     */
    public void removeAlbum(Album u) {
        albums.remove(u);
    }

    /**
     * Remove a album to the AlbumList.
     *
     * @param index    index of {@link Album} to be removed.
     */
    public void removeAlbum(int index) {
        removeAlbum(albums.get(index));
    }

    /**
     * Get the number of {@link Album}s in the list.
     *
     * @return    Number of {@link Album}s in the list.
     */
    public int getLength(){
        return albums.size();
    }

    /**
     * Get the ArrayList of {@link Album}s.
     *
     * @return    ArrayList of {@link Album}.
     */
    public ArrayList<Album> getAlbums() {
        return this.albums;
    }

    /**
     * Get the album at index <code>index</code>.
     *
     * @param index    Index of {@link Album}.
     * @return         {@link Album} at index <code>index</code>.
     */
    public Album getAlbum(int index){
        return albums.get(index);
    }

    /**
     * Write the passed instance of AlbumList to the filesystem.
     *
     * @param albumList        Instance of AlbumList to be written.
     * @throws IOException    Thrown if file failed to be opened.
     */
    public static void writeAlbumList(Context context, AlbumList albumList) throws IOException {
        if(albumList == null || albumList.getAlbums() == null) albumList = new AlbumList();
        ObjectOutputStream oos = new ObjectOutputStream(context.openFileOutput(storeFile, Context.MODE_PRIVATE));
        oos.writeObject(albumList);
        oos.close();
        //Toast.makeText(context, "Successfully saved album list to filesystem!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Read from the filesystem a previously written AlbumList instance.
     *
     * @return                           AlbumList instance read from filesystem.
     * @throws IOException               Thrown if file failed to be opened.
     * @throws ClassNotFoundException    Thrown if object could not be created.
     */
    public static AlbumList readAlbumList(Context context)  {
        ObjectInputStream ois;
        AlbumList albumList;
        try {
            ois = new ObjectInputStream(context.openFileInput(storeFile));
            albumList = (AlbumList) ois.readObject();
            ois.close();
        } catch(Exception e) {
            albumList = new AlbumList();
        }

        if(albumList == null) {
            albumList = new AlbumList();
        }

        return albumList;
    }

    /**
     * Returns <code>true</code> if passed Album u is already in this album list.
     *
     * @param u    The album to be checked.
     * @return     <code>true</code> if passed Album u is already in this album list.
     *             <code>false</code> otherwise.
     */
    public boolean containsAlbum(Album u) {
        for(Album listAlbum : this.albums) {
            if(listAlbum.equals(u)) return true;
        }
        return false;
    }

    /**
     * Returns <code>true</code> if passed String u matches a album's album name in this album list.
     *
     * @param u    The album name to be checked.
     * @return     <code>true</code> if passed String u matches a album's album name in this album list.
     *             <code>false</code> otherwise.
     */
    public boolean containsAlbum(String u) {
        for(Album listAlbum : this.albums) {
            if(listAlbum.equals(u)) return true;
        }
        return false;
    }

    public Album getAlbumByName(String u) throws IOException{
        for(Album listAlbum: this.albums){
            if(listAlbum.getName().equals(u)) return listAlbum;
        }
        throw new IOException("Album with name: " + u + " does not exist.");
    }

}
