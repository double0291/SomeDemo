#Android的数据库操作#
通过反射的方式，将类中的变量映射为数据库中的字段，实现对数据类的数据库操作

关于多DBHelper操作的问题，网上很多资料说不要搞多个DBHelper同时操作，会抛异常，但是，我试了一下，很正常。
相关的资料涉及：
[android-sqlite-locking](http://touchlabblog.tumblr.com/post/24474398246/android-sqlite-locking)
[single-sqlite-connection](http://touchlabblog.tumblr.com/post/24474750219/single-sqlite-connection)
此人写的github示例：
[Android-Database-Locking-Collisions-Example](https://github.com/touchlab/Android-Database-Locking-Collisions-Example)

stackoverflow上的讨论，此人的答案也是第一位的：
[what-are-the-best-practices-for-sqlite-on-android](http://stackoverflow.com/questions/2493331/what-are-the-best-practices-for-sqlite-on-android/2493839#2493839)

但是，仔细看此人的blog，都是几年前的，而且里面提到，他出现问题的机型是Android2.1，所以考虑，是不是早期Android存在这个问题，后来被修复了。

类似资料：
[concurrent-database](http://dmytrodanylyk.com/pages/blog/concurrent-database.html)

王汪提供了一个SQLite3的crash问题列表文章[How To Corrupt An SQLite Database File](https://www.sqlite.org/howtocorrupt.html)，，里面写到:

>###Linux Threads###
>*Some older versions of Linux used the LinuxThreads library for thread support. LinuxThreads is similar to Pthreads, but is subtly different with respect to handling of POSIX advisory locks. SQLite versions 2.2.3 through 3.6.23 recognized that LinuxThreads where being used at runtime and took appropriate action to work around the non-standard behavior of LinuxThreads. But most modern Linux implementations make use of the newer, and correct, NPTL implementation of Pthreads. Beginning with SQLite version 3.7.0, the use of NPTL is assumed. No checks are made. Hence, recent versions of SQLite will subtly malfunction and may corrupt database files if used in multi-threaded application that run on older linux systems that make use of LinuxThreads.*

**所以，暂时得出的结论是，较新的Android版本，多线程多DBHelper实例操作数据库不会出现问题，毕竟自己写的demo跑的很正常。**
