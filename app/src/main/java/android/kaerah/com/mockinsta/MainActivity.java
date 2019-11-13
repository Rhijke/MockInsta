package android.kaerah.com.mockinsta;

import androidx.appcompat.app.AppCompatActivity;

import android.kaerah.com.mockinsta.data.model.Post;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    private EditText etDescription;
    private Button btnCaptureImage;
    private ImageView ivPostImage;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etDescription = findViewById(R.id.etDescription);
        btnCaptureImage = findViewById(R.id.btnCaptureImage);
        btnSubmit = findViewById(R.id.btnSubmit);
        ivPostImage = findViewById(R.id.ivCaptureImage);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = etDescription.getText().toString();
                ParseUser user = ParseUser.getCurrentUser();
                savePost(description, user);
            }
        });
        //queryPosts();
    }

    private void savePost(String description, ParseUser user) {
        Post post = new Post();
        post.setDescription(description);
        post.setUser(user);
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, e.toString());
                    e.printStackTrace();
                    return;
                }
                
                Log.d(TAG, "Successful post");
                etDescription.setText("");
            }
        });
    }

    private void queryPosts() {
        final ParseQuery<Post> postParseQuery = new ParseQuery<Post>(Post.class);
        postParseQuery.include(Post.KEY_USER);
        postParseQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, e.toString());
                    e.printStackTrace();
                    return;
                }
                for (Post post: posts) {
                    Log.i(TAG, "Post: " + post.getDescription());
                    etDescription.setText(post.getDescription());
                }

            }
        });
    }
}
