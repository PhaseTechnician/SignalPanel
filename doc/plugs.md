## 通用外观属性
Width|Height|X|Y|
---|---|---|---|
宽度|高度|X定位|Y定位
## 特殊属性
PlugName(enum)|mainString(string)|spareString(string)|positiveKey(string)|negativeKey(string)|positiveEnable(bool)|negativieEnable(bool)|mode(string)|src(string)
---|---|---|---|---|---|---|---|---|
控件名称|主字符串|备用字符|正键值|负键值|正使能|负使能|模式|资源
|
Buttun|按钮未启用名称|按钮启用名称|按键按下时发送信息|按键抬起时发送信息|按键按下是否发送|按键抬起是否发送|触发/自锁|按钮的图片资源
Switch|开关未启用名称|开关启用名称|开关打开时发送信息|开关关闭时发送信息|开关状态时改变对应名称|初始状态|null|null/(shape)(color)
SeekBar|拖拽条名称|数值改变时发送格式串|最小值|最大值|值增加时是否发送|值减少时是否发送|positive/negative|null/(shape)(color)
Axe|摇杆名称|数值改变时发送格式串|X数值范围|Y数值范围|X值改变时是否发送|Y值改变时是否发送|default/lock|{BGimg}+{FWimg}
| 
ProgressBar|进度条名称|null|
view|视图主标题|视图副标题|null|
