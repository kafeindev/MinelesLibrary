package net.mineles.library.dependency;

public enum Repository {
    MAVEN_CENTRAL("https://repo1.maven.org/maven2/%s/%s/%s/%s-%s.jar"),
    SONATYPE("https://oss.sonatype.org/content/repositories/releases/%s/%s/%s/%s-%s.jar"),
    CODEMC("https://repo.codemc.io/repository/maven-public/%s/%s/%s/%s-%s.jar"),
    MINEBENCH("https://repo.minebench.de/%s/%s/%s/%s-%s.jar");

    private final String url;

    Repository(String url) {
        this.url = url;
    }

    public String getUrl() { return url; }

    public String format(Dependency dependency) {
        return format(dependency.getGroupId(), dependency.getArtifactId(), dependency.getVersion());
    }

    public String format(String groupId, String artifactId, String version) {
        String groupPath = groupId.replace(".", "/");
        return String.format(this.url, groupPath, artifactId, version, artifactId, version);
    }
}
