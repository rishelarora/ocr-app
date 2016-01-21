package com.example.rishel.ocr;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rishel on 1/13/16.
 */
public class OcrActivity extends Activity {
    String [] tagArray;
    LinearLayout showtags;
     private List<TextView> TextViewList = new ArrayList<>();

    public static final String PACKAGE_NAME = "com.datumdroid.android.ocr.simple";
    public static final String DATA_PATH = Environment
            .getExternalStorageDirectory().toString() + "/SimpleAndroidOCR/";

    // You should have the trained data file in assets folder
    // You can get them at:
    // http://code.google.com/p/tesseract-ocr/downloads/list
    public static final String lang = "eng";

    private static final String TAG = "SimpleAndroidOCR.java";

    protected Button _button,b1;

    // protected ImageView _image;
    protected EditText _field;

    protected String _path;
    protected boolean _taken;
    String text;
    protected static final String PHOTO_TAKEN = "photo_taken";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        String[] paths = new String[] { DATA_PATH, DATA_PATH + "tessdata/" };

        for (String path : paths) {
            File dir = new File(path);
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    Log.v(TAG, "ERROR: Creation of directory " + path + " on sdcard failed");
                    return;
                } else {
                    Log.v(TAG, "Created directory " + path + " on sdcard");
                }

            }

        }

        // lang.traineddata file with the app (in assets folder)
        // You can get them at:
        // http://code.google.com/p/tesseract-ocr/downloads/list
        // This area needs work and optimization
        if (!(new File(DATA_PATH + "tessdata/" + lang + ".traineddata")).exists()) {
            try {

                AssetManager assetManager = getAssets();
                InputStream in = assetManager.open("tessdata/" + lang + ".traineddata");
                //GZIPInputStream gin = new GZIPInputStream(in);
                OutputStream out = new FileOutputStream(DATA_PATH
                        + "tessdata/" + lang + ".traineddata");

                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                //while ((lenf = gin.read(buff)) > 0) {
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                //gin.close();
                out.close();

                Log.v(TAG, "Copied " + lang + " traineddata");
            } catch (IOException e) {
                Log.e(TAG, "Was unable to copy " + lang + " traineddata " + e.toString());
            }
        }

        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        // _image = (ImageView) findViewById(R.id.image);
        _field = (EditText) findViewById(R.id.field);
        tagArray = getIntent().getExtras().getStringArray("tags");
        _button = (Button) findViewById(R.id.button);
        b1 = (Button) findViewById(R.id.button2);
        showtags=(LinearLayout)findViewById(R.id.display);


        for (int j = 0; j < tagArray.length; j++) {
            showtags.addView(TextView(j));
            TextViewList.get(j).setText(tagArray[j]);
        }


        _button.setOnClickListener(new ButtonClickHandler());





        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle tagbundle = new Bundle();
                tagbundle.putStringArray("tags",tagArray);
                Intent myintent = new Intent(OcrActivity.this, extract.class).putExtra("TEXT", _field.getText().toString());
                myintent.putExtras(tagbundle);
                startActivity(myintent);
            }
        });

        _path = DATA_PATH + "/ocr.jpg";

    }


    public class ButtonClickHandler implements View.OnClickListener {
        public void onClick(View view) {
            Log.v(TAG, "Starting Camera app");
            startCameraActivity();

        }
    }


    // Simple android photo capture:
    // http://labs.makemachine.net/2010/03/simple-android-photo-capture/

    protected void startCameraActivity() {
        File file = new File(_path);
        Uri outputFileUri = Uri.fromFile(file);

        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.i(TAG, "resultCode: " + resultCode);

        if (resultCode == -1) {
            new OCRTask().execute();
        } else {
            Log.v(TAG, "User cancelled");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(OcrActivity.PHOTO_TAKEN, _taken);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.i(TAG, "onRestoreInstanceState()");
        if (savedInstanceState.getBoolean(OcrActivity.PHOTO_TAKEN)) {
            new OCRTask().execute();
        }
    }


//    protected void onPhotoTaken() {
//        _taken = true;
//
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inSampleSize = 4;
//
//        Bitmap bitmap = BitmapFactory.decodeFile(_path, options);
//
//        try {
//            ExifInterface exif = new ExifInterface(_path);
//            int exifOrientation = exif.getAttributeInt(
//                    ExifInterface.TAG_ORIENTATION,
//                    ExifInterface.ORIENTATION_NORMAL);
//
//            Log.v(TAG, "Orient: " + exifOrientation);
//
//            int rotate = 0;
//
//            switch (exifOrientation) {
//                case ExifInterface.ORIENTATION_ROTATE_90:
//                    rotate = 90;
//                    break;
//                case ExifInterface.ORIENTATION_ROTATE_180:
//                    rotate = 180;
//                    break;
//                case ExifInterface.ORIENTATION_ROTATE_270:
//                    rotate = 270;
//                    break;
//            }
//
//            Log.v(TAG, "Rotation: " + rotate);
//
//            if (rotate != 0) {
//
//                // Getting width & height of the given image.
//                int w = bitmap.getWidth();
//                int h = bitmap.getHeight();
//
//                // Setting pre rotate
//                Matrix mtx = new Matrix();
//                mtx.preRotate(rotate);
//
//                // Rotating Bitmap
//                bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);
//            }
//
//            // Convert to ARGB_8888, required by tess
//            bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
//
//        } catch (IOException e) {
//            Log.e(TAG, "Couldn't correct orientation: " + e.toString());
//        }
//
//        // _image.setImageBitmap( bitmap );
//
//        Log.v(TAG, "Before baseApi");
//
//        TessBaseAPI baseApi = new TessBaseAPI();
//        baseApi.setDebug(true);
//        baseApi.init(DATA_PATH, lang);
//        baseApi.setImage(bitmap);
//
//        String recognizedText = baseApi.getUTF8Text();
//
//        baseApi.end();
//
//        // You now have the text in recognizedText var, you can do anything with it.
//        // We will display a stripped out trimmed alpha-numeric version of it (if lang is eng)
//        // so that garbage doesn't make it to the display.
//
//        Log.v(TAG, "OCRED TEXT: " + recognizedText);
//
//        if ( lang.equalsIgnoreCase("eng") ) {
//            recognizedText = recognizedText.replaceAll("[^a-zA-Z0-9]+", " ");
//        }
//
//        recognizedText = recognizedText.trim();
//
//        if ( recognizedText.length() != 0 ) {
//            _field.setText(_field.getText().toString().length() == 0 ? recognizedText : _field.getText() + " " + recognizedText);
//            _field.setSelection(_field.getText().toString().length());
//
//        }
//
//
//
//        // Cycle done.
//    }


    private TextView TextView(int _intID) {
        TextView tag = new TextView(this);
        tag.setId(_intID);
        tag.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.weight = 1.0f;
        params.gravity = Gravity.CENTER;
        tag.setLayoutParams(params);
        TextViewList.add(tag);
        return tag;
    }

    private class OCRTask extends AsyncTask<Void, Integer, String> {


        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(OcrActivity.this);
            pd.setTitle("Processing Image");
            pd.setMessage("Extracting data out of the captured image.");
            pd.setMax(100);
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pd.setCancelable(false);
            pd.show();
        }
        @Override
        protected String doInBackground(Void... params) {
            _taken = true;
            publishProgress(10);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;

            Bitmap bitmap = BitmapFactory.decodeFile(_path, options);


            try {
                ExifInterface exif = new ExifInterface(_path);
                int exifOrientation = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL);

                Log.v(TAG, "Orient: " + exifOrientation);

                int rotate = 0;

                switch (exifOrientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotate = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        rotate = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        rotate = 270;
                        break;
                }


                Log.v(TAG, "Rotation: " + rotate);

                if (rotate != 0) {

                    // Getting width & height of the given image.
                    int w = bitmap.getWidth();
                    int h = bitmap.getHeight();

                    // Setting pre rotate
                    Matrix mtx = new Matrix();
                    mtx.preRotate(rotate);

                    // Rotating Bitmap
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);
                }

                // Convert to ARGB_8888, required by tess
                bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

            } catch (IOException e) {
                Log.e(TAG, "Couldn't correct orientation: " + e.toString());
            }

            // _image.setImageBitmap( bitmap );

            Log.v(TAG, "Before baseApi");
            publishProgress(30);
            TessBaseAPI baseApi = new TessBaseAPI();
            publishProgress(40);
            baseApi.setDebug(true);
            publishProgress(50);
            baseApi.init(DATA_PATH, lang);
            publishProgress(70);
            baseApi.setImage(bitmap);
            publishProgress(80);
            String recognizedText = baseApi.getUTF8Text();
            publishProgress(100);
            baseApi.end();

            // You now have the text in recognizedText var, you can do anything with it.
            // We will display a stripped out trimmed alpha-numeric version of it (if lang is eng)
            // so that garbage doesn't make it to the display.

            Log.v(TAG, "OCRED TEXT: " + recognizedText);

            if ( lang.equalsIgnoreCase("eng") ) {
                recognizedText = recognizedText.replaceAll("[^a-zA-Z0-9]+", " ");
            }

            recognizedText = recognizedText.trim();

            return recognizedText;
        }

        protected void onPostExecute(String result) {
            if (pd != null)
            {
                pd.dismiss();
            }
            if ( result.length() != 0 ) {
                _field.setText(_field.getText().toString().length() == 0 ? result : _field.getText() + " " + result);
                _field.setSelection(_field.getText().toString().length());

            }
        }
        @Override
        protected void onProgressUpdate(Integer... progress) {


            super.onProgressUpdate(progress);
            // Update the ProgressBar
            pd.setProgress(progress[0]);



        }
    }
}
