# MyAssistant
Personal assistant.It could help us in our life and work.

## 消息体控件使用说明
在消息体中涉及图片、视频、音频、网页、倒计时等控件的使用时，请按以下格式进行书写。
### 链接
图片、视频、音频资源的相对路径为 /assistant
#### 图片链接
```
#{type:'image',link:'test1.jpg',text:'image'}#
```
#### 音频链接
```
#{type:'audio',link:'test2.mp3',text:'audio'}#
```
#### 视频链接
```
#{type:'video',link:'test.mp4',text:'video'}#
```
#### WEB链接
```
#{type:'url',link:'www.baidu.com',text:'url'}#
```

### 倒计时
```
<countdown value='2017,3,1'>
```

### wikihow采集链接脚本
```javascript
var s=""; $.each($("#bodycontents .image_map span"),function(index,item){s+="    * ["+$(item).text()+"](https://zh.wikihow.com/"+$(item).text()+")\n";});console.dir(s);
```