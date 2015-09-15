package com.appsolutions.imageloader;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;



public class MainActivity extends ActionBarActivity {
    private Button download_button;
    private EditText edit_text;
    private ImageView imageView;
    private ProgressBar progressBar;

    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Set elements objects from GUI
        download_button = (Button) findViewById(R.id.download_btn);
        edit_text = (EditText) findViewById(R.id.url);
        imageView = (ImageView)findViewById(R.id.imageView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        View.OnClickListener onDownloadListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // download image from URL
                String url = edit_text.getText().toString();
                if (!url.isEmpty()) {

                    // clear imageView
                    imageView.setImageBitmap(null);

                    final DownloadImageTask downloadImageTask = new DownloadImageTask(imageView);
                    downloadImageTask.execute(url);

                    // create handler object to hide progressBar
                    handler = new Handler() {
                        public void handleMessage(Message msg) {
                            switch(msg.what) {
                                case 1: {
                                    progressBar.setVisibility(ProgressBar.VISIBLE);
                                    download_button.setEnabled(false);
                                    break;
                                }
                                case 0: {
                                    Log.w("MyTag", "INVISIBLE");
                                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                                    download_button.setEnabled(true);
                                    break;
                                }
                            }
                        }
                    };

                    //hide button and show progressbar
                    handler.sendEmptyMessage(1);

                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while(true) {
                                if(downloadImageTask.isLoaded) break;
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            handler.sendEmptyMessage(0);
                        }
                    });
                    thread.start();
                }
            }
        };

        download_button.setOnClickListener(onDownloadListener);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
