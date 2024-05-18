from typing import List

from db.mongo_db_manager import MongoDBManager
from db_importer.mongodb_importer import MongoDBImporter
from model.db_type import DBType
from model.node import Node


class DBImporter:
    def __init__(self, config, databases_for_import=[]):
        self._importers = []

        if DBType.MONGO_DB in databases_for_import:
            self._importers.append(MongoDBImporter(MongoDBManager(config)))

    def import_nodes(self, nodes: List[Node]):
        print("Importing nodes...")

        for importer in self._importers:
            importer.import_nodes(nodes)
