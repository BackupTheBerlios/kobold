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
 * $Id: KoboldMessageSorter.java,v 1.1 2004/10/08 15:46:38 martinplies Exp $
 *
 */
package kobold.client.plam.workflow;

import java.util.Comparator;

import kobold.common.data.AbstractKoboldMessage;
import kobold.common.data.KoboldMessage;
import kobold.common.data.WorkflowMessage;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;


/**
 * @author meiner
 *
 *  Sorter of the WorkflowView
 */
public class KoboldMessageSorter extends ViewerSorter
{
    
    private Comparator comparator;
    private boolean inverse = false;
    private Comparator dateComparator = new DateComparator();
    private Comparator subjectComparator = new SubjectComparator();
    private Comparator senderComparator = new SenderComparator();
    private Comparator typeComparator = new TypeComparator();      
    
    public void setComaparator(Comparator comparator){
        this.comparator = comparator;
    }
    
    public void setSortInverse(boolean inverse){
        this.inverse = inverse;
    }
    
    public boolean isSortInverse() {
        return inverse;
    }
    
    /**
     * called to compare the tabelitems (Workflowmessages)
     * @see org.eclipse.jface.viewers.ViewerSorter#compare(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
     */
    public int compare(Viewer viewer,
            Object e1,
            Object e2){
        if (comparator == null) {
            this.setSortByDate(); // default
        }
         if (inverse ){
            return comparator.compare( e1, e2) * -1;
         } else {
            return comparator.compare( e1, e2);
         }
    }
    
    public Comparator getComparator(){
        return this.comparator;
    }
    
    public void setSortByDate(){
        this.comparator = dateComparator;
    }
    
    public void setSortBySubject(){
        this.comparator = subjectComparator;
    }
    
    public void setSortBySender(){
        this.comparator = senderComparator;
    }
    
    public void setSortByType(){
        this.comparator = typeComparator;
    }

    
    
   /**
	 * Sorts Kobold Messages by Date
	 */
	public class DateComparator implements Comparator{

		/**
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(Object o1, Object o2) {
			if (o1 instanceof AbstractKoboldMessage && o2 instanceof AbstractKoboldMessage) {
			    //  o2 and o1 are interchanged, so the newest messages are on non inverse sort at head.
				return ((AbstractKoboldMessage)o2).getDate().compareTo(((AbstractKoboldMessage)o1).getDate());
			} else return 1;
		}

	}
	
	public class SubjectComparator implements Comparator{

		/**
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(Object o1, Object o2) {
			if (o1 instanceof AbstractKoboldMessage && o2 instanceof AbstractKoboldMessage) {
				return ((AbstractKoboldMessage)o1).getSubject().compareTo(((AbstractKoboldMessage)o2).getSubject());
			} else return 0;
		}

	}
	
	public class SenderComparator implements Comparator{

		/**
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(Object o1, Object o2) {
			if (o1 instanceof AbstractKoboldMessage && o2 instanceof AbstractKoboldMessage) {
				return ((AbstractKoboldMessage)o1).getSender().compareTo(((AbstractKoboldMessage)o2).getSender());
			} else return 0;
		}
		
	}
	
	
	public class TypeComparator implements Comparator{
		public int compare(Object o1, Object o2) {
			if (o1 instanceof KoboldMessage && o2 instanceof WorkflowMessage) {
				return 1;
			}
			else if (o2 instanceof KoboldMessage && o1 instanceof WorkflowMessage) {
			    return -1;			
			} else return 0;
		}	  
	}

	public class ReferringToComparator implements Comparator{

		/**
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(Object o1, Object o2) {
			if (o1 instanceof AbstractKoboldMessage && o2 instanceof AbstractKoboldMessage) {
				return ((AbstractKoboldMessage)o1).getProductline().compareTo( ((AbstractKoboldMessage)o2).getProductline() );
			} else return 0;
		}

	}
}
