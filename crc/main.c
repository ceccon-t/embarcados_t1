#include <stdio.h>
#include <stdlib.h>

#include "crc32.c"

int main (int argc, char *argv[]){
    int size;
    srand(177542);

    size = 3000;

    int v[size];
    unsigned long crc_value = 1;

    for (int i = 0; i < size; i++) {
        v[i] = random() % 100000;
    }

    // Run application
    for (int i = 1; i < size; i++) {
        crc_value = Crc32_ComputeBuf(crc_value, &v[i], sizeof(int));
    }

    return 0;

}
