/*
 * Created on 07.07.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package kobold.client.plam.model.edges;

/**
 * @author pliesmn
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MetaNode implements INode {
   public static final String OR = "OR";
   public static final String XOR = "XOR";
   public static final String AND = "AND";
   
   private String type;
   private String id;
   
   public MetaNode(String type){
       this.type = type;
   }
}
