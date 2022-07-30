#include <iostream>
#include <random>
#include <ctime>
#include <cmath>

#include "radix-sort.cpp"
#include "crc32.c"

int main (int argc, char *argv[]){
    int size;
    srand(177542);

    if (argc < 2) {
        std::cout << "Missing benchmark method!\n";
        return 1;
    } else if (argc < 3) {
        size = 3000;
    } else {
        size = atoi(argv[2]);
    } 

    int v[size];
    unsigned long crc_value = 1;

    for (int i = 0; i < size; i++) {
        v[i] = random() % 100000;
    }

    if (strcmp(argv[1], "unknown") == 0) {
        std::cout << "Unrecognized method\n";
    } else if (strcmp(argv[1], "radix") == 0) {
        radix_sort_LSD(v, size);
        // for (int i = 1; i < size; i++) if (v[i-1] > v[i]) std::cout << "Wrong order!\n";
    } else if (strcmp(argv[1], "crc") == 0) {
        for (int i = 1; i < size; i++) crc_value = Crc32_ComputeBuf(crc_value, &v[i], sizeof(int));
    } else {
        std::cout << "Unrecognized method\n";
    }

    return 0;

}
