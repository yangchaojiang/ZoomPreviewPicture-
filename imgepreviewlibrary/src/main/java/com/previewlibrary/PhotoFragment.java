package com.previewlibrary;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.previewlibrary.loader.MySimpleTarget;
import com.previewlibrary.wight.SmoothImageView;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by yangc on 2017/4/26.
 * E-Mail:yangchaojiang@outlook.com
 * Deprecated: 图片预览单个图片的fragment
 */
public class PhotoFragment extends Fragment {
    /**
     * 预览图片 类型
     */
    public static final String KEY_START_BOUND = "startBounds";
    public static final String KEY_TRANS_PHOTO = "is_trans_photo";
    public static final String KEY_SING_FILING = "isSingleFling";
    public static final String KEY_PATH = "key_path";
    //图片地址
    private String imgUrl;
    // 是否是以动画进入的Fragment
    private boolean isTransPhoto = false;
    //图片
    private SmoothImageView photoView;
    //图片的外部控件
    private View rootView;
    //进度条
    private ProgressBar loading;
    private MySimpleTarget mySimpleTarget;

    public static PhotoFragment getInstance() {
        return new PhotoFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image_photo_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initDate();
    }

    @Override
    public void onStop() {
        super.onStop();
        ZoomMediaLoader.getInstance().getLoader().onStop(this);
        mySimpleTarget = null;
    }

    /**
     * 初始化控件
     */
    private void initView(View view) {
        loading = (ProgressBar) view.findViewById(R.id.loading);
        photoView = (SmoothImageView) view.findViewById(R.id.photoView);
        rootView = view.findViewById(R.id.rootView);
        mySimpleTarget = new MySimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap) {
                if (photoView.getTag().toString().equals(imgUrl)) {
                    photoView.setImageBitmap(bitmap);
                    loading.setVisibility(View.GONE);
                }
            }

            @Override
            public void onLoadFailed(Drawable errorDrawable) {
                loading.setVisibility(View.GONE);
                if (errorDrawable != null) {
                    photoView.setImageDrawable(errorDrawable);
                }
            }

            @Override
            public void onLoadStarted() {

            }
        };
    }

    /**
     * 初始化数据
     */
    private void initDate() {
        Bundle bundle = getArguments();
        boolean isSingleFling = true;
        if (bundle != null) {
            isSingleFling = bundle.getBoolean(KEY_SING_FILING);
            //地址
            imgUrl = bundle.getString(KEY_PATH);
            //位置
            Rect startBounds = bundle.getParcelable(KEY_START_BOUND);
            if (startBounds != null) {
                photoView.setThumbRect(startBounds);
            }
            photoView.setTag(imgUrl);
            //是否展示动画
            isTransPhoto = bundle.getBoolean(KEY_TRANS_PHOTO, false);
            //加载缩略图
            //加载原图
            ZoomMediaLoader.getInstance().getLoader().displayImage(this, imgUrl, mySimpleTarget);

        }
        // 非动画进入的Fragment，默认背景为黑色
        if (!isTransPhoto) {
            rootView.setBackgroundColor(Color.BLACK);
        } else
            photoView.setMinimumScale(1f);
        if (isSingleFling) {
            photoView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
                @Override
                public void onViewTap(View view, float x, float y) {
                    if (photoView.checkMinScale()) {
                        ((GPreviewActivity) getActivity()).transformOut();
                    }
                }
            });
        }else {
            photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float x, float y) {
                    if (photoView.checkMinScale()) {
                        ((GPreviewActivity) getActivity()).transformOut();
                    }
                }

                @Override
                public void onOutsidePhotoTap() {

                }
            });
        }
        photoView.setAlphaChangeListener(new SmoothImageView.OnAlphaChangeListener() {
            @Override
            public void onAlphaChange(int alpha) {
                rootView.setBackgroundColor(getColorWithAlpha(alpha / 255f, Color.BLACK));
            }
        });

        photoView.setTransformOutListener(new SmoothImageView.OnTransformOutListener() {
            @Override
            public void onTransformOut() {
                if (photoView.checkMinScale()) {
                    ((GPreviewActivity) getActivity()).transformOut();
                }
            }
        });
    }

    public static int getColorWithAlpha(float alpha, int baseColor) {
        int a = Math.min(255, Math.max(0, (int) (alpha * 255))) << 24;
        int rgb = 0x00ffffff & baseColor;
        return a + rgb;
    }


    public void transformIn() {
        photoView.transformIn(new SmoothImageView.onTransformListener() {
            @Override
            public void onTransformCompleted(SmoothImageView.Status status) {
                rootView.setBackgroundColor(Color.BLACK);
            }
        });
    }

    public void transformOut(SmoothImageView.onTransformListener listener) {
        photoView.transformOut(listener);
    }

    public void changeBg(int color) {
        rootView.setBackgroundColor(color);
    }

}
