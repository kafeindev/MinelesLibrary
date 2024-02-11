package net.mineles.library.storage.mongodb;

import net.mineles.library.libs.mongodb.client.MongoClient;
import net.mineles.library.storage.AbstractStorage;
import org.bson.Document;

public final class MongoDb extends AbstractStorage<MongoDbRepository, Document> {
    private final MongoClient client;

    public MongoDb(String url) {
        this(MongoDbConnector.connect(url));
    }

    public MongoDb(MongoClient client) {
        super();
        this.client = client;
    }

    @Override
    public void initialize() {
        this.client.startSession();
    }

    @Override
    public void shutdown() {
        this.client.close();
    }
}
