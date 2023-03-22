package com.example.mdc.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.mdc.R;

import org.jetbrains.annotations.NotNull;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * A fragment for displaying an image.
 */
@AndroidEntryPoint
public class ImageFragment extends Fragment {

  private static final String KEY_IMAGE_RES = "imageRes";

  public static ImageFragment newInstance(@NotNull String imageUrl) {
    ImageFragment fragment = new ImageFragment();
    Bundle argument = new Bundle();
    argument.putString(KEY_IMAGE_RES, imageUrl);
    fragment.setArguments(argument);
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    final View view = inflater.inflate(R.layout.fragment_image, container, false);

    Bundle arguments = getArguments();
      String imageUrl = arguments.getString(KEY_IMAGE_RES);

    // Just like we do when binding views at the grid, we set the transition name to be the string
    // value of the image res.
    view.findViewById(R.id.image).setTransitionName(String.valueOf(imageUrl));

    // Load the image with Glide to prevent OOM error when the image drawables are very large.
    Glide.with(this)
        .load(imageUrl)
        .listener(new RequestListener<Drawable>() {
          @Override
          public boolean onLoadFailed(@Nullable GlideException e, @Nullable Object model, @Nullable Target<Drawable>
              target, boolean isFirstResource) {
            // The postponeEnterTransition is called on the parent ImagePagerFragment, so the
            // startPostponedEnterTransition() should also be called on it to get the transition
            // going in case of a failure.
            getParentFragment().startPostponedEnterTransition();
            return false;
          }

          @Override
          public boolean onResourceReady(@Nullable Drawable resource, @Nullable Object model, @Nullable Target<Drawable>
              target, DataSource dataSource, boolean isFirstResource) {
            // The postponeEnterTransition is called on the parent ImagePagerFragment, so the
            // startPostponedEnterTransition() should also be called on it to get the transition
            // going when the image is ready.
            getParentFragment().startPostponedEnterTransition();
            return false;
          }
        })
        .into((ImageView) view.findViewById(R.id.image));
    return view;
  }
}
