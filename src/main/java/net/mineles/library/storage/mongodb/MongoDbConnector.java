package net.mineles.library.storage.mongodb;

import net.mineles.library.libs.bson.UuidRepresentation;
import net.mineles.library.libs.mongodb.ConnectionString;
import net.mineles.library.libs.mongodb.MongoClientSettings;
import net.mineles.library.libs.mongodb.client.MongoClient;
import net.mineles.library.libs.mongodb.client.MongoClients;

public final class MongoDbConnector {
    private MongoDbConnector() {
    }

    public static MongoClient connect(String url) {
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(url))
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .build();
        return MongoClients.create(settings);
    }
}
