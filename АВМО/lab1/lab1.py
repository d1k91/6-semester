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
    
def swap_max_in_column(matrix, matrix2, i):
    max_value = -100000
    index = -1
    for k in range(i, len(matrix)):
        if matrix[k][i] > max_value:
            max_value = matrix[k][i]
            index = k
    
    if index != -1:
        matrix[i], matrix[index] = matrix[index], matrix[i]
        matrix2[i], matrix2[index] = matrix2[index], matrix2[i]

def Jordan_Gauss(matrix):
    matrix2 = copy.deepcopy(matrix)
    for i in range(len(matrix)):
        for r in range(len(matrix)):
            ch = check(matrix[r])
            if ch == -1:
                return -1, matrix
            if ch == 0:
                return 0, matrix
            
        printarr(matrix)
        print(' ')

        for k in range(i, len(matrix)):
            swap_max_in_column(matrix, matrix2, i)   

        circ = matrix[i][i]
        if circ == 0:
            continue

        for j in range(len(matrix)):
            if j != i:
                matrix2[j][i] = 0
        for k in range(i,len(matrix2[i])):
            matrix2[i][k] = f.frac(matrix2[i][k], circ)
        
        printarr(matrix2)
        print(' ')

        for r in range(len(matrix)):
            for c in range(len(matrix[r])):
                if r != i and c > i:
                    matrix2[r][c] = matrix[r][c] - f.frac((matrix[i][c]*matrix[r][i]),circ)
                    print(f'{matrix[r][c]} -> {matrix[r][c]} - ({matrix[i][c]}*{matrix[r][i]})/{circ} = {matrix[r][c] - f.frac((matrix[i][c]*matrix[r][i]),circ)}')

            print(' ')
        print(' ')
        printarr(matrix2)
        print('---------------'*(len(matrix)+2))
        
        for r in range(len(matrix)):
            ch = check(matrix2[r])
            if ch == -1:
                return -1, matrix2
            if ch == 0:
                return 0, matrix2

        matrix = copy.deepcopy(matrix2)
    return 1, matrix

def solve(key, mat):
    match key:
        case 0:
            print("Система имеет бесконечно много решений")
            #mat = [x for x in mat if check(x) != 0]
            for i in range(len(mat)):
                if mat[i][i] == 1:
                    print(f'x{i+1} = ', end='')
                for j in range(len(mat[i])):
                    if j != i and j != len(mat[i])-1:
                        if mat[i][j]<0:
                            print(f' + {abs(mat[i][j])}*x{j+1}', end='')
                        elif mat[i][j]>0:
                            print(f' - {mat[i][j]}*x{j+1}',end='')
                    elif j == len(mat[i])-1:
                        if mat[i][j]<0:
                            print(f' {mat[i][j]}',end='')
                        elif mat[i][j]>0:
                            print(f' + {abs(mat[i][j])}',end='')
                print(' ')
        
        case -1:
            print('Система не имеет решений')

        case 1:
            for i in range(len(mat)):
                for j in range(len(mat[i])-1):
                    if mat[i][j] == 1:
                        print(f'x{j+1} = {mat[i][-1]}')


def main():
    with open('lab1.txt', 'r') as file:
        l = [[int(num) for num in line.split(' ')] for line in file]
    rank = np.linalg.matrix_rank(l)
    n = -1
    for i in range(len(l[0])):
        n+=1
    c = C(n,rank)
    fl,m = Jordan_Gauss(l)
    solve(fl,m)


if __name__ == "__main__":
    main()