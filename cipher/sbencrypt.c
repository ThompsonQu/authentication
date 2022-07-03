#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <sys/stat.h>
#include <getopt.h>

#define BLOCKSIZE 16
#define MOD 256

static unsigned long sdbm(unsigned char *str) {
    unsigned long hash = 0;
    int c;
    while ((c = *str++)) {
        hash = c + (hash << 6) + (hash << 16) - hash;
    }
    return hash;
}

unsigned char next_key(unsigned long key) {
    static unsigned long a = 1103515245;
    static unsigned long c = 12345;
    unsigned long x = key * a + c;
    return x % MOD;
}

void keystream(unsigned long* seed, unsigned char keys[BLOCKSIZE]) {
    for (int i = 0; i < BLOCKSIZE; ++i) {
        keys[i] = next_key(*seed);
        *seed = keys[i];
    }
}

size_t read_block(FILE *plain, unsigned char block[BLOCKSIZE]) {
    size_t n = fread(block, 1, BLOCKSIZE, plain);
    return n;
}

int main(int argc, char *argv[])
{
    if (argc < 4) {
        fprintf(stderr, "usage: %s password plaintextfile ciphertextfile\n", argv[0]);
        return EXIT_FAILURE;
    }

    FILE *plain_file = fopen(argv[2], "rb");
    if (!plain_file) {
        fprintf(stderr, "cannot open plaintext file %s\n", argv[2]);
        return EXIT_FAILURE;
    }
    FILE *cipher_file = fopen(argv[3], "wb");
    if (!cipher_file) {
        fprintf(stderr, "cannot open ciphertext file %s\n", argv[2]);
        return EXIT_FAILURE;
    }

    unsigned long seed = sdbm((unsigned char*)argv[1]);
    fprintf(stderr, "using seed=%lu from password=\"%s\"\n", seed, argv[1]);

    unsigned char cipher[BLOCKSIZE];
    keystream(&seed, cipher);

    unsigned char block[BLOCKSIZE];
    unsigned char temp[BLOCKSIZE];
    unsigned char key[BLOCKSIZE];
    while (1) {
        size_t n = read_block(plain_file, block);
        unsigned char padding = BLOCKSIZE - n;
        for (size_t i = n; i < BLOCKSIZE; ++i) {
            block[i] = padding;
        }

        for (size_t i = 0; i < BLOCKSIZE; ++i) {
            temp[i] = block[i] ^ cipher[i];
        }
        keystream(&seed, key);
        for (size_t i = 0; i < BLOCKSIZE; ++i) {
            size_t first = key[i] & 0xf;
            size_t second = (key[i] >> 4) & 0xf;
            unsigned char t = temp[first];
            temp[first] = temp[second];
            temp[second] = t;
        }
        for (size_t i = 0; i < BLOCKSIZE; ++i) {
            cipher[i] = temp[i] ^ key[i];
        }
        fwrite(cipher, 1, BLOCKSIZE, cipher_file);
        if (n != BLOCKSIZE) {
            break;
        }
    }

    fclose(plain_file);
    fclose(cipher_file);

    return 0;
}
