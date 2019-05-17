package com.yxj.hugeimageview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;

/**
 * Author:  Yxj
 * Time:    2019/5/16 上午9:22
 * -----------------------------------------
 * Description:
 */
public class HugeImageActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_huge_image);

        HugeImageView hugeImageView = findViewById(R.id.big_view);

        try {
            InputStream is = getResources().getAssets().open("huge_image.png");
            hugeImageView.setImageStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
