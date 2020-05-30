package graphvis;

import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Node {
    String name;
    int toMe, fromMe, bothDir;
    double heat;

    public Node(String name) {
        this.name = name;
    }
}

class Graph {
    int nodeCnt, edgeCnt;
    List<Node> nodes;
    boolean[][] mat;
}

public class genGraph {
    public static final int edgeMax = 100, nodeMax = 50;
    public static int minFlow = nodeMax - 1, maxFlow = 0, flowRange;

    public static String addE(String from, String to) {
        return from + "->" + to + ";\n";
    }

    public static void main(String[] args) throws Exception {
        Graph g = new Graph();
        g.nodeCnt = nodeMax;
        g.edgeCnt = 0;
        g.nodes = new ArrayList<>();
        g.mat = new boolean[nodeMax][nodeMax];
        Random random = new Random();
        System.out.println(System.getProperty("user.dir"));
        for (int i = 0; i < nodeMax; i++)
            g.nodes.add(new Node(Integer.toString(i + 1)));
        for (int i = 0; i < nodeMax; i++) {
            for (int j = 0; j < nodeMax; j++) {
                if (i == j) continue;
                if (random.nextInt(25) == 1) {
                    g.edgeCnt += 1;
                    g.mat[i][j] = true;
                    g.nodes.get(i).fromMe += 1;
                    g.nodes.get(i).bothDir += 1;
                    g.nodes.get(j).toMe += 1;
                    g.nodes.get(j).bothDir += 1;
                }
            }
        }
        System.out.println(g.edgeCnt);

        for (int i = 0; i < nodeMax; i++) {
            if (g.nodes.get(i).bothDir < minFlow) minFlow = g.nodes.get(i).bothDir;
            if (g.nodes.get(i).bothDir > maxFlow) maxFlow = g.nodes.get(i).bothDir;
        }
        flowRange = maxFlow - minFlow;
        if (flowRange == 0) {
            for (int i = 0; i < nodeMax; i++)
                g.nodes.get(i).heat = 0;
        } else {
            for (int i = 0; i < nodeMax; i++)
                g.nodes.get(i).heat = (2.0 * (g.nodes.get(i).bothDir - minFlow)) / flowRange - 1;
        }
        File f = new File("graph.txt");
        try {
            if (!f.exists()) f.createNewFile();
            FileOutputStream fout = new FileOutputStream(f, true);
            fout.write("digraph G{\n".getBytes());
            graphToTxt(fout, g);
            fout.write("}".getBytes());
            fout.close();
            Runtime rt = Runtime.getRuntime();
            rt.exec(System.getProperty("user.dir") + "\\graphvis\\graphvis\\bin\\dot.exe -Tpng graph.txt -o graph.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (g.edgeCnt >= edgeMax) throw new Exception("Too many edges; some are omitted.", null);
    }

    private static void graphToTxt(FileOutputStream fout, Graph g) throws IOException {
        int eNum = 0;
        for (int i = 0; i < g.nodeCnt; i++) {
            String color = "white";
            if (g.nodes.get(i).heat > 0)
                color = String.format("0.000 %.4f 1.000", Math.abs(g.nodes.get(i).heat));
            if (g.nodes.get(i).heat < 0)
                color = String.format("0.482 %.4f 0.878", Math.abs(g.nodes.get(i).heat));
            fout.write((g.nodes.get(i).name + " [style=filled fillcolor=\"" + color + "\"];").getBytes());
            for (int j = 0; j < g.nodeCnt; j++)
                if (g.mat[i][j] && eNum < edgeMax) {
                    eNum += 1;
                    fout.write(addE(g.nodes.get(i).name, g.nodes.get(j).name).getBytes());
                }
        }
    }
}
