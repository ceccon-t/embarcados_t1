#include <stdio.h>

int calculate_value(int line, int column) {
    return line * column;
}

int main (int argc, char *argv[]){

    int rows = 10;
    int cols = 10;

    int mat[rows][cols];

    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            mat[i][j] = calculate_value(i, j);
        }
    }

    // For debug
    // for (int i = 0; i < rows; i++) {
    //     for (int j = 0; j < cols; j++) {
    //         printf("%d, %d: %d\n", i, j, mat[i][j]);
    //     }
    // }

    return 0;

}
