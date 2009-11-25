/*
 * JBoss, Home of Professional Open Source
 * Copyright 2009, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.arquillian.impl;

import java.lang.reflect.Method;

import org.jboss.arquillian.impl.container.Controlable;
import org.jboss.arquillian.spi.TestMethodExecutor;
import org.jboss.arquillian.spi.util.TestEnrichers;
import org.jboss.shrinkwrap.api.Archive;

/**
 * DeployableTest
 *
 * @author <a href="mailto:aslak@conduct.no">Aslak Knutsen</a>
 * @version $Revision: $
 */
public class DeployableTest
{
   private static boolean inContainer = false;
   
   public static boolean isInContainer()
   {
      return inContainer;
   }

   public static void setInContainer(boolean inContainer)
   {
      DeployableTest.inContainer = inContainer;
   }

   private Controlable containerController;
   private Deployer containerDeployer;
   
   DeployableTest(Controlable containerController, Deployer containerDeployer)
   {
      this.containerController = containerController;
      this.containerDeployer = containerDeployer;
   }
   
   public Controlable getContainerController() 
   {
      return containerController;
   }

   public Deployer getDeployer() 
   {
      return containerDeployer;
   }

   
   public ArchiveGenerator getArchiveGenerator() 
   {
      if(DeployableTest.isInContainer()) 
      {
         return new NullArtifactGenerator();
      }
      return new UserCreatedArchiveGenerator();
   }

   public Archive<?> generateArchive(Class<?> testCase) 
   {
      return getArchiveGenerator().generateArchive(testCase);
   }


   public void run(TestMethodExecutor executor) throws Throwable 
   {
      if(DeployableTest.isInContainer()) 
      {
         injectClass(executor.getInstance());
         executor.invoke();
      } 
      else 
      {
         new ServletMethodExecutor(executor).invoke();
      }
   }
   
   private void injectClass(Object testCase) 
   {
      TestEnrichers.enrich(testCase);
   }
   
   private void invokeMethod(Method testMethod, Class<?> testCase) 
   {
   }

}