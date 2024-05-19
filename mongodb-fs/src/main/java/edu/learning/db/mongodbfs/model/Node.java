package edu.learning.db.mongodbfs.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "nodes")
public class Node {

    @Id
    private String id;
    private String parentPath;
    private String name;
    private StorageNode storage;
    private Instant createdAt;
    private Instant updatedAt;

    public NodeType getType() {
        return storage == null ? NodeType.DIR : NodeType.FILE;
    }
}
