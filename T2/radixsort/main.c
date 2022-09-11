#include <stdlib.h>
#include <stdio.h>

#include "radix-sort.c"
#include "energia.h"

int main (int argc, char *argv[]){
    int size;
    srand(177542);

    size = 1000000;

    int v[size];

    for (int i = 0; i < size; i++) {
        v[i] = random() % 100000;
    }

    // Inicializa dependencias para medir consumo de energia
    rapl_init();  /* inicializar a lib do RAPL */
	start_rapl_sysfs(); // Iniciar a contagem de consumo de energia
    clock_t t = clock(); // Iniciar a contagem de tempo

    // Run application
        radix_sort_LSD(v, size); // O que sera medido

    // Pega valores finais de energia e reporta consumo
    t = clock() - t; // Finalizar contagem do tempo
    double energy = end_rapl_sysfs();   // Finalizar a contagem dos contadores de energia
    double tempo = ((double)t)/CLOCKS_PER_SEC; // transforma tempo para segundos
    printf("Tempo de execucao em segundos: %.5f\n", tempo);
    printf("Energia consumida em Joules:   %.5f\n", energy); // (6) imprimir consumo de energia em Joules

    // If debug is needed
    // for (int i = 1; i < size; i++) if (v[i-1] > v[i]) std::cout << "Wrong order!\n";

    return 0;

}
