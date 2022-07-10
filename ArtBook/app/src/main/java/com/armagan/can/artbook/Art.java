package com.armagan.can.artbook;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.armagan.can.artbook.databinding.ActivityArtBinding;
import com.armagan.can.artbook.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;

public class Art extends AppCompatActivity {

    private ActivityArtBinding binding;
    ActivityResultLauncher<Intent> activityResultLauncher;
    ActivityResultLauncher<String> permissionLauncher;
    Bitmap selectedImage;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityArtBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        registerLauncher();
    }

    public void save(View view){

        String name= binding.nametext.getText().toString();
        String artistName = binding.artisttext.getText().toString();

        Bitmap smallImage = makeSmallerImage(selectedImage, 300);

        ByteArrayOutputStream outputStream =new ByteArrayOutputStream();
        smallImage.compress(Bitmap.CompressFormat.PNG,50,outputStream);
        byte[] byteArray = outputStream.toByteArray();

        try {

            database = this.openOrCreateDatabase("Arts",MODE_PRIVATE,null);
            database.execSQL("CREATE TABLE IF NOT EXISTS arts (id INTEGER PRIMARY KEY, artname VARCHAR, paintername VARCHAR, year VARCHAR, image BLOB)");

            String sqlString= "INSERT INTO arts (artname, paintername,year,image) VALUES(?, ?, ?,)";
            SQLiteStatement sqLiteStatement = database.compileStatement(sqlString);
            sqLiteStatement.bindString(1,name);
            sqLiteStatement.bindString(2,artistName);
            sqLiteStatement.bindBlob(3,byteArray);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        Intent intent = new Intent(Art.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);



    }

    public Bitmap makeSmallerImage(Bitmap image, int maximumSize){
        int width = image.getWidth();
        int heigth =image.getHeight();

        float bitmapRatio = (float) width / (float) heigth;

        if (bitmapRatio >1){
            //landspace image
            width = maximumSize;
            heigth = (int) (width / bitmapRatio);
        }
        else {
            //portrait image
            heigth =maximumSize;
            width = (int)  (heigth * bitmapRatio);
        }
        return image.createScaledBitmap(image,width,heigth,true);

    }

    public void selectimage(View view){

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Snackbar.make(view, "Permission needed for gallery", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //request permission
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);

                    }
                }).show();

            } else {
                //request permission = izin istemek
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }

        }else {
           //gallery
            Intent intenttogallery =new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncher.launch(intenttogallery);
    }

 }

   private void registerLauncher(){

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() ==RESULT_OK) {
                    Intent intentFromResult =result.getData();
                    if (intentFromResult !=null);
                    Uri imageData = intentFromResult.getData();
                    binding.imageView.setImageURI(imageData);

                    try {
                        if (Build.VERSION.SDK_INT >=28) {
                            ImageDecoder.Source source = ImageDecoder.createSource(Art.this.getContentResolver(),imageData);
                            selectedImage =ImageDecoder.decodeBitmap(source);
                            binding.imageView.setImageBitmap(selectedImage);
                        } else {
                            selectedImage =MediaStore.Images.Media.getBitmap(Art.this.getContentResolver(),imageData);
                            binding.imageView.setImageBitmap(selectedImage);
                        }





                    } catch (Exception e) {
                        e.printStackTrace();


                    }


                }
            }
        });

        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if(result){
                    //permission granted
                    Intent intenttogallery =new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    activityResultLauncher.launch(intenttogallery);

                } else {
                    //permission denied
                    Toast.makeText( Art.this,"Permission needed!",Toast.LENGTH_LONG).show();
                }

            }
        });
   }

}