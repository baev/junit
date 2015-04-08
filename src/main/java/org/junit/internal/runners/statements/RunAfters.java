package org.junit.internal.runners.statements;

import java.util.ArrayList;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.MultipleFailureException;
import org.junit.runners.model.Statement;

public class RunAfters extends Statement {
    private final Statement next;

    private final Object target;

    private final List<FrameworkMethod> afters;

    private final RunNotifier notifier;

    private final Description description;

    public RunAfters(RunNotifier notifier, Description description, Statement next, List<FrameworkMethod> afters, Object target) {
        this.next = next;
        this.afters = afters;
        this.target = target;
        this.notifier = notifier;
        this.description = description;
    }

    @Override
    public void evaluate() throws Throwable {
        List<Throwable> errors = new ArrayList<Throwable>();
        try {
            next.evaluate();
        } catch (Throwable e) {
            errors.add(e);
        } finally {
            for (FrameworkMethod each : afters) {
                notifier.fireBeforeFrameworkMethodCalled(each, description);
                try {
                    each.invokeExplosively(target);
                } catch (Throwable e) {
                    errors.add(e);
                } finally {
                    notifier.fireAfterFrameworkMethodCalled(each, description);
                }
            }
        }
        MultipleFailureException.assertEmpty(errors);
    }
}