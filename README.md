# VMware 2nd Interview - Create a class which represents a DAG (directed acyclic graph)

The requirements are:  
Create a class which represents a DAG (directed acyclic graph) and has the following methods:  
**Node createNode(String value)**: Creates a new node in the graph. Returns the created Node.  
**void createEdge(Node src, Node dest)**: Creates a new edge which connects two existing nodes in the graph. The direction of the new edge is from src to dest.  
**boolean isAncestor(Node src, Node dest)**: Return true if and only if there is a (directed) path from src to dest.  
**Set<Node> findAllAncestors(Node dest)**: Returns a set of all ancestors of dest.  

See my solution in class:  
***com.vmware.DAG***

Hope it helps you :)  
