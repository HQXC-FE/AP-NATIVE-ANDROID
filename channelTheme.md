local.properties文件添加
#皮肤编译使用(每增加一个皮肤可自行本地添加配置)
ACTIVE_VARIANT=od_yellow_pre（皮肤渠道）
#ACTIVE_VARIANT=od_purple_pre
...

新建渠道
1，channel_config/channels目录 新增渠道json(ol和pre)
2,将新增的渠道json对应参数添加到channel_config/groupAll.json里面
3，原目录复制library-res/src/od_green皮肤包并更改皮肤渠道名称为对应新增的渠道
4，皮肤目录mipmap文件目录下替换对应的皮肤logo和启动图、raw下替换loading图的logo（0.2x）
5，皮肤目录themes_colors.xml替换对应的规则颜色值
6,皮肤目录drawable目录下按顺序替换对应的皮肤icon
#注意：下载figma上的倍图对应android的需要多一个倍数，例如3x对应android的xx(drawable-xxhdpi)

打包task
./gradlew dyAssemble