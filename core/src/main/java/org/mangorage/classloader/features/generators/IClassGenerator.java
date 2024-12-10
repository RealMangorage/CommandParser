package org.mangorage.classloader.features.generators;

import java.lang.classfile.ClassFile;

@SuppressWarnings("preview")
public interface IClassGenerator {
    UnbakedClass generate(ClassFile classFile);
}
