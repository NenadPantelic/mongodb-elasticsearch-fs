from model.node import NodeType, Node, FileNode, StorageNode
from .random_util import *


def generate_storage_node():
    return StorageNode(
        random_text(100),
        random_int(max_value=2 ** 16),
        random_string(6)
    )


def generate_node(parent_path, name, _type=NodeType.FILE) -> Node:
    if _type == NodeType.FILE:
        return FileNode(parent_path, name, generate_storage_node())
    elif _type == NodeType.DIR:
        return Node(parent_path, name)

    raise ValueError(f"Unable to map {_type} to a valid node type.")
