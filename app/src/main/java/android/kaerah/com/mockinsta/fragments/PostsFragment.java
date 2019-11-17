package android.kaerah.com.mockinsta.fragments;

import android.content.Context;
import android.kaerah.com.mockinsta.PostsAdapter;
import android.kaerah.com.mockinsta.R;
import android.kaerah.com.mockinsta.data.model.Post;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class PostsFragment extends Fragment {
    private final String TAG = "PostsFragment";
    private Context context;
    private PostsAdapter adapter;
    private List<Post> posts;

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_posts, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.i(TAG, "Posts frag created");
        RecyclerView rvPosts = view.findViewById(R.id.rvPosts);
        context = getContext();
        posts = new ArrayList<>();
        adapter = new PostsAdapter(context, posts);
        rvPosts.setAdapter(adapter);
        rvPosts.setLayoutManager(new LinearLayoutManager(context));
        Log.i(TAG, "Query Posts");
        queryPosts();

    }

    // Get users posts from Parse
    private void queryPosts() {
        final ParseQuery<Post> postParseQuery = new ParseQuery<Post>(Post.class);
        postParseQuery.include(Post.KEY_USER);
        postParseQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> queryPosts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, e.toString());
                    e.printStackTrace();
                    return;
                }
                posts.addAll(queryPosts);
                adapter.notifyDataSetChanged();
                for (Post post: posts) {
                    Log.i(TAG, post.getUser().getUsername().toString());
                }
            }
        });
    }
}
