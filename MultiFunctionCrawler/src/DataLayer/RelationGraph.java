package DataLayer;

import java.util.List;

/**
 * RelationGraph: 论文引用关系图类
 */
public class RelationGraph {
    public int nodeCnt, edgeCnt, minFlow = Integer.MAX_VALUE, maxFlow = 0, flowRange;
    public List<Essay> nodes;
    public boolean[][] mat;

    public RelationGraph(int _nodeCnt, int _edgeCnt, List<Essay> _nodes, boolean[][] _mat) {
        nodeCnt = _nodeCnt;
        edgeCnt = _edgeCnt;
        nodes = _nodes;
        mat = _mat;
        for (int i = 0; i < nodeCnt; i++) {
            if (nodes.get(i).citedCnt < minFlow) minFlow = nodes.get(i).citedCnt;
            if (nodes.get(i).citedCnt > maxFlow) maxFlow = nodes.get(i).citedCnt;
        }
        flowRange = maxFlow - minFlow;
        //更新图中结点的heat，用来衡量在图中相对的热度高低
        if (flowRange == 0) {
            for (int i = 0; i < nodeCnt; i++)
                nodes.get(i).heat = 0;
        } else {
            for (int i = 0; i < nodeCnt; i++)
                nodes.get(i).heat = (2.0 * (nodes.get(i).citedCnt - minFlow)) / flowRange - 1;
        }
    }
}