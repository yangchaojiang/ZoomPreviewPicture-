# ZoomPreviewPicture

本项目受Google官方demo Zooming a View 启发，实现了点击小图放大至全屏预览，退出全屏恢复至原来位置这两个过程的动画过渡。
常见应用场景如微信朋友圈照片九宫格和微信聊天图片预览，某些手机系统相册等viewpager图片查看 缩放 拖拽下拉缩小退出（效果同微信图片浏览）

通过Gradle抓取:

```gradle

compile 'com.ycjiang:imgepreviewlibrary:1.0.2'

```
```Maven
<dependency>
  <groupId>com.ycjiang</groupId>
  <artifactId>loadviewhelper</artifactId>
  <version>1.0.2</version>
  <type>pom</type>
</dependency>
```
###  项目类库依赖项目
```
    compile 'com.github.chrisbanes.photoview:library:1.2.4'
    compile 'com.github.bumptech.glide:glide:3.7.0'
    compile 'com.android.support:support-v4:25.3.1'```
```
### 示例代码
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
    *  * 查找信息
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
###  自定义Activity 实现getViewPager 切换动画
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
}
~~~

效果如下：
![](https://github.com/yangchaojiang/ZoomPreviewPicture/blob/master/gif/aaaa.gif)

