# ZoomPreviewPicture

本项目受Google官方demo Zooming a View 启发，实现了点击小图放大至全屏预览，退出全屏恢复至原来位置这两个过程的动画过渡。
常见应用场景如微信朋友圈照片九宫格和微信聊天图片预览，某些手机系统相册等viewpager图片查看 缩放 拖拽下拉缩小退出（效果同微信图片浏览）

 ### 特点
   * 1支持自定义图片加载框架，
   * 2支持重写activity,完成切换切换效果
   * 3图片查看 缩放 拖拽下拉缩小退出
   * 4支持类似微信朋友圈照片九宫格和微信聊天图片预览，只要你中图片已经说支持。
####效果如下：

![](https://github.com/yangchaojiang/ZoomPreviewPicture/blob/master/gif/aaaa.gif)

通过Gradle抓取:

```
    repositories {
        jcenter()
        maven { url "https://jitpack.io" }
    }
```
```grade


   compile 'com.ycjiang:imgepreviewlibrary:1.0.4'

```
```Maven
<dependency>
  <groupId>com.ycjiang</groupId>
  <artifactId>loadviewhelper</artifactId>
  <version>1.0.4</version>
  <type>pom</type>
</dependency>
```
###  1.项目类库依赖项目
```
    compile 'com.github.chrisbanes.photoview:library:1.2.4'
    compile 'com.android.support:support-v4:25.3.1'
```
### 2.示例代码
````
//mRecyclerView item点击事件
     mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
              //在你点击时，调用computeBoundsBackward（）方法
                computeBoundsBackward(mGridLayoutManager.findFirstVisibleItemPosition());
                PhotoActivity.startActivity(MainActivity.this,mThumbViewInfoList,position);
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
### 4.自定义Activity 实现getViewPager 切换动画
~~~
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
        @Override
        public void clearMemory(@NonNull Context c) {
                 Glide.get(c).clearMemory();
        }
}
~~~

#### [九宫格图片控件来自laobie](https://github.com/laobie/NineGridImageView)

### 升级日志
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


