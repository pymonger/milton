package bradswebdavclient;

import javax.swing.tree.DefaultTreeModel;

public class ResourceTreeModel extends DefaultTreeModel {

  public static ResourceTreeModel create(BradsWebdavClientView frame) {
    HostsNode root = new HostsNode(frame);
    return new ResourceTreeModel(root);
  }

  HostsNode hostsNode;
  
  public ResourceTreeModel(HostsNode root) {
    super(root);
    this.hostsNode = root;           
  }

  void select(String s) {
      hostsNode.select(s);
  }
}
