package kobold.common.model;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 */
public class EdgeMatrix {
	private static final Log log = LogFactory.getLog(EdgeMatrix.class);
	
	public static final int INIT_DIMENSION = 100;
	private List assets = new ArrayList(INIT_DIMENSION);
	
	private int[] edges = new int[INIT_DIMENSION * INIT_DIMENSION];
	
	public EdgeMatrix() {
	}
	
	public EdgeMatrix(String fileName) {
		//TODO
	}

	public void addEdge(AbstractAsset from, AbstractAsset to, int value) {
		int fromIndex = assets.indexOf(from);
		if (fromIndex == -1) {
			fromIndex = assets.size();
			assets.add(fromIndex, from);
		}
		
		int toIndex = assets.indexOf(to);
		if (toIndex == -1) {
			toIndex = assets.size();
			assets.add(toIndex, to);
		}

		if (assets.size() > edges.length) {
			log.info("expand Matrix");
			int newSize = (assets.size() * 3) / 2; 
			int[] newEdges = new int[newSize*newSize];
			System.arraycopy(edges, 0, newEdges, 0, edges.length);
			edges = newEdges;
			log.info("Matrix expanded to "+ newSize);
		}
		
		edges[fromIndex * toIndex] = value;
	}
	
	public int getValue(AbstractAsset from, AbstractAsset to) {
		int fromIndex = assets.indexOf(from);
		if (fromIndex == -1) {
			throw new IllegalArgumentException("fromAsset doesn't exist");
		}
		
		int toIndex = assets.indexOf(to);
		if (toIndex == -1) {
			throw new IllegalArgumentException("toAsset doesn't exist");
		}
		
		return edges[fromIndex * toIndex];
	}
	
	public void serialize(String filename) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(filename));

		bw.write("ASSETS\n");
		Iterator it = assets.iterator();
		while(it.hasNext()) {
			AbstractAsset asset = (AbstractAsset)it.next();
			bw.write(asset.getId() + "\n");
		}
		
		bw.write("ADJA\n");
		for (int i = 0; i < edges.length; i++) {
			bw.write(edges[i]);
			bw.write('\n');
		}
		
		bw.close();
	}

	public void deserialize(String filename) throws IOException {
		// TODO
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filename));
		int value;
		while ((value = bis.read()) != -1) {
			
		}
	}
	
}
