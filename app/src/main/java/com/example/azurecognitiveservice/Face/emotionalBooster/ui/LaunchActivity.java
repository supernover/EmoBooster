package com.example.azurecognitiveservice.Face.emotionalBooster.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.azurecognitiveservice.Face.emotionalBooster.R;


public class LaunchActivity extends AppCompatActivity {
    String _playlistID;
    String _playlistMessage;
    String _playlistName;
    String _emotion;


    private static final String CLIENT_ID = "142bf833a7d240488073cde2beeab7a1";
    private static final String REDIRECT_URI = "http://com.yourdomain.yourapp/callback";
    //private SpotifyAppRemote mSpotifyAppRemote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        _playlistID = getIntent().getStringExtra(MainActivity.PLAY_LIST_ID_ID);
        _playlistMessage = getIntent().getStringExtra(MainActivity.PLAY_LIST_MESSAGE_ID);
        _playlistName = getIntent().getStringExtra(MainActivity.PLAY_LIST_NAME_ID);
        _emotion = getIntent().getStringExtra(MainActivity.PLAY_LIST_EMOTION_ID);

        TextView MessagetextView = (TextView) findViewById(R.id.textViewMessage);
        TextView EmotiontextView = (TextView) findViewById(R.id.emotion);
        TextView MusictextView = (TextView) findViewById(R.id.albumMessage);

        MessagetextView.setText("We let you know  that how you feel !\n");
        EmotiontextView.setText(_emotion);
        MusictextView.setText("No matter what mood you’re in, there’s music to match.Sometimes it might be hard to identify the right playlist for precisely how you’re feeling—so let us do the work for you !\n\n We recommend you to check this playlist called :-->" +  _playlistName +"\n\n it might help you "+ ", \n" + _playlistMessage);

    }
    public void launch(View v){
        String url = "https://open.spotify.com/user/spotify/playlist/" + _playlistID;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }


    /*@Override
    protected void onStart() {
        super.onStart();
        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build();

        SpotifyAppRemote.connect(this, connectionParams,
                new Connector.ConnectionListener() {

                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote;
                        Log.d("MainActivity", "Connected! Yay!");

                        // Now you can start interacting with App Remote
                        connected();

                    }

                    public void onFailure(Throwable throwable) {
                        Log.e("MyActivity", throwable.getMessage(), throwable);

                        // Something went wrong when attempting to connect! Handle errors here
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }

    private void connected() {
        // Play a playlist
        mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:37i9dQZF1DX2sUQwD7tbmL");

        // Subscribe to PlayerState
        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    final Track track = playerState.track;
                    if (track != null) {
                        Log.d("MainActivity", track.name + " by " + track.artist.name);
                    }
                });
    }*/
}
