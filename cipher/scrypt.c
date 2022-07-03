#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <sys/stat.h>
#include <getopt.h>

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

int main(int argc, char *argv[])
{
    if (argc < 4) {
        fprintf(stderr, "usage: %s password plaintextfile ciphertextfile\n", argv[0]);
        return EXIT_FAILURE;
    }

    unsigned long seed = sdbm((unsigned char*)argv[1]);
    FILE *plain = fopen(argv[2], "rb");
    if (!plain) {
        fprintf(stderr, "cannot open plaintext file %s\n", argv[2]);
        return EXIT_FAILURE;
    }
    FILE *cipher = fopen(argv[3], "wb");
    if (!cipher) {
        fprintf(stderr, "cannot open ciphertext file %s\n", argv[2]);
        return EXIT_FAILURE;
    }
    fprintf(stderr, "using seed=%lu from password=\"%s\"\n", seed, argv[1]);

    unsigned char ch;
    unsigned char key = next_key(seed);
    while (1) {
        fread(&ch, 1, 1, plain);
        if (feof(plain)) {
            break;
        }
        unsigned char r = ch ^ key;
        fwrite(&r, 1, 1, cipher);
        key = next_key(key);
    }

    return 0;
}
