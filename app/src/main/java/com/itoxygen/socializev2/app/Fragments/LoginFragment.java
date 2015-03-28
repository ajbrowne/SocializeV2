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

import com.itoxygen.socializev2.app.Activities.LoginActivity;
import com.itoxygen.socializev2.app.R;
import com.itoxygen.socializev2.app.SignUpFragments.SignupNameFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class LoginFragment extends Fragment {

    private Button login;
    private TextView emailText;
    private TextView passwordText;
    private String email;
    private String password;
    private Map<String,String> params;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View loginView = inflater.inflate(R.layout.fragment_login, container, false);
        login  = (Button) loginView.findViewById(R.id.loginButton);
        emailText = (TextView) loginView.findViewById(R.id.email);
        passwordText = (TextView) loginView.findViewById(R.id.password);
        params = new HashMap<String, String>();
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

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailText.getText().toString();
                password = passwordText.getText().toString();
                ((LoginActivity) getActivity()).login(email, password);
            }
        });

        return loginView;
    }


}
