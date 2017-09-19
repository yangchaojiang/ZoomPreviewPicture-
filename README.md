# ZoomPreviewPicture

本项目受Google官方demo Zooming a View 启发，实现了点击小图放大至全屏预览，退出全屏恢复至原来位置这两个过程的动画过渡。
常见应用场景如微信朋友圈照片九宫格和微信聊天图片预览，某些手机系统相册等viewpager图片查看 缩放 拖拽下拉缩小退出（效果同微信图片浏览）

 ### 特点
   * 1.支持自定义图片加载框架，
   * 2.支持重写activity,完成切换切换效果
   * 3.图片查看 缩放 拖拽下拉缩小退出
   * 4.支持自定义activity内容
   * 5.支持类似微信朋友圈照片九宫格和微信聊天图片预览，只要你中图片已经说支持。
   * 6.指示器类型选择 [圆点模式(贝塞尔圆点指示器)](https://github.com/yanyiqun001/bannerDot)和数字模式
####效果如下：

![](gif/test.gif)

通过Gradle抓取:

```
    repositories {
        jcenter()
        maven { url "https://jitpack.io" }
    }
```
```grade


   compile 'com.ycjiang:imgepreviewlibrary:1.0.8'

```
```Maven
<dependency>
  <groupId>com.ycjiang</groupId>
  <artifactId>loadviewhelper</artifactId>
  <version>1.0.8</version>
  <type>pom</type>
</dependency>
```
###  1.项目类库依赖项目
```
    compile 'com.github.chrisbanes.photoview:library:1.3.1'
    compile 'com.android.support:support-v4:25.3.1'
```
### 2.示例代码

   使用方式
```
                 GPreviewBuilder.from(RecycleViewActivity.this)
                                .setData(mThumbViewInfoList)
                                .setCurrentIndex(position)
                                .setSingleFling(true)
                                .setType(GPreviewBuilder.IndicatorType.Number)
                                .start();

```

````
//mRecyclerView item点击事件
     mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
              //在你点击时，调用computeBoundsBackward（）方法
                computeBoundsBackward(mGridLayoutManager.findFirstVisibleItemPosition());
              GPreviewBuilder.from(RecycleViewActivity.this)
                                .setData(mThumbViewInfoList)
                                .setCurrentIndex(position)
                                .setType(GPreviewBuilder.IndicatorType.Number)
                                .start();
            }
        });

    /**
     ** 查找信息
     * 从第一个完整可见item逆序遍历，如果初始位置为0，则不执行方法内循环
     */
    private void computeBoundsBackward(int firstCompletelyVisiblePos) {
        for (int i = firstCompletelyVisiblePos;i < mThumbViewInfoList.size(); i++) {
            View itemView = mGridLayoutManager.findViewByPosition(i);
            Rect bounds = new Rect();
            if (itemView != null) {
                ImageView thumbView = (ImageView) itemView.findViewById(R.id.iv);
                thumbView.getGlobalVisibleRect(bounds);
            }
            mThumbViewInfoList.get(i).setBounds(bounds);
        }
    }
````

### 3.使用自定义图片加载配置  **注意这个必须实现哦。不然加载**
   * 1在你项目工程，创建一个类 实现接口IZoomMediaLoader接口 如下代码
       demo 采用glide ，可以使用Picassor Imagloader 图片加载框架
````
public class TestImageLoader implements IZoomMediaLoader {
    @Override
    public void displayImage(Fragment context, String path, final MySimpleTarget<Bitmap> simpleTarget) {
        Glide.with(context)
                .load(path)
                .asBitmap()
                .centerCrop()
                .error(R.drawable.ic_ssss)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        simpleTarget.onResourceReady(resource);
                    }

                    @Override
                    public void onLoadStarted(Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        simpleTarget.onLoadStarted();

                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        simpleTarget.onLoadFailed(errorDrawable);
                    }


                });
    }

     @Override
     public void onStop(@NonNull Fragment context) {
           Glide.with(context).onStop();

     }

     @Override
     public void clearMemory(@NonNull Context c) {
              Glide.get(c).clearMemory();
     }

````
  * 2注册 你实现自定义类，在你 app onCreate() 中
````
    @Override
      public void onCreate() {
          super.onCreate();
          ZoomMediaLoader.getInstance().init(new TestImageLoader());
      }
````
### 4.自定义Activity
   实现业务自己逻辑操作，例如返回按钮toolbar  实现getViewPager 切换动画等等
~~~
public class CustomActivity extends GPreviewActivity {

    /***
     * 重写该方法
     * 使用你的自定义布局
     **/
    @Override
    public int setContentLayout() {
        return R.layout.activity_custom_preview;
    }
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //退出时调用，d封装方法的 不然没有动画效果
                transformOut();
            }
        });
    }

~~~
 * 布局文件
 ````
 <?xml version="1.0" encoding="utf-8"?>
 <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
     xmlns:app="http://schemas.android.com/apk/res-auto"
     android:layout_width="match_parent"
     android:layout_height="match_parent"
     android:orientation="vertical">
     <android.support.v7.widget.Toolbar
         android:id="@+id/toolbar"
         android:layout_width="match_parent"
         android:layout_height="50dp"
         android:background="@color/colorPrimaryDark"
         app:titleTextColor="@android:color/white"
         app:navigationIcon="@mipmap/ic_back"
         app:title="自定义预览" />
         <!-- 在你布局引用预览布局内容 -->
      <include layout="@layout/activity_image_preview_photo"/>

 </LinearLayout>

 ````

  >注意：
  >>
  >>1. **自定义使用布局时，不在子类使用setContentView()方法**

  >>2. **你在Activity 重写 setContentLayout()，返回你的自定义布局**

  >>3. **在你布局内容 使用include layout="@layout/activity_image_preview_photo" 预览布局添加你布局中**

  >>4. **GPreviewBuilder 调用 from()方法后，调用to();指向你.to(CustomActivity.class)自定义预览activity**

  >>5. **别忘了在AndroidManifest  activity 使用主题**
  >> 示例：

              <!--d注册自定义activity-->
                <activity android:name=".custom.CustomActivity"
                    android:screenOrientation="portrait"
                    android:theme="@android:style/Theme.Translucent.NoTitleBar"
              />


#### [九宫格图片控件来自laobie](https://github.com/laobie/NineGridImageView)

### 升级日志
 ####1.0.8
   * 1.完善自定义预览实现步骤，让自定义更简洁

 #### 1.0.7
   * 1.修复双手缩放失效问题
 #### 1.0.6
   * 1.修改启动预览activity启动方式 采用GPreviewBuilder 链接调用
   * 2.增加指示器类型选择 [圆点(贝塞尔圆点指示器)](https://github.com/yanyiqun001/bannerDot)和数字模式
   * 3.升级[PhotoView版本1.3.1](https://github.com/chrisbanes/PhotoView/)
   * 4.设置最小版本15
 #### 1.0.4
   * 1.修复占位图错位问题
   * 2.支持色值和svg 等矢量图。
   * 3.优化bitmap使用
 #### 1.0.3
   * 1  移除Glide 类库依赖，使用自定义图片加载框架
   * 2  增加自定义配置接口，实现即可完成自定义加载框架
   * 3  修复占位图，因错误问题
   * 4  修改包名路径，分类
 #### 1.0.2
   * 1 增加自定义Activity实现实现getViewPager 切换动画
 ####  1.0.1
   *  发布第一版


