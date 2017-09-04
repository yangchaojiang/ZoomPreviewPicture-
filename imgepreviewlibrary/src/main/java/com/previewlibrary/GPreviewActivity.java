package com.previewlibrary;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.previewlibrary.wight.PhotoViewPager;
import com.previewlibrary.wight.SmoothImageView;
import com.previewlibrary.enitity.ThumbViewInfo;

import java.util.ArrayList;
import java.util.List;

 /**
 * Created by yangc on 2017/4/26.
 * E-Mail:yangchaojiang@outlook.com
 * Deprecated:图片预览页面
 */
public class GPreviewActivity extends FragmentActivity {
    private boolean isTransformOut = false;
    //图片的地址
    private List<ThumbViewInfo> imgUrls;
    //当前图片的位置
    private int currentIndex;
    //图片的展示的Fragment
    private List<PhotoFragment> fragments = new ArrayList<>();
    //展示图片的viewPager
    private PhotoViewPager viewPager;
    //显示图片数
    private TextView ltAddDot;

     /***
      * 启动预览
      *
      * @param activity     活动对象
      * @param tempData     图片集合
      * @param currentIndex 当前索引坐标
      ***/
     public static void startActivity(Activity activity, ArrayList<ThumbViewInfo> tempData, int currentIndex) {
         // 图片的地址
         //获取图片的bitmap
         Intent intent = new Intent(activity, GPreviewActivity.class);
         intent.putParcelableArrayListExtra("imagePaths", tempData);
         intent.putExtra("position", currentIndex);
         activity.startActivity(intent);
     }
     /***
      * 启动预览
      *
      * @param activity     活动对象
      * @param    className  目标类
      * @param tempData     图片集合
      * @param currentIndex 当前索引坐标
      ***/
     public static void startActivity(Activity activity, Class className, ArrayList<ThumbViewInfo> tempData, int currentIndex) {
         // 图片的地址
         //获取图片的bitmap
         Intent intent = new Intent(activity,className);
         intent.putParcelableArrayListExtra("imagePaths", tempData);
         intent.putExtra("position", currentIndex);
         activity.startActivity(intent);
     }


     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_imge_preview_photo);
         initData();
         initView();
     }

     @Override
     protected void onDestroy() {
         super.onDestroy();
         fragments.clear();
         fragments=null;
         imgUrls=null;
         viewPager.clearOnPageChangeListeners();
     }

     /**
     * 初始化数据
     */
    private void initData() {
        imgUrls = getIntent().getParcelableArrayListExtra("imagePaths");
        currentIndex = getIntent().getIntExtra("position", -1);
        if (imgUrls != null) {
            for (int i = 0; i < imgUrls.size(); i++) {
                PhotoFragment fragment = new PhotoFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(PhotoFragment.KEY_PATH, imgUrls.get(i).getUrl());
                bundle.putParcelable(PhotoFragment.KEY_START_BOUND, imgUrls.get(i).getBounds());
                bundle.putBoolean(PhotoFragment.KEY_TRANS_PHOTO, currentIndex == i);
                fragment.setArguments(bundle);
                fragments.add(fragment);
            }
        } else {
            finish();
        }
    }

    /**
     * 初始化控件
     */
    private void initView() {
        viewPager = (PhotoViewPager) findViewById(R.id.viewPager);
        ltAddDot = (TextView) findViewById(R.id.ltAddDot);
        ltAddDot.setText(currentIndex + 1 + "/" + imgUrls.size());
        //viewPager的适配器
        PhotoPagerAdapter adapter = new PhotoPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //当被选中的时候设置小圆点和当前位置
                if (ltAddDot != null) {
                    ltAddDot.setText((position + 1) + "/" + imgUrls.size());
                }
                currentIndex = position;
                viewPager.setCurrentItem(currentIndex, true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(currentIndex);
        viewPager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                viewPager.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                final PhotoFragment fragment = fragments.get(currentIndex);
                fragment.transformIn();
            }
        });

    }

    //退出预览的动画
     void transformOut() {
        if (isTransformOut) {
            return;
        }
        isTransformOut = true;
        int currentItem = viewPager.getCurrentItem();
        if (currentItem < imgUrls.size()) {
            PhotoFragment fragment = fragments.get(currentItem);
            ltAddDot.setVisibility(View.GONE);
            fragment.changeBg(Color.TRANSPARENT);
            fragment.transformOut(new SmoothImageView.onTransformListener() {
                @Override
                public void onTransformCompleted(SmoothImageView.Status status) {
                    exit();
                }
            });
        } else {
            exit();
        }
    }

     /**
     * 关闭页面
     */
    public void exit() {
        finish();
        overridePendingTransition(0, 0);
    }

     public List<ThumbViewInfo> getImgUrls() {
         return imgUrls;
     }

     public PhotoViewPager getViewPager() {
         return viewPager;
     }

     @Override
    public void onBackPressed() {
        transformOut();
    }

     /**
     * pager的适配器
     */
    private class PhotoPagerAdapter extends FragmentPagerAdapter {

        PhotoPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

}
