<div align="center"><img width="200" src="https://images.gitee.com/uploads/images/2021/0515/223435_47e656ad_7864521.png"/>
<h1> PuFFer </h1>
<div>
<img src="https://img.shields.io/badge/Jdk-1.8-green.svg?style=flat-square">
<img src="https://img.shields.io/badge/License-MIT-orange?style=flat-square">
</div>
<small>Created by Fxtack</small>
</div>


---------------

## 📌 项目简介

  PuFFer 是一个使用 Java 编写的项目。

  该项目通过 Javafx 编写界面，实现了对键值对数据文件的编辑修改与保存的功能。

  该项目是一个实践练习项目，由作者 Fxtack 一个人完成。

  PuFFer 遵守 [MIT 协议](https://gitee.com/Fxtack/PuFFer/blob/master/LICENSE)

---------------

## 📌 项目内容

1. 基础的 Javafx 界面编写。通过 Scene Builder（或 IDEA）编辑 .fxml 文件编写界面；为 .fxml 界面或自定义组件编写 controller。（Javafx 的 MVC 框架实践）
2. Javafx 实用组件的使用。例如：文件选择器（FileChooser）、警告对话框（Alert）、菜单（Menu）、分支表（TreeTableView），等。
3. 使用警告对话框自定义异常报告对话框，并将异常保存至异常日志。
4. 使用 Java8 特性 Lambda 表达式进行事件注册；自定义函数式接口实现函数式编程。
5. 使用依赖注入、单例模式、工厂模式、享元模式、建造者模式，来简化开发。
6. 对象流（ObjectInputStream、ObjectOutputStream）来实现序列化与反序列化。
7. 状态对象链算法实现撤销与重做。

------------

## 📌 改进方向与问题

1. 该项目并非是框架化的。这里的非框架化不单单是指该项目没有使用像 spring 这样的框架，更指因为在设计之初的一些考量存在缺陷（项目开始设计时，仅仅是为了实现编辑与保存键值对数据的业务功能，较少考虑可能还会新增功能模块），使得项目内部分模块耦合度较高，扩展性较差，无法达到像框架一样的拥有极高扩展性。

2. 尚未进行高压性能测试，高压情况下性能表现未知。

3. 函数式编程的部分代码理解起来稍有困难。

4. 部分代码冗余。

**虽然存在以上缺陷与问题，但作者会在后续进行修改与维护**。

--------------------

## 📌 项目动机

1. 该项目是 Javafx 很好的实践。若在学习过 Javafx 开发知识后，该项目具有一定的指导意义与教学意义。
2. 该项目中对部分设计模式进行了实践，体现了设计模式在实际设计中带来的便利。
3. 该项目同时是作者学习如何管理开源项目，如何维护开源项目，如何使用 Git 与远程 Git 仓库平台的一次重要实践。
4. 作者希望通过为该项目的其他开发者撰写方便学习与使用的文档与指南，增强自身的文档能力。

---------------------------

## 📌 运行展示



![输入图片说明](https://images.gitee.com/uploads/images/2021/0515/222546_cf0a8722_7864521.png "Y%NR0S`F5`Z1EEB[Q%LUV4W.png")
![输入图片说明](https://images.gitee.com/uploads/images/2021/0515/225439_a14ee41d_7864521.png "IGCPLPU@7N5X)_1OZ4H664W.png")
![输入图片说明](https://images.gitee.com/uploads/images/2021/0515/225620_691d3044_7864521.png "$U$I)N1I[VU`1J{ZPV7K6[O.png")
![输入图片说明](https://images.gitee.com/uploads/images/2021/0515/225500_8fc013e7_7864521.png "124$OCXW{RT$FU)9CPIM793.png")
![输入图片说明](https://images.gitee.com/uploads/images/2021/0515/225517_2c9d45d8_7864521.png "76OH4PH{4MUDNJM7BURXJ3Y.png")
![输入图片说明](https://images.gitee.com/uploads/images/2021/0515/225532_001469db_7864521.png "P{`KPP7W~($(_CN1``6OG7T.png")

---------

如有问题可通过作者邮箱沟通交流：1244875112@qq.com