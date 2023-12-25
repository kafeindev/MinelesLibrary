package net.mineles.library.require;

import com.google.common.collect.Maps;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public final class RequirementCollection {
    private static final RequirementCollection DEFAULTS;

    static {
        DEFAULTS = RequirementCollection.of(Maps.newHashMap());
    }

    private final @NotNull Map<String, Requirement<?>> requirements;

    public RequirementCollection() {
        this(DEFAULTS);
    }

    public RequirementCollection(@NotNull RequirementCollection collection) {
        this(collection.getRequirements());
    }

    public RequirementCollection(@NotNull Map<String, Requirement<?>> requirements) {
        this.requirements = requirements;
    }

    @NotNull
    public static RequirementCollection of() {
        return new RequirementCollection();
    }

    @NotNull
    public static RequirementCollection of(@NotNull RequirementCollection collection) {
        return new RequirementCollection(collection);
    }

    @NotNull
    public static RequirementCollection of(@NotNull Map<String, Requirement<?>> requirements) {
        return new RequirementCollection(requirements);
    }

    public @NotNull Map<String, Requirement<?>> getRequirements() {
        return this.requirements;
    }

    public @NotNull Set<String> getRequirementNames() {
        return this.requirements.keySet();
    }

    public int size() {
        return this.requirements.size();
    }

    public boolean isRegistered(@NotNull String key) {
        return this.requirements.containsKey(key);
    }

    public boolean isRegistered(@NotNull RegisteredRequirement requirement) {
        return this.isRegistered(requirement.getKey());
    }

    public @NotNull Optional<Requirement<?>> findRequirement(@NotNull String key) {
        return Optional.ofNullable(this.requirements.get(key));
    }

    public @NotNull Optional<Requirement<?>> findRequirement(@NotNull RegisteredRequirement requirement) {
        return this.findRequirement(requirement.getKey());
    }

    public @NotNull RequirementCollection register(@NotNull String key, @NotNull Requirement<?> requirement) {
        this.requirements.put(key, requirement);
        return this;
    }

    public @NotNull RequirementCollection unregister(@NotNull String key) {
        this.requirements.remove(key);
        return this;
    }

    public @NotNull RequirementCollection unregister(@NotNull RegisteredRequirement requirement) {
        return this.unregister(requirement.getKey());
    }

    public @NotNull RequirementCollection unregisterAll() {
        this.requirements.clear();
        return this;
    }

    public boolean isEmpty() {
        return this.requirements.isEmpty();
    }

    public @NotNull RequirementCollection copy() {
        return new RequirementCollection(this);
    }

    @NotNull
    public static RequirementCollection defaults() {
        return DEFAULTS;
    }
}
