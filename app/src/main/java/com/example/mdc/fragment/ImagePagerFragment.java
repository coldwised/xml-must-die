package com.example.mdc.fragment;

import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.SharedElementCallback;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.mdc.MainActivity;
import com.example.mdc.R;
import com.example.mdc.adapter.ImagePagerAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * A fragment for displaying a pager of images.
 */
@AndroidEntryPoint
public class ImagePagerFragment extends Fragment {

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    ViewPager viewPager = (ViewPager) inflater.inflate(R.layout.fragment_pager, container, false);
    viewPager.setAdapter(new ImagePagerAdapter(this));
    // Set the current position and add a listener that will update the selection coordinator when
    // paging the images.
    viewPager.setCurrentItem(MainActivity.currentPosition);
    viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
      @Override
      public void onPageSelected(int position) {
        MainActivity.currentPosition = position;
      }
    });

    prepareSharedElementTransition(viewPager);

    // Avoid a postponeEnterTransition on orientation change, and postpone only of first creation.
    if (savedInstanceState == null) {
      postponeEnterTransition();
    }

    return viewPager;
  }

  /**
   * Prepares the shared element transition from and back to the grid fragment.
   */
  private void prepareSharedElementTransition(@NotNull ViewPager viewPager) {

    Transition transition =
        TransitionInflater.from(getContext())
            .inflateTransition(R.transition.image_shared_element_transition);
    setSharedElementEnterTransition(transition);

    // A similar mapping is set at the GridFragment with a setExitSharedElementCallback.
    setEnterSharedElementCallback(
        new SharedElementCallback() {
          @Override
          public void onMapSharedElements(@Nullable List<String> names, @Nullable Map<String, View> sharedElements) {
            // Locate the image view at the primary fragment (the ImageFragment that is currently
            // visible). To locate the fragment, call instantiateItem with the selection position.
            // At this stage, the method will simply return the fragment at the position and will
            // not create a new one.
            final ViewPager viewPagerParam = viewPager;
            Fragment currentFragment = (Fragment) Objects.requireNonNull(viewPagerParam.getAdapter())
                .instantiateItem(viewPagerParam, MainActivity.currentPosition);
            View view = currentFragment.getView();
            if (view == null) {
              return;
            }

            // Map the first shared element name to the child ImageView.
            sharedElements.put(names.get(0), view.findViewById(R.id.image));
          }
        });
  }
}