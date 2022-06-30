/*******************************************************************************
 * Copyright (c) 2002 - 2006 IBM Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.ibm.wala.examples.drivers;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.function.Predicate;

import com.ibm.wala.classLoader.IClass;
import com.ibm.wala.core.util.config.AnalysisScopeReader;
import com.ibm.wala.core.viz.PDFViewUtil;
import com.ibm.wala.examples.util.ExampleUtil;
import com.ibm.wala.ipa.callgraph.AnalysisScope;
import com.ibm.wala.ipa.cha.ClassHierarchy;
import com.ibm.wala.ipa.cha.ClassHierarchyFactory;
import com.ibm.wala.ipa.cha.IClassHierarchy;
import com.ibm.wala.types.ClassLoaderReference;
import com.ibm.wala.util.WalaException;
import com.ibm.wala.util.collections.CollectionFilter;
import com.ibm.wala.util.graph.Graph;
import com.ibm.wala.util.graph.GraphSlicer;
import com.ibm.wala.util.graph.impl.SlowSparseNumberedGraph;
import com.ibm.wala.util.viz.DotUtil;

/**
 * 
 * This simple example WALA application builds a TypeHierarchy and fires off
 * ghostview to viz a DOT representation.
 * 
 * @author sfink
 */
public class PDFTypeHierarchy {
  // This example takes one command-line argument, so args[1] should be the "-classpath" parameter
  final static int CLASSPATH_INDEX = 1;  


  public static void main(String[] args) throws IOException {
    run(args);
  }

  public static Process run(String[] args) throws IOException {
    try {
      validateCommandLine(args);
      String classpath = args[CLASSPATH_INDEX];
      AnalysisScope scope = AnalysisScopeReader.instance.makeJavaBinaryAnalysisScope(classpath, null);
      ExampleUtil.addDefaultExclusions(scope);

      // invoke WALA to build a class hierarchy
      ClassHierarchy cha = ClassHierarchyFactory.make(scope);

      Graph<IClass> g = typeHierarchy2Graph(cha);

      g = pruneForAppLoader(g);
      String dotFile = File.createTempFile("out", ".dt").getAbsolutePath();
      String pdfFile = File.createTempFile("out", ".pdf").getAbsolutePath();
      String dotExe = "dot";
      String gvExe = "open";
      DotUtil.dotify(g, null, dotFile, pdfFile, dotExe);
      return PDFViewUtil.launchPDFView(pdfFile, gvExe);

    } catch (WalaException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return null;
    }
  }

  public static <T> Graph<T> pruneGraph(Graph<T> g, Predicate<T> f) {
    Collection<T> slice = GraphSlicer.slice(g, f);
    return GraphSlicer.prune(g, new CollectionFilter<>(slice));
  }
  
  /**
   * Restrict g to nodes from the Application loader
   */
  public static Graph<IClass> pruneForAppLoader(Graph<IClass> g) throws WalaException {
    Predicate<IClass> f = c -> (c.getClassLoader().getReference().equals(ClassLoaderReference.Application));
    return pruneGraph(g, f);
  }
  
  /**
   * Validate that the command-line arguments obey the expected usage.
   * 
   * Usage: args[0] : "-classpath" args[1] : String, a ";"-delimited class path
   * 
   * @throws UnsupportedOperationException if command-line is malformed.
   */
  public static void validateCommandLine(String[] args) {
    if (args.length < 2) {
      throw new UnsupportedOperationException("must have at least 2 command-line arguments");
    }
    if (!args[0].equals("-classpath")) {
      throw new UnsupportedOperationException("invalid command-line, args[0] should be -classpath, but is " + args[0]);
    }
  }
  
  /**
   * Return a view of an {@link IClassHierarchy} as a {@link Graph}, with edges from classes to immediate subtypes
   */
  public static Graph<IClass> typeHierarchy2Graph(IClassHierarchy cha) throws WalaException {
    Graph<IClass> result = SlowSparseNumberedGraph.make();
    for (IClass c : cha) {
      result.addNode(c);
    }
    for (IClass c : cha) {
      for (IClass x : cha.getImmediateSubclasses(c)) {
        result.addEdge(c, x);
      }
      if (c.isInterface()) {
        for (IClass x : cha.getImplementors(c.getReference())) {
          result.addEdge(c, x);
        }
      }
    }
    return result;
  }  
}
