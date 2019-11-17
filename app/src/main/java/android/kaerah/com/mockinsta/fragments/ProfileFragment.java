package android.kaerah.com.mockinsta.fragments;

import android.content.Context;
import android.kaerah.com.mockinsta.R;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.parse.ParseUser;

public class ProfileFragment extends Fragment {

    private final String TAG = "ProfileFragment";
    private TextView tvUserName;
    Context context = getContext();
    ParseUser user;
    private TextView tvUserHandle;

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_profile, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.i(TAG, "Profile frag created");
      tvUserName = view.findViewById(R.id.tvUserName);
      tvUserHandle = view.findViewById(R.id.tvUserHandle);
      context = getContext();
      user = ParseUser.getCurrentUser();

      tvUserName.setText(user.getString("username"));
      tvUserHandle.setText(user.getString("handle"));

    }

}
