import enum
from dataclasses import dataclass


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
            "md5_checksum": self.md5_checksum,
            "extension": self.extension,
        }


@dataclass
class Node:
    name: str
    parent_path: str

    def to_dict(self):
        return {
            "name": self.name,
            "parentPath": self.parent_path
        }

    def full_path(self):
        return f"{self.parent_path}/{self.name}/"


@dataclass
class FileNode(Node):
    storage_node: StorageNode

    def to_dict(self):
        return {
            "name": self.name,
            "parentPath": self.parent_path,
            "storage": self.storage_node.to_dict()
        }

    def full_path(self):
        return f"{self.parent_path}/{self.name}"
