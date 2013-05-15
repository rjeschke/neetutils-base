/*
 * Copyright (C) 2012 René Jeschke <rene_jeschke@yahoo.de>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.rjeschke.neetutils.dispose;

/**
 * Specialized linked list to hold weak references or similar stuff. Adding and
 * removing is guaranteed O(1).
 * 
 * @author René Jeschke <rene_jeschke@yhoo.de>
 */
public class ReferenceList<T>
{
    private Node<T> root;
    private int     size;

    /**
     * Constructor.
     */
    public ReferenceList()
    {
        // empty
    }

    /**
     * Gets the number of items currently in this list.
     * 
     * @return The size.
     */
    public int size()
    {
        return this.size;
    }

    /**
     * Checks if this list is empty.
     * 
     * @return <code>true</code> is this list is empty
     */
    public boolean isEmpty()
    {
        return this.size == 0;
    }

    /**
     * Adds a value to this list (at head).
     * 
     * @param value
     *            Value to add
     * @return Node reference needed for remove
     */
    public Node<T> add(final T value)
    {
        final Node<T> node = new Node<>(value);
        this.size++;
        if (this.root != null)
        {
            node.next = this.root;
            this.root.previous = node;
        }
        this.root = node;

        return node;
    }

    /**
     * Removes the last added Node.
     * 
     * @return The last added Node or <code>null</code> if this list is empty.
     */
    public Node<T> removeLast()
    {
        if (this.root == null) return null;

        this.size--;
        final Node<T> node = this.root;
        if (node.next != null) node.next.previous = null;
        this.root = node.next;

        node.next = node.previous = null;
        node.inside = false;
        return node;
    }

    /**
     * Removes the given Node from this list. Multiple removes are prevented.
     * 
     * @param node
     *            The Node to remove
     */
    public void remove(final Node<T> node)
    {
        if (!node.inside) return;

        this.size--;
        if (node.previous == null)
        {
            this.root = node.next;
        }
        else
        {
            node.previous.next = node.next;
            if (node.next != null) node.next.previous = node.previous;
        }

        node.next = node.previous = null;
        node.inside = false;
    }

    /**
     * 
     * 
     * @author René Jeschke <rene_jeschke@yhoo.de>
     */
    public static class Node<T>
    {
        final T value;
        boolean inside = true;
        Node<T> previous;
        Node<T> next;

        public Node(final T value)
        {
            this.value = value;
        }

        public T value()
        {
            return this.value;
        }

        @Override
        public String toString()
        {
            return "Node: " + this.value;
        }
    }
}
