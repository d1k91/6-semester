import FracClass as f
import copy


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

def Jordan_Gauss(matrix):
    matrix2 = copy.deepcopy(matrix)
    for i in range(len(matrix)):
        for r in range(len(matrix)):
            ch = check(matrix[r])
            if ch == -1:
                return -1, matrix
            if ch == 0:
                return 0, matrix
        circ = matrix[i][i]
        if circ == 0:
            continue
        for j in range(len(matrix)):
            if j != i:
                matrix2[j][i] = 0
        for k in range(i,len(matrix2[i])):
            matrix2[i][k] = f.frac(matrix2[i][k], circ)
        
        printarr(matrix)
        print(' ')

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
        

        matrix = copy.deepcopy(matrix2)
    return 1, matrix

def solve(key, mat):
    match key:
        case 0:
            print("Система имеет бесконечно много решений")
            mat = [x for x in mat if check(x) != 0]
            for i in range(len(mat)):
                if mat[i][i] == 1:
                    print(f'x{i} = ', end='')
                for j in range(len(mat[i])):
                    if j != i and j != len(mat[i])-1:
                        if mat[i][j]<0:
                            print(f' + {abs(mat[i][j])}*x{j}', end='')
                        elif mat[i][j]>0:
                            print(f' - {mat[i][j]}*x{j}',end='')
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
    res, mat = Jordan_Gauss(l)
    solve(res,mat)

if __name__ == "__main__":
    main()