#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <sys/stat.h>
#include <getopt.h>

#define GRIDSIZE 256

void read_file(const char *filename, unsigned char**content, size_t* size) {
    struct stat buf;
    if (stat(filename, &buf) < 0) {
        perror(filename);
        exit(EXIT_FAILURE);
    }
    *size = buf.st_size;
    *content = malloc(*size);
    FILE *fp = fopen(filename, "r");
    fread(*content, 1, *size, fp);
    fclose(fp);
}

void encryption(const unsigned char* key,
                size_t nkey,
                FILE* message,
                FILE* ciphertext) {
    unsigned char ch;
    size_t i = 0;
    while (1) {
        fread(&ch, 1, 1, message);
        if (feof(message)) {
            break;
        }
        unsigned char enc = (key[i]+ ch) % GRIDSIZE;
        i = (i + 1) % nkey;
        fwrite(&enc, 1, 1, ciphertext);
    }
}

void usage(const char *name) {
    fprintf(stderr, "usage: %s [-d] [keyfile | -k key] plaintextfile ciphertextfile\n", name);
    exit(EXIT_FAILURE);
}

int main(int argc, char *argv[])
{
    int c;
    unsigned char *key = NULL;
    size_t nkey = 0;
    while ((c = getopt(argc, argv, "dk:")) != -1) {
        switch (c) {
        case 'd':
            break;
        case 'k':
            key = (unsigned char *)optarg;
            nkey = strlen(optarg);
            break;
        case '?':
            usage(argv[0]);
        }
    }

    if ((key == NULL && (argc - optind) < 3) ||
        (key != NULL && (argc - optind) < 2)) {
        usage(argv[0]);
    }

    char *key_file = NULL;
    if (key == NULL) {
        key_file = argv[optind++];
    }
    char *msg_file = argv[optind++];
    char *cipher_file = argv[optind++];

    FILE *message = fopen(msg_file, "rb");
    if (!message) {
        fprintf(stderr, "cannot open key file %s\n", argv[2]);
        return EXIT_FAILURE;
    }

    FILE *ciphertext = fopen(cipher_file, "w");
    if (!ciphertext) {
        fprintf(stderr, "cannot open key file %s\n", argv[3]);
        return EXIT_FAILURE;
    }

    if (key == NULL) {
        read_file(key_file, &key, &nkey);
        fprintf(stderr, "keyfile=%s, length=%ld\n", argv[1], nkey);
    }

    encryption(key, nkey, message, ciphertext);
    if (key_file != NULL) {
        free(key);
    }
    return 0;
}
