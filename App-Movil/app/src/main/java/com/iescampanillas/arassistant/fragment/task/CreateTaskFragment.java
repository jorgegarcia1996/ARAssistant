package com.iescampanillas.arassistant.fragment.task;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.iescampanillas.arassistant.R;
import com.iescampanillas.arassistant.constant.AppCode;
import com.iescampanillas.arassistant.constant.AppString;
import com.iescampanillas.arassistant.database.CategoriesDBHelper;
import com.iescampanillas.arassistant.database.CategoriesContract;
import com.iescampanillas.arassistant.model.Task;
import com.iescampanillas.arassistant.utils.Generator;
import com.iescampanillas.arassistant.utils.KeyboardUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

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

    private String content, imageName;

    private ImageView imageSelected;

    //Uri
    private Uri localImageUri;

    //Buttons
    private Button btnReturn, btnSaveTask, btnSelectImage;

    public CreateTaskFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View createTaskView = inflater.inflate(R.layout.fragment_create_task, container, false);

        //Bind elements
        fbDatabase = FirebaseDatabase.getInstance();
        fbAuth = FirebaseAuth.getInstance();
        fbStorage = FirebaseStorage.getInstance();
        taskTitle = createTaskView.findViewById(R.id.fragmentCreateTaskTitleText);
        taskDescription = createTaskView.findViewById(R.id.fragmentCreateTaskDescText);
        taskCategory = createTaskView.findViewById(R.id.fragmentCreateTaskCategoriesSpinner);
        btnReturn = createTaskView.findViewById(R.id.fragmentCreateTaskReturnButton);
        btnSaveTask = createTaskView.findViewById(R.id.fragmentCreateTaskSaveButton);
        btnSelectImage = createTaskView.findViewById(R.id.fragmentCreateTaskSelectImageButton);

        //Image elements and variables
        localImageUri = Uri.EMPTY;
        imageName = "";
        imageSelected = createTaskView.findViewById(R.id.fragmentCreateTaskImageToUpload);

        //Get categories from database
        categoriesDBHelper = new CategoriesDBHelper(getActivity().getApplicationContext());
        ArrayList<String> spinnerEntries = new ArrayList<>();
        //Get categories
        Cursor nameCursor = categoriesDBHelper.getAllCategories();
        content = Locale.getDefault().getLanguage();
        //Check language
        nameCursor.move(1);
        if (nameCursor.getColumnIndex(content) == -1) {
            while(nameCursor.moveToNext()) {
                spinnerEntries.add(nameCursor.getString(nameCursor.getColumnIndex(CategoriesContract.CategoriesEntry.CAT_NAME)));
            }
            content = CategoriesContract.CategoriesEntry.CAT_NAME;
        } else {
            while(nameCursor.moveToNext()) {
                spinnerEntries.add(nameCursor.getString(nameCursor.getColumnIndex(content)));
            }
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.main_spinner_item_layout, spinnerEntries);
        taskCategory.setAdapter(arrayAdapter);


        //Check if are any arguments for create new task or update
        if(getArguments() != null) {
            //Get arguments
            task = (Task) getArguments().get(AppString.TASK_TO_EDIT);
            taskTitle.setText(task.getTitle());
            taskDescription.setText(task.getDescription());
            taskCategory.setSelection(getCategoryPos(taskCategory, task.getCategory()));
            getTaskImage();
            isUpdate = true;
        } else {
            //Create new task
            task = new Task();
            isUpdate = false;
        }

        //Set focus
        taskTitle.setFocusable(true);
        taskTitle.requestFocus();

        //Show keyboard
        KeyboardUtils.showKeyboard(getActivity());

        //Return button
        btnReturn.setOnClickListener(v -> {
            findNavController(v).navigateUp();
        });

        //Save button (Method reference)
        btnSaveTask.setOnClickListener(this::saveTask);

        //Select image Button
        btnSelectImage.setOnClickListener(this::selectImage);

        return createTaskView;
    }

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
                updateImage(); //Update the task image
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
                uploadNewImage(); //Upload an image
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
    private void uploadNewImage() {
        if(localImageUri != Uri.EMPTY && !imageName.equals("")) {
            StorageReference storageRef = fbStorage.getReference().child(AppString.IMAGES_FOLDER).child(task.getId()).child(imageName);
            task.setMedia(imageName);
            storageRef.putFile(localImageUri).addOnSuccessListener(taskSnapshot -> {
                Toast.makeText(getContext(),R.string.toast_image_upload_success, Toast.LENGTH_LONG).show();
            }).addOnFailureListener(e -> {
                Toast.makeText(getContext(),R.string.toast_image_upload_failure, Toast.LENGTH_LONG).show();
            });
        } else {
            task.setMedia("");
        }
    }

    /**
     * Update the current image of a task
     * */
    private void updateImage() {
        if(localImageUri != Uri.EMPTY && !imageName.equals("")) {
            if(!task.getMedia().equals("")) {
                StorageReference oldStorageRef = fbStorage.getReference().child(AppString.IMAGES_FOLDER).child(task.getId()).child(task.getMedia());
                oldStorageRef.delete();
            }
            StorageReference newStorageRef = fbStorage.getReference().child(AppString.IMAGES_FOLDER).child(task.getId()).child(imageName);
            task.setMedia(imageName);
            newStorageRef.putFile(localImageUri);
        }
    }

    /**
     * Open gallery and select an image
     * */
    private void selectImage(View v) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, AppCode.LOAD_IMAGE);
    }

    /**
     * Get task image from Firebase Storage
     * */
    private void getTaskImage() {
        if(!task.getMedia().equals("")) {
            StorageReference storageRef = fbStorage.getReference().child(AppString.IMAGES_FOLDER).child(task.getId()).child(task.getMedia());
            storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                Picasso.get().load(uri).into(imageSelected);
            });
        }
    }


    /**
     * Load the image in the ImageView
     * */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == AppCode.LOAD_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            //Get image Uri
            localImageUri = data.getData();

            //Get file name
            Cursor cursor = getActivity().getContentResolver().query(localImageUri,
                    null, null, null, null,
                    null);
            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            cursor.moveToFirst();
            imageName = cursor.getString(nameIndex);

            //Get file size
            File tempFile = new File(localImageUri.getPath());
            long fileSizeInMb = (tempFile.length() / 1024) / 1024;

            //Check image size, width and height
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(localImageUri));
                if(bitmap.getHeight() > 4096 || bitmap.getWidth() > 4096) { //Check width and height
                    Toast.makeText(getContext(), R.string.toast_image_too_big_error, Toast.LENGTH_LONG).show();
                    localImageUri = Uri.EMPTY;
                } else if(fileSizeInMb > 1) { //Check size
                    Toast.makeText(getContext(), R.string.toast_image_size_too_big_error, Toast.LENGTH_LONG).show();
                    localImageUri = Uri.EMPTY;
                } else {
                    imageSelected.setImageBitmap(bitmap); //Put image on image view
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
