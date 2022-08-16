#include <stdlib.h>
#include <stdio.h>
#include <math.h>

#include "mean.c"

int main (int argc, char *argv[]){
    int size;
    srand(177542);

    size = 3000;

    int v[size];
    double result;

    for (int i = 0; i < size; i++) {
        v[i] = random() % 100000;
    }

    // Run application
    result = compute(v[0], v[1]);
    for (int i = 2; i < size; i++) {
        result = compute(result, v[i]);
    }

    return 0;

}
