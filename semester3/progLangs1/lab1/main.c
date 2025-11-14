#include <stdio.h>
#include <stdlib.h>

void encrypt(char* word, int shift, int alphabetSize);
void decrypt(char* word, int shift, int alphabetSize);
char processLetter(char currentChar, int alphabetSize, int shift, char base);

int main(int argc, char* argv[])
{
    const int alphabetSize = 26;
    if (argc != 3) {
        return 2;
    }
    char* word = argv[1];
    int shift = atoi(argv[2]);
    encrypt(word, shift, alphabetSize);
    printf("%s = ", word);
    decrypt(word, shift, alphabetSize);
    printf("%s", word);
}

void decrypt(char* word, int shift, int alphabetSize)
{
    encrypt(word, -shift, alphabetSize);
}

void encrypt(char* word, int shift, int alphabetSize)
{
    shift = shift % alphabetSize + alphabetSize;
    for (int i = 0; word[i] != '\0'; ++i) {
        char currentChar = word[i];
        if (currentChar >= 'a' && currentChar <= 'z') {
            word[i] = processLetter(currentChar, alphabetSize, shift, 'a');
        }
        else if (currentChar >= 'A' && currentChar <= 'Z') {
            word[i] = processLetter(currentChar, alphabetSize, shift, 'A');
        }
    }
}

char processLetter(char currentChar, int alphabetSize, int shift, char base) {
    currentChar = (currentChar - base + shift) % alphabetSize + base;
    return currentChar;
}