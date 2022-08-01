package com.example.myapttest

import com.google.auto.service.AutoService
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
        println("zppppppp2222")
    }

    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnv: RoundEnvironment?
    ): Boolean {
        processingEnv.messager.printMessage(Diagnostic.Kind.NOTE, "zppppppppppp")
        return false
    }
}