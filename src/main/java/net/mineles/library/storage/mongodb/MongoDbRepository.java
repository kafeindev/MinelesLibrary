package net.mineles.library.storage.mongodb;

import com.google.common.collect.Maps;
import net.mineles.library.libs.bson.Document;
import net.mineles.library.libs.bson.conversions.Bson;
import net.mineles.library.libs.mongodb.client.MongoClient;
import net.mineles.library.libs.mongodb.client.MongoCollection;
import net.mineles.library.libs.mongodb.client.MongoDatabase;
import net.mineles.library.libs.mongodb.client.model.Filters;
import net.mineles.library.libs.mongodb.client.model.ReplaceOptions;
import net.mineles.library.serializer.Serializer;
import net.mineles.library.storage.Repository;
import net.mineles.library.storage.RepositoryProperties;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class MongoDbRepository<T> implements Repository<T> {
    protected final RepositoryProperties<T> properties;
    protected final MongoDatabase database;
    protected final String collectionName;
    protected final Serializer<T, Document> serializer;

    protected MongoDbRepository(RepositoryProperties<T> properties, MongoDatabase database, String collectionName, Serializer<T, Document> serializer) {
        this.properties = properties;
        this.database = checkNotNull(database, "database");
        this.collectionName = collectionName;
        this.serializer = serializer;
    }

    protected MongoDbRepository(RepositoryProperties<T> properties, MongoClient client, String databaseName, String collectionName, Serializer<T, Document> serializer) {
        this(properties, client.getDatabase(databaseName), collectionName, serializer);
    }

    @Override
    public RepositoryProperties<T> getProperties() {
        return this.properties;
    }

    public MongoDatabase getDatabase() {
        return this.database;
    }

    public String getCollectionName() {
        return this.collectionName;
    }

    public Serializer<T, Document> getSerializer() {
        return this.serializer;
    }

    @Override
    public Map<String, T> load(@NotNull Iterator<String> keyValues) {
        Map<String, T> users = Maps.newHashMap();
        while (keyValues.hasNext()) {
            String keyValue = keyValues.next();
            Bson filter = Filters.eq(this.properties.key(), keyValue);

            Document document = this.database.getCollection(this.collectionName, Document.class)
                    .find(filter).first();
            if (document != null) {
                T value = this.serializer.deserialize(document);
                users.put(keyValue, value);
            }
        }
        return users;
    }

    @Override
    public T load(@NotNull String keyValue) {
        MongoCollection<Document> collection = this.database.getCollection(this.collectionName, Document.class);

        Bson filter = Filters.eq(this.properties.key(), keyValue);
        Document document = collection.find(filter).first();
        if (document == null) {
            return null;
        } else {
            return this.serializer.deserialize(document);
        }
    }

    @Override
    public T load(@NotNull String keyValue, @NotNull T def) {
        MongoCollection<Document> collection = this.database.getCollection(this.collectionName, Document.class);

        Bson filter = Filters.eq(this.properties.key(), keyValue);
        Document document = collection.find(filter).first();
        if (document == null) {
            return def;
        } else {
            return this.serializer.deserialize(document);
        }
    }

    @Override
    public void save(@NotNull Map<String, T> map) {
        MongoCollection<Document> collection = this.database.getCollection(this.properties.key(), Document.class);
        ReplaceOptions replaceOptions = new ReplaceOptions().upsert(true);

        for (Map.Entry<String, T> entry : map.entrySet()) {
            Bson filter = Filters.eq(this.properties.key(), entry.getKey());
            Document document = this.serializer.serialize(entry.getValue());
            collection.replaceOne(filter, document, replaceOptions);
        }
    }

    @Override
    public void save(@NotNull String keyValue, @NotNull T value) {
        MongoCollection<Document> collection = this.database.getCollection(this.properties.key(), Document.class);
        ReplaceOptions replaceOptions = new ReplaceOptions().upsert(true);

        Bson filter = Filters.eq(this.properties.key(), keyValue);
        Document document = this.serializer.serialize(value);
        collection.replaceOne(filter, document, replaceOptions);
    }

    @Override
    public void delete(@NotNull Iterator<String> keyValues) {
        MongoCollection<Document> collection = this.database.getCollection(this.properties.key(), Document.class);
        while (keyValues.hasNext()) {
            String keyValue = keyValues.next();

            Bson filter = Filters.eq(this.properties.key(), keyValue);
            collection.deleteOne(filter);
        }
    }

    @Override
    public void delete(@NotNull String keyValue) {
        MongoCollection<Document> collection = this.database.getCollection(this.properties.key(), Document.class);

        Bson filter = Filters.eq(this.properties.key(), keyValue);
        collection.deleteOne(filter);
    }
}
