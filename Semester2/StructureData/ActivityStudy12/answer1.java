import java.util.*;

public class answer1 {

    static List<List<Integer>> graph;
    static boolean[] visited;

    // DFS function to search for target node
    static void dfs(int node, int target) {
        visited[node] = true;
        System.out.println("DFS: Visiting node " + node + " (a" + (node + 1) + ")");

        if (node == target) {
            System.out.println("DFS: Found target " + target + " (a" + (target + 1) + ")");
            return;
        }

        for (int neighbor : graph.get(node)) {
            if (!visited[neighbor]) {
                dfs(neighbor, target);
            }
        }
    }

    // BFS function to search for target node
    static void bfs(int start, int target) {
        Queue<Integer> queue = new LinkedList<>();
        queue.add(start);
        visited = new boolean[8];
        visited[start] = true;

        System.out.println("BFS: Starting from node " + start + " (a" + (start + 1) + ")");
        System.out.println("BFS: Initial queue: [" + start + "]");

        while (!queue.isEmpty()) {
            int node = queue.poll();
            System.out.println("BFS: Dequeue node " + node + " (a" + (node + 1) + ")");

            if (node == target) {
                System.out.println("BFS: Found target " + target + " (a" + (target + 1) + ")");
                return;
            }

            System.out.print("BFS: Neighbors of " + node + ": ");
            for (int neighbor : graph.get(node)) {
                System.out.print(neighbor + " ");
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    queue.add(neighbor);
                }
            }
            System.out.println();
            System.out.println("BFS: Queue after enqueue: " + queue);
        }
    }

    public static void main(String[] args) {
        // Initialize graph with 8 nodes (0 to 7 representing a1 to a8)
        graph = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            graph.add(new ArrayList<>());
        }

        // Define undirected graph connections (adjacency list)
        // a1 (0) connected to a2 (1), a3 (2)
        graph.get(0).add(1);
        graph.get(0).add(2);
        graph.get(1).add(0); // reverse
        graph.get(2).add(0); // reverse
        // a2 (1) connected to a4 (3), a5 (4)
        graph.get(1).add(3);
        graph.get(1).add(4);
        graph.get(3).add(1); // reverse
        graph.get(4).add(1); // reverse
        // a3 (2) connected to a6 (5)
        graph.get(2).add(5);
        graph.get(5).add(2); // reverse
        // a4 (3) connected to a7 (6)
        graph.get(3).add(6);
        graph.get(6).add(3); // reverse
        // a5 (4) connected to a8 (7)
        graph.get(4).add(7);
        graph.get(7).add(4); // reverse

        int n = 5; // Target node index (a6), "angka n" = 5

        System.out.println("Graf dengan 8 node (a1 sampai a8):");
        for (int i = 0; i < 8; i++) {
            System.out.print("a" + (i + 1) + ": ");
            for (int j : graph.get(i)) {
                System.out.print("a" + (j + 1) + " ");
            }
            System.out.println();
        }

        System.out.println("\n1. Pencarian menggunakan DFS untuk angka n = " + n + " (a" + (n + 1) + ") mulai dari a1:");
        System.out.println("Penjelasan tahap demi tahap DFS:");
        System.out.println("- DFS menggunakan pendekatan depth-first: eksplorasi cabang paling dalam terlebih dahulu sebelum kembali.");
        System.out.println("- Mulai dari node awal, tandai sebagai visited, periksa jika target, lalu rekursif ke tetangga yang belum visited.");
        visited = new boolean[8];
        dfs(0, n);

        System.out.println("\n2. Pencarian menggunakan BFS untuk angka n = " + n + " (a" + (n + 1) + ") mulai dari a1:");
        System.out.println("Penjelasan tahap demi tahap BFS:");
        System.out.println("- BFS menggunakan pendekatan breadth-first: eksplorasi semua tetangga level saat ini sebelum ke level berikutnya.");
        System.out.println("- Gunakan queue: enqueue node awal, dequeue, periksa target, enqueue tetangga yang belum visited.");
        bfs(0, n);
    }
}
