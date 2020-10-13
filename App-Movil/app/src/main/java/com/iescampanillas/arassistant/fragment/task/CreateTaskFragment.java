package com.iescampanillas.arassistant.fragment.task;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.iescampanillas.arassistant.R;
import com.iescampanillas.arassistant.constant.AppCode;
import com.iescampanillas.arassistant.constant.AppString;
import com.iescampanillas.arassistant.database.CategoriesContract;
import com.iescampanillas.arassistant.database.CategoriesDBHelper;
import com.iescampanillas.arassistant.model.Task;
import com.iescampanillas.arassistant.utils.Generator;
import com.iescampanillas.arassistant.utils.KeyboardUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static androidx.navigation.Navigation.findNavController;

public class CreateTaskFragment extends Fragment {

    //Firebase
    private FirebaseDatabase fbDatabase;
    private FirebaseAuth fbAuth;
    private  FirebaseStorage fbStorage;

    //Local database
    private CategoriesDBHelper categoriesDBHelper;

    //Task
    private Task task;

    //Boolean
    private Boolean isUpdate;

    //Inputs
    private EditText taskTitle, taskDescription;
    private Spinner taskCategory;
    private TextView fileName;
    private Button btnViewVideo;

    //Media
    private String imageName;
    private ImageView imageSelected;
    private String videoName;
    private VideoView videoSelected;

    //Uri
    private Uri imageUri;
    private Uri videoUri;

    public CreateTaskFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View createTaskView = inflater.inflate(R.layout.fragment_create_task, container, false);

        //Firebase
        fbDatabase = FirebaseDatabase.getInstance();
        fbAuth = FirebaseAuth.getInstance();
        fbStorage = FirebaseStorage.getInstance();

        //Bind elements
        taskTitle = createTaskView.findViewById(R.id.fragmentCreateTaskTitleText);
        taskDescription = createTaskView.findViewById(R.id.fragmentCreateTaskDescText);
        taskCategory = createTaskView.findViewById(R.id.fragmentCreateTaskCategoriesSpinner);
        //Buttons
        Button btnReturn = createTaskView.findViewById(R.id.fragmentCreateTaskReturnButton);
        Button btnSaveTask = createTaskView.findViewById(R.id.fragmentCreateTaskSaveButton);
        Button btnSelectImage = createTaskView.findViewById(R.id.fragmentCreateTaskSelectImageButton);
        Button btnTakePicture = createTaskView.findViewById(R.id.fragmentCreateTaskTakePictureButton);
        Button btnRecordVideo = createTaskView.findViewById(R.id.fragmentCreateTaskRecordVideoButton);
        btnViewVideo = createTaskView.findViewById(R.id.fragmentCreateTaskViewVideoButton);
        fileName = createTaskView.findViewById(R.id.fragmentCreateTaskFileName);

        //Media elements and variables
        videoUri = Uri.EMPTY;
        imageUri = Uri.EMPTY;
        videoName = "";
        imageName = "";
        videoSelected = createTaskView.findViewById(R.id.fragmentCreateTaskVideoToUpload);
        imageSelected = createTaskView.findViewById(R.id.fragmentCreateTaskImageToUpload);


        //Get categories from database
        categoriesDBHelper = new CategoriesDBHelper(getActivity().getApplicationContext());
        ArrayList<String> spinnerEntries = new ArrayList<>();
        //Get categories
        Cursor nameCursor = categoriesDBHelper.getAllCategories();
        String content = Locale.getDefault().getLanguage();
        //Check language
        nameCursor.move(1);
        if (nameCursor.getColumnIndex(content) == -1) {
            while(nameCursor.moveToNext()) {
                spinnerEntries.add(nameCursor.getString(nameCursor.getColumnIndex(CategoriesContract.CategoriesEntry.CAT_NAME)));
            }
        } else {
            while(nameCursor.moveToNext()) {
                spinnerEntries.add(nameCursor.getString(nameCursor.getColumnIndex(content)));
            }
        }
        //Load the categories in the spinner
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.main_spinner_item_layout, spinnerEntries);
        taskCategory.setAdapter(arrayAdapter);


        //Check if are any arguments for create new task or update
        if(getArguments() != null) {
            //Get arguments
            task = (Task) getArguments().get(AppString.TASK_TO_EDIT);
            taskTitle.setText(task.getTitle());
            taskDescription.setText(task.getDescription());
            taskCategory.setSelection(getCategoryPos(taskCategory, task.getCategory()));
            getTaskMedia();
            isUpdate = true;
        } else {
            //Create new task
            task = new Task();
            isUpdate = false;
        }

        //Show keyboard
        KeyboardUtils.showKeyboard(getActivity());

        //Set focus
        taskTitle.setFocusable(true);
        taskTitle.requestFocus();

        //Return button
        btnReturn.setOnClickListener(v -> {
            findNavController(v).navigateUp();
        });

        //Save button (Method reference)
        btnSaveTask.setOnClickListener(this::saveTask);

        //Open Gallery Button
        btnSelectImage.setOnClickListener(this::openGallery);

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            btnTakePicture.setVisibility(View.GONE);
            btnRecordVideo.setVisibility(View.GONE);
        } else {
            //Take Picture Button
            btnTakePicture.setOnClickListener(this::takePicture);
            //Record Video
            btnRecordVideo.setOnClickListener(this::recordVideo);

        }
        return createTaskView;
    }

    /**
     * Get the position on an item in the spinner
     *
     * @param item The item to get the position
     * @param spinner The spinner to search the item
     * */
    private int getCategoryPos(Spinner spinner, String item) {
        ArrayAdapter<String> spinnerAdapter = (ArrayAdapter<String>) spinner.getAdapter();
        return spinnerAdapter.getPosition(item);
    }

    /**
     * Method to save the task data in firebase
     *
     * @param v is the current view
     * */
    private void saveTask(View v) {
        //Check if title input is empty
        if (taskTitle.getText().toString().equals("")) {
            //Empty
            taskTitle.setError(getString(R.string.error_empty_fields));
        } else {
            //Not empty
            String title = taskTitle.getText().toString();
            String desc = taskDescription.getText().toString();
            String cat = taskCategory.getSelectedItem().toString();
            task.setTitle(title);
            task.setDescription(desc);
            task.setCategory(cat);

            //Get icon and color
            Cursor colorAndIconCursor = categoriesDBHelper.getCategoryColorAndIconByName(cat, CategoriesContract.CategoriesEntry.CAT_NAME);
            while(colorAndIconCursor.moveToNext()) {
                task.setColor(colorAndIconCursor.getString(colorAndIconCursor.getColumnIndex(CategoriesContract.CategoriesEntry.CAT_COLOR)));
                task.setIcon(colorAndIconCursor.getInt(colorAndIconCursor.getColumnIndex(CategoriesContract.CategoriesEntry.CAT_ICON)));
            }

            //Database reference
            DatabaseReference dbRef = fbDatabase.getReference();

            //Get user UID
            String uid = fbAuth.getCurrentUser().getUid();

            //Check if is an update or create a new task
            if (isUpdate) {
                //Update the task in Firebase
                HashMap<String, Object> taskUpdate = new HashMap<>();
                taskUpdate.put(AppString.DB_TASK_REF + uid + "/" + task.getId(), task);
                updateMedia(); //Update the task image
                dbRef.updateChildren(taskUpdate).addOnSuccessListener(aVoid -> {
                    //Update Success
                    Toast.makeText(v.getContext(), R.string.toast_update_task_success, Toast.LENGTH_LONG).show();
                }).addOnFailureListener(e -> {
                    //Update Failed
                    Toast.makeText(v.getContext(), R.string.toast_update_task_error, Toast.LENGTH_LONG).show();
                }).addOnCompleteListener(task1 -> findNavController(v).navigateUp());//Return to task fragment
            } else {
                //New task
                //Generate task Id
                String taskId = Generator.generateId(AppString.TASK_PREFIX);
                task.setId(taskId);
                uploadNewMedia(); //Upload an image
                //Create the task in Firebase
                dbRef.child(AppString.DB_TASK_REF).child(uid).child(taskId).setValue(task)
                        .addOnSuccessListener(aVoid -> {
                            //Created Successfully
                            Toast.makeText(getActivity().getApplicationContext(), R.string.toast_create_task_success, Toast.LENGTH_LONG).show();
                            taskTitle.getText().clear();
                            taskDescription.getText().clear();
                            taskCategory.setSelection(0);
                            imageSelected.setImageResource(0);
                            //Set focus
                            taskTitle.setFocusable(true);
                            taskTitle.requestFocus();
                        }).addOnFailureListener(e -> {
                            //Failure in creation process
                            Toast.makeText(getActivity().getApplicationContext(), R.string.toast_create_task_error, Toast.LENGTH_LONG).show();
                        });

            }
        }
    }

    /**
     * Upload the selected image to Firebase Storage
     * */
    private void uploadNewMedia() {
        if(imageUri != Uri.EMPTY && !imageName.equals("")) {
            StorageReference storageRef = fbStorage.getReference().child(AppString.IMAGES_FOLDER).child(task.getId()).child(imageName);
            task.setMedia(imageName);
            task.setMediaType(AppString.IMAGE_TYPE);
            storageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                Toast.makeText(getContext(), R.string.toast_image_upload_success, Toast.LENGTH_LONG).show();
            }).addOnFailureListener(e -> {
                Toast.makeText(getContext(), R.string.toast_image_upload_failure, Toast.LENGTH_LONG).show();
            });
        } else if (videoUri != Uri.EMPTY && !videoName.equals("")) {
            StorageReference storageRef = fbStorage.getReference().child(AppString.VIDEOS_FOLDER).child(task.getId()).child(videoName);
            task.setMedia(videoName);
            task.setMediaType(AppString.VIDEO_TYPE);
            storageRef.putFile(videoUri).addOnSuccessListener(taskSnapshot -> {
                Toast.makeText(getContext(), R.string.toast_image_upload_success, Toast.LENGTH_LONG).show();
            }).addOnFailureListener(e -> {
                Toast.makeText(getContext(), R.string.toast_image_upload_failure, Toast.LENGTH_LONG).show();
            });
        } else {
            task.setMedia("");
            task.setMediaType("");
        }
    }

    /**
     * Update the current image of a task
     * */
    private void updateMedia() {
        if(!task.getMedia().equals("")) {
            if (task.getMediaType().equals(AppString.IMAGE_TYPE)) {
                StorageReference oldStorageRef = fbStorage.getReference().child(AppString.IMAGES_FOLDER).child(task.getId()).child(task.getMedia());
                oldStorageRef.delete();
            } else {
                StorageReference oldStorageRef = fbStorage.getReference().child(AppString.VIDEOS_FOLDER).child(task.getId()).child(task.getMedia());
                oldStorageRef.delete();
            }
            if (imageUri != Uri.EMPTY && !imageName.equals("")) {
                StorageReference newStorageRef = fbStorage.getReference().child(AppString.IMAGES_FOLDER).child(task.getId()).child(imageName);
                task.setMedia(imageName);
                task.setMediaType(AppString.IMAGE_TYPE);
                newStorageRef.putFile(imageUri);
            } else if (videoUri != Uri.EMPTY && !videoName.equals("")){
                StorageReference newStorageRef = fbStorage.getReference().child(AppString.VIDEOS_FOLDER).child(task.getId()).child(videoName);
                task.setMedia(videoName);
                task.setMediaType(AppString.VIDEO_TYPE);
                newStorageRef.putFile(videoUri);
            }
        }
    }

    /**
     * Get task image from Firebase Storage
     * */
    private void getTaskMedia() {
        if(!task.getMedia().equals("")) {
            switch(task.getMediaType()) {
                case AppString.IMAGE_TYPE:
                    StorageReference imageRef = fbStorage.getReference().child(AppString.IMAGES_FOLDER).child(task.getId()).child(task.getMedia());
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        Picasso.get().load(uri).into(imageSelected);
                        fileName.setText(task.getMedia());
                    });
                    break;
                case AppString.VIDEO_TYPE:
                    StorageReference videoRef = fbStorage.getReference().child(AppString.VIDEOS_FOLDER).child(task.getId()).child(task.getMedia());
                    videoRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        btnViewVideo.setVisibility(View.VISIBLE);
                        btnViewVideo.setOnClickListener(view -> openVideo(uri));
                        fileName.setText(task.getMedia());
                    });
                    break;
            }
        }
    }

    /**
     * open video
     */
    private void openVideo(Uri uri) {
        Intent viewVideoIntent = new Intent(Intent.ACTION_VIEW);
        viewVideoIntent.setData(uri);
        startActivity(viewVideoIntent);
    }

    /**
     *Open gallery and select a picture
     */
    private void openGallery(View v){
        Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(openGalleryIntent, AppCode.OPEN_GALLERY);
    }

    /**
     * Take a picture with the camera
     * */
    private void takePicture(View v) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imageUri = Uri.fromFile(getOutputImageFile());
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, AppCode.TAKE_PICTURE);
        }
    }

    /**
     * Create output media file to store the photo
     */
    private static File getOutputImageFile() {
        File imageStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), AppString.MEDIA_DIR);
        if (!imageStorageDir.exists()) {
            if (!imageStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat(AppString.TIME_LONG_FORMAT).format(new Date());
        return new File(imageStorageDir.getPath() + File.separator
                         + AppString.IMAGE_PREFIX + timeStamp + AppString.IMAGE_SUFFIX);
    }

    /**
     * Record a video with the camera
     * */
    private void recordVideo(View v) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent takePictureIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        videoUri = Uri.fromFile(getOutputVideoFile());
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, AppCode.RECORD_VIDEO);
        }
    }

    /**
     * Create output media file to store the video
     */
    private static File getOutputVideoFile() {
        File videoStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), AppString.MEDIA_DIR);
        if (!videoStorageDir.exists()) {
            if (!videoStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat(AppString.TIME_LONG_FORMAT).format(new Date());
        return new File(videoStorageDir.getPath() + File.separator
                + AppString.VIDEO_PREFIX + timeStamp + AppString.VIDEO_SUFFIX);
    }

    /**
     * Clear image
     */
    private void clearImage() {
        imageName = "";
        imageUri = Uri.EMPTY;
        imageSelected.setImageURI(imageUri);
    }

    /**
     * Clear video
     */
    private void clearVideo() {
        videoSelected.setMinimumHeight(0);
        videoName = "";
        videoUri = Uri.EMPTY;
        videoSelected.setVideoURI(videoUri);
        videoSelected.setVisibility(View.GONE);
    }

    /**
     * Load the local image in the ImageView
     * */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case AppCode.RECORD_VIDEO:
                if(resultCode == Activity.RESULT_OK) {
                    btnViewVideo.setVisibility(View.GONE);
                    videoSelected.setVideoURI(videoUri);
                    videoSelected.setVisibility(View.VISIBLE);
                    File f = new File("" + videoUri);
                    videoName = f.getName();
                    fileName.setText(videoName);
                    MediaController mediaController = new MediaController(getContext());
                    videoSelected.setMediaController(mediaController);
                    mediaController.setAnchorView(videoSelected);
                    clearImage();
                }
                break;
            case AppCode.TAKE_PICTURE:
                if(resultCode == Activity.RESULT_OK) {
                    btnViewVideo.setVisibility(View.GONE);
                    imageSelected.setImageURI(imageUri);
                    File f = new File("" + imageUri);
                    imageName = f.getName();
                    fileName.setText(imageName);
                    clearVideo();
                }
                break;
            case AppCode.OPEN_GALLERY:
                if(resultCode == Activity.RESULT_OK) {
                    btnViewVideo.setVisibility(View.GONE);
                    //Get image Uri
                    imageUri = data.getData();

                    //Get file name
                    Cursor cursor = getActivity().getContentResolver().query(imageUri,
                            null, null, null, null,
                            null);
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    cursor.moveToFirst();
                    imageName = cursor.getString(nameIndex);
                    fileName.setText(imageName);

                    //Get file size
                    File tempFile = new File(imageUri.getPath());
                    long fileSizeInMb = (tempFile.length() / 1024) / 1024;

                    //Check image size, width and height
                    Bitmap bitmap;
                    try {
                        bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
                        if (bitmap.getHeight() > 4096 || bitmap.getWidth() > 4096) { //Check width and height
                            Toast.makeText(getContext(), R.string.toast_image_too_big_error, Toast.LENGTH_LONG).show();
                            imageUri = Uri.EMPTY;
                        } else if (fileSizeInMb > 1) { //Check size
                            Toast.makeText(getContext(), R.string.toast_image_size_too_big_error, Toast.LENGTH_LONG).show();
                            imageUri = Uri.EMPTY;
                        } else {
                            imageSelected.setImageBitmap(bitmap); //Put image on image view
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    clearVideo();
                }
                break;

        }
    }
}
