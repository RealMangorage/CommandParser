package org.mangorage.cmd.impl;

import org.mangorage.cmd.api.IAutoRegister;
import org.mangorage.cmd.api.ICommandRegistrar;
import org.mangorage.cmd.impl.misc.Util;
import org.mangorage.cmd.api.ICommand;
import org.mangorage.cmd.api.ICommandDispatcher;
import org.mangorage.cmd.impl.context.CommandSourceStack;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

public final class CommandDispatcher<S> implements ICommandDispatcher<S> {
    public static <S> CommandDispatcher<S> create(Class<S> contextClass) {
        return new CommandDispatcher<>();
    }

    private final Map<String, ICommand<S>> commandMap = new HashMap<>();

    private IAutoRegister<? extends Annotation, S> autoRegister;
    private Class<? extends Annotation> annotationClass;
    private boolean registeredAuto = false;

    private CommandDispatcher() {}

    @Override
    public void register(ICommand<S> command) {
        commandMap.put(command.getId(), command);
    }

    @Override
    public <T extends Annotation> void setAutoRegistration(Class<T> annotationClass, IAutoRegister<T, S> autoRegistration) {
        this.autoRegister = autoRegistration;
        this.annotationClass = annotationClass;
    }

    @Override
    public int execute(S context, String... args) {
        if (args.length >= 1) {
            var cmd = commandMap.get(args[0]);
            if (cmd != null)
                return cmd.execute(
                        CommandSourceStack.of(
                                context,
                                Util.shrinkArray(args)
                        )
                );
        }
        return -1;
    }

    @SuppressWarnings("all")
    @Override
    public <T extends Annotation> void autoRegister(Class<T> annotationClazz) {
        if (autoRegister == null) return;
        if (this.annotationClass != annotationClazz) return;
        if (registeredAuto) return;
        this.registeredAuto = true;

        Reflections reflections = new Reflections(
                new ConfigurationBuilder()
                        .setUrls(ClasspathHelper.forJavaClassPath())

        );

        reflections.getTypesAnnotatedWith(annotationClass).forEach(clz -> {
            System.out.println("Attempting to register Class '%s' to the Dispatcher".formatted(clz));
            try {
                ICommandRegistrar<ICommand<S>> registrar = (ICommandRegistrar<ICommand<S>>) clz.newInstance();
                IAutoRegister<T, S> autoRegisterCasted = (IAutoRegister<T, S>) autoRegister;
                Optional<ICommand<S>> optionalSICommand = autoRegisterCasted.register(clz.getAnnotation(annotationClazz), registrar);
                optionalSICommand.ifPresentOrElse(c -> {
                    System.out.println("Successfully registered command '%s'".formatted(c.getId()));
                    register(c);
                }, () -> {
                    System.out.println("Failed to register Class '%s' to the Dispatcher".formatted(clz));
                });
            } catch (Throwable throwable) {
                System.out.println("Failed to register Class '%s' to the Dispatcher".formatted(clz));
            }
        });
    }
}
