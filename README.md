# Android XRouter
![](https://img.shields.io/bintray/v/sevennight2012/maven/xrouter-api)    

1.  利用APT实现的一个轻量级路由框架，注意本库基于Java1.8，所以需要配置compileOptions
2.  配合[AndroidStudio插件](https://github.com/SevenNight2012/XRouter-Navigator)使用更方便(使用插件需要从1.2.2版本开始)

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
    classpath "com.mrcd:xrouter-gradle-plugin:1.2.2"
    
    apply plugin: 'com.mrcd.xrouter.engine'
    ```
3.  在app目录下的build.gradle文件中添加自动绑定数据的插件，如下所示
    ```
    apply plugin: 'com.android.application'
    apply plugin: "com.mrcd.xrouter.auto"
    ```
    如果不想使用插件，开发者也可以在activity中手动调用绑定数据和释放数据的方法，如下所示
    ```java
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
4.  在routers库的build.gradle文件中添加依赖，自1.2.2版本开始，需要添加annotation库的依赖支持，主要用于辅助AS的插件使用
    ```
    api "com.mrcd:xrouter-api:${API_VERSION_NAME}"
    implementation "com.mrcd:xrouter-annotation:${ANNOTATION_VERSION_NAME}"
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
                arguments = [MODULE_PATH: project.projectDir.absolutePath]
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
6.  运行gradlew
    :routers:make-Debug-routers或者在AS右侧gradle任务导航栏中找到对应的task双击运行即可
7.  task运行完成后，在routers目录下会多出com.mrcd.xrouter.routers目录，在目录下即都是生成的路由表
8.  如果不做配置，插件会自动生成两个task，分别对应make-Debug-routers和make-Release-routers，
    开发者可通过XRouterConfig配置，目前config支持属性有：excludeProject，buildTypes，routerPath，appModule，
    在一个工程有多个application项目时，可以通过appModule强制指定router库对应的app工程，详情可查看源码    
    
### XRouterConfig配置详情    

1.  对应Bean为：com.mrcd.xrouter.gradle.plugin.configs.DevelopConfig
2.  excludeProject：排除某些library，XRouter不会扫描对应目录下缓存的路由JSon，也不会生成对应的Router类
3.  buildTypes：构建类型，默认情况下为Debug与Release，若自定义了BuildType或者productFlavors，且有代码差异，需构建对应type的task，且此处的buildTypes，首字母需大写，
    如：build.gradle中自定义了dev的productFlavors，那么buildType中需要增加DevDebug,DevRelease，
    这样在对应routers库的gradle任务中就会看到make-DevDebug-routers，和make-DevRelease-routers
4.  routerPath：路由表的输出路径，即所有生成的XXXActivityRouter类的输出路径
5.  supportAndroidX：自1.2.1.1已被废弃，是否支持AndroidX，若项目中已经使用AndroidX，则设置为true，此处只影响fragment中activity的启动
6.  appModule：根目录下application工程的工程名，如果根目录下只有一个application可以忽略，XRouter会自动扫描，
如果有多个application，可以强制指定对应的application工程名，配合excludeProject使用，生成对应application的路由
7.  customRouterLauncher：自定义的路由启动类的类名，即“XRouter”，默认情况下会生成com.mrcd.xrouter.XRouter类用于统一调用所有的Router类，
如果开发者需要自定义类名，或用于区分项目中已有的类名等，可以在此处配置，如“AppRouter”，那么调用方法如下：
    ```
    AppRouter.getInstance().mainActivity().launch(context);
    ```
### 注意       
  
1.  XRouter在处理Activity继承情况下，要求父类的数据访问权限设置为public，否则无法绑定父类的数据
    所以建议在声明数据变量时都设置为public，以避免不必要的异常情况
2.  XRouter在编译期会将所有处理过的路由信息按照一定格式输出，开发者可以自行查看  
3.  自1.2.1.1版本开始，对AndroidX的支持通过对artifactId做区分，如
    ```
    implementation "com.mrcd:xrouter-gradle-plugin-x:1.2.1.1"
    ```
4.  如果需要模块化单独运行某个工程中的module，该module下一定要配置applicationId，
    XRouter的auto插件内部会自动读取项目的defaultConfig.applicationId，否则将会报错

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

    
