package com.emc.emergency.ChatBox;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.emc.emergency.R;
import com.emc.emergency.Helper.Utils.SystemUtils;

public class VideoPlayActivity extends AppCompatActivity {
    private MediaController mediacontroller;
    private VideoView videoView;
    private String videoUrl;
    private Uri uri;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_fragment_play_video);
        Intent intent = getIntent();
        videoUrl = intent.getStringExtra(SystemUtils.VideoUrl);
        progressBar = (ProgressBar) findViewById(R.id.progressBarVideo);
    
        MaterialDialog dialog = new MaterialDialog.Builder(this)
            .title(R.string.progress_dialog)
            .content(R.string.please_wait)
            .progress(true, 0)
            .show();
        
        

        videoView = (VideoView) findViewById(R.id.videoView);
        mediacontroller = new MediaController(this);
        mediacontroller.setAnchorView(videoView);

        uri = Uri.parse(videoUrl);
        videoView.setMediaController(mediacontroller);
        
        
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        try {
            videoView.start();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Some thing went wrong ,please try later", Toast.LENGTH_SHORT).show();
            this.finish();
        }
        progressBar.setVisibility(View.GONE);
        dialog.dismiss();
       
    }
}
