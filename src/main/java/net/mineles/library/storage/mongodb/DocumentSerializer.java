package net.mineles.library.storage.mongodb;

import net.mineles.library.serializer.Serializer;
import org.bson.Document;

public interface DocumentSerializer<T> extends Serializer<T, Document> {
}
