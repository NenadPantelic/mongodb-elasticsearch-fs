import pymongo
from pymongo import MongoClient

DB_USER = "db.mongodb.user"
DB_PASSWORD = "db.mongodb.password"
DB_HOST = "db.mongodb.host"
DB_PORT = "db.mongodb.port"
DB_AUTH_DB = "db.mongodb.auth-db"


def _create_connection_string(config):
    user = config.get(DB_USER)
    if not user:
        raise ValueError("Database user has not been provided.")

    password = config.get(DB_PASSWORD)
    if not password:
        raise ValueError("Database password has not been provided.")

    host = config.get(DB_HOST, "localhost")
    port = config.get(DB_PORT, 27017)
    auth_db = config.get(DB_AUTH_DB, "admin")

    return f"mongodb://{user}:{password}@{host}:{port}/?authSource={auth_db}"


class MongoDBManager:
    def __init__(self, config):
        self._client = MongoClient(_create_connection_string(config))
        self._ping()
        # self._create_indexes()

    def _ping(self):
        self._client.admin.command('ping')
        print("Pinged your deployment. You successfully connected to MongoDB!")

    def get_or_create_nodes_collection(self):
        # touch the database
        db = self._client.filesystem
        # touch the collection
        nodes = db.nodes
        print(f"Collection: {nodes}")
        return nodes

    def _create_indexes(self):
        print(f"Creating indexes...")
        nodes = self.get_or_create_nodes_collection()
        nodes.create_index([("parent_path,name", pymongo.HASHED)], name="parent_path_name_idx", unique=True)
        nodes.create_index([("name", pymongo.TEXT)], name="name_idx", default_language="english")
        nodes.create_index([("storage.content", pymongo.TEXT)], name="storage_content_idx", default_language="english")
