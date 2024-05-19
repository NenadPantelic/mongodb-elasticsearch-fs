import enum
from dataclasses import dataclass
from datetime import datetime


def _slash_or_empty(value):
    if value and not value.endswith("/"):
        return value + "/"

    return value


class NodeType(enum.Enum):
    FILE = 0
    DIR = 1


@dataclass  # only for files
class StorageNode:
    content: str = ""
    size: int = 0
    md5_checksum: str = ""
    extension: str = "application/text"

    def to_dict(self):
        return {
            "content": self.content,
            "size": self.size,
            "md5Checksum": self.md5_checksum,
            "extension": self.extension,
        }


@dataclass
class Node:
    parent_path: str
    name: str
    created_at: datetime
    updated_at: datetime

    def __init__(self, parent_path: str, name: str, created_at: datetime = datetime.utcnow(),
                 updated_at: datetime = datetime.utcnow()):
        self.parent_path = parent_path
        self.name = name
        self.created_at = created_at
        self.updated_at = updated_at

    def to_dict(self):
        return {
            "parentPath": self.parent_path,
            "name": self.name,
            "createdAt": self.created_at,
            "updatedAt": self.updated_at,
        }

    def full_path(self):
        return f"{_slash_or_empty(self.parent_path)}{_slash_or_empty(self.name)}"


@dataclass
class FileNode(Node):
    storage_node: StorageNode

    def __init__(self, parent_path: str, name: str, storage_node: StorageNode, created_at: datetime = datetime.utcnow(),
                 updated_at: datetime = datetime.utcnow()):
        super().__init__(parent_path, name, created_at, updated_at)
        self.storage_node = storage_node

    def to_dict(self):
        return {
            "name": self.name,
            "parentPath": self.parent_path,
            "storage": self.storage_node.to_dict(),
            "createdAt": self.created_at,
            "updatedAt": self.updated_at,
        }

    def full_path(self):
        return f"{_slash_or_empty(self.parent_path)}{self.name}"
