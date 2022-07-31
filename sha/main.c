#include <stdlib.h>
#include <stdio.h>

#include "sha256.c"

int main (int argc, char *argv[]){
    int size;
    srand(177542);

    size = 3000;

    // int run = 0;     // DEBUG
    int v[size];
    uint8_t h[SHA256_SIZE_BYTES];

    for (int i = 0; i < size; i++) {
        v[i] = random() % 100000;
    }

    // Run application
    for (int i = 1; i < size; i++) {
        sha256(&v[i], sizeof(int), h);
        // DEBUG
        // run++;
        // printf("Cur: %d, ", v[i], h);
        // for (size_t j = 0; j < SHA256_SIZE_BYTES; j++) {
        //     printf("%02x%s", h[j], ((j % 4) == 3) ? " " : "");
        // }
        // printf("\n");
        // END DEBUG
    }

    // If debug is needed
    // printf("Done: %d\n", run);

    return 0;

}
