/*
 * $Id: ProductNodeList.java,v 1.1 2009-04-28 14:39:33 lveci Exp $
 *
 * Copyright (C) 2002 by Brockmann Consult (info@brockmann-consult.de)
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation. This program is distributed in the hope it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.esa.beam.framework.datamodel;

import org.esa.beam.util.Guardian;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A type-safe list for elements of the type <code>ProductNode</code>.
 *
 * @author Norman Fomferra
 * @version $Revision: 1.1 $ $Date: 2009-04-28 14:39:33 $
 */
public final class ProductNodeList<T extends ProductNode> {

    private final List<T> _nodes;
    private final List<T> _removedNodes;

    /**
     * Constructs a new list named nodes.
     */
    public ProductNodeList() {
        _nodes = new ArrayList<T>();
        _removedNodes = new ArrayList<T>();
    }

    /**
     * Constructs a new list named nodes.
     * @deprecated in 4.1, not required anymore
     */
    public ProductNodeList(final Class<T> elemType) {
        this();
    }

    /**
     * Gets the element type of this list.
     * @deprecated in 4.1, not required anymore
     */
    public Class<? extends ProductNode> getElemType() {
        return ProductNode.class;
    }

    /**
     * Gets the size of this list.
     */
    public final int size() {
        return _nodes.size();
    }

    /**
     * Gets the element at the spcified index.
     *
     * @param index the index, must be in the range zero to <code>size()</code>
     */
    public final T getAt(int index) {
        return _nodes.get(index);
    }

    /**
     * Gets the names of all nodes contained in this list. If this list is empty a zero-length array is returned.
     *
     * @return a string array containing all node names, never <code>null</code>
     */
    public final String[] getNames() {
        String[] names = new String[_nodes.size()];
        for (int i = 0; i < _nodes.size(); i++) {
            names[i] = _nodes.get(i).getName();
        }
        return names;
    }

    /**
     * Gets the display names of all nodes contained in this list.
     * If this list is empty a zero-length array is returned.
     *
     * @return a string array containing all node display names, never <code>null</code>
     * @see ProductNode#getDisplayName()
     */
    public String[] getDisplayNames() {
        String[] displayNames = new String[_nodes.size()];
        for (int i = 0; i < _nodes.size(); i++) {
            displayNames[i] = _nodes.get(i).getDisplayName();
        }
        return displayNames;
    }

    /**
     * Gets the element with the given name. The method performs a case insensitive search.
     *
     * @param name the name of the node, must not be <code>null</code>
     *
     * @return the node with the given name or <code>null</code> if a node with the given name is not contained in this
     *         list
     *
     * @throws IllegalArgumentException if the name is <code>null</code>
     */
    public final T get(String name) {
        int n = size();
        for (int i = 0; i < n; ++i) {
            if (_nodes.get(i).getName().equalsIgnoreCase(name)) {
                return _nodes.get(i);
            }
        }
        return  null;
    }

    /**
     * Gets the element with the given display name.
     *
     * @param displayName the display name of the node, must not be <code>null</code>
     *
     * @return the node with the given display name or <code>null</code> if a node with the given display name is not contained in this
     *         list
     *
     * @throws IllegalArgumentException if the display name is <code>null</code>
     * @see ProductNode#getDisplayName()
     */
    public T getByDisplayName(String displayName) {
        Guardian.assertNotNull("displayName", displayName);
        for (T node : _nodes) {
            if (node.getDisplayName().equals(displayName)) {
                return node;
            }
        }
        return  null;
    }

    /**
     * Tests if this list contains a node with the given name.
     *
     * @param name the name of the node, must not be <code>null</code>
     *
     * @return true if this list contains a node with the given name.
     *
     * @throws IllegalArgumentException if the name is <code>null</code>
     */
    public final boolean contains(String name) {
        int n = size();
        for (int i = 0; i < n; ++i) {
            if (_nodes.get(i).getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Tests if this list contains the given node.
     *
     * @param node the node
     *
     * @return true if this list contains the given node.
     *
     * @throws IllegalArgumentException if the node is <code>null</code>
     */
    public final boolean contains(T node) {
        if (node == null) {
            return false;
        }
        return _nodes.contains(node);
    }

    /**
     * Adds a new node to this list. Note that <code>null</code> nodes are not added to this list.
     *
     * @param node the node to be added, ignored if <code>null</code>
     *
     * @return true if the node was added, otherwise false.
     */
    public final boolean add(T node) {
        if (node != null) {
            return _nodes.add(node);
        }
        return false;
    }

    /**
     * Inserts a new node to this list at the given index. Note that <code>null</code> nodes are not added to this
     * list.
     *
     * @param node  the node to be added, ignored if <code>null</code>
     * @param index the insert index
     *
     * @throws ArrayIndexOutOfBoundsException if the index was invalid.
     */
    public final void insert(T node, int index) {
        if (node != null) {
            _nodes.add(index, node);
        }
    }

    /**
     * Clears the internal removed product nodes list.
     */
    public void clearRemovedList() {
        _removedNodes.clear();
    }

    /**
     * Gets all removed product nodes.
     *
     * @return a collection of all removed product nodes.
     */
    public Collection<T> getRemovedNodes() {
        return _removedNodes;
    }

    /**
     * Removes the given node from this list. The removed nodes will be added to the internal list of removed product
     * nodes.
     *
     * @param node the node to be removed, ignored if <code>null</code>
     *
     * @return <code>true</code> if the node is a member of this list and could successfully be removed,
     *         <code>false</code> otherwise
     */
    public final boolean remove(T node) {
        if (node != null) {
            _removedNodes.add(node);
            return _nodes.remove(node);
        }
        return false;
    }

    /**
     * Removes all nodes from this list.
     */
    public final void removeAll() {
        _removedNodes.addAll(_nodes);
        _nodes.clear();
    }

    /**
     * Releases all of the resources used by this object instance and all of its owned children. Its primary use is to
     * allow the garbage collector to perform a vanilla job.
     * <p/>
     * <p>This method should be called only if it is for sure that this object instance will never be used again. The
     * results of referencing an instance of this class after a call to <code>dispose()</code> are undefined.
     */
    public final void dispose() {
        for (int i = 0; i < size(); i++) {
            getAt(i).dispose();
        }
        removeAll();
        disposeRemovedList();
    }

    /**
     * Creates a subset of this list using the given filter.
     *
     * @param filter the product node filter to be used, if <code>null</code> a clone of this list is created
     */
    public ProductNodeList<T> createSubset(ProductNodeFilter filter) {
        ProductNodeList<T> list = new ProductNodeList<T>();
        for (int i = 0; i < size(); i++) {
            T node = getAt(i);
            if (filter.accept(node)) {
                list.add(node);
            }
        }
        return list;
    }

    /**
     * Returns the list of named nodes as an array. If this list is empty a zero-length array is returned.
     *
     * @return a string array containing all node names, never <code>null</code>
     */
    public final ProductNode[] toArray() {
        return _nodes.toArray(new ProductNode[0]);
    }

    /**
     * Returns the list of named nodes as an array. If this list is empty a zero-length array is returned.
     *
     * @param array the array into which the elements of the Vector are to be stored, if it is big enough; otherwise, a
     *              new array of the same runtime type is allocated for this purpose.
     *
     * @return an array containing the elements of the list. never <code>null</code>
     */
    public final T[] toArray(T[] array) {
        return _nodes.toArray(array);
    }

    /**
     * Copies the product nodes of this product node list into the specified array. The array must be big enough to hold
     * all the product nodes in this product node list, else an <tt>IndexOutOfBoundsException</tt> is thrown.
     *
     * @param array the array into which the product nodes get copied.
     *
     * @throws NullPointerException      if the given array is null.
     * @throws IndexOutOfBoundsException if the given array is to small.
     */
    public final void copyInto(T[] array) {
        for (T node : array) {
            add(node);
        }
    }

    /**
     * Gets the index of the node with the given name. The method performs a case insensitive search.
     *
     * @param name the name of the node, must not be <code>null</code>
     *
     * @return the index of the node with the given name or <code>-1</code> if a node with the given name is not
     *         contained in this list
     *
     * @throws IllegalArgumentException if the name is <code>null</code>
     */
    public final int indexOf(String name) {
        Guardian.assertNotNull("name", name);
        int n = size();
        for (int i = 0; i < n; i++) {
            if (_nodes.get(i).getName().equalsIgnoreCase(name)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Gets the index of the given node.
     *
     * @param node the node to get the index, must not be <code>null</code>
     *
     * @return the index of the given node or <code>-1</code> if the node is not contained in this list
     *
     * @throws IllegalArgumentException if the node is <code>null</code>
     */
    public final int indexOf(T node) {
        Guardian.assertNotNull("node", node);
        return _nodes.indexOf(node);
    }

    private void disposeRemovedList() {
        for (T removedNode : _removedNodes) {
            removedNode.dispose();
        }
        clearRemovedList();
    }
}