package net.mineles.library.docker.container;

import com.google.common.collect.Lists;

import java.util.List;

public final class ContainerTemplateCollection {
    private final List<ContainerTemplate> templates;

    public ContainerTemplateCollection() {
        this.templates = Lists.newArrayList();
    }

    public ContainerTemplateCollection(List<ContainerTemplate> templates) {
        this.templates = templates;
    }

    public List<ContainerTemplate> getTemplates() {
        return this.templates;
    }

    public ContainerTemplate getTemplate(String name) {
        for (ContainerTemplate template : this.templates) {
            if (template.name().equalsIgnoreCase(name)) {
                return template;
            }
        }
        return null;
    }

    public ContainerTemplate getTemplateByImage(String image) {
        for (ContainerTemplate template : this.templates) {
            if (template.image().getFullName().equals(image)) {
                return template;
            }
        }
        return null;
    }

    public void register(ContainerTemplate template) {
        this.templates.add(template);
    }

    public void unregister(ContainerTemplate template) {
        this.templates.remove(template);
    }
}
