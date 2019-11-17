package android.kaerah.com.mockinsta.fragments;

import android.kaerah.com.mockinsta.data.model.Post;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class ProfileFragment extends PostsFragment {

    private final String TAG = "ProfileFragment";
    // Get users posts from Parse
    protected void queryPosts() {
        final ParseQuery<Post> postParseQuery = new ParseQuery<Post>(Post.class);
        postParseQuery.include(Post.KEY_USER);
        postParseQuery.setLimit(20);
        postParseQuery.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        postParseQuery.addDescendingOrder(Post.KEY_CREATED_AT);
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
