package in.curis.visual.testvisual;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyImagesOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassification;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ClassifyImage extends AppCompatActivity {

    ClassifyImagesOptions options;
    VisualRecognition service;

    byte[] img1=null;

    String filename=null;

    Button camera;
    Uri sample;

    ArrayList<ItemData> itemDataArrayList;
    ListView listView;
    private static CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        camera = (Button)findViewById(R.id.camera_btn);

        listView=(ListView)findViewById(R.id.list_view);
        itemDataArrayList = new ArrayList<>();



        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageCamera();
            }
        });


    }

    public void AnalyseImage(String path){
        service = new VisualRecognition(VisualRecognition.VERSION_DATE_2016_05_20);
        service.setApiKey("Your API Key here");

        System.out.println(""+path);
        options = new ClassifyImagesOptions.Builder()
                .images(new File(path))
                .build();

        new RetrieveFeedTask().execute();
    }


    public void ImageCamera() {
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("-mm-ss");

        File folder_new = new File(Environment.getExternalStorageDirectory()+"/RecognizeMe");
        if (!folder_new.exists()) {
            folder_new.mkdir();
        }

        String newPicFile = "Bild"+ df.format(date) + ".jpg";
        filename = Environment.getExternalStorageDirectory() +"/New/"+ newPicFile;
        Uri fileUri = FileProvider.getUriForFile(ClassifyImage.this,
                BuildConfig.APPLICATION_ID + ".provider",new File(filename));
        sample = Uri.fromFile(new File(filename));
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        i.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(i, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


        //taking an image from camera
        if (requestCode == 100 && resultCode == RESULT_OK ){
            Bitmap photo = null;//= (Bitmap) data.getExtras().get("data");
            InputStream image_stream;
            try {
                image_stream = this.getContentResolver().openInputStream(sample);
                photo = BitmapFactory.decodeStream(image_stream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            ImageView imageView = (ImageView) findViewById(R.id.phototaken);
            imageView.setImageBitmap(photo);
            storeImage();
        }

    }



    private void storeImage(){
        File dir = new File(filename);
        Bitmap b= BitmapFactory.decodeFile(filename);

        Bitmap out = Bitmap.createScaledBitmap(b, 500, 800, false);
        FileOutputStream fOut;
        try {
            fOut = new FileOutputStream(dir);
            out.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            b.recycle();
            out.recycle();
        } catch (Exception e) {}
        AnalyseImage(filename);
    }


    public class RetrieveFeedTask extends AsyncTask<Void, Void, VisualClassification> {

        protected VisualClassification doInBackground(Void... voids) {
            try {
                VisualClassification result = service.classify(options).execute();
                System.out.println(result);
                return result;
            } catch (Exception e) {
                System.out.print(""+e);
                return null;
            }
        }

        protected void onPostExecute(VisualClassification result) {
            // TODO: check this.exception
            // TODO: do something with the feed
            //TextView new_text = (TextView)findViewById(R.id.new_text);
            //new_text.setText(""+result);
            String jsonstr=""+result;
            try {
                JSONObject jObject = new JSONObject(jsonstr);
                JSONArray images = jObject.getJSONArray("images");
                JSONArray classifiers = images.getJSONObject(0).getJSONArray("classifiers");
                JSONArray classes = classifiers.getJSONObject(0).getJSONArray("classes");

                for(int i=0;i<classes.length();i++){
                    JSONObject classes_arr = classes.getJSONObject(i);
                    String name = classes_arr.getString("class");
                    Float score = Float.parseFloat(classes_arr.getString("score"))*100;

                    itemDataArrayList.add(new ItemData(name,score));
                }


                adapter = new CustomAdapter(itemDataArrayList,getApplicationContext());
                listView.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
