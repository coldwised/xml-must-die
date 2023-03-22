package com.example.mdc.adapter;

import android.graphics.drawable.Drawable;
import android.transition.TransitionSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.mdc.MainActivity;
import com.example.mdc.R;
import com.example.mdc.fragment.ImagePagerFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A fragment for displaying a grid of images.
 */
public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ImageViewHolder> {

  /**
   * A listener that is attached to all ViewHolders to handle image loading events and clicks.
   */
  private interface ViewHolderListener {

    void onLoadCompleted(@NotNull ImageView view, int adapterPosition);

    void onItemClicked(@NotNull View view, int adapterPosition);
  }

  @Nullable
  private final RequestManager requestManager;
  @Nullable
  private final ViewHolderListener viewHolderListener;
  @Nullable
  private ArrayList<String> list;

  /**
   * Constructs a new grid adapter for the given {@link Fragment}.
   */
  public GridAdapter(@NotNull Fragment fragment) {
    this.requestManager = Glide.with(fragment);
    this.viewHolderListener = new ViewHolderListenerImpl(fragment);
  }

  public void submitList(@NotNull ArrayList<String> list) {
    this.list = list;
  }

  @NonNull
  @Override
  public ImageViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.image_card, parent, false);
    return new ImageViewHolder(view, requestManager, viewHolderListener);
  }

  @Override
  public void onBindViewHolder(@NotNull ImageViewHolder holder, int position) {
    holder.onBind(list);
  }

  @Override
  public int getItemCount() {
    return list.size();
  }


  /**
   * Default {@link ViewHolderListener} implementation.
   */
  private static class ViewHolderListenerImpl implements ViewHolderListener {

    @NonNull
    private final Fragment fragment;
    @NonNull
    private final AtomicBoolean enterTransitionStarted;

    ViewHolderListenerImpl(@NotNull Fragment fragment) {
      this.fragment = fragment;
      this.enterTransitionStarted = new AtomicBoolean();
    }

    @Override
    public void onLoadCompleted(@NonNull ImageView view, int position) {
      // Call startPostponedEnterTransition only when the 'selected' image loading is completed.
      if (MainActivity.currentPosition != position) {
        return;
      }
      if (enterTransitionStarted.getAndSet(true)) {
        return;
      }
      fragment.startPostponedEnterTransition();
    }

    /**
     * Handles a view click by setting the current position to the given {@code position} and
     * starting a {@link  ImagePagerFragment} which displays the image at the position.
     *
     * @param view the clicked {@link ImageView} (the shared element view will be re-mapped at the
     * GridFragment's SharedElementCallback)
     * @param position the selected view position
     */
    @Override
    public void onItemClicked(@NonNull View view, int position) {
      // Update the position.
      MainActivity.currentPosition = position;

      // Exclude the clicked card from the exit transition (e.g. the card will disappear immediately
      // instead of fading out with the rest to prevent an overlapping animation of fade and move).
      ((TransitionSet) Objects.requireNonNull(fragment.getExitTransition())).excludeTarget(view, true);

      ImageView transitioningView = view.findViewById(R.id.card_image);
      fragment.getParentFragmentManager()
          .beginTransaction()
          .setReorderingAllowed(true) // Optimize for shared element transition
          .addSharedElement(transitioningView, transitioningView.getTransitionName())
          .replace(R.id.fragment_container, new ImagePagerFragment(), ImagePagerFragment.class
              .getSimpleName())
          .addToBackStack(null)
          .commit();
    }
  }

  /**
   * ViewHolder for the grid's images.
   */
  static class ImageViewHolder extends RecyclerView.ViewHolder implements
      View.OnClickListener {

    @NotNull
    private final ImageView image;
    @NotNull
    private final RequestManager requestManager;
    @NotNull
    private final ViewHolderListener viewHolderListener;

    ImageViewHolder(@NotNull View itemView, @NonNull RequestManager requestManager,
                    @NonNull ViewHolderListener viewHolderListener) {
      super(itemView);
      this.image = itemView.findViewById(R.id.card_image);
      this.requestManager = requestManager;
      this.viewHolderListener = viewHolderListener;
      itemView.findViewById(R.id.card_image).setOnClickListener(this);
    }

    /**
     * Binds this view holder to the given adapter position.
     *
     * The binding will load the image into the image view, as well as set its transition name for
     * later.
     */
    void onBind(@NotNull ArrayList<String> list) {
      int adapterPosition = getAdapterPosition();
      setImage(adapterPosition, list);
      // Set the string value of the image resource as the unique transition name for the view.
      image.setTransitionName(String.valueOf(list.get(adapterPosition)));
    }

    void setImage(final int adapterPosition, @NotNull ArrayList<String> list) {
      // Load the image with Glide to prevent OOM error when the image drawables are very large.
      ImageView imageView = image;
      requestManager
          .load(list.get(adapterPosition))
          .listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, @Nullable Object model,
                                        @Nullable  Target<Drawable> target, boolean isFirstResource) {
              viewHolderListener.onLoadCompleted(imageView, adapterPosition);
              return false;
            }

            @Override
            public boolean onResourceReady(@Nullable Drawable resource, @Nullable Object model, @Nullable Target<Drawable>
                target, @Nullable DataSource dataSource, boolean isFirstResource) {
              viewHolderListener.onLoadCompleted(imageView, adapterPosition);
              return false;
            }
          })
          .into(image);
    }

    @Override
    public void onClick(@NotNull View view) {
      // Let the listener start the ImagePagerFragment.
      viewHolderListener.onItemClicked(view, getAdapterPosition());
    }
  }

}