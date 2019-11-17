package android.kaerah.com.mockinsta;

import android.content.Intent;
import android.kaerah.com.mockinsta.fragments.ComposeFragment;
import android.kaerah.com.mockinsta.ui.login.LoginActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {
    private static final int SOME_WIDTH = 200;
    private final String TAG = "MainActivity";
    private BottomNavigationView bottomNavigationView;
    // Inflate menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.mToolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);



            // Navigation menu

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                // Begin the transaction
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                switch(menuItem.getItemId()) {
                    case R.id.action_compose:
                        ft.replace(R.id.frameManager, new ComposeFragment());
                        ft.commit();
                        return true;
                    case R.id.action_home:
                        return true;
                    case R.id.action_profile:
                        return true;
                    default:
                        return false;
                }
            }
        });
        //queryPosts();
    }


    // Handle menu items on clicks
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout() {
        ParseUser.logOut();
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }


//    private void queryPosts() {
//        final ParseQuery<Post> postParseQuery = new ParseQuery<Post>(Post.class);
//        postParseQuery.include(Post.KEY_USER);
//        postParseQuery.findInBackground(new FindCallback<Post>() {
//            @Override
//            public void done(List<Post> posts, ParseException e) {
//                if (e != null) {
//                    Log.e(TAG, e.toString());
//                    e.printStackTrace();
//                    return;
//                }
//                for (Post post: posts) {
//                    Log.i(TAG, "Post: " + post.getDescription());
//                    etDescription.setText(post.getDescription());
//                }
//
//            }
//        });
//    }
}
