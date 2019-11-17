package android.kaerah.com.mockinsta;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.kaerah.com.mockinsta.data.model.Post;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.parse.ParseFile;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {
    private Context context;
    private List<Post> posts;

    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }
    

    @Override
    public int getItemCount() {
        return posts.size();
    }
    // Clean all elements of the recycler

    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }
// Add a list of items -- change to type used

    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvHandle, tvDescription;
        private ImageView ivImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvHandle = itemView.findViewById(R.id.tvHandle);
            ivImage = itemView.findViewById(R.id.ivImage);
        }

        public void bind(Post post) {
            tvDescription.setText(post.getDescription()) ;
            ParseFile image = post.getImage();
            if (image != null) {
                Log.i("Posts", image.getUrl());
                Glide.with(context)
                        .load(image.getUrl())
                        .placeholder(R.drawable.camera_shadow_fill)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                // log exception
                                Log.e("TAG", "Error loading image", e);
                                return false; // important to return false so the error placeholder can be placed
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                return false;
                            }
                        })
                        .fitCenter()
                        .into(ivImage);
            } else {
                Log.i("Posts", "Image is null.");
            }
            tvHandle.setText(post.getUser().getUsername());
        }
    }
}
