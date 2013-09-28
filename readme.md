#java版本中文文本纠错服务
####这个服务主要是为家具声控服务的

#服务方式
* 使用socket，端口10000 与python 的socket客户端通讯


#开发语言
* java

#录入非结构性数据
结构 id   desc    cmd    type
* 电视  type tv
 * 1 我想看电视        20
 * 2 把电视打开        20
 * 3 把电视关了        20
 * 4 关闭电视          20
 × 5 切换电视模式       01
 × 6 电视声音太小了     0c
 × 7 电视声音太大了     04
 × 8 电视声音太嘈了     04
 × 9 换一个频道         1b
 × 10 下一个频道        1b
 × 11 上一个频道        07
 × 12 返回上一个频道     21
 × 13  
× 空调 type air
 × 14 把空调打开  2a
 × 15 把空调关了  2a
 × 16 空调打的太低了 15
 × 17 空调太冷了     15
 × 18 空调温度太高了  15
 × 19 空调温度太低了  15

× 电灯 type light
 × 20 开灯  2f
 × 21 灯光太暗了 13
 × 22 灯光太亮了 13
 × 23 关灯  1f

× 电扇 type fan
 × 24 把电风扇打开 b1
 × 25 把电风扇关了 05
 × 26 电风扇换档  1d
 × 27 电风扇摇头  b2

× 天气 type weather
 × 28 今天的天气怎么样  today
 × 29 今天的天气预报    today
 × 30 现在的天气怎么样 now
 × 31 未来的天气怎么样 future

 

#依赖包
* lucene-core-3.1.0.jar 
* lucene-spellchecker-3.1.0.jar

#如何使用
* java -jar proofread.jar

#参考资料
* [lucene3.6.0的查询条件分析](http://blog.csdn.net/zhongweijian/article/details/7622693)
* [MIK_CAnalyzer分词]
* [Lucene进阶:and 和or的条件查询](http://lucene-group.group.iteye.com/group/wiki/466)

#How to Contact
####QQ:36405410
####Email:apanly@163.com

#Copying
####Free use of this software is granted under the terms of the GNU Lesser General Public License (LGPL)