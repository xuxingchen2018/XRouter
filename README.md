# Android XRouter
利用APT实现的一个轻量级路由框架，注意本库基于Java1.8，所以需要配置compileOptions

### 功能实现：

1.  自动生成跳转代码
2.  自动生成设置数据的set方法，支持8中基本数据类型外加String，Serializable，Parcelable，并且支持大型数据的跳转
3.  一行代码实现数据注入和释放
4.  一键生成路由表，路由过期提醒
5.  自定义路由表生成路径

### 使用

1.  创建一个用于管理路由的库（routers）
2.  根目录下的build.gradle文件中添加classpath，并且在routers库的build.gradle文件中添加插件
    ```
    classpath "com.mrcd:xrouter-gradle-plugin:1.0.12"
    
    apply plugin: 'com.mrcd.xrouter.engine'
    ```
3.  在routers库的build.gradle文件中添加依赖 
    ```
    api "com.mrcd:xrouter-api:${VERSION_NAME}"
    ```
4.  将在需要使用路由功能的库中添加routers库的依赖，如果需要注解可另外添加依赖 
    ```
        implementation "com.mrcd:xrouter-annotation:${VERSION_NAME}"
        annotationProcessor "com.mrcd:xrouter-compiler:${VERSION_NAME}"
    ```
    注意，在添加了annotationProcessor注解处理后，请务必在build.gradle文件中的defaultConfig节点下添加如下配置
    
    ```
        javaCompileOptions {
                    annotationProcessorOptions {
                        arguments = [MODULE_NAME: project.getName()]
                    }
                }
    ```

    如果有乱码问题，请在android节点下添加如下配置

    ```
        compileOptions {
                encoding = "UTF-8"
                sourceCompatibility = '1.8'
                targetCompatibility = '1.8'
            }
    ```
5.  点击Build -> MakeProject 构建生成路由配置文件
6.  运行gradlew
    :routers:makeRouters或者在AS右侧gradle任务导航栏中找到对应的task双击运行即可
7.  task运行完成后，在routers目录下会多出com.mrcd.xrouter.routers目录，在目录下即都是生成的路由表

### 已知问题    

1.  在library下，如果只有一个Activity，将Activity的注解移除后，路由配置文件无法及时更新，需手动将文件内容清空

### 混淆Proguard

1.  如下配置
    ```
    -keepclasseswithmembernames class * {
        @com.mrcd.xrouter.annotation.XParam <fields>;
    }
    ```  

    