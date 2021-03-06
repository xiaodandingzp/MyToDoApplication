package com.example.myapttest

import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asTypeName
import javax.lang.model.element.Element
import javax.lang.model.type.MirroredTypeException
import javax.lang.model.type.TypeMirror
import kotlin.reflect.KClass


//* APT编译期调试
/**
 * https://blog.csdn.net/jonch_hzc/article/details/78796592
 * https://medium.com/@cafonsomota/debug-annotation-processor-in-kotlin-6eb462e965f8
 * 在gradle.properties里添加kapt.use.worker.api=true
 * 1.0，run -> Edit Configurations
 * 1.1 Go to Edit Configurations… (Run/ Debug Configurations)
 * 1.2 Click on the plus sign (“+”)
 * 1.3 Select Remote
 * 1.4 You should have a new configuration added that’s similar to this one:
 * 2.1 选择一个task,比如compileDebugJavaWithJavac(Gradle->Tasks->other->比如compileDebugJavaWithJavac)
 * 2.1 选择compileDebugJavaWithJavac，右键选择create‘AptDemo....'
 * 2.2 vm Option  -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005
 * 3.1 运行AptDemo[compileDebugJavaWithJavac],会等待调试
 * 3.2 选择步骤一新添加的Remote, 点击调试按钮，开始调试
 **/

//安卓 javax的包都引用不了
/**
 * 参考文档：https://blog.csdn.net/u011435933/article/details/102953975
 * 问题原因：
 * android的sdk中有javax，就是说javax包默认去取android中的javax，所以无法加载java中javax。
 * //该配置会导入<Android API 29 Platform>的相关代码，其中包含javax包，会优先有这个
 * android {
 * compileSdkVersion 29
 * buildToolsVersion "29.0.3"
 *
 * defaultConfig {
 * applicationId "com.example.apttest_compiler"
 * minSdkVersion 26
 * targetSdkVersion 29
 * versionCode 1
 * versionName "1.0"
 * testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
 * }
 * buildTypes {
 * release {
 * minifyEnabled false
 * proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
 * }
 * }
 * }
 *
 * 解决：
 * 方法一
 * //若配置了compileSdkVersion,则另外添加对java的依赖
 * provided files ('C:\\Program Files\\Java\\jdk1.8.0_201\\jre\\lib/rt.jar')
 * compileOnly files ('C:\\Program Files\\Java\\jdk1.8.0_201\\jre\\lib/rt.jar')
 * 方法二
 * //若创建的module是Java or Kotlin Library, 则不会有compileSdkVersion的配置。则可以直接找到java下的javax包。
 **/


/**
 * 获取注解中类型为class的属性
 * element: 通过注解查找到的Element
 * cla: 注解类  如：MultiModuleKindsItem::class.java
 **/
private fun getAnnotationValueTypeName(element: Element, cla: Class<Annotation>): TypeName? {
    var result: TypeName? = null
    val annotation = element.getAnnotation(cla) as Test
    try {
        annotation.parent
    } catch (mte: MirroredTypeException) {
        result = mte.typeMirror.asTypeName()
    }
    return result
}

/**
 * 获取注解中类型为class的属性
 * element: 通过注解查找到的Element
 * cla: 注解类  如：MultiModuleKindsItem::class.java
 * key: 要获取属性的key
 **/
private fun getAnnotationValueTypeName(element: Element, cla: Class<*>, key: String):
        TypeName? {
    var result: TypeMirror? = null
    val mirrors = element.annotationMirrors
    for (mirror in mirrors) {
        if (mirror.annotationType.toString() == cla.name) {
            for ((k, v) in mirror.elementValues) {
                if (k.simpleName.toString() == key) {
                    result = v.value as TypeMirror
                    return result.asTypeName()
                }
            }
        }
    }
    return result?.asTypeName()
}

internal annotation class Test(val parent: KClass<*> = Test::class)


//apt基本配置
/**
 *  参考文档： https://juejin.cn/post/6947992544252788767
 * 配置后没有执行注解处理器的代码 参考文档：https://github.com/liujingxing/rxhttp/wiki/ksp%E3%80%81kapt%E3%80%81annotationProcessor-%E7%94%A8%E6%B3%95%E5%8F%8A%E5%8C%BA%E5%88%AB
 *
 * annotationProcessor仅会检索Java代码，且没有用到注解时，不会工作；生成的代码在build/generated/ap_generated_sources目录下

 * kapt是Kotlin时代的产物，会检索kotlin/java代码，但kotlin的一些专有属性拿不到，如const关键字、伴生对象等在代码层面无法判断，同样的，没有用到注解时，不会工作；生成的代码在build/generated/source/kapt目录下

 * ksp则是最新的注解处理器，编译速度号称是kapt的2倍有余，且完全弥补了kapt/annotationProcessor的缺陷，但纯Java项目用不了，必需要集成kotlin；生成的代码在build/generated/ksp目录下
 **/

//AbstractProcessor的init方法有执行 但process()方法没有执行
/**
 * 重写getSupportedAnnotationTypes方法，因为没有监测到这个方法里添加的注解，不会执行process()
 **/