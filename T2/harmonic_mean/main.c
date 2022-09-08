#include <stdlib.h>
#include <stdio.h>
#include <math.h>

#include "mean.c"
#include "energia.h"

int main (int argc, char *argv[]){
    int size;
    srand(177542);

    size = 3000;

    int v[size];
    double result;

    for (int i = 0; i < size; i++) {
        v[i] = random() % 100000;
    }

    // Inicializa dependencias para medir consumo de energia
    rapl_init();  /* inicializar a lib do RAPL */
	start_rapl_sysfs(); // Iniciar a contagem de consumo de energia
    clock_t t = clock(); // Iniciar a contagem de tempo

    // Run application
        result = compute(v[0], v[1]);
        for (int i = 2; i < size; i++) {
            result = compute(result, v[i]);
        }
    
    // Pega valores finais de energia e reporta consumo
    t = clock() - t; // Finalizar contagem do tempo
    double energy = end_rapl_sysfs();   // Finalizar a contagem dos contadores de energia
    double tempo = ((double)t)/CLOCKS_PER_SEC; // transforma tempo para segundos
    printf("Tempo de execucao em segundos: %.5f\n", tempo);
    printf("Energia consumida em Joules:   %.5f\n", energy); // (6) imprimir consumo de energia em Joules

    return 0;

}
