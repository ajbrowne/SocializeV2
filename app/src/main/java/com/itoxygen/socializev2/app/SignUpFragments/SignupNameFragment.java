package com.itoxygen.socializev2.app.SignUpFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.itoxygen.socializev2.app.Activities.LoginActivity;
import com.itoxygen.socializev2.app.Fragments.LoginFragment;
import com.itoxygen.socializev2.app.R;

/**
 * Fragment used to get name during sign up or info submission if user isn't logged in.
 */
public class SignupNameFragment extends Fragment {

    private EditText first;
    private EditText last;
    private String firstName;
    private String lastName;

    public SignupNameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_signup_name, container, false);
        Button next = (Button) view.findViewById(R.id.flow2_button);
        TextView signIn = (TextView) view.findViewById(R.id.flow2_signin);
        first = (EditText) view.findViewById(R.id.flow2_first);
        last = (EditText) view.findViewById(R.id.flow2_last);
        if (getActivity().getIntent().getBooleanExtra("submit", false)) {
            signIn.setVisibility(View.GONE);
        }

        nextListener(next);
        signInListener(signIn);

        return view;
    }

    private void signInListener(TextView signIn) {
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginFragment fragment = new LoginFragment();
                Bundle bundle = new Bundle();
                bundle.putString("clear", "clear");
                fragment.setArguments(bundle);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragment.setArguments(bundle);
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_right);
                fragmentTransaction.replace(R.id.loginContainer, fragment);
                fragmentTransaction.commit();
            }
        });
    }

    private void nextListener(Button next) {
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstName = first.getText().toString();
                lastName = last.getText().toString();
                ((LoginActivity) getActivity()).getUser().setFirstName(firstName);
                ((LoginActivity) getActivity()).getUser().setLastName(lastName);
                SignupEmailFragment fragment = new SignupEmailFragment();

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left, R.anim.slide_in_right, R.anim.slide_out_right);
                fragmentTransaction.replace(R.id.loginContainer, fragment, "signupNameFragment").addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

}
