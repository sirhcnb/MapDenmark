/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mapdenmark;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Christopher
 */
public class MapModel
{
      final HashMap<Integer, NodeData> nodes = new HashMap<>();
      final ArrayList<EdgeData> edges = new ArrayList<>();
            
    public void loadData() throws IOException {
            String dir = "";
 

 
            KrakLoader loader = new KrakLoader()
            {
                @Override
                public void processNode(NodeData nd)
                {
                    nodes.put(nd.KDV, nd);
                }
 
                @Override
                public void processEdge(EdgeData ed)
                {
                    edges.add(ed);
                }
            };
 
            loader.load(dir + "kdv_node_unload.txt",
                    dir + "kdv_unload.txt");
    }
}
