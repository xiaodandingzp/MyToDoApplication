package com.example.myapttest

import com.example.apt_annotation.FirstAnnotation
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
import java.lang.Exception
import java.util.LinkedHashSet
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic


@AutoService(Processor::class)
class XmlProcessor : AbstractProcessor() {
    override fun init(processingEnv: ProcessingEnvironment?) {
        super.init(processingEnv)
        processingEnv?.messager?.printMessage(Diagnostic.Kind.NOTE, "zppppppppppp11")
        println("zpppppppppppppppppppppppppppppppXmlProcessor")
    }

    override fun getSupportedAnnotationTypes(): Set<String> {
        val types = LinkedHashSet<String>()
        types.add(FirstAnnotation::class.java.canonicalName)
        return types
    }

    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnv: RoundEnvironment?
    ): Boolean {
        println("zppppppppppppppppp222222222222")
        val elements = roundEnv?.getElementsAnnotatedWith(FirstAnnotation::class.java)
        try {
            elements?.let {
                for (element in it) {
                    element?.let {
                        FileSpec.builder("com.example.mytodoapplication", "HelloWorld")
                            .addType(
                                TypeSpec.classBuilder("HelloWorld")
                                    .primaryConstructor(
                                        FunSpec.constructorBuilder()
                                            .build()
                                    ).addFunction(
                                        FunSpec.builder("test")
                                            .addCode(
                                                CodeBlock.builder().apply {
                                                    addStatement("println(\"hello world\")")
                                                }.build()
                                            )
                                            .build()
                                    ).build()
                            )
                            .build()
                            .writeTo(processingEnv.filer)
                        return true
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }
}