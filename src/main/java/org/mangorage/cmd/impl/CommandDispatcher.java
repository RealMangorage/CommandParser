package org.mangorage.cmd.impl;

import org.mangorage.cmd.api.IAutoRegister;
import org.mangorage.cmd.impl.misc.Util;
import org.mangorage.cmd.api.ICommand;
import org.mangorage.cmd.api.ICommandDispatcher;
import org.mangorage.cmd.impl.context.CommandSourceStack;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

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

    @Override
    public void autoRegister() {
        if (autoRegister == null) return;
        if (registeredAuto) return;
        this.registeredAuto = true;

        Reflections reflections = new Reflections();
        reflections.getTypesAnnotatedWith(annotationClass).forEach(clz -> {
            System.out.println("Found Class -> " + clz);
        });
    }
}
