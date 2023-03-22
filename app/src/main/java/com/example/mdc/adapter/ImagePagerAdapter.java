package com.example.mdc.adapter;

import static com.example.mdc.adapter.ImageData.IMAGES_URL_LIST;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.mdc.fragment.ImageFragment;

import org.jetbrains.annotations.NotNull;


public class ImagePagerAdapter extends FragmentStatePagerAdapter {

  public ImagePagerAdapter(@NotNull Fragment fragment) {
    // Note: Initialize with the child fragment manager.
    super(fragment.getChildFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
  }

  @Override
  public int getCount() {
    return IMAGES_URL_LIST.length;
  }

  @NonNull
  @Override
  public Fragment getItem(int position) {
    return ImageFragment.newInstance(IMAGES_URL_LIST[position]);
  }
}
