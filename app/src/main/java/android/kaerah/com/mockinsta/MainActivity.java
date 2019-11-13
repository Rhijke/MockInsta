package android.kaerah.com.mockinsta;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.kaerah.com.mockinsta.data.model.BitmapScaler;
import android.kaerah.com.mockinsta.data.model.Post;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int SOME_WIDTH = 200;
    private final String TAG = "MainActivity";
    private EditText etDescription;
    private Button btnCaptureImage;
    private ImageView ivPostImage;
    private Button btnSubmit;
    private Toolbar mTopToolbar;

    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public String photoFileName = "photo.jpg";
    File photoFile;


    // Inflate menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Log.i(TAG, "Inflate menu");
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTopToolbar = findViewById(R.id.toolbar);
        etDescription = findViewById(R.id.etDescription);
        btnCaptureImage = findViewById(R.id.btnCaptureImage);
        btnSubmit = findViewById(R.id.btnSubmit);
        ivPostImage = findViewById(R.id.ivCaptureImage);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.mToolbar);
        myToolbar.setLogo(R.drawable.nav_logo_whiteout);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = etDescription.getText().toString();
                ParseUser user = ParseUser.getCurrentUser();
                savePost(description, user, photoFile);
            }
        });

        btnCaptureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                photoFile = getPhotoFileUri(photoFileName);
                // Check that a photo has been taken
                if (photoFile != null || ivPostImage.getDrawable() == null) {
                    btnSubmit.setEnabled(true);
                }
                Uri fileProvider = FileProvider.getUriForFile(MainActivity.this, "com.kaerah.mockinsta", photoFile);
                i.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

                if(i.resolveActivity(getPackageManager())!= null) {
                    startActivityForResult(i, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                }

            }
        });
        //queryPosts();
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // RESIZE BITMAP, see section below
                Uri takenPhotoUri = Uri.fromFile(getPhotoFileUri(photoFileName));
                // by this point we have the camera photo on disk
                Bitmap rawTakenImage = BitmapFactory.decodeFile(takenPhotoUri.getPath());
                Bitmap resizedBitmap = BitmapScaler.scaleToFitWidth(rawTakenImage, 200);
                // Load the taken image into a preview
                ivPostImage.setImageBitmap(resizedBitmap);
            } else {
                Toast.makeText(MainActivity.this,"Unable to take photo", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void savePost(String description, ParseUser user, File photoFile) {
        Post post = new Post();
        post.setDescription(description);
        post.setUser(user);
        post.setImage(new ParseFile(photoFile));
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
                ivPostImage.setImageResource(0);
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
