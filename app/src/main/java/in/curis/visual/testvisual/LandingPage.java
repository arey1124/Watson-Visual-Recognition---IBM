package in.curis.visual.testvisual;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import at.grabner.circleprogress.CircleProgressView;

public class LandingPage extends AppCompatActivity {

    Button classify_image,detect_face;
    CircleProgressView mCircleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        classify_image = (Button)findViewById(R.id.classify_image_btn);
        detect_face = (Button)findViewById(R.id.face_detect_btn);

        classify_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LandingPage.this,ClassifyImage.class);
                startActivity(i);
            }
        });

        detect_face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LandingPage.this,DetectFace.class);
                startActivity(i);
            }
        });
        mCircleView = (CircleProgressView)findViewById(R.id.circleview);
        mCircleView.setShowTextWhileSpinning(true); // Show/hide text in spinning mode
        mCircleView.setText("Loading...");
        mCircleView.setValueAnimated(90);
        mCircleView.setSeekModeEnabled(false);


    }
}
