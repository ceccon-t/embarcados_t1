#include <stdlib.h>
#include <stdio.h>

#include "crc32b.c"

int main (int argc, char *argv[]){
    int size;
    srand(177542);

    size = 3000;

    int v[size];
    unsigned long crc_value = 1;

    for (int i = 0; i < size; i++) {
        v[i] = random() % 100000;
    }
    v[size-1] = '\0';

    // Run application
    // for (int i = 1; i < size; i++) {     // Older CRC implementation (with lookup table)
        crc_value = crc32b(&v);
        // printf("%d\n", crc_value);
    // }

    return 0;

}
