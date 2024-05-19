package edu.learning.db.mongodbfs.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StorageNode {

    private String content;
    private int size;
    private String md5Checksum;
    private String extension;
}
