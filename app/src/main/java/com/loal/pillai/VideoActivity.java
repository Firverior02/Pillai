package com.loal.pillai;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Picture;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

public class VideoActivity extends AppCompatActivity {

    VideoView videoView;
    ImageView head;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        // Initialize video view
        videoView = findViewById(R.id.videoView);

        // Get path to example video and source it from the view
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.making_sense;
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);

        // Add a basic media controller to play the video
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);

        // Start video
        videoView.start();

        // Head SVG
        head = findViewById(R.id.head);

        // Try to get head and render it
        try {
            SVG svg = SVG.getFromResource(this, R.raw.head);
            Picture picture = svg.renderToPicture(300, 300);
            Drawable drawable = new PictureDrawable(picture);

            // Scale the drawable
            int width = picture.getWidth();
            int height = picture.getHeight();
            float scaleFactor = 8.0f; // scale factor. Use 0.5f to half the size, 2.0f to double, etc.

            drawable.setBounds(0, 0, (int)(width * scaleFactor), (int)(height * scaleFactor));

            head.setImageDrawable(drawable);
        } catch (SVGParseException e) {
            throw new RuntimeException(e);
        }
    }
}