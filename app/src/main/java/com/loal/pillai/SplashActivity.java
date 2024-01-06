package com.loal.pillai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

public class SplashActivity extends AppCompatActivity {

    private ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        logo = findViewById(R.id.logo);

        // Try to get logo and render it
        try {
            SVG svg = SVG.getFromResource(this, R.raw.pillai);
            Drawable drawable = new PictureDrawable(svg.renderToPicture());
            logo.setImageDrawable(drawable);
        } catch (SVGParseException e) {
            throw new RuntimeException(e);
        }

        // Set a 3 second delay in which the splash screen is shown
        new Handler().postDelayed(() -> {
            // Start the main activity after
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, 3000);
    }
}