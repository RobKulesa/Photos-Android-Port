package com.application.photos.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.application.photos.fragments.SlideshowPageFragment;
import com.application.photos.structures.Album;

public class SlideshowPagerAdapter extends FragmentStateAdapter {
    private Album album;
    private Context context;
    public SlideshowPagerAdapter(@NonNull FragmentActivity fa, Context context, Album album) {
        super(fa);
        this.context = context;
        this.album = album;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return new SlideshowPageFragment(this.context, album.getPhoto(position));
    }

    @Override
    public int getItemCount() {
        return this.album.getNumPhotos();
    }
}
