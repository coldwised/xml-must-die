package com.example.mdc.fragment;

import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLayoutChangeListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.SharedElementCallback;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.domain.repository.Repository;
import com.example.mdc.AdaptiveGridLayoutManager;
import com.example.mdc.MainActivity;
import com.example.mdc.R;
import com.example.mdc.adapter.GridAdapter;
import com.example.mdc.util.Resource;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * A fragment for displaying a grid of images.
 */
@AndroidEntryPoint
public class GridFragment extends Fragment {

  @Inject
  @NotNull
  public Repository repo;

  @Nullable
  private RecyclerView recyclerView;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    LifecycleOwner lifecycleOwner = this.getViewLifecycleOwner();
    GridAdapter gridAdapter = new GridAdapter(this);
    repo.getImagesUrlList().observe(lifecycleOwner, data -> {
      gridAdapter.submitList(((Resource.Success<ArrayList<String>>) data).data);
              scrollToPosition();
            }
    );


    recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_grid, container, false);
    RecyclerView recyclerView = this.recyclerView;
    recyclerView.setAdapter(gridAdapter);
    recyclerView.setLayoutManager(new AdaptiveGridLayoutManager(requireContext(), 300));

    prepareTransitions();
    postponeEnterTransition();

    return recyclerView;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    //scrollToPosition();
  }

  /**
   * Scrolls the recycler view to show the last viewed item in the grid. This is important when
   * navigating back from the grid.
   */
  private void scrollToPosition() {
    //recyclerView.removeOnLayoutChangeListener(this);
    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        //recyclerView.removeCallbacks(this);
        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        View viewAtPosition = layoutManager.findViewByPosition(MainActivity.currentPosition);
        if (viewAtPosition == null || layoutManager
                .isViewPartiallyVisible(viewAtPosition, false, true)) {
          recyclerView.scrollToPosition(MainActivity.currentPosition);
        }
      }
    };
    recyclerView.removeCallbacks(runnable);
    recyclerView.post(
            runnable
    );
//    recyclerView.addOnLayoutChangeListener(new OnLayoutChangeListener() {
//      @Override
//      public void onLayoutChange(View v,
//                                 int left,
//                                 int top,
//                                 int right,
//                                 int bottom,
//                                 int oldLeft,
//                                 int oldTop,
//                                 int oldRight,
//                                 int oldBottom) {
//        recyclerView.removeOnLayoutChangeListener(this);
////        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
////        View viewAtPosition = layoutManager.findViewByPosition(MainActivity.currentPosition);
//        // Scroll to position if the view for the current position is null (not currently part of
//        // layout manager children), or it's not completely visible.
//        recyclerView.post(
//                () -> {
//                  final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
//                  View viewAtPosition = layoutManager.findViewByPosition(MainActivity.currentPosition);
//                  if (viewAtPosition == null || layoutManager
//                          .isViewPartiallyVisible(viewAtPosition, false, true)) {
//                    recyclerView.scrollToPosition(MainActivity.currentPosition);
//                  }
//                }
//        );
//      }
//    });
  }

//  private void scrollToPosition() {
//    RecyclerView recyclerView = this.recyclerView;
//    recyclerView.addOnLayoutChangeListener(new OnLayoutChangeListener() {
//      @Override
//      public void onLayoutChange(View v,
//                                 int left,
//                                 int top,
//                                 int right,
//                                 int bottom,
//                                 int oldLeft,
//                                 int oldTop,
//                                 int oldRight,
//                                 int oldBottom) {
//        RecyclerView innerRecyclerView = recyclerView;
//        innerRecyclerView.removeOnLayoutChangeListener(this);
//        final AdaptiveGridLayoutManager layoutManager = (AdaptiveGridLayoutManager) innerRecyclerView.getLayoutManager();
//        View viewAtPosition = layoutManager.findViewByPosition(MainActivity.currentPosition);
//        int last = layoutManager.findLastCompletelyVisibleItemPosition();
//        int first = layoutManager.findFirstCompletelyVisibleItemPosition();
//        // Scroll to position if the view for the current position is null (not currently part of
//        // layout manager children), or it's not completely visible.
//        if (viewAtPosition == null || layoutManager
//                .isViewPartiallyVisible(viewAtPosition, false, true)) {
//          innerRecyclerView.post(() -> innerRecyclerView.scrollToPosition(MainActivity.currentPosition));
//        }
//      }
//    });
//  }

  /**
   * Prepares the shared element transition to the pager fragment, as well as the other transitions
   * that affect the flow.
   */
  private void prepareTransitions() {
    setExitTransition(TransitionInflater.from(getContext())
        .inflateTransition(R.transition.grid_exit_transition));

    // A similar mapping is set at the ImagePagerFragment with a setEnterSharedElementCallback.
    setExitSharedElementCallback(
        new SharedElementCallback() {
          @Override
          public void onMapSharedElements(@Nullable List<String> names, @Nullable Map<String, View> sharedElements) {
            // Locate the ViewHolder for the clicked position.
            RecyclerView.ViewHolder selectedViewHolder = recyclerView
                .findViewHolderForAdapterPosition(MainActivity.currentPosition);
            if (selectedViewHolder == null) {
              return;
            }

            // Map the first shared element name to the child ImageView.
            sharedElements
                .put(names.get(0), selectedViewHolder.itemView.findViewById(R.id.card_image));
          }
        });
  }
}
