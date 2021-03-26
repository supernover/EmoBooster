//
// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license.
//
// Microsoft Cognitive Services (formerly Project Oxford): https://www.microsoft.com/cognitive-services
//
// Microsoft Cognitive Services (formerly Project Oxford) GitHub:
// https://github.com/Microsoft/Cognitive-Face-Android
//
// Copyright (c) Microsoft Corporation
// All rights reserved.
//
// MIT License:
// Permission is hereby granted, free of charge, to any person obtaining
// a copy of this software and associated documentation files (the
// "Software"), to deal in the Software without restriction, including
// without limitation the rights to use, copy, modify, merge, publish,
// distribute, sublicense, and/or sell copies of the Software, and to
// permit persons to whom the Software is furnished to do so, subject to
// the following conditions:
//
// The above copyright notice and this permission notice shall be
// included in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED ""AS IS"", WITHOUT WARRANTY OF ANY KIND,
// EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
// MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
// NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
// LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
// OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
// WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
//
package com.example.azurecognitiveservice.Face.emotionalBooster.ui;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.azurecognitiveservice.Face.emotionalBooster.R;
import com.example.azurecognitiveservice.Face.emotionalBooster.helper.ImageHelper;
import com.example.azurecognitiveservice.Face.emotionalBooster.helper.LogHelper;
import com.example.azurecognitiveservice.Face.emotionalBooster.helper.SampleApp;
import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.contract.Emotion;
import com.microsoft.projectoxford.face.contract.Face;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;



// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.


/**
 * Include these library jars (or newer +), add to a lib folder:
 *   commons-logging-4.0.6+
 *   httpclient-4.5.3+
 *   httpcore-4.4.13+
 *   json-20190722+
 *
 * To compile and run from command line:
 *    javac Main.java -cp .;lib\*
 *   java -cp .;lib\* Main
 */

public class MainActivity extends AppCompatActivity {

    // Background task of face detection.
    private class DetectionTask extends AsyncTask<InputStream, String, Face[]> {
        private boolean mSucceed = true;


    @Override
    protected Face[] doInBackground(InputStream... params) {
        //HttpClient httpclient = new DefaultHttpClient();

        // Get an instance of face service client to detect faces in image.
        FaceServiceClient faceServiceClient = SampleApp.getFaceServiceClient();
        try {
            publishProgress("Detecting...");
            /*URIBuilder builder = new URIBuilder(endpoint);

            // Request parameters. All of them are optional.
            builder.setParameter("returnFaceId", "true");
            builder.setParameter("returnFaceLandmarks", "false");
            builder.setParameter("returnFaceAttributes", "age,gender,headPose,smile,facialHair,glasses,emotion,hair,makeup,occlusion,accessories,blur,exposure,noise");

            // Prepare the URI for the REST API call.
            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);

            // Request headers.
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

            // Request body.
            StringEntity reqEntity = new StringEntity("{\"url\":\"" + image + "\"}");
            request.setEntity(reqEntity);

            // Execute the REST API call and get the response entity.
            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null)
            {
                // Format and display the JSON response.
                System.out.println("REST Response:\n");

                String jsonString = EntityUtils.toString(entity).trim();
                if (jsonString.charAt(0) == '[') {
                    JSONArray jsonArray = new JSONArray(jsonString);
                    System.out.println(jsonArray.toString(2));
                }
                else if (jsonString.charAt(0) == '{') {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    System.out.println(jsonObject.toString(2));
                } else {
                    System.out.println(jsonString);
                }
            }*/


            // Start detection.
            return faceServiceClient.detect(
                    params[0],  /* Input stream of image to detect */
                    true,       /* Whether to return face ID */
                    true,       /* Whether to return face landmarks */
                        /* Which face attributes to analyze, currently we support:
                           age,gender,headPose,smile,facialHair */



                    new FaceServiceClient.FaceAttributeType[] {
                            FaceServiceClient.FaceAttributeType.Age,
                            FaceServiceClient.FaceAttributeType.Gender,
                            FaceServiceClient.FaceAttributeType.Smile,
                            FaceServiceClient.FaceAttributeType.Glasses,
                            FaceServiceClient.FaceAttributeType.FacialHair,
                            FaceServiceClient.FaceAttributeType.Emotion,
                            FaceServiceClient.FaceAttributeType.HeadPose,
                            FaceServiceClient.FaceAttributeType.Accessories,
                            FaceServiceClient.FaceAttributeType.Blur,
                            FaceServiceClient.FaceAttributeType.Exposure,
                            FaceServiceClient.FaceAttributeType.Hair,
                            FaceServiceClient.FaceAttributeType.Makeup,
                            FaceServiceClient.FaceAttributeType.Noise,
                            FaceServiceClient.FaceAttributeType.Occlusion
                    });
        } catch (Exception e) {
            mSucceed = false;
            publishProgress(e.getMessage());
            addLog(e.getMessage());
            return null;
        }
    }

    @Override
    protected void onPreExecute() {
        mProgressDialog.show();
        populateArrayLists();
        addLog("Request: Detecting in image " + mImageUri);
    }

    @Override
    protected void onProgressUpdate(String... progress) {
        mProgressDialog.setMessage(progress[0]);
        setInfo(progress[0]);

    }

    @Override
    protected void onPostExecute(Face[] result) {
        if (mSucceed) {
            addLog("Response: Success. Detected " + (result == null ? 0 : result.length)
                    + " face(s) in " + mImageUri);
        }


        // Show the result on screen when detection is done.
        setUiAfterDetection(result, mSucceed);
    }


}



    public static final String PLAY_LIST_NAME_ID = "PLAY_LIST_NAME_ID";
    public static final String PLAY_LIST_ID_ID = "PLAY_LIST_ID_ID";
    public static final String PLAY_LIST_MESSAGE_ID = "PLAY_LIST_MESSAGE_ID";
    public static final String PLAY_LIST_EMOTION_ID = "PLAY_LIST_EMOTION_ID";


    private void makeSongRequest(String _playlistID,String _PlaylistName, String _PlaylistMessage, String emotion){

        Intent intent = new Intent(getBaseContext(), LaunchActivity.class);
        intent.putExtra(PLAY_LIST_NAME_ID, _PlaylistName);
        intent.putExtra(PLAY_LIST_ID_ID, _playlistID);
        intent.putExtra(PLAY_LIST_MESSAGE_ID, _PlaylistMessage);
        intent.putExtra(PLAY_LIST_EMOTION_ID, emotion);
        startActivity(intent);

    }

    private Face ConcatenateFaces(Face[] faces){
        Face finalface = faces[0];
        for(int i = 1; i < faces.length; ++i){
            finalface.faceAttributes.emotion.anger += faces[i].faceAttributes.emotion.anger;
            finalface.faceAttributes.emotion.anger += faces[i].faceAttributes.emotion.contempt;
            finalface.faceAttributes.emotion.anger += faces[i].faceAttributes.emotion.disgust;
            finalface.faceAttributes.emotion.anger += faces[i].faceAttributes.emotion.fear;
            finalface.faceAttributes.emotion.anger += faces[i].faceAttributes.emotion.happiness;
            finalface.faceAttributes.emotion.anger += faces[i].faceAttributes.emotion.neutral;
            finalface.faceAttributes.emotion.anger += faces[i].faceAttributes.emotion.sadness;
            finalface.faceAttributes.emotion.anger += faces[i].faceAttributes.emotion.surprise;
        }
        return finalface;
    }
    private HashMap<String,String> anxiousFearPlaylists = new HashMap<String, String>();
    private HashMap<String,String> happyPlaylists = new HashMap<String, String>();
    private HashMap<String,String> sadPlaylists = new HashMap<String, String>();
    private HashMap<String,String> angerPlaylists = new HashMap<String, String>();
    private String m_PlaylistName;
    private String m_playlistID;
    private String m_PlaylistMessage = "This is what your emotions sound like.";


    private String pickRandomPlaylist(HashMap<String, String> map){
        List<String> keysAsArray = new ArrayList<String>(map.keySet());
        Random r = new Random();
        return keysAsArray.get(r.nextInt(map.size()));
    }



    //importing  playlist  https://open.spotify.com/

    private void populateArrayLists() {






        happyPlaylists.put("46pGanI4seRyXHzc1b7GDn","Mood Booster");
        happyPlaylists.put("37i9dQZF1DX3rxVfibe1L0","Mood Booster");
        happyPlaylists.put("37i9dQZF1DX6mvEU1S6INL","You & Me");



        happyPlaylists.put("37i9dQZF1DX7KNKjOK0o75","Have a Great Day!");
        happyPlaylists.put("37i9dQZF1DWYBO1MoTDhZI","Good Vibes");
        happyPlaylists.put("37i9dQZF1DXdPec7aLTmlC","Happy Hits");
        happyPlaylists.put("37i9dQZF1DWSkMjlBZAZ07","Happy Folk");
        happyPlaylists.put("37i9dQZF1DX9XIFQuFvzM4","Feelin' Good");
        happyPlaylists.put("37i9dQZF1DX2sUQwD7tbmL","Feel-Good Indie Rock");
        happyPlaylists.put("37i9dQZF1DX0UrRvztWcAU","Wake Up Happy");
        happyPlaylists.put("37i9dQZF1DXaK0O81Xtkis","Happy Chill Good Time Vibes");
        happyPlaylists.put("37i9dQZF1DWSf2RDTDayIx","Happy Beats");

        sadPlaylists.put("37i9dQZF1DX3YSRoSdA634","Life Sucks");
        sadPlaylists.put("37i9dQZF1DWSqBruwoIXkA","Down in the Dumps");
        sadPlaylists.put("37i9dQZF1DWVV27DiNWxkR","Melancholia");
        sadPlaylists.put("37i9dQZF1DXaLvcwjNfHBR","Breaking Hits");
        angerPlaylists.put("37i9dQZF1DXarRysLJmuju","Pop All Day");



        angerPlaylists.put("37i9dQZF1DWU6kYEHaDaGA","Unleash the Fury");
        angerPlaylists.put("37i9dQZF1DWWJOmJ7nRx0C","Rock Hard");
        angerPlaylists.put("5s7Sp5OZsw981I2OkQmyrz","Rage Quit");
        angerPlaylists.put("37i9dQZF1DWTcqUzwhNmKv","Kickass Metal");
        angerPlaylists.put("37i9dQZF1DWXIcbzpLauPS","Metalcore");
        anxiousFearPlaylists.put("37i9dQZF1DX4sWSpwq3LiO"  ,"Peaceful Piano"     );
        anxiousFearPlaylists.put("37i9dQZF1DX3Ogo9pFvBkY"  ,"Ambient Chill"     );
        anxiousFearPlaylists.put("37i9dQZF1DXcCnTAt8CfNe"  ,"Musical Therapy"     );
        anxiousFearPlaylists.put("7A2YimOfIrmAWkCeSIY8Rq"  ,"Calm Classics"     );
        anxiousFearPlaylists.put("37i9dQZF1DWU0ScTcjJBdj"  ,"Relax & Unwind"     );
        anxiousFearPlaylists.put("37i9dQZF1DX3PIPIT6lEg5"  ,"Microtherapy"     );
        anxiousFearPlaylists.put("37i9dQZF1DX1s9knjP51Oa"  ,"Calm Vibes"     );
        anxiousFearPlaylists.put("37i9dQZF1DXa9xHlDa5fc6"  ,"License to Chill"     );
        anxiousFearPlaylists.put("37i9dQZF1DWTkxQvqMy4WW"  ,"Chillin' on a Dirt Road"     );
        anxiousFearPlaylists.put("37i9dQZF1DX8ymr6UES7vc"  ,"Rain Sounds"     );
        anxiousFearPlaylists.put("37i9dQZF1DWZqd5JICZI0u"  ,"Peaceful Meditation"     );


    }

    private String getEmotion(Emotion emotion)
    {
        String emotionType = "";
        double emotionValue = 0.0;
        if (emotion.anger > emotionValue)
        {
            emotionValue = emotion.anger;
            emotionType = "Anger";
            m_PlaylistMessage = "Let out some of your rage with this pick: ";
            m_playlistID = pickRandomPlaylist(angerPlaylists);
            m_PlaylistName = angerPlaylists.get(m_playlistID);
        }
        if (emotion.contempt > emotionValue)
        {
            emotionValue = emotion.contempt;
            emotionType = "Contempt";
            m_PlaylistMessage = "Show everyone your contempt: ";
            m_playlistID = pickRandomPlaylist(angerPlaylists);
            m_PlaylistName = angerPlaylists.get(m_playlistID);
        }
        if (emotion.disgust > emotionValue)
        {
            emotionValue = emotion.disgust;
            emotionType = "Disgust";
            m_PlaylistMessage = "Get disgusted with this: ";
            m_playlistID = pickRandomPlaylist(angerPlaylists);
            m_PlaylistName = angerPlaylists.get(m_playlistID);
        }
        if (emotion.fear > emotionValue)
        {
            emotionValue = emotion.fear;
            emotionType = "Fear";
            m_PlaylistMessage = "Fear not, listen to these chill beats: ";
            m_playlistID = pickRandomPlaylist(anxiousFearPlaylists);
            m_PlaylistName = anxiousFearPlaylists.get(m_playlistID);
        }
        if (emotion.happiness > emotionValue)
        {
            emotionValue = emotion.happiness;
            emotionType = "Happiness";
            m_PlaylistMessage = "Get happy with today's dose of feel-good songs!; ";
            m_playlistID = pickRandomPlaylist(happyPlaylists);
            m_PlaylistName = happyPlaylists.get(m_playlistID);
        }
        if (emotion.neutral > emotionValue)
        {
            emotionValue = emotion.neutral;
            emotionType = "Neutral";
            m_PlaylistMessage = "Neutral feelings call for uplifting beats: ";
            m_playlistID = pickRandomPlaylist(happyPlaylists);
            m_PlaylistName = happyPlaylists.get(m_playlistID);
        }
        if (emotion.sadness > emotionValue)
        {
            emotionValue = emotion.sadness;
            emotionType = "Sadness";
            m_PlaylistMessage = "Sometimes, spilled milk is worth crying for: ";
            m_playlistID = pickRandomPlaylist(sadPlaylists);
            m_PlaylistName = sadPlaylists.get(m_playlistID);
        }
        if (emotion.surprise > emotionValue)
        {
            emotionValue = emotion.surprise;
            emotionType = "Surprise";
            m_PlaylistMessage = "Come down from your surprise with these mellow vibes: ";
            m_playlistID = pickRandomPlaylist(anxiousFearPlaylists);
            m_PlaylistName = anxiousFearPlaylists.get(m_playlistID);

        }
        return emotionType;
    }



    // Flag to indicate which task is to be performed.
    private static final int REQUEST_SELECT_IMAGE = 0;

    // The URI of the image selected to detect.
    private Uri mImageUri;

    // The image selected to detect.
    private Bitmap mBitmap;

    // Progress dialog popped up when communicating with server.
    ProgressDialog mProgressDialog;


    // When the activity is created, set all the member variables to initial state.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detection);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle(getString(R.string.progress_dialog_title));

        // Disable button "detect" as the image to detect is not selected.
        setDetectButtonEnabledStatus(false);

    }

    // Save the activity state when it's going to stop.
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("ImageUri", mImageUri);
    }

    // Recover the saved state when the activity is recreated.
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mImageUri = savedInstanceState.getParcelable("ImageUri");
        if (mImageUri != null) {
            mBitmap = ImageHelper.loadSizeLimitedBitmapFromUri(
                    mImageUri, getContentResolver());
        }
    }
    // Called when image selection is done.
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_SELECT_IMAGE:
                if (resultCode == RESULT_OK) {
                    // If image is selected successfully, set the image URI and bitmap.
                    mImageUri = data.getData();
                    mBitmap = ImageHelper.loadSizeLimitedBitmapFromUri(
                            mImageUri, getContentResolver());
                    if (mBitmap != null) {
                        // Show the image on screen.
                        ImageView imageView = (ImageView) findViewById(R.id.image);
                        imageView.setImageBitmap(mBitmap);

                        // Add detection log.
                        addLog("Image: " + mImageUri + " resized to " + mBitmap.getWidth()
                                + "x" + mBitmap.getHeight());
                    }

                    // Clear the detection result.
                    FaceListAdapter faceListAdapter = new FaceListAdapter(null);
                    ListView listView = (ListView) findViewById(R.id.list_detected_faces);
                    listView.setAdapter(faceListAdapter);

                    // Clear the information panel.
                    setInfo("");

                    // Enable button "detect" as the image is selected and not detected.
                    setDetectButtonEnabledStatus(true);
                }
                break;
            default:
                break;
        }
    }

    // Called when the "Select Image" button is clicked.
    public void selectImage(View view) {
        Intent intent = new Intent(this, SelectImageActivity.class);
        startActivityForResult(intent, REQUEST_SELECT_IMAGE);
    }

    // Called when the "Detect" button is clicked.
    public void detect(View view) {
        // Put the image into an input stream for detection.
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(output.toByteArray());

        // Start a background task to detect faces in the image.
        new DetectionTask().execute(inputStream);

        // Prevent button click during detecting.
        setAllButtonsEnabledStatus(false);
    }

    // View the log of service calls.
    //public void viewLog(View view) {
    //Intent intent = new Intent(this, DetectionLogActivity.class);
    //startActivity(intent);
    //}

    // Show the result on screen when detection is done.
    private void setUiAfterDetection(Face[] result, boolean succeed) {
        // Detection is done, hide the progress dialog.
        mProgressDialog.dismiss();

        // Enable all the buttons.
        setAllButtonsEnabledStatus(true);

        // Disable button "detect" as the image has already been detected.
        setDetectButtonEnabledStatus(false);

        if (succeed) {
            // The information about the detection result.
            String detectionResult;


            if (result != null) {
                detectionResult = result.length + " face"
                        + (result.length != 1 ? "s" : "") + " detected";

                Face overallface = ConcatenateFaces(result);
                String emotion = getEmotion(overallface.faceAttributes.emotion);
                makeSongRequest(m_playlistID, m_PlaylistName, m_PlaylistMessage,emotion);
                detectionResult = "success";

                // Show the detected faces on original image.
                ImageView imageView = (ImageView) findViewById(R.id.image);
                imageView.setImageBitmap(ImageHelper.drawFaceRectanglesOnBitmap(
                        mBitmap, result, true));

                // Set the adapter of the ListView which contains the details of the detected faces.
                FaceListAdapter faceListAdapter = new FaceListAdapter(result);

                // Show the detailed list of detected faces.
                ListView listView = (ListView) findViewById(R.id.list_detected_faces);
                listView.setAdapter(faceListAdapter);
            } else {
                detectionResult = "0 face detected";
            }
            setInfo(detectionResult);
        }

        mImageUri = null;
        mBitmap = null;
    }

    // Set whether the buttons are enabled.
    private void setDetectButtonEnabledStatus(boolean isEnabled) {
        Button detectButton = (Button) findViewById(R.id.detect);
        detectButton.setEnabled(isEnabled);
    }

    // Set whether the buttons are enabled.
    private void setAllButtonsEnabledStatus(boolean isEnabled) {
        Button selectImageButton = (Button) findViewById(R.id.select_image);
        selectImageButton.setEnabled(isEnabled);

        Button detectButton = (Button) findViewById(R.id.detect);
        detectButton.setEnabled(isEnabled);

        //Button ViewLogButton = (Button) findViewById(R.id.view_log);
       // ViewLogButton.setEnabled(isEnabled);
    }

    // Set the information panel on screen.
    private void setInfo(String info) {
        TextView textView = (TextView) findViewById(R.id.info);
        textView.setText(info);
    }

    // Add a log item.
    private void addLog(String log) {
        LogHelper.addDetectionLog(log);
    }

    // The adapter of the GridView which contains the details of the detected faces.
    private class FaceListAdapter extends BaseAdapter {
        // The detected faces.
        List<Face> faces;

        // The thumbnails of detected faces.
        List<Bitmap> faceThumbnails;

        // Initialize with detection result.
        FaceListAdapter(Face[] detectionResult) {
            faces = new ArrayList<>();
            faceThumbnails = new ArrayList<>();

            if (detectionResult != null) {
                faces = Arrays.asList(detectionResult);
                for (Face face : faces) {
                    try {
                        // Crop face thumbnail with five main landmarks drawn from original image.
                        faceThumbnails.add(ImageHelper.generateFaceThumbnail(
                                mBitmap, face.faceRectangle));
                    } catch (IOException e) {
                        // Show the exception when generating face thumbnail fails.
                        setInfo(e.getMessage());
                    }
                }
            }
        }

        @Override
        public boolean isEnabled(int position) {
            return false;
        }

        @Override
        public int getCount() {
            return faces.size();
        }

        @Override
        public Object getItem(int position) {
            return faces.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater layoutInflater =
                        (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.item_face_with_description, parent, false);
            }
            convertView.setId(position);

            // Show the face thumbnail.
            ((ImageView) convertView.findViewById(R.id.face_thumbnail)).setImageBitmap(
                    faceThumbnails.get(position));

            // Show the face details.
            DecimalFormat formatter = new DecimalFormat("#0.0");
            String face_description = String.format("\n\n\n\n\n" +
                            "\n\n\nEmotion: %s",

                    getEmotion(faces.get(position).faceAttributes.emotion)

            );
            ((TextView) convertView.findViewById(R.id.text_detected_face)).setText(face_description);

            return convertView;
        }
    }



}
