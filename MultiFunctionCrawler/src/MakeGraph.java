import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MakeGraph {
    public static final int edgeMax = 100, nodeMax = 50;

    public static String addE(String from, String to) {
        return from + "->" + to + ";\n";
    }

    public static void generate(Graph g) throws Exception {
        //System.out.println(System.getProperty("user.dir"));
        if (g.edgeCnt >= nodeMax) throw new Exception("Too many essays.", null);
        try {
            File f = new File(".\\saved\\graph.txt");
            if (f.exists()) f.delete();
            f.createNewFile();
            FileOutputStream fout = new FileOutputStream(f);
            fout.write("digraph G{\n".getBytes());
            graphToTxt(fout, g);
            fout.write("}".getBytes());
            fout.close();
            Runtime rt = Runtime.getRuntime();
            rt.exec(System.getProperty("user.dir") + ".\\lib\\graphvis\\bin\\dot.exe -Tpng .\\saved\\graph.txt -o .\\saved\\graph.png");
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
        }
        for (int i = 0; i < g.nodeCnt; i++)
            for (int j = 0; j < g.nodeCnt; j++)
                if (g.mat[i][j] && eNum < edgeMax) {
                    eNum += 1;
                    fout.write(addE(g.nodes.get(i).name, g.nodes.get(j).name).getBytes());
                }
    }
}
