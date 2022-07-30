#include <stdlib.h>
#include <stdio.h>

#include "radix-sort.c"

int main (int argc, char *argv[]){
    int size;
    srand(177542);

    size = 3000;

    int v[size];

    for (int i = 0; i < size; i++) {
        v[i] = random() % 100000;
    }

    // Run application
    radix_sort_LSD(v, size);

    // If debug is needed
    // for (int i = 1; i < size; i++) if (v[i-1] > v[i]) std::cout << "Wrong order!\n";

    return 0;

}
