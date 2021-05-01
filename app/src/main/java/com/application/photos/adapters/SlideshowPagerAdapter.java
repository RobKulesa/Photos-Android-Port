package com.application.photos.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.application.photos.fragments.SlideshowPageFragment;
import com.application.photos.structures.Album;
import com.application.photos.structures.AlbumList;

public class SlideshowPagerAdapter extends FragmentStateAdapter {
    private AlbumList albumList;
    private int albumIndex;
    private Album album;
    private Context context;
    public SlideshowPagerAdapter(@NonNull FragmentActivity fa, Context context, AlbumList albumList, int albumIndex) {
        super(fa);
        this.context = context;
        this.albumList = albumList;
        this.albumIndex = albumIndex;
        this.album = this.albumList.getAlbum(this.albumIndex);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return new SlideshowPageFragment(this.context, this.albumList, this.albumIndex, position);
    }

    @Override
    public int getItemCount() {
        return this.album.getNumPhotos();
    }
}
