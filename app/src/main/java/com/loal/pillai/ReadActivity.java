package com.loal.pillai;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Picture;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

public class ReadActivity extends AppCompatActivity {

    LinearLayout backBtn;
    ImageView head;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

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

        // Back btn
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(view -> this.finish());
    }
}