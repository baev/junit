package org.junit.internal.runners.statements;

import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

public class RunBefores extends Statement {
    private final Statement next;

    private final Object target;

    private final List<FrameworkMethod> befores;

    private final RunNotifier notifier;

    private final Description description;

    public RunBefores(RunNotifier notifier, Description description, Statement next, List<FrameworkMethod> befores, Object target) {
        this.next = next;
        this.befores = befores;
        this.target = target;
        this.notifier = notifier;
        this.description = description;
    }

    @Override
    public void evaluate() throws Throwable {
        for (FrameworkMethod before : befores) {
            notifier.fireBeforeFrameworkMethodCalled(before, description);
            try {
                before.invokeExplosively(target);
            } finally {
                notifier.fireAfterFrameworkMethodCalled(before, description);
            }
        }
        next.evaluate();
    }
}