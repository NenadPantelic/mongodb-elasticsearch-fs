package edu.learning.db.mongodbfs.repository;

import edu.learning.db.mongodbfs.model.Node;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface NodeRepository extends MongoRepository<Node, String> {

    // db.nodes.find({parentPath: "abc"}).sort({_id: 1}).skip(page * size).limit(size)
    List<Node> findByParentPath(String parentPath, Pageable pageable);

    // db.nodes.find({parentPath: "abc", name: "def})
    Optional<Node> findByParentPathAndName(String parentPath, String name);

    // db.nodes.find({name: "abc"}).sort({_id: 1}).skip(page * size).limit(size)
    List<Node> findByName(String name, Pageable pageable);

    // db.nodes.find({name: {"$regex": "abc"}}).sort({_id: 1}).skip(page * size).limit(size)
    List<Node> findByNameRegex(String nameRegex, Pageable pageable);

    // db.nodes.find({"storage.content": {"$regex": "abc"}}).sort({_id: 1}).skip(page * size).limit(size)
    List<Node> findByStorage_contentRegex(String contentRegex, Pageable pageable);

    // db.nodes.count({"parentPath": {"$regex": "^/ur0EriZVNqqzjf38/4gJvm6cfbp4dsfbE/98Qk0MkdhhKkczmr/"}})
    int countByParentPathStartsWith(String parentPathRegex);

    // db.nodes.count({"parentPath": {"$regex": "^/ur0EriZVNqqzjf38/4gJvm6cfbp4dsfbE/98Qk0MkdhhKkczmr/"}, "storage": {$exists: true}})
    int countByParentPathStartsWithAndStorageExists(String parentPathRegex, boolean containsStorage);

    @Aggregation(pipeline = {
            "{$match: { parentPath: {$regex : ?0 }, 'storage.size': {$gt: ?1 }}}",
            "{$group: {_id: null, 'countOfLargerFiles':{$sum: 1}}}",
            "{$project: {'countOfLargerFiles': 1}}"
    })
//                  db.nodes.aggregate([
//                    { $match: { parentPath: {$regex: '/ur0EriZVNqqzjf38/'}, 'storage.size':{$gt: 32768 }}},
//                    {$group:{_id: null, 'countOfLargerFiles':{ $sum:1}}}
//                    {project:{'countOfLargerFiles': 1}}
//                ])

    long countFileInSubtreeLargerThanThreshold(String parentPathRegex, long threshold);

    @Aggregation(pipeline = {
            "{$match: { parentPath: {$regex : ?0 }, 'storage': {$exists: true}}}",
            "{$sort: {'storage.size': -1}}",
            "{$limit: 1}"
    })
        // db.nodes.find({parentPath: {$regex :'^/ur0EriZVNqqzjf38/4gJvm6cfbp4dsfbE/98Qk0MkdhhKkczmr/'}})
        // .sort({'storage.size': -1}).limit(1)
    Optional<Node> findLargestFileInSubtree(String parentPathRegex);

    @Aggregation(pipeline = {
            "{$match: { parentPath: {$regex : ?0 }, 'storage': {$exists: true }}}",
            "{$group: {_id: null, 'totalSize':{$sum: '$storage.size'}}}",
            "{$project: {'totalSize': 1}}"
    })
//                  db.nodes.aggregate([
//                    { $match: { parentPath: {$regex :'^/ur0EriZVNqqzjf38/4gJvm6cfbp4dsfbE/98Qk0MkdhhKkczmr/'}, 'storage':{$exists: true }}},
//                    {$group:{_id: null, 'totalSize':{ $sum:'$storage.size'}}}
//                ]);
    long getTotalSizeInSubtree(String parentPathRegex);
}
