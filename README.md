# ZoomPreviewPicture
本项目受Google官方demo Zooming a View 启发，实现了点击小图放大至全屏预览，退出全屏恢复至原来位置这两个过程的动画过渡。
常见应用场景如微信朋友圈照片九宫格和微信聊天图片预览，某些手机系统相册等viewpager图片查看 缩放 拖拽下拉缩小退出（效果同微信图片浏览）

通过Gradle抓取:

```gradle

compile 'com.ycjiang:imgepreviewlibrary:1.0.1'

```
```Maven
<dependency>
  <groupId>com.ycjiang</groupId>
  <artifactId>loadviewhelper</artifactId>
  <version>1.0.0</version>
  <type>pom</type>
</dependency>
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

效果如下：
![](https://github.com/yangchaojiang/ZoomPreviewPicture/blob/master/gif/aaaa.gif)

License
--------
The project by the Google official demo Zooming a View inspired to achieve a click
on the thumbnail zoom to full screen preview, exit the full screen to restore the original location 
of the two processes of the animation transition.
Common application scenes such as WeChat friends circle photo Jiugongge and WeChat chat picture preview,
some mobile phone system album, etc. viewpager picture zoom zoom pull down to pull out (effect with micro letter picture browsing
