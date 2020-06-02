import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;

public class MakeGraph {
    public static final int edgeMax = 100, nodeMax = 50;

    public static String addE(int from, int to) {
        return String.format("node%d -> node%d;\n", from, to);
    }

    public static void generate(Graph g) throws Exception {
        //System.out.println(System.getProperty("user.dir"));
        if (g.nodeCnt > nodeMax) throw new Exception("Too many essays.", null);
        try {
            File f = new File("./saved/graph.txt"), fdir = new File("./saved");
            if (!fdir.exists()) fdir.mkdir();
            if (f.exists()) f.delete();
            f.createNewFile();
            FileOutputStream fout = new FileOutputStream(f);
            fout.write("digraph G{\nratio=0.6;\n".getBytes());
            graphToTxt(fout, g);
            fout.write("}".getBytes());
            fout.close();
            Runtime rt = Runtime.getRuntime();
            Process p = rt.exec("graphvis\\bin\\dot.exe -Tpng ./saved/graph.txt -o ./saved/graph.png");
            p.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (g.edgeCnt > edgeMax) throw new Exception("Too many edges; some are omitted.", null);
    }

    private static void graphToTxt(FileOutputStream fout, Graph g) throws IOException {
        int eNum = 0;
        for (int i = 0; i < g.nodeCnt; i++) {
            String color = "white", name = g.nodes.get(i).name;
            if (g.nodes.get(i).heat > 0)
                color = String.format("0.000 %.4f 1.000", Math.abs(g.nodes.get(i).heat));
            if (g.nodes.get(i).heat < 0)
                color = String.format("0.482 %.4f 0.878", Math.abs(g.nodes.get(i).heat));
            if (name.length() > 20)
                name = name.substring(0, 17) + "...";
            fout.write(String.format("node%d [label=\"[%d]%s\" style=filled fillcolor=\"%s\"];\n", i, i, name, color).getBytes());
        }
        for (int i = 0; i < g.nodeCnt; i++)
            for (int j = 0; j < g.nodeCnt; j++)
                if (g.mat[i][j] && eNum < edgeMax) {
                    eNum += 1;
                    fout.write(addE(i, j).getBytes());
                }
    }
}
