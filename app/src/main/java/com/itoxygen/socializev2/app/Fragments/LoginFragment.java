package com.itoxygen.socializev2.app.Fragments;



import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.itoxygen.socializev2.app.R;
import com.itoxygen.socializev2.app.SignUpFragments.SignupNameFragment;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class LoginFragment extends Fragment {


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View loginView = inflater.inflate(R.layout.fragment_login, container, false);
        TextView loginTitle = (TextView) loginView.findViewById(R.id.login_title);
        Button signUp = (Button) loginView.findViewById(R.id.signUpButton);
        Typeface typeFace=Typeface.createFromAsset(getActivity().getAssets(),"fonts/Pacifico.ttf");
        loginTitle.setTypeface(typeFace);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        SignupNameFragment fragment = new SignupNameFragment();

                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left, R.anim.slide_in_right, R.anim.slide_out_right);
                        fragmentTransaction.replace(R.id.loginContainer, fragment, "number").addToBackStack(null);
                        fragmentTransaction.commit();
            }
        });

        return loginView;
    }


}
