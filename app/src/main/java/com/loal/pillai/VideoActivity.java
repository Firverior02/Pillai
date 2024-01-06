package com.loal.pillai;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoActivity extends AppCompatActivity {

    VideoView videoView;

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
    }
}