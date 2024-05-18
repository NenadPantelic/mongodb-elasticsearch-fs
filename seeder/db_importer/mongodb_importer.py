from typing import List

from db.mongo_db_manager import MongoDBManager
from model.node import Node


class MongoDBImporter:
    def __init__(self, db_manager: MongoDBManager):
        self._nodes = db_manager.get_or_create_nodes_collection()

    def import_nodes(self, nodes: List[Node]):
        print(f"Importing {len(nodes)} nodes to MongoDB...")
        documents = [node.to_dict() for node in nodes]

        insertion_result = self._nodes.insert_many(documents)

        inserted_ids = insertion_result.inserted_ids
        if len(inserted_ids) != len(nodes):
            print(
                f"The number of inserted documents do not match the number of provided nodes: {inserted_ids} != {nodes}"
            )
