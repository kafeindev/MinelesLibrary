/*
 * MIT License
 *
 * Copyright (c) 2022-2023 DreamCoins
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.mineles.library.dependency;

import me.lucko.jarrelocator.Relocation;

import java.util.ArrayList;
import java.util.List;

public enum Dependency {
    LOG4J_API(
            "org{}apache{}logging{}log4j",
            "log4j-api",
            "2.22.1"
    ),
    LOG4J_CORE(
            "org{}apache{}logging{}log4j",
            "log4j-core",
            "2.22.1"
    ),
    LOG4J_IO_STREAMS(
            "org{}apache{}logging{}log4j",
            "log4j-iostreams",
            "2.22.1"
    ),
    SLF4J_API(
            "org{}slf4j",
            "slf4j-api",
            "1.7.36"
    ),
    FASTERXML_JACKSON_ANNOTATIONS(
            "com{}fasterxml{}jackson{}core",
            "jackson-annotations",
            "2.10.3"
    ),
    FASTERXML_JACKSON_CORE(
            "com{}fasterxml{}jackson{}core",
            "jackson-core",
            "2.10.3"
    ),
    FASTERXML_JACKSON_DATABIND(
            "com{}fasterxml{}jackson{}core",
            "jackson-databind",
            "2.10.3"
    ),
    GUAVA(
            "com{}google{}guava",
            "guava",
            "30.1.1-jre"
    ),
    SNAKEYAML(
            "org{}yaml",
            "snakeyaml",
            "2.2"
    ),
    GSON(
            "com{}google{}code{}gson",
            "gson",
            "2.10.1"
    ),
    GEANTYREF(
            "io{}leangen{}geantyref",
            "geantyref",
            "1.3.15"
    ),
    CHECKER_QUAL(
            "org{}checkerframework",
            "checker-qual",
            "3.42.0"
    ),
    CONFIGURATE_CORE(
            "org{}spongepowered",
            "configurate-core",
            "4.1.2"
    ),
    CONFIGURATE_YAML(
            "org{}spongepowered",
            "configurate-yaml",
            "4.1.2"
    ),
    CONFIGURATE_GSON(
            "org{}spongepowered",
            "configurate-gson",
            "4.1.2"
    ),
    BSON(
            "org{}mongodb",
            "bson",
            "4.11.1"
    ),
    MONGODB_DRIVER_CORE(
            "org{}mongodb",
            "mongodb-driver-core",
            "4.11.1"
    ),
    MONGODB_DRIVER_SYNC(
            "org{}mongodb",
            "mongodb-driver-sync",
            "4.10.2"
    ),
    COMMONS_LANG3(
            "org{}apache{}commons",
            "commons-lang3",
            "3.12.0"
    ),
    COMMONS_COMPRESS(
            "org{}apache{}commons",
            "commons-compress",
            "1.25.0"
    ),
    COMMONS_POOL2(
            "org{}apache{}commons",
            "commons-pool2",
            "2.12.0"
    ),
    COMMONS_IO(
            "commons-io",
            "commons-io",
            "2.11.0"
    ),
    HTTPCORE5(
            "org{}apache{}httpcomponents{}core5",
            "httpcore5",
            "5.2.4"
    ),
    HTTPCORE5_H2(
            "org{}apache{}httpcomponents{}core5",
            "httpcore5-h2",
            "5.2.4"
    ),
    HTTPCLIENT5(
            "org{}apache{}httpcomponents{}client5",
            "httpclient5",
            "5.3.1"
    ),
    JEDIS(
            "redis{}clients",
            "jedis",
            "5.1.0"
    ),
    BC_PROV_JDK15ON(
            "org{}bouncycastle",
            "bcprov-jdk15on",
            "1.68"
    ),
    JNA(
            "net{}java{}dev{}jna",
            "jna",
            "5.14.0"
    ),
    REACTIVE_STREAMS(
            "org{}reactivestreams",
            "reactive-streams",
            "1.0.4"
    ),
    REACTOR_CORE(
            "io{}projectreactor",
            "reactor-core",
            "3.5.8"
    ),
    DOCKER_JAVA_API(
            "com{}github{}docker-java",
            "docker-java-api",
            "3.3.2"
    ),
    DOCKER_JAVA_TRANSPORT(
            "com{}github{}docker-java",
            "docker-java-transport",
            "3.3.2"
    ),
    DOCKER_JAVA(
            "com{}github{}docker-java",
            "docker-java-core",
            "3.3.2"
    ),
    DOCKER_JAVA_TRANSPORT_HTTPCLIENT5(
            "com{}github{}docker-java",
            "docker-java-transport-httpclient5",
            "3.3.2"
    ),
    MINEDOWN(
            "de{}themoep",
            "minedown",
            "1.7.1-SNAPSHOT",
            Repository.MINEBENCH
    ),
    XSERIES(
            "com{}github{}cryptomorin",
            "XSeries",
            "9.8.0"
    ),
    NBTAPI(
            "de{}tr7zw",
            "item-nbt-api",
            "2.12.0",
            Repository.CODEMC
    );

    private final String groupId;
    private final String artifactId;
    private final String version;
    private final Repository repository;
    private final List<Relocation> relocations;

    Dependency(String groupId, String artifactId, String version) {
        this(groupId, artifactId, version, Repository.MAVEN_CENTRAL);
    }

    Dependency(String groupId, String artifactId, String version, Repository repository) {
        this.groupId = groupId.replace("{}", ".");
        this.artifactId = artifactId;
        this.version = version;
        this.repository = repository;

        List<Relocation> relocationsList = new ArrayList<>();
        for (Relocations relocation : Relocations.values()) {
            String from = relocation.getFrom().replace("{}", ".");
            String to = relocation.getTo().replace("{}", ".");
            relocationsList.add(new Relocation(from, to));
        }
        this.relocations = relocationsList;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public String getVersion() {
        return version;
    }

    public Repository getRepository() {
        return repository;
    }

    public List<Relocation> getRelocations() {
        return relocations;
    }

    private enum Relocations {
/*        LOG4J_CORE("org{}apache{}logging{}log4j{}core", "net{}mineles{}library{}libs{}log4j{}core"),
        LOG4J_IO_STREAMS("org{}apache{}logging{}log4j{}io", "net{}mineles{}library{}libs{}log4j{}io"),
        LOG4J_API("org{}apache{}logging{}log4j", "net{}mineles{}library{}libs{}log4j{}api"),*/
        CONFIGURATE_CORE("org{}spongepowered{}configurate", "net{}mineles{}library{}libs{}configurate"),
        CONFIGURATE_YAML("org{}spongepowered{}configurate{}yaml", "net{}mineles{}library{}libs{}configurate{}yaml"),
        CONFIGURATE_GSON("org{}spongepowered{}configurate{}gson", "net{}mineles{}library{}libs{}configurate{}gson"),
        BSON("org{}bson", "net{}mineles{}library{}libs{}bson"),
        MONGODB_DRIVER_CORE("org{}mongodb", "net{}mineles{}library{}libs{}mongodb"),
        MONGODB_DRIVER_SYNC("org{}mongodb{}client", "net{}mineles{}library{}libs{}mongodb"),
        COMMONS_LANG3("org{}apache{}commons{}lang3", "net{}mineles{}library{}libs{}commons{}lang3"),
        COMMONS_COMPRESS("org{}apache{}commons{}compress", "net{}mineles{}library{}libs{}commons{}compress"),
        COMMONS_POOL2("org{}apache{}commons{}pool2", "net{}mineles{}library{}libs{}commons{}pool2"),
        COMMONS_IO("org{}apache{}commons{}io", "net{}mineles{}library{}libs{}commons{}io"),
        HTTPCLIENT5("org{}apache{}hc", "net{}mineles{}library{}libs{}hc"),
        JEDIS("redis{}clients{}jedis", "net{}mineles{}library{}libs{}jedis"),
        BC_PROV_JDK15ON("org{}bouncycastle", "net{}mineles{}library{}libs{}bouncycastle"),
        JNA("com{}sun{}jna", "net{}mineles{}library{}libs{}jna"),
        REACTIVE_STREAMS("org{}reactivestreams", "net{}mineles{}library{}libs{}reactivestreams"),
        REACTOR_CORE("reactor", "net{}mineles{}library{}libs{}reactor"),
        DOCKER_JAVA_API("com{}github{}dockerjava{}api", "net{}mineles{}library{}libs{}dockerjava{}api"),
        DOCKER_JAVA_TRANSPORT("com{}github{}dockerjava{}transport", "net{}mineles{}library{}libs{}dockerjava{}transport"),
        DOCKER_JAVA("com{}github{}dockerjava{}core", "net{}mineles{}library{}libs{}dockerjava{}core"),
        DOCKER_JAVA_TRANSPORT_HTTPCLIENT5("com{}github{}dockerjava{}httpclient5", "net{}mineles{}library{}libs{}dockerjava{}httpclient5"),
        MINEDOWN("de{}themoep{}minedown", "net{}mineles{}library{}libs{}minedown"),
        XSERIES("com{}cryptomorin{}xseries", "net{}mineles{}library{}libs{}xseries"),
        NBTAPI("de{}tr7zw", "net{}mineles{}library{}libs{}nbtapi");

        private final String from;
        private final String to;

        Relocations(String from, String to) {
            this.from = from;
            this.to = to;
        }

        public String getFrom() {
            return from;
        }

        public String getTo() {
            return to;
        }
    }
}
