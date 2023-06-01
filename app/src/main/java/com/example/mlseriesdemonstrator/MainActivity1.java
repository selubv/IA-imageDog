/*
 * Created by ishaanjav
 * github.com/ishaanjav
*/

package com.example.mlseriesdemonstrator;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/*import com.example.mlseriesdemonstrator.ml.Model;*/
import com.example.mlseriesdemonstrator.ml.Modelorazas;

public class MainActivity1 extends AppCompatActivity {

    TextView result, confidence, coincidencia;
    ImageView imageView;

    int imageSize = 224;
    Button picture,visibilidad;
    String textoif ="";
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        result = findViewById(R.id.result);
        confidence = findViewById(R.id.confidence);
        imageView = findViewById(R.id.imageView);
        picture = findViewById(R.id.button);
        imageView.setVisibility(View.VISIBLE);
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch camera if we have permission
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 1);
                } else {
                    //Request camera permission if we don't have it.
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                }
            }
        });
    }
    @SuppressLint("ResourceType")
    public void classifyImage(Bitmap image) {
        imageView.setVisibility(View.VISIBLE);
        try {
            Modelorazas model = Modelorazas.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            // get 1D array of 224 * 224 pixels in image
            int[] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());

            // iterate over pixels and extract R, G, and B values. Add to bytebuffer.
            int pixel = 0;
            for (int i = 0; i < imageSize; i++) {
                for (int j = 0; j < imageSize; j++) {
                    int val = intValues[pixel++]; // RGB
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255.f));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            Modelorazas.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();
            // find the index of the class with the biggest confidence.
            int maxPos = 0;
            float maxConfidence = 0;
            for (int i = 0; i < confidences.length; i++) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }
            String[] classes = {"bulldog inglés", "golden retriever", "pastor alemán", "doberman", "labrador retriever", "pitbull", "pug", "rottweiler", "no es un perro"};
            result.setText(classes[maxPos]);

            String found[] = new String[200];
            for (int i = 0; i < classes.length; i++) {
                if (confidences[i] * 100 > 50) {
                    found[i] = String.format("%s: %.1f%%\n", classes[i], confidences[i] * 100);
                } else {
                    found[i] = "";
                }
            }
            confidence.setVisibility(View.VISIBLE);

            SpannableStringBuilder builder = new SpannableStringBuilder();
            for (int i = 0; i < classes.length; i++) {
                if (found[i] != "") {
                    SpannableString str = new SpannableString(found[i]);
                    if (confidences[i] * 100 > 50 && confidences[i] * 100 <= 75) {
                        str.setSpan(new ForegroundColorSpan(Color.parseColor("#FFFF00")), 0, str.length(), 0);
                    } else {
                        str.setSpan(new ForegroundColorSpan(Color.parseColor("#008000")), 0, str.length(), 0);
                    }
                    builder.append(str);
                } else {

                }
            }
            confidence.setText(builder, TextView.BufferType.SPANNABLE);


            // Releases model resources if no longer used.
            model.close();

            textoif = (String) result.getText();

            if (textoif == "no es un perro") {
                Intent i = new Intent(this, PopupRazas.class);
                startActivity(i);
                result.setText("");
                confidence.setText("");
                imageView.setVisibility(View.INVISIBLE);
            } else {


            }


        } catch (IOException e) {
            // TODO Handle the exception

    }}
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            int dimension = Math.min(image.getWidth(), image.getHeight());
            image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
            imageView.setImageBitmap(image);

            image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
            classifyImage(image);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



}
