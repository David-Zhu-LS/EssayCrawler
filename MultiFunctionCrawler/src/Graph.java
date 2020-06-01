import java.util.List;

public class Graph {
    int nodeCnt, edgeCnt, minFlow = Integer.MAX_VALUE, maxFlow = 0, flowRange;
    List<Essay> nodes;
    boolean[][] mat;
    Graph(int _nodeCnt, int _edgeCnt, List<Essay> _nodes, boolean[][] _mat){
        nodeCnt = _nodeCnt;
        edgeCnt = _edgeCnt;
        nodes = _nodes;
        mat = _mat;
        for (int i = 0; i < nodeCnt; i++) {
            if (nodes.get(i).citedCnt < minFlow) minFlow = nodes.get(i).citedCnt;
            if (nodes.get(i).citedCnt > maxFlow) maxFlow = nodes.get(i).citedCnt;
        }
        flowRange = maxFlow - minFlow;
        if (flowRange == 0) {
            for (int i = 0; i < nodeCnt; i++)
                nodes.get(i).heat = 0;
        } else {
            for (int i = 0; i < nodeCnt; i++)
                nodes.get(i).heat = (2.0 * (nodes.get(i).citedCnt - minFlow)) / flowRange - 1;
        }
    }
}