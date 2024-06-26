from dotenv import dotenv_values

from db_importer.db_importer import DBImporter
from generator.generator import generate_node
from generator.random_util import random_string
from model.db_type import DBType
from model.node import NodeType


def generate(db_importer: DBImporter):
    # top level
    print("###### TOP LEVEL ######")
    root = generate_node("", "/", NodeType.DIR)
    db_importer.import_nodes([root])
    print("###### END ######")

    ###### 1st level ######
    # add 3 folders to the top level
    print("###### 1st LEVEL ######")
    first_level_folders = []
    for _ in range(3):
        first_level_folders.append(generate_node(root.full_path(), random_string(16), NodeType.DIR))
    db_importer.import_nodes(first_level_folders)
    print("###### END ######")

    ###### 2nd level ######
    ### 3 X 100 files = 300 files
    ### 3 x 20 folders = 60 folders
    print("###### 2nd LEVEL ######")

    second_level_folders = []
    for folder in first_level_folders:
        second_level_files = []
        # add 100 files to every folder
        for _ in range(100):
            second_level_files.append(generate_node(folder.full_path(), random_string(16), NodeType.FILE))

        db_importer.import_nodes(second_level_files)

        # add 20 folders to every folder
        folders = []
        for _ in range(20):
            folder = generate_node(folder.full_path(), random_string(16), NodeType.DIR)
            folders.append(folder)
            second_level_folders.append(folder)

        db_importer.import_nodes(folders)
    print("###### END ######")

    ###### 3rd level ######
    ### 60 X 1000 files = 60_000 files
    ### 60 X 10 folders = 600 folders
    print("###### 3rd LEVEL ######")

    third_level_folders = []
    for folder in second_level_folders:
        third_level_files = []
        # add 1000 files to every folder
        for _ in range(1000):
            third_level_files.append(generate_node(folder.full_path(), random_string(16), NodeType.FILE))

        db_importer.import_nodes(third_level_files)

        # add 10 folders to every folder
        folders = []
        for _ in range(10):
            folder = generate_node(folder.full_path(), random_string(16), NodeType.DIR)
            folders.append(folder)
            third_level_folders.append(folder)

        db_importer.import_nodes(folders)

    print("###### END ######")

    ###### 4th level ######
    ### 600 X 2500 files = 1_500_000 files
    ### 600 X 2 folders = 1200 folders
    print("###### 4th LEVEL ######")

    fourth_level_folders = []
    for folder in third_level_folders:
        fourth_level_files = []
        # add 1000 files to every folder
        for _ in range(2500):
            fourth_level_files.append(generate_node(folder.full_path(), random_string(16), NodeType.FILE))

        db_importer.import_nodes(fourth_level_files)

        # add 2 folders to every folder
        folders = []
        for _ in range(2):
            folder = generate_node(folder.full_path(), random_string(16), NodeType.DIR)
            folders.append(folder)
            fourth_level_folders.append(folder)

        db_importer.import_nodes(folders)

    print("###### END ######")

    ###### 5th level ######
    ### 1200 x 1000 = 1_200_000
    print("###### 5th LEVEL ######")

    for folder in fourth_level_folders:
        fifth_level_files = []
        # add 1000 files to every folder
        for _ in range(1000):
            fifth_level_files.append(generate_node(folder.full_path(), random_string(16), NodeType.FILE))

        db_importer.import_nodes(fifth_level_files)

    print("###### END ######")


if __name__ == "__main__":
    dbs_to_import = [DBType.MONGO_DB]
    config = dotenv_values(".env")

    db_importer = DBImporter(config, dbs_to_import)

    generate(db_importer)
