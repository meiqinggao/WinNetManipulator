## WinNetManipulator - 一款Windows平台下基于Java的网络数据操纵工具

[英文](https://github.com/meiqinggao/WinNetManipulator/blob/master/Readme.md)

这个工具能够在Windows平台下让你随心所欲的操纵网络数据，能够网络数据内容，修改原地址、目的地址、源端口、目的端口。可以在你收到数据后，或者要发出数据前进行修改。

这是一款基于SpringBoot对WinDivert C++ dll进行的封装工具。我在WinNetApplition里面放了一些Demo。要使用WinDivert复杂功能，请参考WinDivert API。

为了让程序能够跑起来，你需要修改里面的SRC_IP和DEST_IP。你也可能需要修改你想要过滤筛选的字段，方便抓取数据。REPLACEMENT则是你想要进行替换进去的字符。这里面最重要的则是changed_size,因为每次替换进去的字符和被替换的字符长度是不一样的，为了保持tcp连接，你需要根据这个changed_size来修改sequence和acknoledgement number。这非常非常重要，也是非常非常容易出错的。

这是我的个人项目，我基于此做了一些很有意思的事情。欢迎交流。

Furture enhancement: 
 - 升级到WinDivert version 2.2.0，接口不太一样
 - 代码里面的几个TO-DO 
 - 增加网页配置功能，能够用网页就能够配置，你需要filter，修改的网络数据，原地址，目的地址的，方便使用
 - 封装成SpringBoot的一个Starter, 方便引入Jar，集成到大项目里，配置一下，直接跑起来。 