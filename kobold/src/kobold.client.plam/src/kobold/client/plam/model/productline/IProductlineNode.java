/*
 * Created on 22.07.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package kobold.client.plam.model.productline;

import java.util.List;

import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.edges.INode;


/**
 * @author pliesmn
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface IProductlineNode extends INode {
  /**
   * The returned list must not contain methanodes!
 * @return Retruns all Children, that are productline nodes.
 */
public List getChildren();


public String getType();


/**
 * @return
 */
public AbstractAsset getParent();

}
