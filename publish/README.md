### 开源仓库发布工具使用
#### 使用方法一
1. 将publish目录拷贝到项目根目录下
2. 在根目录下的build.gradle文件buildscript->dependencies节点下添加classpath
   ```
   classpath 'com.github.dcendents:android-maven-gradle-plugin:2.1'
   classpath 'com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.4'
   ```
2. 在需要发布的工程目录下的build.gradle文件的最后一行添加    
    ```
        apply from: rootProject.file("publish/PublishConfig.gradle")
    ```    
3. 在需要发布的工程目录下添加gradle.properties文件，配置内容参考 [示例](gradle.properties.simple)
4. 如需发布，选择AS右侧的gradle导航栏，找到要发布的工程，点开tasks，先运行other下的install任务，
然后如果配置的platform=jcenter，那么选择publishing下的bintrayUpload任务，双击即可，等待运行完成，
如果配置的platform=maven，那么选择upload下的uploadArchives任务，双击即可，不配置platform字段默认为jcenter
5. 拷贝完Publish目录后，要求配置下环境变量，需要将bintray_user，bintray_apiKey等变量配置下，否则运行报错，
一下为所有环境变量：
    ```
    bintray_user                    账号
    bintray_apiKey                  apiKey
    NEXUS_USERNAME                  maven仓库中的用户名
    NEXUS_PASSWORD                  maven仓库中的用户密码
    MAVEN_REMOTE_URL                maven仓库地址(release)
    MAVEN_REMOTE_SNAPSHOT_URL       maven仓库地址(snapshot)
    MAVEN_LOCAL_URL                 maven的本地地址(release)
    MAVEN_LOCAL_SNAPSHOT_URL        maven的本地地址(snapshot)
    ```    
#### 使用方法二
1. 在需要发布的工程目录下的build.gradle文件最后一行添加如下配置    
    ```
    apply from: "https://raw.githubusercontent.com/SevenNight2012/MyScript/master/publish/PublishConfig.gradle"
    ```    
    ***以http这种远程方式apply以后，publish目录下的所有文件可以不用拷贝，内部将全部以远程依赖的方式进行依赖***    
    其余properties与环境变量的配置与方法一相同，不可省略。    
    **极简优化**：如果觉得apply的URL太长的话，可以将连接 [转成短连接](https://tool.chinaz.com/tools/dwz.aspx)，
    注意转成短连接后，将不容易理解，使用需自行斟酌。
