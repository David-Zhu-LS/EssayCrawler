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

    //往txt文件中写入一条边
    public static String addE(int from, int to, String edgeType) {
        return String.format("node%d -> node%d%s;\n", from + 1, to + 1, edgeType);
    }

    public static void generate(RelationGraph g) throws Exception {
        //顶点太多，直接报错
        if (g.nodeCnt > nodeMax) throw new Exception("Too many essays.", null);
        try {
            File f = new File("./saved/graph.txt"), fdir = new File("./saved");
            if (!fdir.exists()) fdir.mkdir();
            if (f.exists()) f.delete();
            f.createNewFile();
            FileOutputStream fout = new FileOutputStream(f);
            //开始准备制图的txt文件
            fout.write("digraph G{\nratio=0.6;\nNode[shape=record,height=.1];\n".getBytes());
            graphToTxt(fout, g);
            fout.write("}".getBytes());
            fout.close();
            //命令行调用graphviz的dot.exe执行txt生成png图像操作
            Runtime rt = Runtime.getRuntime();
            Process p = rt.exec(".\\graphvis\\bin\\dot.exe -Tpng ./saved/graph.txt -o ./saved/graph.png");
            p.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //若边太多，仍能生成结果，但会提示有的边被省去了
        if (g.edgeCnt > edgeMax) throw new Exception("Too many edges; some are omitted.", null);
    }

    //用一个数据图处理得到描述文档txt，用于生成可视图
    private static void graphToTxt(FileOutputStream fout, RelationGraph g) throws IOException {
        int eNum = 0;
        //先加顶点，设置其标签及颜色
        for (int i = 0; i < g.nodeCnt; i++) {
            for (int j = 0; j < g.nodeCnt; j++) {
                if (i != j && (g.mat[i][j] || g.mat[j][i])) {
                    String color = "white", name = g.nodes.get(i).name;
                    //用heat设定颜色，heat为(-1,1)，对应论文从冷门到爆款
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
        //再加边，由于百度学术的缺陷，一些不恰当的引用关系需要特别处理
        for (int i = 0; i < g.nodeCnt; i++)
            for (int j = 0; j < g.nodeCnt; j++)
                if (g.mat[i][j] && eNum < edgeMax) {
                    eNum += 1;
                    if (!g.mat[j][i])
                        fout.write(addE(j, i, "").getBytes());
                    else if (i < j) //相互引用的错误情况
                        fout.write(addE(j, i, " [dir = \"none\"]").getBytes());
                }
    }
}