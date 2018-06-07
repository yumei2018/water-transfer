package com.gei.entities.collection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 *
 * @author clay
 * @param <E>
 */
public class EntityCollection<E extends Object> implements Serializable, List<E>/**/ {

  //<editor-fold defaultstate="collapsed" desc="Private Properties">
  private List<E> mCollection;
  private Class<E> mEClass;
  //</editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="Constructor(s)">
  public EntityCollection(Class<E> eClass) {
    this.mCollection = new ArrayList<>();
    this.mEClass = eClass;
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Finalize Override">
  @Override
  protected void finalize() throws Throwable {
    super.finalize(); //To change body of generated methods, choose Tools | Templates.
    this.mCollection.clear();
    this.mCollection = null;
    this.mEClass = null;
  }
  //</editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="Get Collection">
  public List<E> getCollection() {
    return this.mCollection;
  }
  //</editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="List Implementations">
  @Override
  public int size() {
    return this.mCollection.size();
  }

  @Override
  public boolean isEmpty() {
    return this.mCollection.isEmpty();
  }

  @Override
  public boolean contains(Object o) {
    return this.mCollection.contains(o);
  }

  @Override
  public Iterator<E> iterator() {
    return this.mCollection.iterator();
  }

  @Override
  public Object[] toArray() {
    return this.mCollection.toArray();
  }

  @Override
  public <T> T[] toArray(T[] a) {
    return this.mCollection.toArray(a);
  }

  @Override
  public boolean add(E e) {
    return this.mCollection.add(e);
  }

  @Override
  public boolean remove(Object o) {
    return this.mCollection.remove(o);
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    return this.mCollection.containsAll(c);
  }

  @Override
  public boolean addAll(Collection<? extends E> c) {
    return this.mCollection.addAll(c);
  }

  @Override
  public boolean addAll(int index, Collection<? extends E> c) {
    return this.mCollection.addAll(index, c);
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    return this.mCollection.removeAll(c);
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    return this.mCollection.retainAll(c);
  }

  @Override
  public void clear() {
    this.mCollection.clear();
  }

  @Override
  public E get(int index) {
    return this.mCollection.get(index);
  }

  @Override
  public E set(int index, E element) {
    return this.mCollection.set(index, element);
  }

  @Override
  public void add(int index, E element) {
    this.mCollection.add(index, element);
  }

  @Override
  public E remove(int index) {
    return this.mCollection.remove(index);
  }

  @Override
  public int indexOf(Object o) {
    return this.mCollection.indexOf(o);
  }

  @Override
  public int lastIndexOf(Object o) {
    return this.mCollection.lastIndexOf(o);
  }

  @Override
  public ListIterator<E> listIterator() {
    return this.mCollection.listIterator();
  }

  @Override
  public ListIterator<E> listIterator(int index) {
    return this.mCollection.listIterator(index);
  }

  @Override
  public java.util.List<E> subList(int fromIndex, int toIndex) {
    return this.mCollection.subList(fromIndex, toIndex);
  }
  //</editor-fold>
}
