# ZoomPreviewPicture

本项目受Google官方demo Zooming a View 启发，实现了点击小图放大至全屏预览，退出全屏恢复至原来位置这两个过程的动画过渡。
常见应用场景如微信朋友圈照片九宫格和微信聊天图片预览，某些手机系统相册等viewpager图片查看 缩放 拖拽下拉缩小退出（效果同微信图片浏览）

[ ![Download](https://api.bintray.com/packages/ycjiang/yangjiang/ImagePreview/images/download.svg) ](https://bintray.com/ycjiang/yangjiang/ImagePreview/_latestVersion)

 ### 特点
   * 1.支持自定义图片加载框架。
   * 2.支持重写activity,完成切换切换效果。
   * 3.图片查看 缩放 拖拽下拉缩小退出。
   * 4.支持自定义activity,Fragment。
   * 5.支持类似微信朋友圈照片九宫格和微信聊天图片预览。
   * 6.指示器类型选择 [圆点模式(贝塞尔圆点指示器)](https://github.com/yanyiqun001/bannerDot)和数字模式。
   * 7.增加接口实体类。不在使用数据转化。
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
  compile 'com.ycjiang:ImagePreview:2.0.0'
```
 
###  1.本项目类库依赖第三库
```
    compile 'com.github.chrisbanes.photoview:library:1.3.1'
      compile 'com.android.support:support-fragment:25.3.1'
```
### 2.示例代码
 1.使用方式
```
     GPreviewBuilder.from(GridViewCustomActivity.this)//activity实例必须
                            .to(CustomActivity.class)//自定义Activity 使用默认的预览不需要
                            .setData(mThumbViewInfoList)//集合
                            .setUserFragment(UserFragment.class)//自定义Fragment 使用默认的预览不需要
                            .setCurrentIndex(position)
                            .setSingleFling(false)//是否在黑屏区域点击返回
                            .setDrag(false)//是否禁用图片拖拽返回  
                            .setType(GPreviewBuilder.IndicatorType.Dot)//指示器类型
                            .start();//启动            

```
2.列表控件item点击事件添加相应代码。
(RecyclerView为例，demo有(ListView和GridView和九宫格控件实例代码))
```
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
  2.构造实体类： 你的实体类实现IThumbViewInfo接口
 `````
 public class UserViewInfo implements IThumbViewInfo {
     private String url;  //图片地址
     private Rect mBounds; // 记录坐标
     private String user;//
 
     public UserViewInfo(String url) {
         this.url = url;
     }
 
     @Override
     public String getUrl() {//将你的图片地址字段返回
         return url;
     }
     public void setUrl(String url) {
         this.url = url;
     }
     @Override
     public Rect getBounds() {//将你的图片显示坐标字段返回
         return mBounds;
     }
     
     public void setBounds(Rect bounds) {
         mBounds = bounds;
     }
    } 
 `````

### 3.使用自定义图片加载配置  **注意这个必须实现哦。不然加载**
   * 1在你项目工程，创建一个类 实现接口IZoomMediaLoader接口 如下代码
       demo 采用glide ，可以使用Picassor Imagloader 图片加载框架
````
public class TestImageLoader implements IZoomMediaLoader {
    @Override
    public void displayImage(Fragment context, String path, final MySimpleTarget<Bitmap> simpleTarget) {
         Glide.with(context).load(path).asBitmap().centerCrop().diskCacheStrategy(DiskCacheStrategy.SOURCE)
                      .error(R.drawable.ic_default_image)
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
### 4.自定义Activity,Fragment
 1.实现自定义Activity，实现你业务操作例如加入标题栏，ViewPager切换动画等等
 .![image.png](http://upload-images.jianshu.io/upload_images/1190712-fed3e16d9a686110.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
 
 在你的布局中,引用类库核心布局
 
 >><include layout="@layout/activity_image_preview_photo" />

 
2.实现自定义Fragment   实现自定义业务  例如 长按保存图片，编辑图片,对图片说明内容等等
图片缩放效果采用[PhotoView](https://github.com/chrisbanes/PhotoView)
![image.png](http://upload-images.jianshu.io/upload_images/1190712-d0175aaf2931116e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

>>需要布局自定义重写onCreateView()。引用你自定义布局中添加
>><include layout="@layout/fragment_image_photo_layout" />
 
3 使用细节注意：
  >>1  **Activity和Fragment可以单独使用,也可以组合一起使用**
  
  >>2. **自定义使用布局时，不在子类使用setContentView()方法**
  
  >>3. **你在Activity 重写 setContentLayout()，返回你的自定义布局**
  
  >>4. **在你布局内容 使用include layout="@layout/activity_image_preview_photo" 预览布局添加你布局中**
 
  >>5. **GPreviewBuilder 调用 from()方法后，调用to();指向你.to(CustomActivity.class)自定义预览activity**
 
  >>6. **别忘了在AndroidManifest  activity 使用主题**
 
  >> 示例：
              <!--注册自定义activity-->
                <activity android:name=".custom.CustomActivity"
                    android:screenOrientation="portrait"
                    android:theme="@android:style/Theme.Translucent.NoTitleBar"
              /> 

#### [九宫格图片控件来自laobie](https://github.com/laobie/NineGridImageView)

### 升级日志
 #### 2.0.0
   * 1.类库引用名称”com.ycjiang:imgepreviewlibrary:xxx“改为“com.ycjiang:ImagePreview:xxx”
   * 2.ThumbViewInfo 父类 改为 IThumbViewInfo 接口实现。
   * 3.增加支持Fragment自定义.实现各种业务需求。
   * 4.增加图片拖拽禁用方法。[#25](https://github.com/yangchaojiang/ZoomPreviewPicture/issues/25)
 #### 1.1.3
   * 1.fragment改用静态方法实例化。
 #### 1.1.2
   * 1.列表数据过长OOM问题问题[#17](https://github.com/yangchaojiang/ZoomPreviewPicture/issues/17)
   * 2.sdk 23版本九宫格图片类型可能进入的动画效果[#15](https://github.com/yangchaojiang/ZoomPreviewPicture/issues/15)
 #### 1.1.0
   * 1.bug修复21版本下奔溃问题[#16](https://github.com/yangchaojiang/ZoomPreviewPicture/issues/16)
 #### 1.0.9
   * 1.bug修复返回销毁对null判断处理[#14](https://github.com/yangchaojiang/ZoomPreviewPicture/issues/14)
 #### 1.0.8
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


