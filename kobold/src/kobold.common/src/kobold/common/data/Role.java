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
 * $Id: Role.java,v 1.5 2004/05/04 22:31:12 garbeam Exp $
 *
 */
package kobold.common.data;

import org.dom4j.Element;

/**
 * This is the base class for RoleP, -PE and -PLE. a Role-Object is always
 * associated to a specific user and stores information about the user's
 * permissions that are related to that Role
 *
 * @author garbeam
 */
public abstract class Role {

	/**
	 * Creates the specific role and returns it.
	 * 
	 * @param element
	 * @return
	 */
	public static Role createRole(Element element) {

		String roleType = element.selectSingleNode("//roles/role").getText();
		if (roleType == "PLE") {
			return new RolePLE(element);
		} else if (roleType == "PE") {
			return new RolePE(element);
		} else {
			return new RoleP(element);
		}
	}

	/**
	 * Serializes this object.
	 * 
	 * @param roles DOM parent element to attach this role.
	 */
	public abstract void serialize(Element roles);

}
