package com.dzco.vaccination;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dzco.vaccination.Models.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.droidsonroids.gif.GifImageView;

public class FragmentProfile extends Fragment {

    private StorageReference storageRef;
    private DatabaseReference db;
    private RelativeLayout root;
    private CircleImageView accountImage;
    private GifImageView loadinAnimation;
    private TextView nameText;
    private TextView emailText;
    private TextView sexText;
    private TextView ageText;
    private TextView vaccineText;
    private TextView firstDateText;
    private TextView secondDateText;
    private TextView reactivationDate;
    private User user;
    private InProgress progress;

    public static FragmentProfile newInstance() {
        Bundle args = new Bundle();
        FragmentProfile fragment = new FragmentProfile();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        user = new User();

        root = getActivity().findViewById(R.id.main_activity);
        progress = new InProgress(getActivity());

        storageRef = FirebaseStorage.getInstance().getReference();
        nameText = view.findViewById(R.id.name_profile_text);

        accountImage = view.findViewById(R.id.account_image_view);
        if (!TextUtils.equals(user.getEmail(), User.DEFAULT_EMAIL)) {
            accountImage.setOnClickListener(view1 -> {
                Intent intentChooser = new Intent();
                intentChooser.setType("image/*");
                intentChooser.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intentChooser, 1);
            });
        }

        loadinAnimation = view.findViewById(R.id.loading_animation);

        emailText = view.findViewById(R.id.email_profile_text);
        sexText = view.findViewById(R.id.sex_profile_text);
        ageText = view.findViewById(R.id.age_profile_text);
        vaccineText = view.findViewById(R.id.vaccine_profile_text);
        firstDateText = view.findViewById(R.id.first_date_profile_text);
        secondDateText = view.findViewById(R.id.second_date_profile_text);
        reactivationDate = view.findViewById(R.id.revactination_profile_text);

        new DownloadImageTask(accountImage, loadinAnimation, getActivity()).execute(user.getImageURI());
        String reactivationDateString;
        nameText.setText(user.getName());
        emailText.setText(user.getEmail());
        if (user.isSex()) {
            sexText.setText(R.string.male_label);
        } else {
            sexText.setText(R.string.female_label);
        }
        ageText.setText(user.getAge() + "");
        if (TextUtils.equals(user.getVaccine(), User.VACCINES[User.VACCINES.length-1])) {
            vaccineText.setText(R.string.none_label);
        } else {
            vaccineText.setText(user.getVaccine());
        }
        if (TextUtils.equals(user.getVaccine(), User.VACCINES[User.VACCINES.length - 1])) {
            firstDateText.setText(R.string.none_label);
            secondDateText.setText(R.string.none_label);
        } else if (TextUtils.equals(user.getVaccine(), User.VACCINES[User.VACCINES.length - 2])) {
            firstDateText.setText(user.getFirst().toString());
            secondDateText.setText(user.getSecond().toString());

        } else {
            firstDateText.setText(user.getFirst().toString());
            secondDateText.setText(R.string.none_label);
        }
        if (!TextUtils.equals(user.getVaccine(), User.VACCINES[User.VACCINES.length - 1])) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(user.getFirst().getYear(), user.getFirst().getMonth() - 1, user.getFirst().getDay());
            calendar.add(Calendar.DAY_OF_MONTH, user.getTime());
            reactivationDateString = calendar.get(Calendar.DAY_OF_MONTH) + "-"
                    + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.YEAR);
        } else {
            reactivationDateString = getString(R.string.none_label);
        }
        reactivationDate.setText(reactivationDateString);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data != null && data.getData() != null) {

            if (resultCode == RESULT_OK) {
                accountImage.setImageURI(data.getData());
                uploadImage();
            }
        }
    }

    private void uploadImage() {
        progress.show();
        db = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        storageRef = FirebaseStorage.getInstance().getReference();
        Bitmap bitmap = ((BitmapDrawable) accountImage.getDrawable()).getBitmap();
        final int requiredSize = 500;
        int ratio = 1;

        if (bitmap.getWidth() > bitmap.getHeight()) {
            if (bitmap.getHeight() > requiredSize) {
                ratio = bitmap.getHeight() / requiredSize;
            }
        } else {
            if (bitmap.getWidth() > requiredSize) {
                ratio = bitmap.getWidth() / requiredSize;
            }
        }
        Bitmap result = Bitmap.createBitmap(bitmap.getWidth() / ratio, bitmap.getHeight() / ratio, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Rect rect = new Rect(0, 0, bitmap.getWidth() / ratio, bitmap.getHeight() / ratio);
        canvas.drawBitmap(bitmap, null, rect, null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        result.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] byteArray = baos.toByteArray();
        FirebaseStorage.getInstance().getReference().child("Images/" + FirebaseAuth.getInstance().getCurrentUser().getUid()).delete();
        StorageReference connectToDownloadingPic = storageRef.child("Images").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        UploadTask upload = connectToDownloadingPic.putBytes(byteArray);
        Task<Uri> task = upload.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return connectToDownloadingPic.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                user.setImageURI(task.getResult().toString());
                db.child("imageURI").setValue(user.getImageURI());
                progress.hide();
            }
        });

    }
}
