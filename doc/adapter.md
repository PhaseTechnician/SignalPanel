# Adapter
你可以选择两种方式来编写Adapter文件，从而利用其解析你的LOT上传的数据流。
## 要求
+ 你需要给自己的上传流添加结束符
+ 你要保证上传流是合乎规则的

---
## 正则表达式
#### 简述 
基于常见的正则表达式来获取值，这通常能够及时的更新部分数据，而不必全部上传所有数据。你需要以键值对的形式，为一个值添加主键值，类型，和正则表达式。该种方法下，你的上传数据流必须把值编写成字符流。
#### 优点
局部错误只会导致部分信息无法解算或错误。
无需上传全部的数据信息。
#### 缺点
你的上传流之中不能使用字节编码来减少数据量。
#### 实例

---
## 报文格式串
#### 简述
类似C#的printf()中所使用的那样，你需要完全的解释报文的格式，你的数据使用占位符{1(主键),bool(类型)}代替。
