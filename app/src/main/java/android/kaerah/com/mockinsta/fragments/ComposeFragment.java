package android.kaerah.com.mockinsta.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.kaerah.com.mockinsta.R;
import android.kaerah.com.mockinsta.data.model.BitmapScaler;
import android.kaerah.com.mockinsta.data.model.Post;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;

import static android.app.Activity.RESULT_OK;

public class ComposeFragment extends Fragment {
        private final String TAG = "ComposeFragment";
        private EditText etDescription;
        private Button btnCaptureImage;
        private ImageView ivPostImage;
        private Button btnSubmit;
        public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
        public String photoFileName = "photo.jpg";
        File photoFile;
        Context context ;
        // The onCreateView method is called when Fragment should create its View object hierarchy,
        // either dynamically or via XML layout inflation.
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
            // Defines the xml file for the fragment

            return inflater.inflate(R.layout.fragment_compose, parent, false);
        }

        // This event is triggered soon after onCreateView().
        // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
                Log.i(TAG, "Compose frag created");
                etDescription = view.findViewById(R.id.etDescription);
                btnCaptureImage = view.findViewById(R.id.btnCaptureImage);
                btnSubmit = view.findViewById(R.id.btnSubmit);
                ivPostImage = view.findViewById(R.id.ivCaptureImage);
                context = getContext();
                // Set on click listeners
                setOnClickBtnSubmit();
                setOnClickBtnCaptureImage();
        }

        private void setOnClickBtnSubmit() {
                btnSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                String description = etDescription.getText().toString();
                                ParseUser user = ParseUser.getCurrentUser();
                                savePost(description, user, photoFile);
                        }
                });
        }

        private void setOnClickBtnCaptureImage() {
                btnCaptureImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                photoFile = getPhotoFileUri(photoFileName);
                                // Check that a photo has been taken
                                if (photoFile != null || ivPostImage.getDrawable() == null) {
                                        btnSubmit.setEnabled(true);
                                }
                                Uri fileProvider = FileProvider.getUriForFile(context, "com.kaerah.mockinsta", photoFile);
                                i.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

                                if(i.resolveActivity(context.getPackageManager())!= null) {
                                        startActivityForResult(i, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                                }

                        }
                });
        }

        // Returns the File for a photo stored on disk given the fileName
        public File getPhotoFileUri(String fileName) {
                // Get safe storage directory for photos
                // Use `getExternalFilesDir` on Context to access package-specific directories.
                // This way, we don't need to request external read/write runtime permissions.
                File mediaStorageDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

                // Create the storage directory if it does not exist
                if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
                        Log.d(TAG, "failed to create directory");
                }

                // Return the file target for the photo based on filename
                File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

                return file;
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
                                Toast.makeText(context,"Unable to take photo", Toast.LENGTH_SHORT).show();
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

}
