# 方块竞速/BlockRacing

## 0x00-前言
> 这是一个以收集方块为目标的，Minecraft多人竞速小游戏

## 0x10-介绍/Introduction

## 0x10-规则/Rule
> 目前有五条规则，分别是

- Give : 在一个队伍的方块目标被完成时，所有其他队伍获得一组（64个）该方块
- Racing : 所有队伍的目标方块列表完全一致
- Seize : 你可以抢夺其他队伍的目标方块，并使你的队伍获得相应积分，被抢夺的目标方块将会被替换为随机方块，
- Swift/急速模式 : 获得 抗性/速度/急迫 效果，获得损坏的鞘翅，获得 经验修补附魔书 ，获得 精准采集 铁镐
- TeamTp : 同队伍的玩家可以使用tp

<!--
## 0x20-菜单/Menu
### 0x21-选队菜单/TeamJoinMenu
- 点击火把图标添加队伍
- 右键删除队伍（当且仅当队伍为空时
### 0x22-准备菜单/PrepareMenu
- 可以选择开关规则
- 可以选择开关指定方块库
- 当所有人都准备时点
-->
## 0x20-技能/Skill
- AddBlock：为其他队伍添加目标方块
- Locate：获得定位指令权限
- RandomTp：基于当前位置，随机tp
- ReBlock：与随机一个队伍交换计分板上已显示的方块
- RePlace：与随机一个其他队伍玩家交换位置
- Roll：刷新当前方块列表
- TeamChest：购买额外的团队箱子
- TeamWayPoint：购买额外的团队传送锚点

## 0x30-指令/Command
- `/skill <skillName>`：尝试购买技能
- `/chest <chestId>`：打开团队箱子
- `/waypoint [rm|use] <wayPointId>`：删除/使用团队传送锚点
- `/locate`：定位结构或群系
- `/menu`：打开菜单
- `/team`：打开选队 菜单 
- `/tp <playerName>`：传送到队友

## 0x40-安装教程/Usage
- 准备一个Paper服务器（也可以是Spigot或Purpur）（如果不会，可以去看我的博客里的相关文章，网址lqsnow.top）
- 下载插件，将插件放到服务器目录下的`plugins`文件夹中
- （**推荐**）将`server.properties`中`spawn-protection`的值改为0（避免出生点无法破坏方块）
- （**推荐**）在`server.properties`文件中，更改如下设置：

   ```
   pvp=false
   seed=
   ```

   推荐关闭PVP，让玩家沉浸于方块收集。

   推荐将种子留空，玩完一局后将`world` `world_nether` `world_the_end`三个文件夹删除，起到重置种子的作用。

   你也可以更改服务器启动文件（start.bat）以自动重启、自动重置种子（seed留空就是随机种子）：

   ```
   :start
   java -Xmx4G -Xms4G -jar server.jar nogui
   rd /s /q world
   rd /s /q world_nether
   rd /s /q world_the_end
   timeout /nobreak /t 5
   goto start
   ```

   记得修改server.jar为你的服务器核心文件名，并按实际情况分配内存。

## 0x50-配置文件/ConfigFile

### 0x51-游戏配置/configGame
### 0x51-方块库/configBlockSet

## 0xd0-更新日志

### 2025.3.19 - BlockRacing 4.0
- 重构项目
- 添加新技能
- 添加新规则

### 2025.1.23 - BlockRacing 3.3
**感谢[@FHSHKL](https://github.com/FHSHKL): https://github.com/LQSnow/BlockRacing/pull/8**
- 更新游戏版本至1.21.4
- 添加团队箱子个数设置
- 添加团队路径点个数设置
- 添加路径点群系描述
- 修改路径点图标为脚下方块
- 对方队伍获得方块时，优先放入标号大的箱子

### 2024.10.4 - BlockRacing 3.2
**感谢[@xiaojiuwo233](https://github.com/xiaojiuwo233): https://github.com/LQSnow/BlockRacing/pull/6**
- 更新游戏版本至1.21.1
- 新增1.21新方块（为考虑平衡性，铜类只添加到轻微锈蚀，使玩家不必须寻找遗迹，在比赛时间中足够完成）
- 新增1.21 locate和语言文件（完整提取 可用）
- 移动 瓶子草 和 火把花 到 困难方块
- 极速模式将食物修改为金胡萝卜 增加 速度2 抗性2 效果
- 增加初始工具 石镐、石斧、石铲
- 游戏开始初始化新增 删除世界掉落物 重置世界天气

### 2024.4.13 - BlockRacing 3.1

- 修复了使用指令进行随机传送时不扣积分的Bug
- 游戏结束时会显示方块收集排行榜
- 添加了语言文件与配置文件的版本检查
- 添加了block指令，如果记分板的方块名显示不全，可以使用该指令查询完整方块名
- 移除了EasyBlocks中的GRASS
- 更新游戏版本至1.20.4

### 2024.1.30 - BlockRacing 3.0

- 重构了所有代码和执行逻辑
- 增加了语言文件与配置文件
- 修改了locate指令执行方式，拆分为两个指令
- 服务器开启时会自动读取配置文件，加载上一局的配置
- 不同难度方块在游戏的不同时期，生成权重将不一样，更有助于游戏推进
- 现在轮换方块改为队伍内所有**在线**玩家申请即可轮换
- 游戏开始前的准备可以取消了
- 玩家发言拥有队伍前缀
- 删除记录点需要在聊天框确认

### 2023.9.28 - BlockRacing 2.2.1

- 修复了极速模式下急迫效果等级不正确的Bug
- 修复了随机传送世界错误的Bug
- /locate指令追加1.20生物群系
- 极速模式下初始道具中鞘翅的RepairCost改为15
- 修改了部分物品描述错误
- /menu指令追加了更多功能
- 优化了部分代码和执行逻辑

### 2023.9.25 - BlockRacing 2.2

- 轮换方块次数改为3次，并且不再只能Roll到简单方块（[@BlockyDeer](https://github.com/BlockyDeer)）
- 添加极速模式，开局有额外物资和效果（[@xiaojiuwo233](https://github.com/xiaojiuwo233)）
- 更换了新的准备菜单GUI
- 游戏开始前限制世界边界防止提前探图
- 添加了指令/menu，可以通过指令打开各种菜单
- 修复了方块数量无法正常修改的Bug
- 修复了选队后退出游戏重进会被判定为旁观者的Bug
- 修复了死亡复活后药水效果丢失的Bug
- 优化了部分代码和执行逻辑
- 更新游戏版本至1.20.2

### 2023.9.18 - BlockRacing 2.1

- 玩家进入游戏将获得无限夜视效果
- 全队玩家全部申请轮换方块之后才会替换本队方块
- 添加了Debug命令（需要OP权限）
- 修复了玩家重进游戏能再次免费随机传送的Bug
- 修复了下界疣显示为Null的Bug
- 修复了部分显示错误
- 优化了部分代码和执行逻辑
- 更新游戏版本至1.19.4

## 0xe0-意见反馈

游戏反馈：https://docs.qq.com/form/page/DU0Fvc0xtUmZWRUJN

联系方式：

>  邮箱：lq_snow@outlook.com
> 
>  QQ：2784628010

## 0xf0-版权说明

该项目签署 [**GNU Affero General Public License v3.0**](https://github.com/LQSnow/BlockRacing/blob/main/LICENSE) 授权许可

The project is licensed under the [**GNU Affero General Public License v3.0**](https://github.com/LQSnow/BlockRacing/blob/main/LICENSE)
