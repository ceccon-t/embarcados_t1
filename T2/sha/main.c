#include <stdlib.h>
#include <stdio.h>

#include "sha256.c"
#include "energia.h"

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

    // Inicializa dependencias para medir consumo de energia
    rapl_init();  /* inicializar a lib do RAPL */
	start_rapl_sysfs(); // Iniciar a contagem de consumo de energia
    clock_t t = clock(); // Iniciar a contagem de tempo

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

    // Pega valores finais de energia e reporta consumo
    t = clock() - t; // Finalizar contagem do tempo
    double energy = end_rapl_sysfs();   // Finalizar a contagem dos contadores de energia
    double tempo = ((double)t)/CLOCKS_PER_SEC; // transforma tempo para segundos
    printf("Tempo de execucao em segundos: %.5f\n", tempo);
    printf("Energia consumida em Joules:   %.5f\n", energy); // (6) imprimir consumo de energia em Joules

    // If debug is needed
    // printf("Done: %d\n", run);

    return 0;

}
