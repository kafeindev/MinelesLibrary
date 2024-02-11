package net.mineles.library.storage.mongodb;

import net.mineles.library.libs.mongodb.client.MongoClient;
import net.mineles.library.libs.mongodb.client.MongoDatabase;
import net.mineles.library.storage.Repository;
import org.jetbrains.annotations.Nullable;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class MongoDbRepository implements Repository {
    protected final MongoClient client;
    protected final @Nullable MongoDatabase database;
    protected final String collectionName;

    protected MongoDbRepository(MongoClient client, String collectionName) {
        this(client, null, collectionName);
    }

    protected MongoDbRepository(MongoClient client, @Nullable MongoDatabase database, String collectionName) {
        this.client = checkNotNull(client, "client");
        this.database = database;
        this.collectionName = checkNotNull(collectionName, "collectionName");
    }

    public @Nullable MongoDatabase getDatabase() {
        return this.database;
    }

    public String getCollectionName() {
        return this.collectionName;
    }
}
