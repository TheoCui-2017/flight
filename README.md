OTA 功能列表
===========

#DONE

- 航班查询（实时查询和定时查询）： 实现简单来源和分页筛选

- 汇率管理（列表和添加页面）：实现实时查询汇率功能

- 航段管理（列表和添加页面）

- 任务管理（列表非动态添加）：实现航段添加、并发数、抓取间隔和抓取天数设置等功能。

#TODO

- 需实现城市代码和机场代码管理（后台功能，用于航班查询功能）

- 抓取任务状态显示页面

- 航段查询：需实现航空公司、中转城市、分页等ajax点击查询事件

- 任务管理：需实现指定采集服务器IP、采集开始时间和采集结束时间设置

功能说明
===========

#汇率管理

- 实时查询汇率：该功能为了帮助管理员进行添加或修改货币汇率时的一个辅助功能

- 目前货币汇率使用的是nowapi.com的免费接口，每小时接口调用限制为50次

- 后台设置的汇率列表中的货币，每天早上7点进行全部更新

#任务管理

- OTA列表：该下拉框表示当前可以设置的列表，点击该列表中的一个，则调整到该OTA的设置界面

- 任务激活功能：用于关闭或开启该OTA采集的开关，即选中代表开启，未选中代表关闭

- 并发数：该并发数代表每个航段抓取的并发送设置， 默认并发数为5

- 抓取间隔：单位为小时，默认抓取时间为12小时，后台使用crontab表达式，即 0 0 0/12 * * ？

- 抓取天数： 每个航段抓取未来的天数（不包含当天），默认天数为7天

- 添加／删除航段： 左侧框为后台设置的全部航段，左侧为该OTA已添加的航段（已城市代码为单位）

- 添加和删除按钮： 使用shift可以选择多个航段进行添加和删除，点击按钮之后会立即添加或删除航段任务

- 提交设置按钮： 只是保存并发数、抓取间隔和抓取天数字段

#提交ISSUE和功能

- 打开https://code.csdn.net/theo789/ota/issues页面，点击新开issue即可

