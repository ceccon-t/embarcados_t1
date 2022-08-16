void copy(int *dest, int *src, int size) {
   for (int i=0; i < size; i++)
       dest[i] = src[i];
}

void radix_sort_LSD(int C[], int tam){
    int aux[tam];
    int dig = 1, higher = C[0];

    for (int i = 1; i < tam; i++) 
        if (C[i] > higher) 
            higher = C[i];

    while (higher / dig > 0){
        int acm[10] = {0};
        for (int i = 0; i < tam; i++) acm[C[i] / dig % 10]++;
        for (int i = 1; i < 10; i++) acm[i] += acm[i - 1];
        for (int i = tam - 1; i >= 0; i--) aux[--acm[C[i] / dig % 10]] = C[i];

        copy(C, aux, tam);
        dig *= 10;
    }
}