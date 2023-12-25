package net.mineles.library.require;

import java.util.function.BiPredicate;

public interface Requirement<T> extends BiPredicate<T, RegisteredRequirement> {
    void apply(T t, RegisteredRequirement registeredRequirement);

    static <T> Requirement<T> of(BiPredicate<T, RegisteredRequirement> predicate, Requirement<T> requirement) {
        return new Requirement<T>() {
            @Override
            public boolean test(T t, RegisteredRequirement registeredRequirement) {
                return predicate.test(t, registeredRequirement);
            }

            @Override
            public void apply(T t, RegisteredRequirement registeredRequirement) {
                requirement.apply(t, registeredRequirement);
            }
        };
    }
}
