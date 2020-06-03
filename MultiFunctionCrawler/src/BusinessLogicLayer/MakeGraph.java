package BusinessLogicLayer;

import DataLayer.RelationGraph;

import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;

/**
 * MakeGraph: 由论文爬取结果生成引用关系图类
 */
public class MakeGraph {
    public static final int edgeMax = 100, nodeMax = 50;

    public static String addE(int from, int to, String edgeType) {
        return String.format("node%d -> node%d%s;\n", from + 1, to + 1, edgeType);
    }

    public static void generate(RelationGraph g) throws Exception {
        //System.out.println(System.getProperty("user.dir"));
        if (g.nodeCnt > nodeMax) throw new Exception("Too many essays.", null);
        try {
            File f = new File("./saved/graph.txt"), fdir = new File("./saved");
            if (!fdir.exists()) fdir.mkdir();
            if (f.exists()) f.delete();
            f.createNewFile();
            FileOutputStream fout = new FileOutputStream(f);
            fout.write("digraph G{\nratio=0.6;\nNode[shape=record,height=.1];\n".getBytes());
            graphToTxt(fout, g);
            fout.write("}".getBytes());
            fout.close();
            Runtime rt = Runtime.getRuntime();
            Process p = rt.exec(".\\graphvis\\bin\\dot.exe -Tpng ./saved/graph.txt -o ./saved/graph.png");
            p.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (g.edgeCnt > edgeMax) throw new Exception("Too many edges; some are omitted.", null);
    }

    private static void graphToTxt(FileOutputStream fout, RelationGraph g) throws IOException {
        int eNum = 0;
        for (int i = 0; i < g.nodeCnt; i++) {
            for (int j = 0; j < g.nodeCnt; j++) {
                if (i != j && (g.mat[i][j] || g.mat[j][i])) {
                    String color = "white", name = g.nodes.get(i).name;
                    if (g.nodes.get(i).heat > 0)
                        color = String.format("0.000 %.4f 1.000", Math.abs(g.nodes.get(i).heat));
                    if (g.nodes.get(i).heat < 0)
                        color = String.format("0.482 %.4f 0.878", Math.abs(g.nodes.get(i).heat));
                    if (name.length() > 20)
                        name = name.substring(0, 17) + "...";
                    name = String.format("\"{[%d]%s|Cited:%d}\"", i + 1, name, g.nodes.get(i).citedCnt);
                    fout.write(String.format("node%d [label=%s style=filled fillcolor=\"%s\"];\n", i + 1, name, color).getBytes());
                    break;
                }
            }
        }
        for (int i = 0; i < g.nodeCnt; i++)
            for (int j = 0; j < g.nodeCnt; j++)
                if (g.mat[i][j] && eNum < edgeMax) {
                    eNum += 1;
                    if (!g.mat[j][i])
                        fout.write(addE(j, i, "").getBytes());
                    else if (i < j)
                        fout.write(addE(j, i, " [dir = \"none\"]").getBytes());
                }
    }
}