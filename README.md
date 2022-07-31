# embarcados_t1
// ----------------------------- crc32b --------------------------------

/* This is the basic CRC-32 calculation with some optimization but no
table lookup. The the byte reversal is avoided by shifting the crc reg
right instead of left and by using a reversed 32-bit word to represent
the polynomial.
   When compiled to Cyclops with GCC, this function executes in 8 + 72n
instructions, where n is the number of bytes in the input message. It
should be doable in 4 + 61n instructions.
   If the inner loop is strung out (approx. 5*8 = 40 instructions),
it would take about 6 + 46n instructions. */

unsigned int crc32b(unsigned char *message) {
   int i, j;
   unsigned int byte, crc, mask;

   i = 0;
   crc = 0xFFFFFFFF;
   while (message[i] != 0) {
      byte = message[i];            // Get next byte.
      crc = crc ^ byte;
      for (j = 7; j >= 0; j--) {    // Do eight times.
         mask = -(crc & 1);
         crc = (crc >> 1) ^ (0xEDB88320 & mask);
      }
      i = i + 1;
   }
   return ~crc;
}



// C++ implementation of computation  of
// arithmetic mean, geometric mean
// and harmonic mean
#include <bits/stdc++.h>
using namespace std;
 
// Function to calculate arithmetic
// mean, geometric mean and harmonic mean
double compute(int a, int b)
{
 
    double AM, GM, HM;
 
    AM = (a + b) / 2;
    GM = sqrt(a * b);
    HM = (GM * GM) / AM;
    return HM;
}
 
// Driver function
int main()
{
 
    int a = 5, b = 15;
    double HM = compute(a, b);
    cout << "Harmonic Mean between " << a
          << " and " << b << " is " << HM ;
    return 0;
}
