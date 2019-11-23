# Android XRouter
![](https://img.shields.io/bintray/v/sevennight2012/maven/xrouter-api)    

利用APT实现的一个轻量级路由框架，注意本库基于Java1.8，所以需要配置compileOptions

### 功能实现：

1.  自动生成跳转代码
2.  自动生成设置数据的set方法，支持8中基本数据类型外加String，Serializable，Parcelable，并且支持大型数据的跳转
3.  自动注入数据，自动释放数据
4.  一键生成路由表，路由过期提醒
5.  自定义路由表生成路径

### 使用

1.  创建一个用于管理路由的库（routers）
2.  根目录下的build.gradle文件中添加classpath，并且在routers库的build.gradle文件中添加插件
    ```
    classpath "com.mrcd:xrouter-gradle-plugin:1.1.3"
    
    apply plugin: 'com.mrcd.xrouter.engine'
    ```
3.  在app目录下的build.gradle文件中添加自动绑定数据的插件，如下所示
    ```
    apply plugin: 'com.android.application'
    apply plugin: "com.mrcd.xrouter.auto"
    ```
    如果不想使用插件，开发者也可以在activity中手动调用绑定数据和释放数据的方法，如下所示
    ```
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        IntentWrapper.bindData(this);
        super.onCreate(savedInstanceState);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        IntentWrapper.release(this);
    }
    ```
4.  在routers库的build.gradle文件中添加依赖 
    ```
    api "com.mrcd:xrouter-api:${API_VERSION_NAME}"
    ```
5.  将在需要使用路由功能的库中添加routers库的依赖，如果需要注解可另外添加依赖 
    ```
        implementation "com.mrcd:xrouter-annotation:${ANNOTATION_VERSION_NAME}"
        annotationProcessor "com.mrcd:xrouter-compiler:${COMPILER_VERSION_NAME}"
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
6.  点击Build -> MakeProject 构建生成路由配置文件
7.  运行gradlew
    :routers:make-Debug-routers或者在AS右侧gradle任务导航栏中找到对应的task双击运行即可
8.  task运行完成后，在routers目录下会多出com.mrcd.xrouter.routers目录，在目录下即都是生成的路由表
9.  如果不做配置，插件会自动生成两个task，分别对应make-Debug-routers和make-Release-routers，
    开发者可通过XRouterConfig配置，目前config支持属性有：excludeProject，buildTypes，routerPath，详情可查看源码

### 注意       
  
1.  XRouter在处理Activity继承情况下，要求父类的数据访问权限设置为public，否则无法绑定父类的数据
    所以建议在声明数据变量时都设置为public，以避免不必要的异常情况
2.  XRouter在编译期会将所有处理过的路由信息按照一定格式输出，开发者可以自行查看    

### 混淆Proguard

1.  如下配置
    ```
    -keep class com.mrcd.xrouter.annotation.** { *; }
    -keepclasseswithmembernames class * {
        @com.mrcd.xrouter.annotation.XParam <fields>;
    }
    -keepclasseswithmembernames class * {
        @com.mrcd.xrouter.annotation.Parcelable <fields>;
    }
    -keepclasseswithmembernames class * {
        @com.mrcd.xrouter.annotation.Serializable <fields>;
    }
    -keep class **$$DataBinder { *; }
    ```  

    