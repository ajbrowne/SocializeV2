package com.itoxygen.socializev2.app.SignUpFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.itoxygen.socializev2.app.Activities.LoginActivity;
import com.itoxygen.socializev2.app.AlertDialogs.CustomAlertDialog;
import com.itoxygen.socializev2.app.Fragments.LoginFragment;
import com.itoxygen.socializev2.app.R;

/**
 * Fragment used to get password and then retrieve other info during sign up or submission.
 */
public class SignupPasswordFragment extends Fragment {

    private EditText password;
    private EditText confirm;
    private String userPassword;
    private String confirmPassword;
    private String email;
    private String first;
    private String last;
    private String zip;
    private String phone;

    public SignupPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_signup_password, container, false);
        Button next = (Button) view.findViewById(R.id.flow4_button);
        TextView signIn = (TextView) view.findViewById(R.id.flow4_signin);
        password = (EditText) view.findViewById(R.id.flow4_password);
        confirm = (EditText) view.findViewById(R.id.flow4_verify);
        if (getActivity().getIntent().getBooleanExtra("submit", false)) {
            signIn.setVisibility(View.GONE);
        }

        // Link listeners to identify matching passwords
        confirm.addTextChangedListener(new MyTextWatcher());
        password.addTextChangedListener(new MyTextWatcher());

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get all previously entered info
                userPassword = password.getText().toString();
                confirmPassword = confirm.getText().toString();
                email = ((LoginActivity) getActivity()).getUser().getEmail();
                first = ((LoginActivity) getActivity()).getUser().getFirstName();
                last = ((LoginActivity) getActivity()).getUser().getLastName();

                // Password checking
                if (userPassword.compareTo(confirmPassword) != 0) {
                    new CustomAlertDialog(getActivity(), "Passwords do not match", "Please enter your password correctly both times.").show();
                } else if (userPassword.length() == 0 || confirmPassword.length() == 0) {
                    new CustomAlertDialog(getActivity(), "Password of length zero", "Passwords of length zero are not allowed.").show();
                } else {
                    ((LoginActivity) getActivity()).getUser().setPassword(userPassword);
                    ((LoginActivity) getActivity()).register(email, userPassword); // Async call to register
                }
            }
        });

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
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left, R.anim.slide_in_right, R.anim.slide_out_right);
                fragmentTransaction.replace(R.id.loginContainer, fragment);
                fragmentTransaction.commit();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    /**
     * Text Watcher used to confirm that passwords are matching, displayed with red/green
     * surrounding of text box
     */
    private class MyTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String pass = password.getText().toString();
            String conf = confirm.getText().toString();

            if (pass.compareTo(conf) == 0 && pass.length() != 0 && conf.length() != 0) {
                password.setBackground(getResources().getDrawable(R.drawable.rounded_confirm));
                confirm.setBackground(getResources().getDrawable(R.drawable.rounded_confirm));
            } else {
                password.setBackground(getResources().getDrawable(R.drawable.rounded_input));
                confirm.setBackground(getResources().getDrawable(R.drawable.rounded_input));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

}
