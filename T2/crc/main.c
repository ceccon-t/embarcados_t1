#include <stdlib.h>
#include <stdio.h>

#include "crc32b.c"
#include "energia.h"


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
    rapl_init();  /* inicializar a lib do RAPL */
	start_rapl_sysfs(); // Iniciar a contagem de consumo de energia
    clock_t t = clock(); // Iniciar a contagem de tempo

        // for (int i = 1; i < size; i++) {     // Older CRC implementation (with lookup table)
            crc_value = crc32b(&v);
            // printf("%d\n", crc_value);
        // }

    t = clock() - t; // Finalizar contagem do tempo
    double energy = end_rapl_sysfs();   // Finalizar a contagem dos contadores de energia
    double tempo = ((double)t)/CLOCKS_PER_SEC; // transforma tempo para segundos
    printf("Tempo de execucao em segundos: %.5f\n", tempo);
    printf("Energia consumida em Joules:   %.5f\n", energy); // (6) imprimir consumo de energia em Joules
    
    return 0;

}
