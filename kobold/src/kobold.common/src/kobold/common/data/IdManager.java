/*
 * Copyright (c) 2003 - 2004 Necati Aydin, Armin Cont, 
 * Bettina Druckenmueller, Anselm Garbe, Michael Grosse, 
 * Tammo van Lessen,  Martin Plies, Oliver Rendgen, Patrick Schneider
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 * and/or sell copies of the Software, and to permit persons to whom the 
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in 
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING 
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
 * DEALINGS IN THE SOFTWARE.
 *
 * $Id: IdManager.java,v 1.6 2004/07/25 23:17:48 vanto Exp $
 *
 */
package kobold.common.data;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import kobold.common.KoboldCommonsPlugin;

import org.apache.commons.id.IdentifierUtils;
import org.apache.commons.id.uuid.NodeManager;
import org.apache.commons.id.uuid.UUID;
import org.apache.commons.id.uuid.clock.Clock;
import org.apache.commons.id.uuid.state.Node;
import org.apache.commons.id.uuid.state.State;
import org.apache.commons.id.uuid.state.StateHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Provides an id manager using the IETF UUID standard.
 * 
 * @author Tammo van Lessen
 */
public class IdManager 
{
    private static final Log logger = LogFactory.getLog(IdManager.class);
	
	public static String nextId(String idtype) {
		String id = ((UUID)IdentifierUtils.UUID_VERSION_ONE_GENERATOR.nextIdentifier()).toString();
		//logger.debug("new id for "+idtype+": "+id);
		return id;
	}
	
	public static class NodeManagerImpl implements NodeManager {

	    /** Reference to the State implementation to use for loading and storing */
	    private State nodeState;
	    /** The current array index for the Node in use. */
	    private int currentNodeIndex = 0;
	    /** Flag indicating the node state has been initialized. */
	    private boolean isInit = false;
	    /** Set that references all instances. */
	    private Set nodesSet;
	    /** Array of the Nodes */
	    private Node[] allNodes;
	    /** UUID timestamp of last call to State.store */
	    private long lastUUIDTimeStored = 0;
	    
	    public NodeManagerImpl() {
	        // tell common-plugin my instance.
	        KoboldCommonsPlugin.getDefault().setNodeManager(this);
	    }
	    
	    /*** Initialization */
	    public void init() {
	        nodeState = new EclipseUUIDState();
	        nodeState.load();
	        nodesSet = nodeState.getNodes();
	        Iterator it = nodesSet.iterator();
	        allNodes = new Node[nodesSet.size()];
	        int i = 0;
	        while (it.hasNext()) {
	            allNodes[i++] = (Node) it.next();
	        }
	        isInit = true;
	    }
	    
	    public void store() {
	        try {
                nodeState.store(nodesSet);
            } catch (IOException e) {
                // nothing to do here.
            }    
	    }
	    
        /* (non-Javadoc)
         * @see org.apache.commons.id.uuid.NodeManager#currentNode()
         */
        public Node currentNode()
        {
            if (!isInit) {
                init();
            }
            // See if we need to store state information.
            if ((lastUUIDTimeStored + nodeState.getSynchInterval()) > (findMaxTimestamp() / Clock.INTERVALS_PER_MILLI)) {
                try {
                    nodeState.store(nodesSet);
                } catch (IOException ioe) {
                    //@TODO add listener and send notify
                }
            }
            return allNodes[currentNodeIndex];
        }

        /* (non-Javadoc)
         * @see org.apache.commons.id.uuid.NodeManager#nextAvailableNode()
         */
        public Node nextAvailableNode()
        {
            if (!isInit) {
                init();
            }
            currentNodeIndex++;
            if (currentNodeIndex >= allNodes.length) {
                currentNodeIndex = 0;
            }
            return currentNode();        
        }

        /**
         * <p>Returns the maximum uuid timestamp generated from all <code>Node</code>s</p>
         *
         * @return maximum uuid timestamp generated from all <code>Node</code>s.
         */
        private long findMaxTimestamp() {
            if (!isInit) {
                init();
            }
            long max = 0;
            for (int i = 0; i < allNodes.length; i++) {
                if (allNodes[i] != null && allNodes[i].getLastTimestamp() > max) {
                    max = allNodes[i].getLastTimestamp();
                }
            }
            return max;
        }
        
        public void lockNode(Node arg0)
        {
            // not implemented
        }
        public void releaseNode(Node arg0)
        {
            // not implemented
        }
	    
	}
	
	private static class EclipseUUIDState implements State
	{
	    private HashSet nodes = new HashSet(1);
	    private Node node = null;
	    
	    /**
	     * @see org.apache.commons.id.uuid.state.State#load()
	     */
	    public void load() throws IllegalStateException
	    {
	        String nodeId = KoboldCommonsPlugin.getDefault().getPluginPreferences().getString("uuid-nodeid");
	        long lastTime = KoboldCommonsPlugin.getDefault().getPluginPreferences().getLong("uuid-lasttime");
	        short seq = (short)KoboldCommonsPlugin.getDefault().getPluginPreferences().getInt("uuid-clockseq");
	        
	        node = null;
	        if ("".equals(nodeId)) {
	            logger.debug("no node found, creating a new one");
	            node = new Node(StateHelper.randomNodeIdentifier());    
	        } else {
	            logger.debug("node found. using node "+ nodeId +"");
	            node = new Node(StateHelper.decodeMACAddress(nodeId), lastTime, seq);
	        }
	        
	        nodes.add(node);
	    }

	    /**
	     * @see org.apache.commons.id.uuid.state.State#getNodes()
	     */
	    public Set getNodes()
	    {
	        return nodes;
	    }

	    /**
	     * @see org.apache.commons.id.uuid.state.State#store(java.util.Set)
	     */
	    public void store(Set arg0) throws IOException
	    {
	        KoboldCommonsPlugin.getDefault().getPluginPreferences().setValue("uuid-nodeid", StateHelper.encodeMACAddress(node.getNodeIdentifier()));
	        KoboldCommonsPlugin.getDefault().getPluginPreferences().setValue("uuid-lasttime", node.getLastTimestamp());
	        KoboldCommonsPlugin.getDefault().getPluginPreferences().setValue("uuid-clockseq", node.getClockSequence());
	        KoboldCommonsPlugin.getDefault().savePluginPreferences();
	        logger.debug("uuid node stored.");
	    }

	    /**
	     * @see org.apache.commons.id.uuid.state.State#store(java.util.Set, long)
	     */
	    public void store(Set arg0, long arg1)
	    {
	       logger.error("store(Set, int) not implemented!");
	    }

	    /**
	     * @see org.apache.commons.id.uuid.state.State#getSynchInterval()
	     */
	    public long getSynchInterval()
	    {
	        return 3000;//Long.MAX_VALUE;
	    }

	}

}
