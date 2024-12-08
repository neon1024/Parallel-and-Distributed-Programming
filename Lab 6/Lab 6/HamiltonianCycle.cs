using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Threading;
using System.Threading.Tasks;

namespace Lab_6;

class HamiltonianCycle
{
    private int V; // Number of vertices
    private bool cycleFound = false; // Flag to stop other threads once cycle is found
    private object lockObj = new object(); // Synchronization lock for updating cycleFound

    public HamiltonianCycle(int V)
    {
        this.V = V;
    }

    // Check if the current path is a Hamiltonian cycle
    private bool IsHamiltonianCycle(List<int> path, bool[,] graph)
    {
        if (path.Count == V && graph[path[path.Count - 1], path[0]])
        {
            return true;
        }
        return false;
    }

    // Backtracking function to find Hamiltonian cycle
    private void HamiltonianCycleUtil(bool[,] graph, List<int> path, bool[] visited, int pos, CancellationToken token)
    {
        if (token.IsCancellationRequested) return;

        if (path.Count == V)
        {
            if (IsHamiltonianCycle(path, graph))
            {
                lock (lockObj)
                {
                    if (!cycleFound)
                    {
                        cycleFound = true;
                        Console.WriteLine("Hamiltonian Cycle found: " + string.Join(" -> ", path));
                    }
                }
            }
            return;
        }

        for (int v = 0; v < V; v++)
        {
            if (graph[pos, v] && !visited[v])
            {
                path.Add(v);
                visited[v] = true;

                // Start new task for deeper recursion
                Task.Factory.StartNew(() => 
                {
                    bool[] visitedCopy = (bool[])visited.Clone();
                    HamiltonianCycleUtil(graph, new List<int>(path), visitedCopy, v, token);
                }, token);

                // Backtrack
                visited[v] = false;
                path.RemoveAt(path.Count - 1);
            }
        }
    }

    // Function to start the Hamiltonian cycle search
    public void FindHamiltonianCycle(bool[,] graph, int startVertex)
    {
        List<int> path = new List<int> { startVertex };
        bool[] visited = new bool[V];
        visited[startVertex] = true;

        CancellationTokenSource cts = new CancellationTokenSource();
        HamiltonianCycleUtil(graph, path, visited, startVertex, cts.Token);
    }

    static void Main()
    {
        // Example graph (directed)
        const int V = 5;
        bool[,] graph = new bool[V, V]
        {
            { false, true, true, false, false },
            { true, false, true, true, false },
            { true, true, false, true, true },
            { false, true, true, false, true },
            { true, false, true, true, false }
        };

        /*
         * 0    1   1   0   0
         * 1    0   1   1   0
         * 1    1   0   1   1
         * 0    1   1   0   1
         * 1    0   1   1   0
         *
         * graph editor:
            0 1
            0 2
            1 0
            1 2
            1 3
            2 0
            2 1
            2 3
            2 4
            3 1
            3 2
            3 4
            4 0
            4 2
            4 3
         */
        
        HamiltonianCycle hc = new HamiltonianCycle(V);

        Stopwatch stopwatch = Stopwatch.StartNew();
        hc.FindHamiltonianCycle(graph, 0); // Start from vertex 0
        stopwatch.Stop();

        Console.WriteLine($"Time taken: {stopwatch.ElapsedMilliseconds} ms");
    }
}
