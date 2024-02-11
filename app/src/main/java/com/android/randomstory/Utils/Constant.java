package com.android.randomstory.Utils;

import android.widget.ImageView;

import com.android.randomstory.R;

import java.util.Objects;

import io.paperdb.Paper;

public class Constant {

    // Static Function for setting Profile Image
    public static void ShowDrawableImage(ImageView avatar) {
        String profilePicture = Paper.book().read("MyDp", "");

        switch (Objects.requireNonNull(profilePicture)) {
            case "1":
                avatar.setBackgroundResource(R.drawable.img_1);
                break;
            case "2":
                avatar.setBackgroundResource(R.drawable.img_2);
                break;
            case "3":
                avatar.setBackgroundResource(R.drawable.img_3);
                break;
            case "4":
                avatar.setBackgroundResource(R.drawable.img_4);
                break;
            case "5":
                avatar.setBackgroundResource(R.drawable.img_5);
                break;
            case "6":
                avatar.setBackgroundResource(R.drawable.img_6);
                break;
            case "7":
                avatar.setBackgroundResource(R.drawable.img_7);
                break;
            case "8":
                avatar.setBackgroundResource(R.drawable.img_8);
                break;
            case "9":
                avatar.setBackgroundResource(R.drawable.img_9);
                break;
        }
    }



}
