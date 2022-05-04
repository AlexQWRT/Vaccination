package com.dzco.myapplication;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.dzco.myapplication.Models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class FragmentSettings extends Fragment {

    private RelativeLayout root;
    private Button deleteProfileImageButton;
    private Button signOutButton;
    private Button editProfileButton;
    private Button deleteAccountButton;
    private User user;

    public static FragmentSettings newInstance() {
        Bundle args = new Bundle();
        FragmentSettings fragment = new FragmentSettings();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        root = (RelativeLayout) view.findViewById(R.id.settings_root_fragment);
        user = new User();

        deleteProfileImageButton = (Button) view.findViewById(R.id.delete_profile_image_button);
        deleteProfileImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.equals(user.getImageURI(), User.DEFAULT_IMAGE)) {
                    Snackbar.make(root, R.string.no_image_for_delete_label, Snackbar.LENGTH_LONG).show();
                    return;
                }
                InProgress progress = new InProgress(getActivity());
                progress.show();
                DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                db.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        FirebaseStorage.getInstance().getReference().child("Images/" + FirebaseAuth.getInstance().getCurrentUser().getUid()).delete();
                        user.setImageURI(User.DEFAULT_IMAGE);
                        db.child("imageURI").setValue(User.DEFAULT_IMAGE);
                        progress.hide();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(root, e.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                });

            }
        });

        signOutButton = (Button) view.findViewById(R.id.signout_profile_button);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), AuthorizationActivity.class));
                getActivity().finish();
            }
        });

        editProfileButton = (Button) view.findViewById(R.id.edit_profile_button);
        if (TextUtils.equals(user.getEmail(), User.DEFAULT_EMAIL)) {
            editProfileButton.setEnabled(false);
            editProfileButton.setBackgroundColor(getResources().getColor(R.color.gray));
        }
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), EditProfileActivity.class));
            }
        });

        deleteAccountButton = (Button) view.findViewById(R.id.delete_profile_button);
        if (TextUtils.equals(user.getEmail(), User.DEFAULT_EMAIL)) {
            deleteAccountButton.setEnabled(false);
            deleteAccountButton.setBackgroundColor(getResources().getColor(R.color.gray));
        }
        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InProgress progress = new InProgress(getActivity());
                progress.show();
                FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                FirebaseStorage.getInstance().getReference().child("Images/" + FirebaseAuth.getInstance().getCurrentUser().getUid()).delete();
                                FirebaseAuth.getInstance().getCurrentUser().delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        FirebaseAuth.getInstance().signOut();
                                        progress.hide();
                                        startActivity(new Intent(getActivity(), AuthorizationActivity.class));
                                        getActivity().finish();
                                    }
                                });
                            }
                        });
            }
        });
        return view;
    }
}
