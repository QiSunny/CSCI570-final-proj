# CSCI570-final-proj
We used Dynamic Programming to solve the Sequence Alignment Problem. However, it’s considered not efficient in its memory usage. As a result, we implement a memory efficient version that uses Dynamic Programming and Divide & Conquer.

Supposed that we have two input string called X and Y and m is length of X, n is length of Y.
For DP version, we will directly construct a 2D matrix with size of m*n. It’s easy to learn that its space complexity is O(m*n).
Using Divide and Conquer in Dynamic Programming, we divide Y into two parts and construct two arrays (called frontend and backend), and this two arrays are used to get the penalty of two parts. We called forward-alignment for (X,Y[1:n/2]) and called backward-alignment for (X,Y[n/2+1,n]). After getting the result of the subproblem, we add frontend[i]+backend[i] for each i, and then get the index i that gets minimum value of frontend[i]+backend[i], called q. We record the point(n/2, q), which must be on the final path. Then we recursively call the alignment (X[1,q],Y[1,n/2]) and alignment (X[q+1,m]), Y[n/2+1,n] function, and then get the final path and return it.
It’s easy to learn that we use two lists with size of n, and a list for recording path that has a size of m, so it will use a O(m+n) memory.

In total, time complexity for DP version is O(m*n), and space complexity is O(m*n); while time complexity for DP&DC version	is O(m*n) and space complexity is O(m+n), where m and n represent the length of two input strings.
