package lab;

import java.util.Map;
import java.util.Scanner;

import static lab.ShowGraph.showDAG;
import static lab.ShowGraph.showG;

public class Main {
    public static void main(String[] args) {
        Graph G;
        int choice;
        int choice2;
        Scanner in = new Scanner(System.in);
        System.out.println("Использовать готовый граф(1) или сгенерировать(2)?");
        choice = in.nextInt();
        if (choice == 1) {
            int[][] M2 = {
                    {1, 1, 1, 1, 1, 0, 0, 0},
                    {1, 1, 1, 1, 1, 0, 0, 0},
                    {1, 1, 1, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 1, 1, 1},
                    {0, 0, 0, 0, 0, 1, 1, 1},
                    {0, 0, 0, 1, 1, 0, 1, 1},
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
            };
            System.out.println("Граф имеет следующую матрицу смежности:");
            System.out.println("{");
            for (int i = 0; i < M2.length; i++) {
                System.out.print("{");
                for (int j = 0; j < M2[0].length; j++) {
                    System.out.print(M2[i][j]);
                    if (j != M2[0].length - 1) {
                        System.out.print(", ");
                    }
                }
                System.out.print("},");
                System.out.print("\n");
            }
            System.out.println("};");
            G = new Graph(M2);
        }
        else {
            G = new Graph();
            System.out.println("Введите размер графа ");
            choice = in.nextInt();
            System.out.println("Введите '1' если граф плотный и '2' если разреженный ");
            choice2 = in.nextInt();
            G.generate(choice, choice2==1?'y':'r', 10);
            int[][] M = G.getMatrix();
            System.out.println("Граф имеет следующую матрицу смежности:");
            System.out.println("{");
            for (int i = 0; i < M.length; i++) {
                System.out.print("{");
                for (int j = 0; j < M[0].length; j++) {
                    System.out.print(M[i][j]);
                    if (j != M[0].length - 1) {
                        System.out.print(", ");
                    }
                }
                System.out.print("},");
                System.out.print("\n");
            }
            System.out.println("};");
        }
        long startTime = System.nanoTime();
        DAGraph dag = G.DAGCompression();
        long endTime = System.nanoTime();
        double algTime = (double) (endTime - startTime) / 1000000;
        System.out.println("Граф был DAG-сжат за " + algTime + " мс");
        SwitchingGraph swg = new SwitchingGraph(dag);
        System.out.println("Граф был преобразован в switching graph");
        choice2 = 1;
        while (choice2 != 0) {
            System.out.println("Введите:\n'1' чтобы вывести switching graph,\n'2' для запуска DFS,\n'3' для запуска BFS," +
                    "\n'4' для запуска алгоритма Дейкстры,\n'5' чтобы вывести исходный граф,\n'6' чтобы вывести DAG-сжатый граф" +
                    "\n'7' чтобы изменить вес ребра switching graph,\n'0' чтобы выйти.");
            choice2 = in.nextInt();
            switch (choice2) {
                case 1:
                    swg.printGraph();
                    break;
                case 2:
                    System.out.println("Введите стартовую вершину для обхода в глубину ");
                    choice = in.nextInt();
                    swg.DFS(choice);
                    break;
                case 3:
                    System.out.println("Введите стартовую вершину для обхода в ширину ");
                    choice = in.nextInt();
                    swg.BFS(choice);
                    break;
                case 4:
                    System.out.println("Введите стартовую вершину для алгоритма Дейкстры ");
                    choice = in.nextInt();
                    Map<Integer, Integer> distances = swg.dijkstra(choice);
                    System.out.println("Расстояния, найденные алгоритмом Дейкстры  от вершины " + choice);
                    for (int t = 0; t < swg.Size; t++) {
                        if (distances.get(t) < Integer.MAX_VALUE) {
                            System.out.println(t + ": " + distances.get(t));
                        }
                        else {
                            System.out.println(t + ": " + "inf");
                        }
                    }
                    break;
                case 5:
                    showG(G);
                    break;
                case 6:
                    showDAG(dag);
                    break;
                case 7:
                    System.out.println("Введите первую вершину ребра");
                    int u = in.nextInt();
                    System.out.println("Введите вторую вершину ребра");
                    int v = in.nextInt();
                    System.out.println("Введите новый вес ребра");
                    int w = in.nextInt();
                    swg.changeEdgeWeight(u, v, w);
                    break;
                default:
                    break;
            }
        }
    }
}