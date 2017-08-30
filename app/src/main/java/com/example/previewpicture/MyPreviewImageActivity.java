package com.example.previewpicture;

import android.os.Bundle;
import com.previewlibrary.GPreviewActivity;
/**
 * Created by yangc on 2017/8/30.
 * E-Mail:yangchaojiang@outlook.com
 * Deprecated:  自定义预览
 */

public class MyPreviewImageActivity  extends GPreviewActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getViewPager().setPageTransformer(true,new DepthPageTransformer());
       // getViewPager();
       // getImgUrls();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
