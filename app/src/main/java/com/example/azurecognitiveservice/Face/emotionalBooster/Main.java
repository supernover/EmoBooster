package com.example.azurecognitiveservice.Face.emotionalBooster;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.azurecognitiveservice.Face.emotionalBooster.ui.MainActivity;


public class Main extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        if (getString(R.string.subscription_key).startsWith("Please")) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.add_subscription_key_tip_title))
                    .setMessage(getString(R.string.add_subscription_key_tip))
                    .setCancelable(false)
                    .show();
        }
    }

    public void detection(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    //public void verification(View view) {
    //Intent intent = new Intent(this, VerificationMenuActivity.class);
    //startActivity(intent);
    //}

    //public void grouping(View view) {
    //Intent intent = new Intent(this, GroupingActivity.class);
    //startActivity(intent);
    //}

    //public void findSimilarFace(View view) {
    //Intent intent = new Intent(this, FindSimilarFaceActivity.class);
    //startActivity(intent);
    //}

    //public void identification(View view) {
    //Intent intent = new Intent(this, IdentificationActivity.class);
    //startActivity(intent);
    //}
}