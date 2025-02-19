import FracClass as f
import math
import copy
import numpy as np

def printarr(arr):
    for i in range(0, len(arr)):
        for j in range(0, len(arr[i])):
            if j == len(arr[i])-1:
                print(" |", end='')
            print(str(arr[i][j]).rjust(15), end=' ')
        print()

def check(arr):
    if all(x == 0 for x in arr[:-1]) and arr[-1] != 0:
        print('gg')
        return -1
    if all(x == 0 for x in arr):
        return 0
    
def swap_max_in_column(matrix, matrix2, i, col):
    max_value = -100000
    index = -1
    for k in range(i, len(matrix)):
        if matrix[k][col] > max_value:
            max_value = matrix[k][col]
            index = k
    
    if index != -1:
        matrix[i], matrix[index] = matrix[index], matrix[i]
        matrix2[i], matrix2[index] = matrix2[index], matrix2[i]


def Jordan_Gauss(matrix):
    matrix2 = copy.deepcopy(matrix)
    n = len(matrix)
    m = len(matrix[0]) - 1

    for i in range(n):
        for r in range(len(matrix)):
            ch = check(matrix2[r])
            if ch == -1:
                return -1, matrix2
            if ch == 0:
                return 0, matrix2
        col = i
        while col < m:
            skip_column = True
            for k in range(i, n):
                if matrix[k][col] != 0:
                    skip_column = False
                    break
            
            if skip_column:
                col += 1
            else:
                break

        if col >= m:
            break
        
        printarr(matrix2)
        print(' ')
        

        swap_max_in_column(matrix, matrix2, i, col)

        printarr(matrix2)
        print(' ')
        

        circ = matrix[i][col]
        if circ == 0:
            continue

        for j in range(col, len(matrix[i])):
            matrix2[i][j] = f.frac(matrix2[i][j], circ)

        for r in range(n):
            if r != i:
                factor = matrix[r][col]
                for c in range(col, len(matrix[r])):
                    matrix2[r][c] = matrix[r][c] - f.frac((matrix[i][c] * factor), circ)
                    print(f'{matrix[r][c]} -> {matrix[r][c]} - ({matrix[i][c]}*{matrix[r][i]})/{circ} = {matrix[r][c] - f.frac((matrix[i][c]*matrix[r][i]),circ)}')
                print(' ')

        printarr(matrix2)
        print('---------------' * (len(matrix) + 2))

        for r in range(len(matrix)):
            ch = check(matrix2[r])
            if ch == -1:
                return -1, matrix2
            if ch == 0:
                return 0, matrix2

        matrix = copy.deepcopy(matrix2)

    return 1, matrix

def solve(key, mat):
    if key == -1:
        print("Система не имеет решений")
        return
    elif key == 0:
        print("Система имеет бесконечно много решений")
    else:
        print("Система имеет единственное решение")

    n = len(mat)
    m = len(mat[0]) - 1

    for col in range(m):
        for row in range(n):
            if mat[row][col] != 0 and row == col:
                break

    for row in range(n):
        for col in range(m):
            if mat[row][col] == 1:
                print(f'x{col + 1} = ', end='')
                for j in range(m):
                    if j != col and mat[row][j] != 0:
                        if mat[row][j] < 0:
                            print(f' + {abs(mat[row][j])}*x{j + 1}', end='')
                        else:
                            print(f' - {mat[row][j]}*x{j + 1}', end='')
                if mat[row][m] != 0:
                    if mat[row][m] < 0:
                        print(f' {mat[row][m]}', end='')
                    else:
                        print(f' + {mat[row][m]}', end='')
                print()
                break
        else:
            if all(x == 0 for x in mat[row][:-1]):
                if mat[row][m] != 0:
                    print(f'0 = {mat[row][m]}')


def main():
    with open('lab1.txt', 'r') as file:
        l = [[int(num) for num in line.split(' ')] for line in file]
    fl,m = Jordan_Gauss(l)
    solve(fl,m)


if __name__ == "__main__":
    main()