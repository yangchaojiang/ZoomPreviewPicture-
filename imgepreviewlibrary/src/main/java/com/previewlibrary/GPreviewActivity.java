package com.previewlibrary;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.previewlibrary.wight.BezierBannerView;
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
    private BezierBannerView bezierBannerView;
    private GPreviewBuilder.IndicatorType type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (setContentLayout() == 0) {
            setContentView(R.layout.activity_image_preview_photo);
        } else {
            setContentView(setContentLayout());
        }
        initData();
        initView();
    }

    @Override
    protected void onDestroy() {
        ZoomMediaLoader.getInstance().getLoader().clearMemory(this);
        if (viewPager != null) {
            viewPager.setAdapter(null);
            viewPager.clearOnPageChangeListeners();
            viewPager.removeAllViews();
        }
        if (fragments != null) {
            fragments.clear();
            fragments = null;
        }
        if (imgUrls != null) {
            imgUrls.clear();
            imgUrls = null;
        }
        super.onDestroy();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        imgUrls = getIntent().getParcelableArrayListExtra("imagePaths");
        currentIndex = getIntent().getIntExtra("position", -1);
        type = (GPreviewBuilder.IndicatorType) getIntent().getSerializableExtra("type");
        if (imgUrls != null) {
            Bundle bundle;
            for (int i = 0; i < imgUrls.size(); i++) {
                PhotoFragment fragment = PhotoFragment.getInstance();
                bundle = new Bundle();
                bundle.putSerializable(PhotoFragment.KEY_PATH, imgUrls.get(i).getUrl());
                bundle.putParcelable(PhotoFragment.KEY_START_BOUND, imgUrls.get(i).getBounds());
                bundle.putBoolean(PhotoFragment.KEY_TRANS_PHOTO, currentIndex == i);
                bundle.putBoolean(PhotoFragment.KEY_SING_FILING, getIntent().getBooleanExtra("isSingleFling", false));
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
        //viewPager的适配器
        PhotoPagerAdapter adapter = new PhotoPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(currentIndex);
        if (type == GPreviewBuilder.IndicatorType.Dot) {
            bezierBannerView = (BezierBannerView) findViewById(R.id.bezierBannerView);
            bezierBannerView.setVisibility(View.VISIBLE);
            bezierBannerView.attachToViewpager(viewPager);
        } else {
            ltAddDot = (TextView) findViewById(R.id.ltAddDot);
            ltAddDot.setVisibility(View.VISIBLE);
            ltAddDot.setText(currentIndex + 1 + "/" + imgUrls.size());
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
        }
        viewPager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                viewPager.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                PhotoFragment fragment = fragments.get(currentIndex);
                fragment.transformIn();
            }
        });


    }

    //退出预览的动画
    protected void transformOut() {
        if (isTransformOut) {
            return;
        }
        isTransformOut = true;
        int currentItem = viewPager.getCurrentItem();
        if (currentItem < imgUrls.size()) {
            PhotoFragment fragment = fragments.get(currentItem);
            if (ltAddDot != null) {
                ltAddDot.setVisibility(View.GONE);
            } else {
                bezierBannerView.setVisibility(View.GONE);
            }
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
    private void exit() {
        finish();
        overridePendingTransition(0, 0);
    }

    public PhotoViewPager getViewPager() {
        return viewPager;
    }

    /***
     * 自定义布局内容
     ***/
    public int setContentLayout() {
        return 0;
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
            return fragments == null ? 0 : fragments.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }
    }


}
