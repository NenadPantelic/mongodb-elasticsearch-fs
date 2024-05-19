import random
from string import ascii_letters

ASCII_LETTERS = ascii_letters
NUMS = "0123456789"
ASCII_ALPHANUMS = list(ASCII_LETTERS + NUMS)
random.shuffle(ASCII_ALPHANUMS)

PUNCTUATION_MARKS = [", ", ", ", ";", ".", ".", ".", "?", "!", ":"]
random.shuffle(PUNCTUATION_MARKS)


def load_words(file_name):
    with open(file_name, 'r') as fin:
        return [line.strip() for line in fin.readlines()]


WORDS = load_words('resources/english_words.txt')


def random_string(size=10, include_letters=True, include_nums=True):
    if not include_letters and not include_nums:
        raise ValueError('String must contain letters or nums or both of them.')

    if include_letters and include_nums:
        target_set_of_chars = ASCII_ALPHANUMS
    elif include_letters:
        target_set_of_chars = ASCII_LETTERS
    else:
        target_set_of_chars = NUMS

    chars = []
    for _ in range(size):
        chars.append(target_set_of_chars[random.randint(0, len(target_set_of_chars) - 1)])

    return "".join(chars)


def random_int(min_value=0, max_value=2 ** 32):
    return random.randint(min_value, max_value)


def random_bool():
    return True if random_int(0, 1) == 1 else False


def random_text(num_of_words=100):
    words = []
    for i in range(1, num_of_words):
        word = get_random_word()
        words.append(word)

        # add a separator
        if i % 10 == 0:
            words.append(random.choice(PUNCTUATION_MARKS))
        else:
            words.append(" ")

    words.append(get_random_word())
    words.append(".")

    return "".join(words)


def get_random_word():
    return WORDS[random_int(1, len(WORDS) - 1)]
